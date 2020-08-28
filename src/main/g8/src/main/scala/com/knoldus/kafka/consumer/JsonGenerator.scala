package com.knoldus.kafka.consumer

import org.json4s.JsonAST._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

object JsonGenerator {
  def generate(map: scala.collection.Map[String, Any]) = {
    val jObject = toJObject(map)
    compact(render(jObject))
  }

  private def toJObject(map: scala.collection.Map[String, Any]): List[JField] = {
    map.toList.map { case (k, v) =>
      val value = toJValue(v)
      JField(k, value)
    }
  }

  private def toJValue(value: Any): JValue = {
    value match {
      case i: String => JString(i)
      case i: Int => JInt(i)
      case i: Long => JInt(i)
      case i: Boolean => JBool(i)
      case i: Double => JDouble(i)
      case i: BigInt => JInt(i)
      case i: BigDecimal => JDecimal(i)
      case i: Seq[Any] => JArray(i.map(toJValue).toList)
      case i: Array[Any] => JArray(i.map(toJValue).toList)
      case i: Set[Any] => JArray(i.toList.map(toJValue))
      case i: scala.collection.Map[String, Any] => toJObject(i)
      case null => JNull
    }
  }
}
