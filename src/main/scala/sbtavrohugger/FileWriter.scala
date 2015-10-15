package sbtavrohugger;

import avrohugger.Generator
import java.io.File
import sbt.Keys._
import sbt.{Logger, globFilter, singleFileFinder}
import sbt.Path._

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

}
