package com.dbaneman.leveldb.internal

import java.nio.ByteBuffer

import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import org.iq80.leveldb._

// TODO: add timeouts for getting response from server!
/**
 * Created by dan on 9/6/14.
 */
class LevelDbClient extends DB {
  private[this] var transport: TSocket = _
  private[this] var client: DbService.Client = _

  def start(serverHost: String, serverPort: Int): LevelDbClient = {
    transport = new TSocket(serverHost, serverPort, 5000)
    transport.open()
    val protocol = new TBinaryProtocol(transport)
    client = new DbService.Client(protocol)
    System.out.println("LevelDB client initialized")
    this
  }

  override def get(key: Array[Byte]): Array[Byte] = client.get(ByteBuffer.wrap(key)).getValue

  override def iterator: DBIterator = ???

  override def iterator(readOptions: ReadOptions): DBIterator = ???

  override def put(key: Array[Byte], value: Array[Byte]) { client.put(ByteBuffer.wrap(key), ByteBuffer.wrap(value)) }

  override def get(key: Array[Byte], options: ReadOptions): Array[Byte] = ???

  override def delete(key: Array[Byte]) { client.deleteKey(ByteBuffer.wrap(key)) }

  override def write(writeBatch: WriteBatch): Unit = ???

  override def createWriteBatch: WriteBatch = ???

  override def put(bytes: Array[Byte], bytes2: Array[Byte], writeOptions: WriteOptions): Snapshot = ???

  override def delete(bytes: Array[Byte], writeOptions: WriteOptions): Snapshot = ???

  override def write(writeBatch: WriteBatch, writeOptions: WriteOptions): Snapshot = ???

  override def getSnapshot: Snapshot = ???

  override def getApproximateSizes(ranges: Range*): Array[Long] = ???

  override def getProperty(s: String): String = ???

  override def suspendCompactions() { ??? }

  override def resumeCompactions() { ??? }

  override def compactRange(bytes: Array[Byte], bytes2: Array[Byte]) { ??? }

  override def close() { transport.close() }
}
