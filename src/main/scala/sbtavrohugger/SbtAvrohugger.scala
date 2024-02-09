package sbtavrohugger

import avrohugger.Generator
import avrohugger.format.{SpecificRecord, Standard}
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
    lazy val avroScalaGenerateSpecific = taskKey[Seq[File]]("Generate Scala sources implementing SpecificRecord")
    lazy val avroScalaGenerate         = taskKey[Seq[File]]("Generate Scala sources from avro files")

    // sbt settings
    // SpecificRecord Format
    lazy val avroSpecificSourceDirectories    = settingKey[Seq[File]]("Avro schema directory for generating SpecificRecord")
    lazy val avroSpecificScalaSource          = settingKey[File]("Specific Scala source directory for compiled avro")
    lazy val avroScalaSpecificCustomTypes     = settingKey[AvroScalaTypes]("Custom Avro to Scala type map")
    lazy val avroScalaSpecificCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Specific Scala code")
    // Standard Format
    lazy val avroSourceDirectories    = settingKey[Seq[File]]("Avro schema directory for Scala code generation")
    lazy val avroScalaSource          = settingKey[File]("Scala source directory for compiled avro")
    lazy val avroScalaCustomTypes     = settingKey[AvroScalaTypes]("Custom Scala types of generated Scala code")
    lazy val avroScalaCustomNamespace = settingKey[Map[String, String]]("Custom namespace of generated Scala code")
  }
    
  import autoImport._
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements
  
  lazy val baseSettings = 
    avroSettings ++
    specificAvroSettings
    
  override lazy val projectSettings: Seq[Def.Setting[_]] =
    inConfig(Compile)(baseSettings) ++
    inConfig(Test)(baseSettings)

  val majMinV = """(\d+.\d+).*""".r

  // Standard Format
  lazy val avroSettings: Seq[Def.Setting[_]] = Seq(
    avroScalaSource               := sourceManaged.value / "compiled_avro",
    avroSourceDirectories         := Seq(sourceDirectory.value / "avro"),
    avroScalaCustomNamespace      := Map.empty[String, String],
    avroScalaCustomTypes          := Standard.defaultTypes,
    avroScalaGenerate / logLevel := (logLevel?? Level.Info).value,
    avroScalaGenerate := {
      val cache = crossTarget.value
      val srcDirs = avroSourceDirectories.value
      val targetDir = avroScalaSource.value
      val out = streams.value
      val majMinV(scalaV) = scalaVersion.value
      val customTypes = avroScalaCustomTypes.value
      val customNamespace = avroScalaCustomNamespace.value
      val res = (Compile / resourceDirectory).value
      val old = (Compile/ scalaInstance).value
      val classLoader = new java.net.URLClassLoader(Array(res.toURI().toURL()), old.loader)
      val isNumberOfFieldsRestricted = scalaV == "2.10"
      val gen = new Generator(
        format = Standard,
        avroScalaCustomTypes = Some(customTypes),
        avroScalaCustomNamespace = customNamespace,
        restrictedFieldNumber = isNumberOfFieldsRestricted,
        classLoader,
        scalaV)
      val cachedCompile = FileFunction.cached(
        cache / "avro",
        inStyle = FilesInfo.lastModified,
        outStyle = FilesInfo.exists
      ) { (in: Set[File]) => FileWriter.generateCaseClasses(gen, srcDirs, targetDir, out.log) }
        
      cachedCompile((srcDirs ** "*.av*").get.toSet).toSeq
    }
  )
  
  // SpecificRecord Format
  lazy val specificAvroSettings: Seq[Def.Setting[_]] = Seq(
    avroSpecificScalaSource := sourceManaged.value / "compiled_avro",
    avroSpecificSourceDirectories := Seq(sourceDirectory.value / "avro"),
    avroScalaSpecificCustomTypes := SpecificRecord.defaultTypes,
    avroScalaSpecificCustomNamespace := Map.empty[String, String],
    avroScalaGenerateSpecific / logLevel := (logLevel?? Level.Info).value,
    avroScalaGenerateSpecific := {
      val cache = crossTarget.value
      val srcDirs = avroSpecificSourceDirectories.value
      val targetDir = avroSpecificScalaSource.value
      val out = streams.value
      val majMinV(scalaV) = scalaVersion.value
      val specificCustomTypes = avroScalaSpecificCustomTypes.value
      val specificCustomNamespace = avroScalaSpecificCustomNamespace.value
      val res = (Compile / resourceDirectory).value
      val old = (Compile / scalaInstance).value
      val classLoader = new java.net.URLClassLoader(Array(res.toURI().toURL()), old.loader)
      val isNumberOfFieldsRestricted = scalaV == "2.10"
      val gen = new Generator(
        SpecificRecord,
        Some(specificCustomTypes),
        specificCustomNamespace,
        isNumberOfFieldsRestricted,
        classLoader,
        scalaV)

      val in = (srcDirs ** "*.av*").get.toSet
      FileWriter.generateCaseClasses(gen, in.toSeq, targetDir, out.log).toSeq
      /*val cachedCompile = FileFunction.cached(
        cache / "avro",
        inStyle = FilesInfo.hash,
        outStyle = FilesInfo.exists
      ) { (_: Set[File]) =>
        FileWriter.generateCaseClasses(gen, in.toSeq, targetDir, out.log) }
      cachedCompile((srcDirs ** "*.av*").get.toSet).toSeq*/
    }
  )
}
