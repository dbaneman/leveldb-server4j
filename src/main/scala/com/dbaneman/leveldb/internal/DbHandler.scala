package com.dbaneman.leveldb.internal

import java.nio.ByteBuffer

import org.iq80.leveldb.DB

/**
 * Created by dan on 9/21/14.
 */
class DbHandler(db: DB) extends DbService.Iface {

  override def get(key: ByteBuffer): GetResult = {
    val value = db.get(key.array())
    if (value == null) new GetResult(false) else new GetResult(true).setValue(value)
  }

  override def put(key: ByteBuffer, value: ByteBuffer) { db.put(key.array(), value.array())}

  override def deleteKey(key: ByteBuffer) { db.delete(key.array()) }

}
