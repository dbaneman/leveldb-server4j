package com.dbaneman.leveldb

import com.dbaneman.leveldb.internal.LevelDbClient
import org.iq80.leveldb.DB

/**
 * Created by dan on 9/6/14.
 */
class LevelDbClientFactory {

  /**
   * Create a new client to a running LevelDB server.
   * @param serverHost server host
   * @param serverPort port that the server is listening on
   * @return
   */
  def open(serverHost: String, serverPort: Int): DB = new LevelDbClient().start(serverHost, serverPort)

}
