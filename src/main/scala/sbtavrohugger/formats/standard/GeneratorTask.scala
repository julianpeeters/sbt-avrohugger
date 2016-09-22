package sbtavrohugger
package formats
package standard

import avrohugger.Generator
import avrohugger.format.Standard

import AvrohuggerSettings.{
  avroScalaCustomTypes,
  avroScalaCustomNamespace,
  avroScalaCustomEnumStyle
}

import java.io.File

import sbt.Keys._
import sbt.{
  Configuration => Config,
  FileFunction,
  FilesInfo,
  Logger,
  globFilter,
  richFile,
  singleFileFinder }

object GeneratorTask {

  private[sbtavrohugger] def caseClassGeneratorTask(avroConfig: Config) = {
    (streams,
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
            val gen = new Generator(Standard, customTypes, customNamespace, customEnumStyle, isNumberOfFieldsRestricted)
            FileWriter.generateCaseClasses(gen, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
  }

}
