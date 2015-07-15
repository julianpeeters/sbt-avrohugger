package sbtavrohugger;

import avrohugger._
import avrohugger.specific._

import java.io.File
import scala.collection.JavaConverters._
import scala.util.Try

import org.apache.avro.generic.GenericData.StringType

import sbt.Classpaths
import sbt.Compile
import sbt.ConfigKey.configurationToKey
import sbt.FileFunction
import sbt.FilesInfo
import sbt.Keys._
import sbt.Logger
import sbt.Plugin
import sbt.Scoped._
import sbt.Setting
import sbt.SettingKey
import sbt.TaskKey
import sbt.config
import sbt.globFilter
import sbt.inConfig
import sbt.richFile
import sbt.singleFileFinder
import sbt.toGroupID

/**
 * Simple plugin for generating the Scala sources for Avro schemas and protocols.
 */
object SbtAvrohugger extends Plugin {

  val avroConfig = config("avro")

  val generate = TaskKey[Seq[File]]("generate", "Generate Scala sources for the Avro files.")

  val generateSpecific = TaskKey[Seq[File]]("generate-specific", "Generate Scala sources for the Avro files.")

  val inputDir = sourceDirectory <<= (sourceDirectory in Compile) { _ / "avro" }

  val outputDir = scalaSource <<= (sourceManaged in Compile) { _ / "generated_case_classes" }

  val classPath = managedClasspath <<= (classpathTypes, update) map { (ct, report) =>
      Classpaths.managedJars(avroConfig, ct, report)
    }

  lazy val avroSettings: Seq[Setting[_]] = inConfig(avroConfig)(Seq[Setting[_]](
    inputDir,
    outputDir,
    classPath,
    generate <<= caseClassGeneratorTask)) ++ Seq[Setting[_]](
    sourceGenerators in Compile <+= (generate in avroConfig),
    managedSourceDirectories in Compile <+= (scalaSource in avroConfig),
    cleanFiles <+= (scalaSource in avroConfig),
    ivyConfigurations += avroConfig)

  lazy val specificAvroSettings: Seq[Setting[_]] = inConfig(avroConfig)(Seq[Setting[_]](
    inputDir,
    outputDir,
    classPath,
    generateSpecific <<= specificCaseClassGeneratorTask)) ++ Seq[Setting[_]](
    sourceGenerators in Compile <+= (generateSpecific in avroConfig),
    managedSourceDirectories in Compile <+= (scalaSource in avroConfig),
    cleanFiles <+= (scalaSource in avroConfig),
    ivyConfigurations += avroConfig)


  /**
   * Tasks and methods for generating Scala case classes
   */ 
  private def generateCaseClasses[T <: GeneratorBase](generator: T , srcDir: File, target: File, log: Logger): Set[java.io.File] = {

    for (idl <- (srcDir ** "*.avdl").get) {
      log.info("Compiling Avro IDL %s".format(idl))
      generator.fromFile(idl, target.getPath)
    }

    for (inFile <- (srcDir ** "*.avsc").get) {
      log.info("Compiling AVSC %s".format(inFile))
      generator.fromFile(inFile, target.getPath)
    }

    for (inFile <- (srcDir ** "*.avro").get) {
      log.info("Compiling Avro datafile %s".format(inFile))
      generator.fromFile(inFile, target.getPath)
    }

    for (protocol <- (srcDir ** "*.avpr").get) {
      log.info("Compiling Avro protocol %s".format(protocol))
      generator.fromFile(protocol, target.getPath)
    }

    (target ** "*.scala").get.toSet
  }


  private def caseClassGeneratorTask = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    target) map {
      (out, srcDir, targetDir, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator
            generateCaseClasses(generator, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

  private def specificCaseClassGeneratorTask = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    target) map {
      (out, srcDir, targetDir, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new SpecificGenerator
            generateCaseClasses(generator, srcDir, targetDir, out.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
