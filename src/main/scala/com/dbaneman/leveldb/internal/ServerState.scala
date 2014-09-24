package com.dbaneman.leveldb.internal

import java.util.UUID

import org.iq80.leveldb.{DBIterator, WriteBatch}

/**
 * Created by dan on 9/23/14.
 */
object ServerState {
  var writeBatches: Map[UUID, WriteBatch] = Map()
  var iterators: Map[UUID, DBIterator] = Map()
}
