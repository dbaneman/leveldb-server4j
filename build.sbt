name := "leveldb-server4j"

version := "0.1.0"

libraryDependencies += "junit" % "junit" % "4.8.1" % "test"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.0",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "commons-lang" % "commons-lang" % "2.6",
  "org.apache.thrift" % "libthrift" % "0.9.1",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)

mainClass := Some("com.dbaneman.leveldb.LevelDbServer")