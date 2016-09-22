package sbtavrohugger

import java.io.File

import avrohugger.Generator
import sbt.{FileFilter, Logger, globFilter, singleFileFinder}
import sbtavrohugger.filesorter.AVSCFileSorter

object FileWriter {

  private[sbtavrohugger] def generateCaseClasses(
    generator: Generator,
    srcDir: File,
    target: File,
    log: Logger,
    inFilter: FileFilter,
    exFilter: FileFilter): Set[java.io.File] = {

    for (inFile <- AVSCFileSorter.sortSchemaFiles((srcDir ** "*.avsc").get) if inFilter.accept(inFile) && !exFilter.accept(inFile)) {
      log.info("Compiling AVSC %s".format(inFile))
      generator.fileToFile(inFile, target.getPath)
    }

    for (idl <- (srcDir ** "*.avdl").get if inFilter.accept(idl) && !exFilter.accept(idl)) {
      log.info("Compiling Avro IDL %s".format(idl))
      generator.fileToFile(idl, target.getPath)
    }

    for (inFile <- (srcDir ** "*.avro").get if inFilter.accept(inFile) && !exFilter.accept(inFile)) {
      log.info("Compiling Avro datafile %s".format(inFile))
      generator.fileToFile(inFile, target.getPath)
    }

    for (protocol <- (srcDir ** "*.avpr").get if inFilter.accept(protocol) && !exFilter.accept(protocol)) {
      log.info("Compiling Avro protocol %s".format(protocol))
      generator.fileToFile(protocol, target.getPath)
    }

    (target ** ("*.java"|"*.scala")).get.toSet
  }

}
