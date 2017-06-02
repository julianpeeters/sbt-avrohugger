package sbtavrohugger

import java.io.File

import org.specs2.mutable.Specification
import avrohugger.filesorter.{AvdlFileSorter, AvscFileSorter}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by jeromewacongne on 06/08/2015.
 */
class SbtAvroSpec extends Specification {

  val sourceDir = new File(getClass.getClassLoader.getResource("avro").toURI)
  val targetDir = new File(sourceDir.getParentFile, "generated")
  val sourceFiles = Seq(new File(sourceDir, "a.avsc"), new File(sourceDir, "b.avsc"), new File(sourceDir, "c.avsc"))
  val avdlSourceFiles = ArrayBuffer(new File(sourceDir, "a.avdl"), new File(sourceDir, "foo/a.avdl"), new File(sourceDir, "c.avdl"))

  "Schema files should be sorted with re-used types schemas first, whatever input order" >> {
    AvscFileSorter.sortSchemaFiles(sourceFiles) must beEqualTo(Seq(new File(sourceDir, "c.avsc"), new File(sourceDir, "b.avsc"), new File(sourceDir, "a.avsc")))
    AvscFileSorter.sortSchemaFiles(sourceFiles.reverse) must beEqualTo(Seq(new File(sourceDir, "c.avsc"), new File(sourceDir, "b.avsc"), new File(sourceDir, "a.avsc")))
  }

  "AVDL files should be sorted correctly for imports" >> {
    val expected = avdlSourceFiles
    AvdlFileSorter.sortSchemaFiles(avdlSourceFiles) must beEqualTo(expected)
    AvdlFileSorter.sortSchemaFiles(avdlSourceFiles.reverse) must beEqualTo(expected)
  }
}
