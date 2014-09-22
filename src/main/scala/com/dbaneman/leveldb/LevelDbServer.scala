package com.dbaneman.leveldb

import java.io._

import com.dbaneman.leveldb.internal.{DbHandler, DbService}
import com.dbaneman.leveldb.internal.UnderlyingDbLibs._
import org.apache.thrift.server.TServer.{AbstractServerArgs, Args}
import org.apache.thrift.server.TSimpleServer
import org.apache.thrift.transport.TServerSocket

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
    startServer(port, db)
  }

  private def startServer(port: Int, db: DB) {
    val handler = new DbHandler(db)
    val processor = new DbService.Processor(handler)
    val serverTransport = new TServerSocket(port)
    val args = new Args(serverTransport).processor(processor)
    val server = new TSimpleServer(args)
    println("Starting the simple server...")
    server.serve()
  }

}
