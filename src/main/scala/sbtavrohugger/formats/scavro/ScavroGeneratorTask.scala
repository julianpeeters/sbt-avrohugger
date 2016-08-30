package sbtavrohugger
package formats
package scavro

import avrohugger.Generator
import avrohugger.format.Scavro
import AvrohuggerSettings.{avroScalaCustomEnumStyle, avroScalaCustomNamespace, avroScalaCustomTypes}
import java.io.File

import sbt.Keys._
import sbt.{FileFunction, FilesInfo, Logger, globFilter, richFile, singleFileFinder}

object ScavroGeneratorTask {

  private[sbtavrohugger] def scavroCaseClassGeneratorTask(
    avroConfig: sbt.Configuration) = (streams,
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
            val generator = new Generator(Scavro, customTypes, customNamespace, customEnumStyle)
            FileWriter.generateCaseClasses(generator, srcDir, targetDir, out.log, inFilter, exFilter)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
