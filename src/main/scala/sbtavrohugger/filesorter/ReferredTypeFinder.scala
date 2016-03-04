package sbtavrohugger
package filesorter

import spray.json.DefaultJsonProtocol._
import spray.json._

/**
  * The order in which avsc files are compiled depends on the underlying file
  * system (under OSX its is alphabetical, under some linux distros it's not).
  * This is an issue when you have a record type that is used in different
  * other types. This ensures that dependent types are compiled in the
  * correct order. Code adapted from https://github.com/ch4mpy/sbt-avro/blob/master/src/main/scala/com/c4soft/sbtavro/SbtAvro.scala
  * by Jerome Wascongne
  */
object ReferredTypeFinder {

  object Keys {
    val Fields = "fields"
    val Type = "type"
    val Items = "items"
    val Array = "array"
    val Enum = "enum"
    val Name = "name"
  }

  def findReferredTypes(strAsJson: JsValue): List[String] = {
    val fields: JsArray = strAsJson.asJsObject.fields.getOrElse(Keys.Fields, JsArray.empty).asInstanceOf[JsArray]
    val referredTypes = for {
      element <- fields.elements
      (key, typeValue) <- element.asJsObject.fields
      if key == Keys.Type
    } yield {

      def matchComplexType(fields: Map[String,spray.json.JsValue]): List[String] = {
        val typeOfRef = fields(Keys.Type)
        typeOfRef match {
          case JsString(Keys.Array) => List(fields(Keys.Items).convertTo[String])
          case JsString(Keys.Enum) => List(fields(Keys.Name).convertTo[String])
          case _ => List.empty
        }
      }

      typeValue match {
        case s: JsString =>
          List(s.value)
        // This means we've found a union
        case a: JsArray =>
          val elementsAsString: List[String] = a.elements.map( e => e match {
            case s: JsString => List(s.value)
            //This means the union type is a complex type or null
            case v: JsValue => v match {
              //This means the union type is a complex type such as an array
              case o: JsObject => matchComplexType(o.fields)
              //This means the union type is a null
              case p: JsValue => Nil
            }
          }).flatten.toList
          elementsAsString
        case t => matchComplexType(t.asJsObject.fields)
          // This means a complex type reference as below
          /*
            "type": {
              "type": "array",
              "items": "common.Z"
            }
            OR
            "type": {
              "name": "Direction",
              "type": "enum",
              "symbols" : ["NORTH", "SOUTH", "EAST", "WEST"]
            }
          */
      }
    }
    referredTypes.flatten.toList
  }

}
