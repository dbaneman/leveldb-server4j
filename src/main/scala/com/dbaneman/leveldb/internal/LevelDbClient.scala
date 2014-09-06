package com.dbaneman.leveldb.internal

import java.io.{BufferedInputStream, BufferedOutputStream, InputStream, OutputStream}
import java.net.{InetAddress, Socket}

import com.dbaneman.leveldb.internal.Messages._
import org.iq80.leveldb._

// TODO: add timeouts for getting response from server!
/**
  * Created by dan on 9/6/14.
 */
class LevelDbClient extends DB {
  private var connection: Socket = _
  private var outputStream: OutputStream = _
  private var inputStream: InputStream = _

  def start(serverHost: String, serverPort: Int) {
    System.out.println("LevelDB client initialized")
    val address: InetAddress = InetAddress.getByName(serverHost)
    connection = new Socket(address, serverPort)
    outputStream = new BufferedOutputStream(connection.getOutputStream)
    inputStream = new BufferedInputStream(connection.getInputStream)
  }

  override def get(key: Array[Byte]): Array[Byte] = {
    val get: Get = new Get(key)
    writeMessage(get)
    val response: Message = MessageCodec.decode(inputStream)
    processPotentialError(response)
    response.asInstanceOf[GetResponse].value
  }

  private def processPotentialError(message: Message) {
    message match {
      case errorMessage: ErrorMessage =>
        throw new DBException("Error processing " + errorMessage.message.getClass + "\n" + errorMessage.stackTrace)
      case _ => // no op
    }
  }

  override def iterator: DBIterator = {
    throw new UnsupportedOperationException
  }

  override def iterator(readOptions: ReadOptions): DBIterator = {
    throw new UnsupportedOperationException
  }

  override def put(key: Array[Byte], value: Array[Byte]) {
    val put: Put = new Put(key, value)
    writeMessage(put)
    val response: Message = MessageCodec.decode(inputStream)
    processPotentialError(response)
  }

  override def get(key: Array[Byte], options: ReadOptions): Array[Byte] = {
    throw new UnsupportedOperationException
  }

  override def delete(key: Array[Byte]) {
    val delete: Delete = new Delete(key)
    writeMessage(delete)
    val response: Message = MessageCodec.decode(inputStream)
    processPotentialError(response)
  }

  private def writeMessage(msg: Message) {
      MessageCodec.encode(msg, outputStream)
      outputStream.flush()
      System.out.println("Client sent a " + msg)
  }

  override def write(writeBatch: WriteBatch) {
    throw new UnsupportedOperationException
  }

  override def createWriteBatch: WriteBatch = {
    throw new UnsupportedOperationException
  }

  override def put(bytes: Array[Byte], bytes2: Array[Byte], writeOptions: WriteOptions): Snapshot = {
    throw new UnsupportedOperationException
  }

  override def delete(bytes: Array[Byte], writeOptions: WriteOptions): Snapshot = {
    throw new UnsupportedOperationException
  }

  override def write(writeBatch: WriteBatch, writeOptions: WriteOptions): Snapshot = {
    throw new UnsupportedOperationException
  }

  override def getSnapshot: Snapshot = {
    throw new UnsupportedOperationException
  }

  override def getApproximateSizes(ranges: Range*): Array[Long] = {
    throw new UnsupportedOperationException
  }

  override def getProperty(s: String): String = {
    throw new UnsupportedOperationException
  }

  override def suspendCompactions() {
    throw new UnsupportedOperationException
  }

  override def resumeCompactions() {
    throw new UnsupportedOperationException
  }

  override def compactRange(bytes: Array[Byte], bytes2: Array[Byte]) {
    throw new UnsupportedOperationException
  }

  override def close() {
    connection.close
  }
}
