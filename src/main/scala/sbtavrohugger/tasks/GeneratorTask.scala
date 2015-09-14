package sbtavrohugger
package tasks

import avrohugger.Generator
import avrohugger.format.{ Standard, SpecificRecord }

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
    target) map {
      (o, srcDir, targetDir, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator(Standard)
            FileWriter.generateCaseClasses(generator, srcDir, targetDir, o.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
  }

}
