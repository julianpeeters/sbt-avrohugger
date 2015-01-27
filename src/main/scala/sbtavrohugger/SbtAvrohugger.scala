package sbtavrohugger;

import avrohugger._

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

  val stringType = SettingKey[String]("string-type", "Type for representing strings. " +
    "Possible values: CharSequence, String, Utf8. Default: CharSequence.")

  val generate = TaskKey[Seq[File]]("generate", "Generate Scala sources for the Avro files.")

  lazy val avroSettings: Seq[Setting[_]] = inConfig(avroConfig)(Seq[Setting[_]](
    sourceDirectory <<= (sourceDirectory in Compile) { _ / "avro" },
    scalaSource <<= (sourceManaged in Compile) { _ / "generated_case_classes" },
    stringType := "CharSequence",
    version := "1.7.3",

    managedClasspath <<= (classpathTypes, update) map { (ct, report) =>
      Classpaths.managedJars(avroConfig, ct, report)
    },

    generate <<= caseClassGeneratorTask)) ++ Seq[Setting[_]](
    sourceGenerators in Compile <+= (generate in avroConfig),
    managedSourceDirectories in Compile <+= (scalaSource in avroConfig),
    cleanFiles <+= (scalaSource in avroConfig),
    libraryDependencies += ("com.julianpeeters" % "avrohugger-core_2.10" % "0.0.1"),
    ivyConfigurations += avroConfig)


  /**
   * Tasks and methods for generating Scala case classes
   */ 
  private def generateCaseClasses(srcDir: File, target: File, log: Logger, stringTypeName: String) = {
    val stringType = StringType.valueOf(stringTypeName);
    log.info("Avro compiler using stringType=%s".format(stringType));

    val generator = new Generator
/*
    // TODO: generate case classes from .avdl files

    for (idl <- (srcDir ** "*.avdl").get) {
      log.info("Compiling Avro IDL %s".format(idl))
      val parser = new Idl(idl.asFile)
      val protocol = Protocol.parse(parser.CompilationUnit.toString)
    // perhaps convert to a schema?
    // something like: protocol.getTypes.asScala.foreach(...) ?
    }
*/

    for (inFile <- (srcDir ** "*.avsc").get) {
      log.info("Generating Scala case class")// %es".format(inFile))
      Try {
        generator.fromFile(inFile)
      }
      
    }

    for (inFile <- (srcDir ** "*.avro").get) {
      log.info("Generating Scala case class")// %es".format(inFile))
      Try {
        generator.fromFile(inFile)
      }

    }

    for (protocol <- (srcDir ** "*.avpr").get) {
      log.info("Compiling Avro protocol %s".format(protocol))
        generator.fromFile(protocol)
    }

    (target ** "*.scala").get.toSet
  }


  private def caseClassGeneratorTask = (streams,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    stringType,
    cacheDirectory) map {
      (out, srcDir, targetDir, stringTypeName, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            generateCaseClasses(srcDir, targetDir, out.log, stringTypeName)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
