package com.dbaneman.leveldb.internal

import java.lang.Long
import java.nio.ByteBuffer
import java.util
import java.util.Map.Entry
import java.util.UUID

import com.dbaneman.leveldb.internal.ServerState._
import com.dbaneman.leveldb.internal.generated._
import org.iq80.leveldb.{DB, DBIterator, WriteBatch}

import scala.collection.JavaConversions._

/**
 * Created by dan on 9/21/14.
 */
class DbHandler(db: DB) extends DbService.Iface {

  override def get(key: ByteBuffer): GetResult = {
    val value = db.get(key.array())
    if (value == null) new GetResult(false) else new GetResult(true).setValue(value)
  }

  override def put(key: ByteBuffer, value: ByteBuffer) { db.put(key.array(), value.array())}

  override def doDelete(key: ByteBuffer) { db.delete(key.array()) }

  override def getApproximateSizes(ranges: util.List[Range]): util.List[Long] = {
    val convertedRanges = db.getApproximateSizes() map Long.valueOf
    convertedRanges.toList
  }

  override def resumeCompactions() { db.resumeCompactions() }

  override def getProperty(property: String): String = { db.getProperty(property) }

  override def writeBatch(): String = {
    val uuid = UUID.randomUUID()
    val writeBatch = db.createWriteBatch()
    writeBatches += (uuid -> writeBatch)
    uuid.toString
  }

  override def compactRange(start: ByteBuffer, limit: ByteBuffer){ db.compactRange(start.array(), limit.array()) }

  override def iterator(): String = {
    val uuid = UUID.randomUUID()
    val iter = db.iterator()
    iterators += (uuid -> iter)
    uuid.toString
  }

  override def suspendCompactions() { db.suspendCompactions() }

  override def write(id: String) { db.write(getWriteBatch(id)) }

  override def iterPeekPrev(id: String): KeyValPair = getIterator(id).peekPrev()

  override def iterSeekToLast(id: String) { getIterator(id).seekToLast() }

  override def iterPeekNext(id: String): KeyValPair = getIterator(id).peekNext()

  override def iterPrev(id: String): KeyValPair = getIterator(id).prev()

  override def wbDelete(id: String, key: ByteBuffer) { getWriteBatch(id).delete(key.array()) }

  override def iterHasPrev(id: String): Boolean = getIterator(id).hasPrev

  override def iterNext(id: String): KeyValPair = getIterator(id).next()

  override def iterSeekToFirst(id: String) { getIterator(id).seekToFirst() }

  override def iterHasNext(id: String): Boolean = getIterator(id).hasNext

  override def iterClose(id: String) {
    getIterator(id).close()
    iterators.remove(id)
  }

  override def wbClose(id: String): Unit = {
    getWriteBatch(id)
    writeBatches.remove(id)
  }

  override def wbPut(id: String, key: ByteBuffer, value: ByteBuffer) { getWriteBatch(id).put(key.array(), value.array()) }

  override def iterSeek(id: String, key: ByteBuffer) { getIterator(id).seek(key.array()) }

  implicit def convertEntry(entry: Entry[Array[Byte], Array[Byte]]): KeyValPair = new KeyValPair().setKey(entry.getKey).setValue(entry.getValue)

  def getWriteBatch(id: String): WriteBatch = writeBatches(UUID.fromString(id))

  def getIterator(id: String): DBIterator = iterators(UUID.fromString(id))

}
