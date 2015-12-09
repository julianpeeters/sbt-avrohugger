package sbtavrohugger
package formats
package specific

import avrohugger.Generator
import avrohugger.format.{ Standard, SpecificRecord }

import java.io.File

import AvrohuggerSettings.{ avroScalaCustomTypes, avroScalaCustomNamespace }

import sbt.Keys._
import sbt.{
  FileFunction,
  FilesInfo,
  Logger,
  globFilter,
  richFile,
  singleFileFinder
}

object SpecificGeneratorTask {

  private[sbtavrohugger] def specificCaseClassGeneratorTask(
    avroConfig: sbt.Configuration) = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    avroScalaCustomTypes in avroConfig,
    avroScalaCustomNamespace in avroConfig,
    target) map {
      (o, srcDir, targetDir, customTypes, customNamespace, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator(SpecificRecord, customTypes, customNamespace)
            FileWriter.generateCaseClasses(generator, srcDir, targetDir, o.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
