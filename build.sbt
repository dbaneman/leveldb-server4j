name := "leveldb-server4j"

version := "1.0"

libraryDependencies += "junit" % "junit" % "4.8.1" % "test"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.0",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "commons-lang" % "commons-lang" % "2.6"
)
