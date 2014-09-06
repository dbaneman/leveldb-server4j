package com.dbaneman.leveldb.internal

/**
 * Created by dan on 9/6/14.
 */
object Messages {
  abstract class Message extends Serializable
  case class Get(key: Array[Byte]) extends Message
  case class Put(key: Array[Byte], value: Array[Byte]) extends Message
  case class Delete(key: Array[Byte]) extends Message
  case class GetResponse(value: Array[Byte]) extends Message
  case object Ok extends Message
  case class ErrorMessage(message: Message, stackTrace: String) extends Message
}
