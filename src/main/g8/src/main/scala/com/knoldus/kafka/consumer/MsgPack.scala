package com.knoldus.kafka.consumer
import org.msgpack.unpacker.Unpacker
import org.msgpack.packer.Packer
import org.msgpack.template.AbstractTemplate
import org.msgpack.template.ScalaTemplateRegistry
import org.msgpack.{ScalaMessagePackWrapper, MessagePack}
import org.msgpack.conversion.ValueConversions
import org.msgpack.`type`.{Value, MapValue}
import scala.collection.JavaConversions._
import scala.collection.mutable.{Map => MMap}

object MsgPack extends ScalaMessagePackWrapper with ValueConversions{

  class MessageWrapper {
    var timestamp: Int = 0
    var host: String = null
    var service: String = null
    var body: List[Any] = List()
  }

  /**
   * dummy init method
   */
  def init() = {}

  val messagePack = new MsgPack()

  def toSerializableMap(record: Map[String, Value]): MMap[String, Any] = {
    val map = MMap[String, Any]()
    record.keySet.foreach {key =>
      val value = toSerializableValue(record(key))
      val keyValue = toSerializableValue(key)
      map += ((keyValue.toString, value))
    }
    map
  }

  def toSerializableMap(record: MapValue): MMap[String, Any] = {
    val map = MMap[String, Any]()
    record.keySet.foreach {key =>
      val value = toSerializableValue(record.get(key))
      val keyValue = toSerializableValue(key)
      map += ((keyValue.toString, value))
    }
    map
  }

  def toSerializableValue(value: Value): Any = {
    val deserializedValue = value match {
      case value if value.isRawValue => value.asRawValue.getString
      case value if value.isNilValue => null
      case value if value.isBooleanValue => value.asBooleanValue.getBoolean
      case value if value.isIntegerValue => value.asIntegerValue.getLong
      case value if value.isFloatValue => value.asFloatValue.getDouble
      case value if value.isArrayValue => value.asArrayValue.getElementArray.map(el => toSerializableValue(el))
      case value if value.isMapValue => toSerializableMap(value.asMapValue)
    }
    deserializedValue.asInstanceOf[Any]
  }
}


class MutableMapableTemplate[T] extends AbstractTemplate[T] {
  def write(pk: Packer, v: T, required: Boolean) {
    pk.write(getCCParams(v))
  }

  // not implemented
  def read(u: Unpacker, to: T, required: Boolean) = {
    to
  }

  def getCCParams(cc: T) =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {(a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(cc))
  }
}


class MsgPackQueueableRegistry extends ScalaTemplateRegistry {
  register(classOf[MsgPack.MessageWrapper], new MutableMapableTemplate[MsgPack.MessageWrapper]())
}


class MsgPack extends MessagePack(new MsgPackQueueableRegistry()){
}
