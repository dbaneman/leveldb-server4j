package com.dbaneman.leveldb.internal

import java.io.{OutputStream, InputStream}
import java.nio.ByteBuffer

import com.dbaneman.leveldb.internal.Messages.Message
import org.apache.commons.lang.SerializationUtils

/**
 * Created by dan on 9/6/14.
 */
object MessageCodec {

  def decode(inputStream: InputStream): Message = {
    val sizeBytes: Array[Byte] = new Array[Byte](4)
    inputStream.read(sizeBytes)
    val payloadSize: Int = ByteBuffer.wrap(sizeBytes).getInt
    val payload: Array[Byte] = new Array[Byte](payloadSize)
    inputStream.read(payload)
    decodePayload(payload)
  }

  def encode(message: Message, outputStream: OutputStream) {
    val payload: Array[Byte] = encodePayload(message)
    val size: Int = payload.length
    val byteBuffer: ByteBuffer = ByteBuffer.allocate(4 + payload.length)
    byteBuffer.putInt(size)
    byteBuffer.put(payload)
    outputStream.write(byteBuffer.array)
  }

  private def encodePayload(message: Message): Array[Byte] = {
    SerializationUtils.serialize(message)
  }

  private def decodePayload(bytes: Array[Byte]): Message = {
    SerializationUtils.deserialize(bytes).asInstanceOf[Message]
  }

}
