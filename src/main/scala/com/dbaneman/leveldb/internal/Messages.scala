package com.dbaneman.leveldb.internal

/**
 * Created by dan on 9/6/14.
 */
object Messages {
  sealed abstract class Message extends Serializable
  case class Get(key: Array[Byte]) extends Message
  case class Put(key: Array[Byte], value: Array[Byte]) extends Message
  case class Delete(key: Array[Byte]) extends Message
  case class GetResponse(value: Array[Byte]) extends Message
  case object Ok extends Message
  case class ErrorMessage(message: Message, stackTrace: String) extends Message
  case object NewIterator extends Message
  case class NewIteratorResponse(id: Int) extends Message
  case class IteratorAction(id: Int, op: IteratorOp) extends Message
  case class IteratorBatch(kvPairs: IndexedSeq[(Array[Byte], Array[Byte])]) extends Message
  case object ReachedIteratorEnd extends Message
  case object ReachedIteratorStart extends Message

  sealed abstract class IteratorOp extends Serializable
  case object GetNextBatch extends IteratorOp
  case object GetPrevBatch extends IteratorOp
  case object SeekToFirst extends IteratorOp
  case object SeekToLast extends IteratorOp
  case class Seek(key: Array[Byte]) extends IteratorOp
}
