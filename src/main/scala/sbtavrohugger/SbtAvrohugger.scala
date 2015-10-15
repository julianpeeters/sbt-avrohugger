package sbtavrohugger

import sbt.KeyRanks._
import settings.{ AvroSettings, SpecificAvroSettings }

import java.io.File
import scala.collection.JavaConverters._

import sbt.Keys._
import sbt._

/**
 * Simple plugin for generating the Scala sources for Avro IDL, schemas and protocols.
 */
object SbtAvrohugger extends Plugin {

  val avroConfig = config("avro")

  val imports = SettingKey[Seq[File]]("imports", "Ordered list of files for Avro to import before applying recursive " +
    "generation. Sometimes necessary for multiple Avro schema files that reference types defined within each " +
    "other", AMinusSetting)

  val importFiles = imports := Seq.empty[File]
  val inputDir = sourceDirectory <<= (sourceDirectory in Compile) { _ / "avro" }
  val outputDir = scalaSource <<= (sourceManaged in Compile) { _ / "" }

  val classPath = managedClasspath <<= (classpathTypes, update) map {
    (ct, report) => Classpaths.managedJars(avroConfig, ct, report)
  }

  lazy val avroSettings: Seq[Setting[_]] = {
    AvroSettings.getSettings(avroConfig, importFiles, inputDir, outputDir, classPath)
  }

  lazy val specificAvroSettings: Seq[Setting[_]] = {
    SpecificAvroSettings.getSettings(avroConfig, importFiles, inputDir, outputDir, classPath)
  }

}
