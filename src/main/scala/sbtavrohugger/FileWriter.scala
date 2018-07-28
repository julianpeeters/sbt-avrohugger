package sbtavrohugger;

import avrohugger.filesorter.{AvdlFileSorter, AvscFileSorter}
import avrohugger.Generator
import java.io.File

import sbt.Keys._
import sbt.{Logger, globFilter, singleFileFinder}
import sbt.Path._

object FileWriter {

  private[sbtavrohugger] def generateCaseClasses(
    generator: Generator,
    srcDirs: Seq[File],
    target: File,
    log: Logger): Set[java.io.File] = {
    log.info("Considering source directories %s".format(srcDirs.mkString(",")))

    for {
      srcDir <- srcDirs
      inFile <- AvscFileSorter.sortSchemaFiles((srcDir ** "*.avsc").get)
    } {
      log.info("Compiling AVSC %s to %s".format(inFile, target.getPath))
      generator.fileToFile(inFile, target.getPath)
    }

    for {
      srcDir <- srcDirs
      idl <- AvdlFileSorter.sortSchemaFiles((srcDir ** "*.avdl").get)
    } {
      log.info("Compiling Avro IDL %s".format(idl))
      generator.fileToFile(idl, target.getPath)
    }

    for {
      srcDir <- srcDirs
      inFile <- (srcDir ** "*.avro").get
    } {
      log.info("Compiling Avro datafile %s".format(inFile))
      generator.fileToFile(inFile, target.getPath)
    }

    for {
      srcDir <- srcDirs
      protocol <- (srcDir ** "*.avpr").get
    } {
      log.info("Compiling Avro protocol %s".format(protocol))
      generator.fileToFile(protocol, target.getPath)
    }

    (target ** ("*.java"|"*.scala")).get.toSet
  }

}
