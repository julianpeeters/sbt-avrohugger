package sbtavrohugger
package formats
package standard

import avrohugger.Generator
import avrohugger.format.Standard

import AvrohuggerSettings.{ scalaCustomTypes, scalaCustomNamespace }

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
    scalaCustomTypes in avroConfig,
    scalaCustomNamespace in avroConfig,
    target) map {
      (out, srcDir, targetDir, customTypes, customNamespace, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val gen = new Generator(Standard, customTypes, customNamespace)
            FileWriter.generateCaseClasses(gen, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
  }

}
