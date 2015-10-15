package sbtavrohugger;

import scala.collection.mutable
import scala.io.Source
import java.io.File

object AVSCFileSorter {

  //The order in which avsc files are compiled depends on the underlying file system (i.e. under OSX its is alphabetical, under some linux distros it's not).
  //This is an issue when you have a record type that is used in different other types. Also makes sure
  //that dependent types are compiled in the correct order.
  // Code adapted from https://github.com/ch4mpy/sbt-avro/blob/master/src/main/scala/com/c4soft/sbtavro/SbtAvro.scala
  // by Jerome Wascongne

  def sortSchemaFiles(files: Traversable[File]): Seq[File] = {
    val sortedButReversed = mutable.MutableList.empty[File]
    var used: Traversable[File] = files
    while(!used.isEmpty) {
      val usedUnused = usedUnusedSchemas(used)
      sortedButReversed ++= usedUnused._2
      used = usedUnused._1
    }
    sortedButReversed.reverse.toSeq
  }

  def strContainsType(str: String, fullName: String): Boolean = {
    val simpleTypeName = fullName.split("\\.").last
    val namespace = fullName.split("\\.") match {
      case x if x.length == 1 => ""
      case x => x.dropRight(1).mkString("\\.")
    }
    val namespaceRegex = "\\\"namespace\\\"\\s*:\\s*\\\"" + namespace + "\\\""
    val simpleTypeRegex = "\\\"type\\\"\\s*:\\s*\\\"" + simpleTypeName + "\\\""
    val fullTypeRegex = "\\\"type\\\"\\s*:\\s*\\\"" + fullName + "\\\""
    def isMatch(regex: String): Boolean = {
      regex.r.findFirstIn(str).isDefined
    }
    isMatch(fullTypeRegex) || ( isMatch(namespaceRegex) && isMatch(simpleTypeRegex) )
  }
  
  def usedUnusedSchemas(files: Traversable[File]): (Traversable[File], Traversable[File]) = {
    val usedUnused = files.map { f =>
      val fullName = extractFullName(f)
      (f, files.count { candidate => strContainsType(fileText(candidate), fullName) } )
    }.partition(_._2 > 0)
    (usedUnused._1.map(_._1), usedUnused._2.map(_._1))
  }

  def extractFullName(f: File): String = {
    val txt = fileText(f)
    val namespace = namespaceRegex.findFirstMatchIn(txt)
    val name = nameRegex.findFirstMatchIn(txt)
    if(namespace == None) {
      return name.get.group(1)
    } else {
      return s"${namespace.get.group(1)}.${name.get.group(1)}"
    }
  }

  def fileText(f: File): String = {
    val src = Source.fromFile(f)
    try {
      return src.getLines.mkString
    } finally {
      src.close()
    }
  }

  val namespaceRegex = "\\\"namespace\\\"\\s*:\\s*\"([^\\\"]+)\\\"".r
  val nameRegex = "\\\"name\\\"\\s*:\\s*\"([^\\\"]+)\\\"".r
}