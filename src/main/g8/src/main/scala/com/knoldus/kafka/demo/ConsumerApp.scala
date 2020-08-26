package com.knoldus.kafka.demo
import com.knoldus.kafka.consumer.KafkaConsumer
object ConsumerApp extends App {
  val topic = "cpm_impressions__saver"
  val groupId = "group1"
  val consumer = new KafkaConsumer(topic, groupId, "172.31.2.71:2181")

  while (true) {
    consumer.read() match {
      case Some(message) =>
        println("Getting message.......................  " + message)
        // wait for 100 milli second for another read
        Thread.sl
      case None =>
        println("Queue is empty.......................  ")
        // wait for 2 second
        Thread.sleep(2 * 1000)
    }
  }
}