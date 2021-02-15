package sbtavrohugger

import avrohugger.Generator
import avrohugger.format.{Scavro, SpecificRecord, Standard}
import avrohugger.types.AvroScalaTypes
import java.io.File

import sbt.Keys._
import sbt._
import sbt.internal.inc.classpath.ClasspathUtilities
import java.net.URLClassLoader
import java.net.URL

/**
 * Simple plugin for generating the Scala sources for Avro IDL, schemas and protocols.
 */
object SbtAvrohugger extends AutoPlugin {
  
  object autoImport {
    
    // sbt tasks:
    lazy val avroScalaGenerateScavro   = taskKey[Seq[File]]("Generate Scala sources for Scavro")
    lazy val avroScalaGenerateSpecific = taskKey[Seq[File]]("Generate Scala sources implementing SpecificRecord")
    lazy val avroScalaGenerate         = taskKey[Seq[File]]("Generate Scala sources from avro files")

    // sbt settings
      // Scavro Format
    lazy val avroScavroSourceDirectories      = settingKey[Seq[File]]("Avro schema directory for generating Scavro Scala")
    lazy val avroScavroScalaSource          = settingKey[File]("Scavro Scala source directory for compiled avro")
    lazy val avroScalaScavroCustomTypes     = settingKey[AvroScalaTypes]("Customize Avro to Scala type map by type")
    lazy val avroScalaScavroCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Scavro Scala code")
      // SpecificRecord Format
    lazy val avroSpecificSourceDirectories      = settingKey[Seq[File]]("Avro schema directory for generating SpecificRecord")
    lazy val avroSpecificScalaSource          = settingKey[File]("Specific Scala source directory for compiled avro")
    lazy val avroScalaSpecificCustomTypes     = settingKey[AvroScalaTypes]("Custom Avro to Scala type map")
    lazy val avroScalaSpecificCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Specific Scala code")
      // Standard Format
    lazy val avroSourceDirectories      = settingKey[Seq[File]]("Avro schema directory for Scala code generation")
    lazy val avroScalaSource          = settingKey[File]("Scala source directory for compiled avro")
    lazy val avroScalaCustomTypes     = settingKey[AvroScalaTypes]("Custom Scala types of generated Scala code")
    lazy val avroScalaCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Scala code")
  }
    
  import autoImport._
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements
  
  lazy val baseSettings = 
    avroSettings ++
    scavroSettings ++
    specificAvroSettings
    
  override lazy val projectSettings: Seq[Def.Setting[_]] =
    inConfig(Compile)(baseSettings) ++
    inConfig(Test)(baseSettings) 
    
  // Standard Format
  lazy val avroSettings: Seq[Def.Setting[_]] = Seq(
    avroScalaSource               := sourceManaged.value / "compiled_avro",
    avroSourceDirectories         := Seq(sourceDirectory.value / "avro"),
    avroScalaCustomNamespace      := Map.empty[String, String],
    avroScalaCustomTypes          := Standard.defaultTypes,
    logLevel in avroScalaGenerate := (logLevel?? Level.Info).value,
    avroScalaGenerate := {
      val cache = target.value
      val srcDirs = avroSourceDirectories.value
      val targetDir = avroScalaSource.value
    
      val out = streams.value
      val scalaV = scalaVersion.value
      val customTypes = avroScalaCustomTypes.value
      val customNamespace = avroScalaCustomNamespace.value
      val res = (resourceDirectory in Compile).value
      val old = (scalaInstance in (Compile, compile)).value
      val classLoader = new java.net.URLClassLoader(Array(res.toURI().toURL()), old.loader)
      
      val cachedCompile = FileFunction.cached(
        cache / "avro",
        inStyle = FilesInfo.lastModified,
        outStyle = FilesInfo.exists
      ) { (in: Set[File]) =>
          val isNumberOfFieldsRestricted = scalaV == "2.10"
          val gen = new Generator(
            format = Standard,
            avroScalaCustomTypes = Some(customTypes),
            avroScalaCustomNamespace = customNamespace,
            restrictedFieldNumber = isNumberOfFieldsRestricted,
            classLoader)
          FileWriter.generateCaseClasses(gen, srcDirs, targetDir, out.log)
        }
        
      cachedCompile((srcDirs ** "*.av*").get.toSet).toSeq
    }
  )
  
