package com.knoldus.kafka.demo

import com.knoldus.kafka.producer.{AsyncProducer, KafkaProducer}


object ProducerApp extends App {

  val topic = "cpm_impressions__saver"

  val producer = new AsyncProducer("172.31.2.71:9092")
  //val producer = new KafkaProducer("localhost:9092")

  val batchSize = 100

  (1 to 1000000).toList.map(no => "Message " + no).grouped(batchSize).foreach { message =>
    println("Sending message batch size " + message.length)
    producer.send(topic, message)
  }

}