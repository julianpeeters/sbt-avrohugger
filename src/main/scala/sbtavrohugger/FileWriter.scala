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

    def getSrcFiles(dirs: Seq[File], fileExtension: String) = for {
      srcDir <- dirs
      srcFile <- (srcDir ** s"*.$fileExtension").get
    } yield srcFile

    val avscFiles = AvdlFileSorter.sortSchemaFiles(getSrcFiles(srcDirs, "avsc")).toList
    if (avscFiles.nonEmpty) {
      log.info("Compiling AVSC files \n%s".format(avscFiles.mkString("\n")))
      generator.filesToFiles(avscFiles, target.getPath)
    }

    val avdlFiles = AvdlFileSorter.sortSchemaFiles(getSrcFiles(srcDirs, "avdl")).toList
    if (avdlFiles.nonEmpty) {
      log.info("Compiling Avro IDL files \n%s".format(avdlFiles.mkString("\n")))
      generator.filesToFiles(avdlFiles, target.getPath)
    }

    val avroFiles = getSrcFiles(srcDirs, "avro").toList
    if (avroFiles.nonEmpty) {
      log.info("Compiling Avro datafiles \n%s".format(avroFiles.mkString("\n")))
      generator.filesToFiles(avroFiles, target.getPath)
    }

    val avprFiles = getSrcFiles(srcDirs, "avpr").toList
    if (avprFiles.nonEmpty) {
      log.info("Compiling Avro protocols \n%s".format(avprFiles.mkString("\n")))
      generator.filesToFiles(avprFiles, target.getPath)
    }

    (target ** ("*.java" | "*.scala")).get.toSet
  }

}
