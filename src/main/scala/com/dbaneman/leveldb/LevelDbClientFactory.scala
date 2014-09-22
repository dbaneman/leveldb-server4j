package com.dbaneman.leveldb

import com.dbaneman.leveldb.internal.LevelDbClient
import org.iq80.leveldb.DB

/**
 * Created by dan on 9/6/14.
 */
class LevelDbClientFactory {

  def open(serverHost: String, serverPort: Int): DB = new LevelDbClient().start(serverHost, serverPort)

}
