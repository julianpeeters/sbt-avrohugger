package sbtavrohugger
package tasks

import settings.AvrohuggerSettings.{ scalaCustomTypes, scalaCustomNamespace }


import avrohugger.Generator
import avrohugger.format.SpecificRecord

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

object SpecificGeneratorTask {

  private[sbtavrohugger] def specificCaseClassGeneratorTask(
    avroConfig: sbt.Configuration) = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    scalaCustomTypes in avroConfig,
    scalaCustomNamespace in avroConfig,
    target) map {
      (o, srcDir, targetDir, customTypes, customNamespaces, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator(SpecificRecord, customTypes, customNamespaces)
            FileWriter.generateCaseClasses(generator, srcDir, targetDir, o.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
