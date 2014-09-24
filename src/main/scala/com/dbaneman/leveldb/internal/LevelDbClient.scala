package com.dbaneman.leveldb.internal

import java.nio.ByteBuffer

import com.dbaneman.leveldb.internal.generated.DbService
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import org.iq80.leveldb._

import scala.collection.JavaConversions._

/**
 * Created by dan on 9/6/14.
 */
class LevelDbClient extends DB {
  var transport: TSocket = _
  var client: DbService.Client = _

  def start(serverHost: String, serverPort: Int): LevelDbClient = {
    transport = new TSocket(serverHost, serverPort, 5000)
    transport.open()
    val protocol = new TBinaryProtocol(transport)
    client = new DbService.Client(protocol)
    this
  }

  override def get(key: Array[Byte]): Array[Byte] = client.get(ByteBuffer.wrap(key)).getValue

  override def iterator: DBIterator = {
    val id = client.iterator()
    new ClientIterator(client, id)
  }

  override def iterator(readOptions: ReadOptions): DBIterator = ???

  override def put(key: Array[Byte], value: Array[Byte]) { client.put(ByteBuffer.wrap(key), ByteBuffer.wrap(value)) }

  override def get(key: Array[Byte], options: ReadOptions): Array[Byte] = ???

  override def delete(key: Array[Byte]) { client.doDelete(ByteBuffer.wrap(key)) }

  override def write(writeBatch: WriteBatch) { client.write(writeBatch.asInstanceOf[ClientWriteBatch].id) }

  override def createWriteBatch: WriteBatch = {
    val id = client.writeBatch()
    new ClientWriteBatch(client, id)
  }

  override def put(bytes: Array[Byte], bytes2: Array[Byte], writeOptions: WriteOptions): Snapshot = ???

  override def delete(bytes: Array[Byte], writeOptions: WriteOptions): Snapshot = ???

  override def write(writeBatch: WriteBatch, writeOptions: WriteOptions): Snapshot = ???

  override def getSnapshot: Snapshot = ???

override def getApproximateSizes(ranges: Range*): Array[Long] = {
  val sizes = client.getApproximateSizes(ranges map { r: Range => new generated.Range(ByteBuffer.wrap(r.start()), ByteBuffer.wrap(r.limit())) })
  // sometimes Scala/Java interop doesn't work so smoothly. hence we've got these next three lines instead of a ".toArray()" call
  val array = new Array[Long](sizes.size)
  for (i <- 0 to sizes.length) {array(i) = sizes.get(i)}
  array
}

  override def getProperty(s: String): String = { client.getProperty(s) }

  override def suspendCompactions() { client.suspendCompactions() }

  override def resumeCompactions() { client.resumeCompactions() }

  override def compactRange(bytes: Array[Byte], bytes2: Array[Byte]) { client.compactRange(ByteBuffer.wrap(bytes), ByteBuffer.wrap(bytes2)) }

  override def close() { transport.close() }
}
