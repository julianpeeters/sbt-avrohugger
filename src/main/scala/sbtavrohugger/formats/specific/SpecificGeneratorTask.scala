package sbtavrohugger
package formats
package specific

import avrohugger.Generator
import avrohugger.format.{ Standard, SpecificRecord }

import java.io.File

import AvrohuggerSettings.{
  avroScalaCustomTypes,
  avroScalaCustomNamespace,
  avroScalaCustomEnumStyle
}

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
    avroScalaCustomEnumStyle in avroConfig,
    target) map {
      (out, srcDir, targetDir, customTypes, customNamespace, customEnumStyle, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator(SpecificRecord, customTypes, customNamespace, customEnumStyle)
            FileWriter.generateCaseClasses(generator, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
