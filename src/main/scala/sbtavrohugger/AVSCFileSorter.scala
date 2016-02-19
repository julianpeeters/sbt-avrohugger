package sbtavrohugger

import java.io.File

import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.collection.mutable
import scala.io.Source

object AVSCFileSorter {

  //The order in which avsc files are compiled depends on the underlying file system (i.e. under OSX its is alphabetical, under some linux distros it's not).
  //This is an issue when you have a record type that is used in different other types. Also makes sure
  //that dependent types are compiled in the correct order.
  // Code adapted from https://github.com/ch4mpy/sbt-avro/blob/master/src/main/scala/com/c4soft/sbtavro/SbtAvro.scala
  // by Jerome Wascongne
  object Keys {
    val Fields = "fields"
    val Type = "type"
    val Items = "items"
    val Array = "array"
    val Enum = "enum"
    val Name = "name"
  }

  def sortSchemaFiles(files: Traversable[File]): Seq[File] = {
    val sortedButReversed = mutable.MutableList.empty[File]
    var pending: Traversable[File] = files
    while(pending.nonEmpty) {
      val (used, unused) = usedUnusedSchemas(pending)
      sortedButReversed ++= unused
      pending = used
    }
    sortedButReversed.reverse.toSeq
  }

  def strContainsType(candidateName:String, str: String, fullName: String): Boolean = {
    def isMatch(regex: String): Boolean = {
      regex.r.findFirstIn(str).isDefined
    }

    val types = findReferredTypes(str.parseJson)

    val namespace = fullName.split("\\.") match {
      case x if x.length == 1 => ""
      case x => x.dropRight(1).mkString("\\.")
    }
    val namespaceRegex = "\\\"namespace\\\"\\s*:\\s*\\\"" + namespace + "\\\""
    val isSameNamespace = isMatch(namespaceRegex)
    val simpleTypeName = fullName.split("\\.").last

    val simpleCandidateName = candidateName.split("\\.").last

    val withoutSelf = types
      .filter(x => x != candidateName)
      .filter(x => if (isSameNamespace) x != simpleCandidateName else true)

    def isReferred(name:String): Boolean = {
      withoutSelf.contains(name)
    }

    isReferred(fullName) || (isSameNamespace && isReferred(simpleTypeName))
  }

  def findReferredTypes(strAsJson: JsValue): List[String] = {
    val fields = strAsJson.asJsObject.fields.getOrElse(Keys.Fields, JsArray.empty).asInstanceOf[JsArray]
    val referredTypes = for {
      element <- fields.elements
      (key, typeValue) <- element.asJsObject.fields
      if key == Keys.Type
    } yield {
      typeValue match {
        case s: JsString =>
          List(s.value)
        case a: JsArray =>
          val elementsAsString = a.elements.map {
            case s: JsString => s.value
            case o: AnyRef => o.toString
          }
          List(elementsAsString: _*)
        case _ =>
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
          val fields = typeValue.asJsObject.fields
          val typeOfRef = fields(Keys.Type)
          typeOfRef match {
            case JsString(Keys.Array) => List(fields(Keys.Items).convertTo[String])
            case JsString(Keys.Enum) => List(fields(Keys.Name).convertTo[String])
            case _ => List.empty
          }
      }
    }
    referredTypes.flatten.toList
  }

  def usedUnusedSchemas(files: Traversable[File]): (Traversable[File], Traversable[File]) = {
    val usedUnused = files.map { f =>
      val fullName = extractFullName(f)
      (f, files.count { candidate =>
        val candidateName = extractFullName(candidate)
        strContainsType(candidateName, fileText(candidate), fullName)
      } )
    }.partition(_._2 > 0)
    (usedUnused._1.map(_._1), usedUnused._2.map(_._1))
  }

  def extractFullName(f: File): String = {
    val txt = fileText(f)
    val namespace = namespaceRegex.findFirstMatchIn(txt)
    val name = nameRegex.findFirstMatchIn(txt)
    val nameGroup = name.get.group(1)
    if(namespace.isEmpty) {
      nameGroup
    } else {
      s"${namespace.get.group(1)}.$nameGroup"
    }
  }

  def fileText(f: File): String = {
    val src = Source.fromFile(f)
    try {
      src.getLines.mkString
    } finally {
      src.close()
    }
  }

  val namespaceRegex = "\\\"namespace\\\"\\s*:\\s*\"([^\\\"]+)\\\"".r
  val nameRegex = "\\\"name\\\"\\s*:\\s*\"([^\\\"]+)\\\"".r
}
