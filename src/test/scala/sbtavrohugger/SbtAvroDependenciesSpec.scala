package sbtavrohugger

import avrohugger.filesorter.AvscFileSorter
import java.io.File

import org.specs2.mutable.Specification

class SbtAvroDependenciesSpec extends Specification {
  val sourceDir = new File(getClass.getClassLoader.getResource("dependencies").toURI)
  val targetDir = new File(sourceDir.getParentFile, "generated")

  val arrayRef = new File(sourceDir, "ArrayRef.avsc")
  val simpleArrayRef = new File(sourceDir, "SimpleArrayRef.avsc")
  val enumRef = new File(sourceDir, "EnumRef.avsc")
  val zFile = new File(sourceDir, "Z.avsc")
  val aFile = new File(sourceDir, "A.avsc")
  val mapRef = new File(sourceDir, "MapRef.avsc")
  val unionRef = new File(sourceDir, "UnionRef.avsc")
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
