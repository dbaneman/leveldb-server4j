name := "leveldb-server4j"

version := "0.1.0"

resolvers += "Twitter Maven repo" at "http://maven.twttr.com/"

libraryDependencies += "junit" % "junit" % "4.8.1" % "test"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.0",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "commons-lang" % "commons-lang" % "2.6",
  "org.apache.thrift" % "libthrift" % "0.9.1",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
//  "com.twitter" % "finagle-http" % "6.2.0",
//  "com.twitter" % "scrooge-core" % "3.3.2"
)

//com.twitter.scrooge.ScroogeSBT.newSettings
//
//scalaVersion := "2.10.1"
//
//libraryDependencies ++= Seq(
//  "org.apache.thrift" % "libthrift" % "0.8.0",
//  "com.twitter" %% "scrooge-core" % "3.9.2",
//  "com.twitter" %% "finagle-thrift" % "6.5.0"
//)

//com.twitter.scrooge.ScroogeSBT.newSettings

//libraryDependencies += "com.twitter" %% "scrooge-core" % "3.12.0"

//scroogeThriftOutputFolder in Compile <<= baseDirectory(_ / "src_gen")