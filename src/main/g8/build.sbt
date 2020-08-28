name := """activator-kafka-scala-producer-consumer"""

version := "1.0"

scalaVersion := "2.11.8"
val msgpackVersion = "0.6.12"


libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka_2.11" % "0.10.0.0",
  "org.msgpack" % "msgpack" % msgpackVersion,
  "org.json4s" %% "json4s-jackson"  % "3.2.10"
)


initialize ~= { _ =>
  System.setProperty("msgpack.version", msgpackVersion)
}
