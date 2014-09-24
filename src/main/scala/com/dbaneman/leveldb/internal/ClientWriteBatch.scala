package com.dbaneman.leveldb.internal

import java.nio.ByteBuffer

import com.dbaneman.leveldb.internal.generated.DbService
import org.iq80.leveldb.WriteBatch

/**
 * Created by dan on 9/23/14.
 */
class ClientWriteBatch(client: DbService.Client, val id: String) extends WriteBatch {

  override def put(p1: Array[Byte], p2: Array[Byte]): WriteBatch = {
    client.wbPut(id, ByteBuffer.wrap(p1), ByteBuffer.wrap(p2))
    this
  }

  override def delete(p1: Array[Byte]): WriteBatch = {
    client.wbDelete(id, ByteBuffer.wrap(p1))
    this
  }

  override def close() = client.wbClose(id)
}
