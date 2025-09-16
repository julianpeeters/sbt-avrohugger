package sbtavrohugger

import java.io.File
import java.nio.file.Files
import org.specs2.mutable.Specification
import avrohugger.filesorter.{AvdlFileSorter, AvscFileSorter}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by jeromewacongne on 06/08/2015.
 */
class SbtAvroSpec extends Specification {

  val classLoader = getClass.getClassLoader

  val tmp = Files.createTempDirectory("avro").toFile
  val a = Files.copy(classLoader.getResourceAsStream("avro/a.avsc"), java.nio.file.Path.of(tmp.toString(), "a.avsc"))
  val b = Files.copy(classLoader.getResourceAsStream("avro/b.avsc"), java.nio.file.Path.of(tmp.toString(), "b.avsc"))
  val c = Files.copy(classLoader.getResourceAsStream("avro/c.avsc"), java.nio.file.Path.of(tmp.toString(), "c.avsc"))
  val sourceDir = java.nio.file.Path.of("avro").toFile()
  val targetDir = new File(sourceDir.getParentFile, "generated")
  val sourceFiles = Seq(new File(tmp, "a.avsc"), new File(tmp, "b.avsc"), new File(tmp, "c.avsc"))

  "Schema files should be sorted with re-used types schemas first, whatever input order" >> {
    AvscFileSorter.sortSchemaFiles(sourceFiles) must beEqualTo(Seq(new File(tmp, "c.avsc"), new File(tmp, "b.avsc"), new File(tmp, "a.avsc")))
    AvscFileSorter.sortSchemaFiles(sourceFiles.reverse) must beEqualTo(Seq(new File(tmp, "c.avsc"), new File(tmp, "b.avsc"), new File(tmp, "a.avsc")))
  }
  // 
  // "AVDL files should be sorted correctly for imports" >> {
  //   val expected = avdlSourceFiles
  //   AvdlFileSorter.sortSchemaFiles(avdlSourceFiles, classLoader) must beEqualTo(expected)
  //   AvdlFileSorter.sortSchemaFiles(avdlSourceFiles.reverse, classLoader) must beEqualTo(expected)
  // }
}
