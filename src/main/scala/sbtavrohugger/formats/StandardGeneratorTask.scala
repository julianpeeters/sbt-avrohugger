package sbtavrohugger
package formats

import avrohugger.Generator
import avrohugger.format.Standard

import java.io.File

import sbt.Keys._
import sbt._

object StandardGeneratorTask {
  def run(
    cache: File,
    isNumberOfFieldsRestricted: Boolean,
    srcDir: File,
    targetDir: File,
    out: TaskStreams,
    customNamespace: Map[String, String],
    customTypes: Map[String, Class[_]],
    customEnumStyle: Map[String,String]): Seq[File] = {
    val cachedCompile = FileFunction.cached(cache / "avro",
      inStyle = FilesInfo.lastModified,
      outStyle = FilesInfo.exists) { (in: Set[File]) =>
        val gen = new Generator(Standard, customTypes, customNamespace, customEnumStyle, isNumberOfFieldsRestricted)
        FileWriter.generateCaseClasses(gen, srcDir, targetDir, out.log)
      }
    cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
  }
}