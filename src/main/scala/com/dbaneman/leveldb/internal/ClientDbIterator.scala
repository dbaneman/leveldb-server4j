package com.dbaneman.leveldb.internal

import java.util.AbstractMap.SimpleEntry
import java.util.Map.Entry

import com.dbaneman.leveldb.internal.Messages._
import org.iq80.leveldb.DBIterator

/**
 * Created by dan on 9/6/14.
 */
class ClientDbIterator(client: LevelDbClient, id: Int) extends DBIterator {
  private[this] var localBatch : IndexedSeq[(Array[Byte], Array[Byte])] = _
  private[this] var valid = true
  private[this] var atStart = true
  private[this] var atEnd = false
  private[this] var currentIndex = 0

  /* TODO:
    * basic idea is that we're going to have a local Iterator (or whatever the appropriate scala thing is; ideally it would also support peek, prev() and peekPrev()). note that eager is as good as lazy here since we know it's all in memory anyway.
    * then whenever we seek or the current batch runs out, we get a new batch
    * the server is storing an iterator with the same int to process refreshes
    * to get a new batch from the server, or to do a seek, we can use client.sendMessage() to send the appropriate message
   */

  def invalidate() {
    valid = false
  }

  def ensureValid() {
    if (!valid) validate()
  }

  def validate() {
    client.sendMessage(IteratorAction(id, GetNextBatch)) match {
      case x: IteratorBatch =>
        localBatch = x.kvPairs
        atStart = false
        atEnd = false
      case ReachedIteratorStart =>
        localBatch = IndexedSeq.empty
        atStart = true
        atEnd = false
      case ReachedIteratorEnd =>
        localBatch = IndexedSeq.empty
        atStart = false
        atEnd = true
    }
    valid = true
    currentIndex = 0
  }

  override def seek(key: Array[Byte]) {
    client.sendMessageExpectOk(IteratorAction(id, Seek(key)))
    invalidate()
  }

  override def prev(): Entry[Array[Byte], Array[Byte]] = ???

  override def seekToLast() {
    client.sendMessageExpectOk(IteratorAction(id, SeekToLast))
    invalidate()
  }

  override def seekToFirst() {
    client.sendMessageExpectOk(IteratorAction(id, SeekToFirst))
    invalidate()
  }

  override def peekPrev(): Entry[Array[Byte], Array[Byte]] = ???

  override def hasPrev: Boolean = ???

  override def peekNext(): Entry[Array[Byte], Array[Byte]] = ???

  override def next(): Entry[Array[Byte], Array[Byte]] = {
    if (reachedEndOfBatch) invalidate()
    ensureValid()
    val ret = localBatch(currentIndex)
    currentIndex += 1
    new SimpleEntry(ret._1, ret._2)
  }

  override def remove(): Unit = ???

  override def hasNext: Boolean = {
    if (!atEnd && reachedEndOfBatch) invalidate()
    ensureValid()
    !atEnd
  }

  override def close(): Unit = ???

  private def reachedEndOfBatch = atEnd || currentIndex == localBatch.size - 1

  private def reachedStartOfBatch = atStart || currentIndex == 0
}
