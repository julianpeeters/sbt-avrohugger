package sbtavrohugger
package formats
package scavro

import avrohugger.Generator
import avrohugger.format.Scavro

import AvrohuggerSettings.{
  avroScalaCustomTypes,
  avroScalaCustomNamespace,
  avroScalaCustomEnumStyle
}

import java.io.File

import sbt.Keys._
import sbt.{
  FileFunction,
  FilesInfo,
  Logger,
  globFilter,
  richFile,
  singleFileFinder
}

object ScavroGeneratorTask {

  private[sbtavrohugger] def scavroCaseClassGeneratorTask(
    avroConfig: sbt.Configuration) = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    avroScalaCustomTypes in avroConfig,
    avroScalaCustomNamespace in avroConfig,
    avroScalaCustomEnumStyle in avroConfig,
    target,
    scalaBinaryVersion) map {
      (out, srcDir, targetDir, customTypes, customNamespace, customEnumStyle, cache, scalaVersion) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val isNumberOfFieldsRestricted = scalaVersion == "2.10"
            val generator = new Generator(Scavro, customTypes, customNamespace, customEnumStyle, isNumberOfFieldsRestricted)
            FileWriter.generateCaseClasses(generator, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
