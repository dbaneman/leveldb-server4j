package com.dbaneman.leveldb

import java.io._
import java.net.{InetAddress, InetSocketAddress}

import com.dbaneman.leveldb.internal.DbHandler
import com.dbaneman.leveldb.internal.generated.DbService
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.thrift.server.TServer.Args
import org.apache.thrift.server.TSimpleServer
import org.apache.thrift.transport.TServerSocket
import org.iq80.leveldb._
import org.iq80.leveldb.impl.Iq80DBFactory

/**
 * Created by dan on 9/6/14.
 */
object LevelDbServer extends App {

  override def main(args: Array[String]) {
    val conf: Config = if (args.length == 0) ConfigFactory.load else ConfigFactory.parseFile(new File(args(0))).withFallback(ConfigFactory.load()).resolve()
    start(conf)
  }

  /**
   * Start the server with the default configuration.
   */
  def start() {
    start(ConfigFactory.load)
  }

  /**
   * Start the server with the supplied configuration.
   * @param conf
   */
  def start(conf: Config) {
    val hostAddress: String = conf.getString("leveldb-server.host-address")
    val port: Int = conf.getInt("leveldb-server.port")
    val dataDir: String = conf.getString("leveldb-server.data-dir")
    val db = new Iq80DBFactory().open(new File(dataDir), new Options)
    // TODO: expose standard LevelDB options through conf
    startServer(hostAddress, port, db)
  }

  private def startServer(hostAddress: String, port: Int, db: DB) {
    val handler = new DbHandler(db)
    val processor = new DbService.Processor(handler)
    val serverTransport = new TServerSocket(new InetSocketAddress(InetAddress.getByName(hostAddress), port))
    val args = new Args(serverTransport).processor(processor)
    val server = new TSimpleServer(args)
    println("LevelDB server now running on " + serverTransport.getServerSocket.getInetAddress.getHostAddress + ":" + serverTransport.getServerSocket.getLocalPort)
    server.serve()
  }

}
