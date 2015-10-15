package sbtavrohugger;

import avrohugger.Generator
import java.io.File
import sbt.Keys._
import sbt.{Logger, globFilter, singleFileFinder}
import sbt.Path._

object FileWriter {

  private[sbtavrohugger] def generateCaseClasses(
    generator: Generator,
    imports: Seq[File],
    srcDir: File,
    target: File,
    log: Logger): Set[java.io.File] = {

    val importsGroupedByExtension = imports.groupBy(_.ext)

    def getSchemasFor(extension: String): Seq[File] = {
      val importsForExtension = importsGroupedByExtension.getOrElse(extension, Seq.empty)
      val sortedSchemas = {
        val allSchemas = (srcDir ** s"*.$extension").get
        if (extension == "avsc") {
          AVSCFileSorter.sortSchemaFiles(allSchemas)
        } else {
          allSchemas
        }

      }
      importsForExtension ++ (sortedSchemas --- importsForExtension).get
    }

    for (idl <- getSchemasFor(extension = "avdl")) {
      log.info("Compiling Avro IDL %s".format(idl))
      generator.fileToFile(idl, target.getPath)
    }

    for (inFile <- getSchemasFor(extension = "avsc")) {
      log.info("Compiling AVSC %s".format(inFile))
      generator.fileToFile(inFile, target.getPath)
    }

    for (inFile <- getSchemasFor(extension = "avro")) {
      log.info("Compiling Avro datafile %s".format(inFile))
      generator.fileToFile(inFile, target.getPath)
    }

    for (protocol <- getSchemasFor(extension = "avpr")) {
      log.info("Compiling Avro protocol %s".format(protocol))
      generator.fileToFile(protocol, target.getPath)
    }

    (target ** ("*.java"|"*.scala")).get.toSet
  }

}
