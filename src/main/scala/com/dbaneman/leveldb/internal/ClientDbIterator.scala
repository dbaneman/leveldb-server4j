package com.dbaneman.leveldb.internal

import java.util.Map.Entry

import org.iq80.leveldb.DBIterator

/**
 * Created by dan on 9/6/14.
 */
class ClientDbIterator(client: LevelDbClient, id: Int) extends DBIterator {

  /* TODO:
    * basic idea is that we're going to have a local Iterator (or whatever the appropriate scala thing is; ideally it would also support peek, prev() and peekPrev()).
    * then whenever we seek or the current batch runs out, we get a new batch
    * the server is storing an iterator with the same int to process refreshes
    * to get a new batch from the server, or to do a seek, we can use client.sendMessage() to send the appropriate message
   */

  def invalidate() {

  }

  def getNextBatch() {

  }

  override def seek(key: Array[Byte]): Unit = ???

  override def prev(): Entry[Array[Byte], Array[Byte]] = ???

  override def seekToLast(): Unit = ???

  override def seekToFirst(): Unit = ???

  override def peekPrev(): Entry[Array[Byte], Array[Byte]] = ???

  override def hasPrev: Boolean = ???

  override def peekNext(): Entry[Array[Byte], Array[Byte]] = ???

  override def next(): Entry[Array[Byte], Array[Byte]] = ???

  override def remove(): Unit = ???

  override def hasNext: Boolean = ???

  override def close(): Unit = ???
}