  // Scavro Format
  lazy val scavroSettings: Seq[Def.Setting[_]] = Seq(
    avroScavroScalaSource          := sourceManaged.value / "compiled_avro",
    avroScavroSourceDirectories    := Seq(sourceDirectory.value / "avro"),
    avroScalaScavroCustomTypes     := Scavro.defaultTypes,
    avroScalaScavroCustomNamespace := Map.empty[String, String],
    logLevel in avroScalaGenerateScavro  := (logLevel?? Level.Info).value,
    avroScalaGenerateScavro := {
      val cache = target.value
      val srcDirs = avroScavroSourceDirectories.value
      val targetDir = avroScavroScalaSource.value
      val out = streams.value
      val scalaV = scalaVersion.value
      val scavroCustomTypes = avroScalaScavroCustomTypes.value
      val scavroCustomNamespace = avroScalaScavroCustomNamespace.value
      val res = (resourceDirectory in Compile).value
      val old = (scalaInstance in (Compile, compile)).value
      val classLoader = new java.net.URLClassLoader(Array(res.toURI().toURL()), old.loader)
      
      val cachedCompile = FileFunction.cached(cache / "avro",
        inStyle = FilesInfo.lastModified,
        outStyle = FilesInfo.exists) { (in: Set[File]) =>
          val isNumberOfFieldsRestricted = scalaV == "2.10"
          val gen = new Generator(
            Scavro,
            Some(scavroCustomTypes),
            scavroCustomNamespace,
            isNumberOfFieldsRestricted,
            classLoader)
          FileWriter.generateCaseClasses(gen, srcDirs, targetDir, out.log)
        }
      cachedCompile((srcDirs ** "*.av*").get.toSet).toSeq
    }
  )
  
  // SpecificRecord Format
  lazy val specificAvroSettings: Seq[Def.Setting[_]] = Seq(
    avroSpecificScalaSource := sourceManaged.value / "compiled_avro",
    avroSpecificSourceDirectories := Seq(sourceDirectory.value / "avro"),
    avroScalaSpecificCustomTypes := SpecificRecord.defaultTypes,
    avroScalaSpecificCustomNamespace := Map.empty[String, String],
    logLevel in avroScalaGenerateSpecific := (logLevel?? Level.Info).value,
    avroScalaGenerateSpecific := {
      val cache = target.value
      val srcDirs = avroSpecificSourceDirectories.value
      val targetDir = avroSpecificScalaSource.value
      val out = streams.value
      val scalaV = scalaVersion.value
      val specificCustomTypes = avroScalaSpecificCustomTypes.value
      val specificCustomNamespace = avroScalaSpecificCustomNamespace.value
      val res = (resourceDirectory in Compile).value
      val old = (scalaInstance in (Compile, compile)).value
      val classLoader = new java.net.URLClassLoader(Array(res.toURI().toURL()), old.loader)
      
      val cachedCompile = FileFunction.cached(cache / "avro",
        inStyle = FilesInfo.lastModified,
        outStyle = FilesInfo.exists) { (in: Set[File]) =>
          val isNumberOfFieldsRestricted = scalaV == "2.10"
          val gen = new Generator(
            SpecificRecord,
            Some(specificCustomTypes),
            specificCustomNamespace,
            isNumberOfFieldsRestricted,
            classLoader)
          FileWriter.generateCaseClasses(gen, srcDirs, targetDir, out.log)
        }
      cachedCompile((srcDirs ** "*.av*").get.toSet).toSeq
    }
  )
}
