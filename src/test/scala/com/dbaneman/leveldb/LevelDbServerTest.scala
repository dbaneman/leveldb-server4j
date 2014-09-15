package com.dbaneman.leveldb

import org.iq80.leveldb.{DBIterator, DB}
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

  @Test def testIterator() {
    LevelDbServerTest.client.put("1".getBytes, "1Value".getBytes)
    LevelDbServerTest.client.put("2".getBytes, "2Value".getBytes)
    LevelDbServerTest.client.put("3".getBytes, "3Value".getBytes)
    val dbIterator = LevelDbServerTest.client.iterator()
    testIterate1To3(dbIterator)
    dbIterator.seekToFirst()
    testIterate1To3(dbIterator)
    dbIterator.seek("1".getBytes)
    testIterate1To3(dbIterator)
  }

  private def testIterate1To3(dbIterator: DBIterator) {
    for (i <- 1 to 3) {
      println("i = " + i)
      Assert.assertTrue(dbIterator.hasNext)
      val peek = dbIterator.peekNext()
      Assert.assertEquals(i.toString, new String(peek.getKey))
      Assert.assertEquals(i.toString + "Value", new String(peek.getValue))
      val next = dbIterator.next()
      Assert.assertEquals(i.toString, new String(next.getKey))
      Assert.assertEquals(i.toString + "Value", new String(next.getValue))
    }
  }

}
