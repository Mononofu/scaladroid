package org.furidamu.scaladroid

import net.minidev.json.JSONValue
import net.minidev.json.JSONArray
import net.minidev.json.JSONObject

import scala.collection.JavaConversions._

object JSON {
  def parseJSON(s: String) = new ScalaJSON(JSONValue.parse(s))
  def makeJSON(a: Any): String = a match {
    case m: Map[String, Any] => m.map {
      case (name, content) => "\"" + name + "\":" + makeJSON(content)
    }.mkString("{", ",", "}")
    case l: List[Any] => l.map(makeJSON).mkString("[", ",", "]")
    case l: java.util.List[Any] => l.map(makeJSON).mkString("[", ",", "]")
    case s: String => "\"" + s + "\""
    case i: Int => i.toString
  }

  implicit def ScalaJSONToString(s: ScalaJSON) = s.toString
  implicit def ScalaJSONToInt(s: ScalaJSON) = s.toInt
  implicit def ScalaJSONToDouble(s: ScalaJSON) = s.toDouble
}

case class JSONException(reason: String) extends Exception(reason)

class ScalaJSONIterator(i: java.util.Iterator[java.lang.Object]) extends Iterator[ScalaJSON] {
  def hasNext = i.hasNext()
  def next() = new ScalaJSON(i.next())
}

class ScalaJSON(o: java.lang.Object) extends Seq[ScalaJSON] with Dynamic {
  override def toString: String = o.toString
  def toInt: Int = o match {
    case i: Integer => i
    case _ => throw new JSONException("%s is not an Integer".format(o))
  }
  def toDouble: Double = o match {
    case d: java.lang.Double => d
    case f: java.lang.Float => f.toDouble
    case _ => throw new JSONException("%s is not a Double".format(o))
  }
  def apply(key: String): ScalaJSON = o match {
    case m: JSONObject => new ScalaJSON(m.get(key))
    case _ => throw new JSONException("%s is not a Map, so can't lookup %s".format(o, key))
  }

  def apply(idx: Int): ScalaJSON = o match {
    case a: JSONArray => new ScalaJSON(a.get(idx))
    case _ => throw new JSONException("%s is not an Array, so can't index".format(o))
  }
  def length: Int = o match {
    case a: JSONArray => a.size()
    case m: JSONObject => m.size()
    case _ => throw new JSONException("%s is neither Map nor Array, so can't take length".format(o))
  }
  def iterator: Iterator[ScalaJSON] = o match {
    case a: JSONArray => new ScalaJSONIterator(a.iterator())
    case _ => throw new JSONException("%s is not an Array, so can't iterate".format(o))
  }

  def selectDynamic(name: String): ScalaJSON = apply(name)
  def applyDynamic(name: String)(arg: Any): ScalaJSON = {
    arg match {
      case s: String => apply(name)(s)
      case n: Int => apply(name)(n)
      case u: Unit => apply(name)
    }
  }
}
