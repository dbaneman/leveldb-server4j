package com.dbaneman.leveldb.internal

import java.nio.ByteBuffer
import java.util
import java.util.Map.Entry

import com.dbaneman.leveldb.internal.generated.{DbService, KeyValPair}
import org.iq80.leveldb.DBIterator

/**
 * Created by dan on 9/23/14.
 */
class ClientIterator(client: DbService.Client, val id: String) extends DBIterator{

  override def seek(p1: Array[Byte]) { client.iterSeek(id, ByteBuffer.wrap(p1)) }

  override def prev(): Entry[Array[Byte], Array[Byte]] = client.iterPrev(id)

  override def seekToLast() { client.iterSeekToLast(id) }

  override def seekToFirst() { client.iterSeekToFirst(id) }

  override def peekPrev(): Entry[Array[Byte], Array[Byte]] = client.iterPeekPrev(id)

  override def hasPrev: Boolean = client.iterHasPrev(id)

  override def peekNext(): Entry[Array[Byte], Array[Byte]] = client.iterPeekNext(id)

  override def close() { client.iterClose(id) }

  override def next(): Entry[Array[Byte], Array[Byte]] = client.iterNext(id)

  override def remove() { throw new UnsupportedOperationException }

  override def hasNext: Boolean = client.iterHasNext(id)

  implicit def convertKeyValPair(kvPair: KeyValPair): Entry[Array[Byte], Array[Byte]] = new util.AbstractMap.SimpleEntry(kvPair.getKey, kvPair.getValue)

}
