package com.dbaneman.leveldb.internal

import org.apache.commons.lang.exception.ExceptionUtils
import org.iq80.leveldb.DB
import com.dbaneman.leveldb.internal.Messages._

/**
 * Created by dan on 9/6/14.
 */
class ServerMessageHandler(db: DB) {

  def processMessage(msg: Message): Message = {
    try {
      msg match {
        case x: Get => get(x)
        case x: Put => put(x)
        case x: Delete => delete(x)
        case _ => throw new RuntimeException("Unknown message type")
      }
    } catch {
      case ex: Throwable => error(msg, ex)
    }
  }

  private def error(message: Message, ex: Throwable): Message = {
    val stackTrace: String = ExceptionUtils.getStackTrace(ex)
    ErrorMessage(message, stackTrace)
  }

  private def get(get: Get): Message = {
    val value: Array[Byte] = db.get(get.key)
    GetResponse(value)
  }

  private def put(put: Put): Message = {
    db.put(put.key, put.value)
    Ok
  }

  private def delete(message: Delete): Message = {
    db.delete(message.key)
    Ok
  }
}
