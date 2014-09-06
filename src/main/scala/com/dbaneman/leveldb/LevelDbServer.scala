package com.dbaneman.leveldb

import java.io._
import java.net.{ServerSocket, Socket}

import com.dbaneman.leveldb.internal.Messages.Message
import com.dbaneman.leveldb.internal.{MessageCodec, ServerMessageHandler}
import com.dbaneman.leveldb.internal.UnderlyingDbLibs._

import com.typesafe.config.{ConfigFactory, Config}
import org.fusesource.leveldbjni.JniDBFactory
import org.iq80.leveldb.{Options, DB, DBFactory}
import org.iq80.leveldb.impl.Iq80DBFactory

/**
 * Created by dan on 9/6/14.
 */
object LevelDbServer extends App {

  override def main(args: Array[String]) {
    val conf: Config = if (args.length == 0) ConfigFactory.load else ConfigFactory.load(args(0))
    start(conf)
  }

  def start() {
    start(ConfigFactory.load)
  }

  def start(conf: Config) {
    val port: Int = conf.getInt("leveldb.port")
    val lib: String = conf.getString("leveldb.lib").toLowerCase
    val dataDir: String = conf.getString("leveldb.data-dir")
    val dbFactory: DBFactory = lib match {
      case IQ80 => new Iq80DBFactory
      case JNI => new JniDBFactory
      case _ => throw new RuntimeException("Unknown lib: '" + lib + "'; expected '" + IQ80 + "' or '" + JNI + "'")
    }
    val db = dbFactory.open(new File(dataDir), new Options)
    // TODO: expose standard LevelDB options through conf
    start(port, db)
  }

  def start(port: Int, db: DB) {
    val serverSocket: ServerSocket = new ServerSocket(port)
    val messageHandler = new ServerMessageHandler(db)
    val connection = serverSocket.accept()
    val inputFromClient = new BufferedInputStream(connection.getInputStream)
    val outputToClient = new BufferedOutputStream(connection.getOutputStream)
    while (true) {
      System.out.println("top of loop!")
      val message = MessageCodec.decode(inputFromClient)
      System.out.println("Server received a " + message)
      val response = messageHandler.processMessage(message)
      System.out.println("Server sent a " + response)
      MessageCodec.encode(response, outputToClient)
      outputToClient.flush()
    }
  }

}
