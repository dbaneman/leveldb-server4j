package com.dbaneman.leveldb

import org.iq80.leveldb.DB
import org.junit._

/**
 * Created by dan on 9/6/14.
 */

object LevelDbServerTest {
  var client: DB = _

  @BeforeClass def setUp() {
    new Thread {
      override def run() {
        LevelDbServer.start()
      }
    }.start()
    Thread.sleep(1000)
    client = new LevelDbClientFactory().open("127.0.0.1", 8001)
  }
}

class LevelDbServerTest {

  @Test def testPutAndGet() {
    val key = "foo".getBytes
    val valueString: String = "bar"
    LevelDbServerTest.client.put(key, valueString.getBytes)
    val value = LevelDbServerTest.client.get(key)
    Assert.assertEquals(valueString, new String(value))
  }

  @Test def testDelete() {
    val key = "baz".getBytes
    LevelDbServerTest.client.put(key, "bar".getBytes)
    LevelDbServerTest.client.delete(key)
    val value = LevelDbServerTest.client.get(key)
    Assert.assertNull(value)
  }

}
