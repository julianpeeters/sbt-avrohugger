package sbtavrohugger;

import avrohugger.Generator
import avrohugger.format.{ Standard, SpecificRecord }

import java.io.File

import sbt.Keys._
import sbt.{ FileFunction, FilesInfo, Logger, globFilter, richFile, singleFileFinder }

object FileWriter {

  private[sbtavrohugger] def generateCaseClasses(
    generator: Generator,
    srcDir: File,
    target: File,
    log: Logger): Set[java.io.File] = {

    for (idl <- (srcDir ** "*.avdl").get) {
      log.info("Compiling Avro IDL %s".format(idl))
      generator.fileToFile(idl, target.getPath)
    }

    for (inFile <- AVSCFileSorter.sortSchemaFiles((srcDir ** "*.avsc").get)) {
      log.info("Compiling AVSC %s".format(inFile))
      generator.fileToFile(inFile, target.getPath)
    }

    for (inFile <- (srcDir ** "*.avro").get) {
      log.info("Compiling Avro datafile %s".format(inFile))
      generator.fileToFile(inFile, target.getPath)
    }

    for (protocol <- (srcDir ** "*.avpr").get) {
      log.info("Compiling Avro protocol %s".format(protocol))
      generator.fileToFile(protocol, target.getPath)
    }

    (target ** ("*.java"|"*.scala")).get.toSet
  }
/*

  private[sbtavrohugger] def caseClassGeneratorTask(avroConfig: sbt.Configuration) = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    target) map {
      (out, srcDir, targetDir, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator(Standard)
            generateCaseClasses(generator, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

  private[sbtavrohugger] def specificCaseClassGeneratorTask(avroConfig: sbt.Configuration) = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    target) map {
      (out, srcDir, targetDir, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator(SpecificRecord)
            generateCaseClasses(generator, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
*/
}
