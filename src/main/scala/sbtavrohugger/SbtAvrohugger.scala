package sbtavrohugger

import avrohugger.Generator
import avrohugger.format.{Scavro, SpecificRecord, Standard}

import java.io.File
import scala.collection.JavaConverters._

import sbt.Keys._
import sbt._

/**
 * Simple plugin for generating the Scala sources for Avro IDL, schemas and protocols.
 */
object SbtAvrohugger extends AutoPlugin {
  
  object autoImport {
    // Scavro Format
    lazy val avroScalaGenerateScavro        = taskKey[Seq[File]]("Generate Scala sources for Scavro")
    lazy val avroScavroSourceDirectory      = settingKey[File]("Avro schema directory for generating Scavro Scala")
    lazy val avroScavroScalaSource          = settingKey[File]("Scavro Scala source directory for compiled avro")
    lazy val avroScalaScavroCustomTypes     = settingKey[Map[String, Class[_]]]("Customize Avro to Scala type map by type")
    lazy val avroScalaScavroCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Scavro Scala code")
    lazy val avroScalaScavroCustomEnumStyle = settingKey[Map[String, String]]("Custom enum style of generated Scavro Scala code")
    // SpecificRecord Format
    lazy val avroScalaGenerateSpecific        = taskKey[Seq[File]]("Generate Scala sources implementing SpecificRecord")
    lazy val avroSpecificSourceDirectory      = settingKey[File]("Avro schema directory for generating SpecificRecord")
    lazy val avroSpecificScalaSource          = settingKey[File]("Specific Scala source directory for compiled avro")
    lazy val avroScalaSpecificCustomTypes     = settingKey[Map[String, Class[_]]]("Custom Avro to Scala type map")
    lazy val avroScalaSpecificCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Specific Scala code")
    lazy val avroScalaSpecificCustomEnumStyle = settingKey[Map[String, String]]("Custom enum style of generated Specific Scala code")
    // Standard Format
    lazy val avroScalaGenerate        = taskKey[Seq[File]]("Generate Scala sources from avro files")
    lazy val avroSourceDirectory      = settingKey[File]("Avro schema directory for Scala code generation")
    lazy val avroScalaSource          = settingKey[File]("Scala source directory for compiled avro")
    lazy val avroScalaCustomTypes     = settingKey[Map[String, Class[_]]]("Custom Avro to Scala type map")
    lazy val avroScalaCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Scala code")
    lazy val avroScalaCustomEnumStyle = settingKey[Map[String, String]]("Custom enum style of generated Scala code")
  }
    
  import autoImport._
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements
  
  override lazy val projectSettings: Seq[Def.Setting[_]] = inConfig(Compile)(
    avroSettings ++
    scavroSettings ++
    specificAvroSettings)

  // Standard Format
  lazy val avroSettings: Seq[Def.Setting[_]] = Seq(
    avroScalaSource               := sourceManaged.value / "compiled_avro",
    avroSourceDirectory           := sourceDirectory.value / "avro",
    avroScalaCustomTypes          := Map.empty[String, Class[_]],
    avroScalaCustomNamespace      := Map.empty[String, String],
    avroScalaCustomEnumStyle      := Map.empty[String, String],
    logLevel in avroScalaGenerate := (logLevel?? Level.Info).value,
    avroScalaGenerate := {
      val cache = target.value
      val srcDir = avroSourceDirectory.value
      val targetDir = avroScalaSource.value
      val out = streams.value
      val cachedCompile = FileFunction.cached(cache / "avro",
        inStyle = FilesInfo.lastModified,
        outStyle = FilesInfo.exists) { (in: Set[File]) =>
          val isNumberOfFieldsRestricted = scalaVersion.value == "2.10"
          val gen = new Generator(
            Standard,
            avroScalaCustomTypes.value,
            avroScalaCustomNamespace.value,
            avroScalaCustomEnumStyle.value,
            isNumberOfFieldsRestricted)
          FileWriter.generateCaseClasses(gen, srcDir, targetDir, out.log)
        }
      cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
  )
  
  // Scavro Format
  lazy val scavroSettings: Seq[Def.Setting[_]] = Seq(
    avroScavroScalaSource          := sourceManaged.value / "compiled_avro",
    avroScavroSourceDirectory      := sourceDirectory.value / "avro",
    avroScalaScavroCustomTypes     := Map.empty[String, Class[_]],
    avroScalaScavroCustomNamespace := Map.empty[String, String],
    avroScalaScavroCustomEnumStyle := Map.empty[String, String],
    logLevel in avroScalaGenerateScavro  := (logLevel?? Level.Info).value,
    avroScalaGenerateScavro := {
      val cache = target.value
      val srcDir = avroScavroSourceDirectory.value
      val targetDir = avroScavroScalaSource.value
      val out = streams.value
      val cachedCompile = FileFunction.cached(cache / "avro",
        inStyle = FilesInfo.lastModified,
        outStyle = FilesInfo.exists) { (in: Set[File]) =>
          val isNumberOfFieldsRestricted = scalaVersion.value == "2.10"
          val gen = new Generator(
            Scavro,
            avroScalaScavroCustomTypes.value,
            avroScalaScavroCustomNamespace.value,
            avroScalaScavroCustomEnumStyle.value,
            isNumberOfFieldsRestricted)
          FileWriter.generateCaseClasses(gen, srcDir, targetDir, out.log)
        }
      cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
  )
  
  // SpecificRecord Format
  lazy val specificAvroSettings: Seq[Def.Setting[_]] = Seq(
    avroSpecificScalaSource := sourceManaged.value / "compiled_avro",
    avroSpecificSourceDirectory := sourceDirectory.value / "avro",
    avroScalaSpecificCustomTypes := Map.empty[String, Class[_]],
    avroScalaSpecificCustomNamespace := Map.empty[String, String],
    avroScalaSpecificCustomEnumStyle := Map.empty[String, String],
    logLevel in avroScalaGenerateSpecific := (logLevel?? Level.Info).value,
    avroScalaGenerateSpecific := {
      val cache = target.value
      val srcDir = avroSpecificSourceDirectory.value
      val targetDir = avroSpecificScalaSource.value
      val out = streams.value
      val cachedCompile = FileFunction.cached(cache / "avro",
        inStyle = FilesInfo.lastModified,
        outStyle = FilesInfo.exists) { (in: Set[File]) =>
          val isNumberOfFieldsRestricted = scalaVersion.value == "2.10"
          val gen = new Generator(
            SpecificRecord,
            avroScalaSpecificCustomTypes.value,
            avroScalaSpecificCustomNamespace.value,
            avroScalaSpecificCustomEnumStyle.value,
            isNumberOfFieldsRestricted)
          FileWriter.generateCaseClasses(gen, srcDir, targetDir, out.log)
        }
      cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }
  )
}
