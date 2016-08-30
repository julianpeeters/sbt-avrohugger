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
    includeFilter in avroConfig,
    excludeFilter in avroConfig) map {
      (out, srcDir, targetDir, customTypes, customNamespace, customEnumStyle, cache, inFilter, exFilter) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val gen = new Generator(Standard, customTypes, customNamespace, customEnumStyle)
            FileWriter.generateCaseClasses(gen, srcDir, targetDir, out.log, inFilter, exFilter)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
  }

}
