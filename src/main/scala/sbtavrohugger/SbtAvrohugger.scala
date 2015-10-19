package sbtavrohugger

import formats.specific.SpecificAvroSettings
import formats.standard.AvroSettings
import formats.scavro.ScavroSettings

import java.io.File
import scala.collection.JavaConverters._

import sbt.Keys._
import sbt.{
  Classpaths,
  Compile,
  Plugin,
  Setting,
  config,
  richFile
}

/**
 * Simple plugin for generating the Scala sources for Avro IDL, schemas and protocols.
 */
object SbtAvrohugger extends Plugin {

  val avroConfig = config("avro")

  val inputDir = sourceDirectory <<= (sourceDirectory in Compile) { _ / "avro" }
  val outputDir = scalaSource <<= (sourceManaged in Compile) { _ / "" }

  val classPath = managedClasspath <<= (classpathTypes, update) map {
    (ct, report) => Classpaths.managedJars(avroConfig, ct, report)
  }

  lazy val avroSettings: Seq[Setting[_]] = {
    AvroSettings.getSettings(avroConfig, inputDir, outputDir, classPath)
  }

  lazy val scavroSettings: Seq[Setting[_]] = {
    ScavroSettings.getSettings(avroConfig, inputDir, outputDir, classPath)
  }

  lazy val specificAvroSettings: Seq[Setting[_]] = {
    SpecificAvroSettings.getSettings(avroConfig, inputDir, outputDir, classPath)
  }

}
