package sbtavrohugger

import avrohugger.filesorter.AvscFileSorter
import java.io.File
import java.nio.file.Files
import org.specs2.mutable.Specification

class SbtAvroDependenciesSpec extends Specification {
  val classLoader = getClass.getClassLoader

  val tmp = Files.createTempDirectory("dependencies").toFile
  val a = Files.copy(classLoader.getResourceAsStream("dependencies/ArrayRef.avsc"), java.nio.file.Path.of(tmp.toString(), "ArrayRef.avsc"))
  val b = Files.copy(classLoader.getResourceAsStream("dependencies/SimpleArrayRef.avsc"), java.nio.file.Path.of(tmp.toString(), "SimpleArrayRef.avsc"))
  val c = Files.copy(classLoader.getResourceAsStream("dependencies/EnumRef.avsc"), java.nio.file.Path.of(tmp.toString(), "EnumRef.avsc"))
  val d = Files.copy(classLoader.getResourceAsStream("dependencies/Z.avsc"), java.nio.file.Path.of(tmp.toString(), "Z.avsc"))
  val e = Files.copy(classLoader.getResourceAsStream("dependencies/A.avsc"), java.nio.file.Path.of(tmp.toString(), "A.avsc"))
  val f = Files.copy(classLoader.getResourceAsStream("dependencies/MapRef.avsc"), java.nio.file.Path.of(tmp.toString(), "MapRef.avsc"))
  val g = Files.copy(classLoader.getResourceAsStream("dependencies/UnionRef.avsc"), java.nio.file.Path.of(tmp.toString(), "UnionRef.avsc"))
  val sourceDir = java.nio.file.Path.of("dependencies").toFile()

  val targetDir = new File(sourceDir.getParentFile, "generated")

  val arrayRef = new File(tmp, "ArrayRef.avsc")
  val simpleArrayRef = new File(tmp, "SimpleArrayRef.avsc")
  val enumRef = new File(tmp, "EnumRef.avsc")
  val zFile = new File(tmp, "Z.avsc")
  val aFile = new File(tmp, "A.avsc")
  val mapRef = new File(tmp, "MapRef.avsc")
  val unionRef = new File(tmp, "UnionRef.avsc")
  val sourceFiles = Seq(arrayRef, zFile, unionRef)
  val sourceFiles2 = Seq(simpleArrayRef, zFile, unionRef)
  val sourceFiles3 = Seq(mapRef, aFile)
  val sourceFilesWithEnum = Seq(enumRef)

  "Schema files should be sorted correctly for union and array references" >> {
    AvscFileSorter.sortSchemaFiles(sourceFiles) must beEqualTo(Seq(zFile, unionRef, arrayRef))
    AvscFileSorter.sortSchemaFiles(sourceFiles.reverse) must beEqualTo(Seq(zFile, unionRef, arrayRef))
  }

  "Schema files should be sorted correctly for transitive union references" >> {
    AvscFileSorter.sortSchemaFiles(sourceFiles2) must beEqualTo(Seq(zFile, unionRef, simpleArrayRef))
    AvscFileSorter.sortSchemaFiles(sourceFiles2.reverse) must beEqualTo(Seq(zFile, unionRef, simpleArrayRef))
  }

  "Schema files should be sorted correctly for enum references" >> {
    AvscFileSorter.sortSchemaFiles(sourceFilesWithEnum) must beEqualTo(Seq(enumRef))
  }

  "Schema files should be sorted correctly for map references" >> {
    AvscFileSorter.sortSchemaFiles(sourceFiles3) must beEqualTo(Seq(aFile, mapRef))
    AvscFileSorter.sortSchemaFiles(sourceFiles3.reverse) must beEqualTo(Seq(aFile, mapRef))
  }
}
