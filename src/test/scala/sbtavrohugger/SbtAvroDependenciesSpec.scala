package sbtavrohugger

import java.io.File

import org.specs2.mutable.Specification

class SbtAvroDependenciesSpec extends Specification {
  val sourceDir = new File(getClass.getClassLoader.getResource("dependencies").toURI)
  val targetDir = new File(sourceDir.getParentFile, "generated")
  
  val arrayRef = new File(sourceDir, "ArrayRef.avsc")
  val simpleArrayRef = new File(sourceDir, "SimpleArrayRef.avsc")
  val zFile = new File(sourceDir, "Z.avsc")
  val unionRef = new File(sourceDir, "UnionRef.avsc")
  val sourceFiles = Seq(arrayRef, zFile, unionRef)
  val sourceFiles2 = Seq(simpleArrayRef, zFile, unionRef)

  "Schema files should be sorted correctly for union and array references" >> {
    AVSCFileSorter.sortSchemaFiles(sourceFiles) must beEqualTo(Seq(zFile, unionRef, arrayRef))
    AVSCFileSorter.sortSchemaFiles(sourceFiles.reverse) must beEqualTo(Seq(zFile, unionRef, arrayRef))
  }

  "Schema files should be sorted correctly for transitive union references" >> {
    AVSCFileSorter.sortSchemaFiles(sourceFiles2) must beEqualTo(Seq(zFile, unionRef, simpleArrayRef))
    AVSCFileSorter.sortSchemaFiles(sourceFiles2.reverse) must beEqualTo(Seq(zFile, unionRef, simpleArrayRef))
  }
}