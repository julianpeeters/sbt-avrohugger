
name := "standard-generation-test"

scalaVersion := "2.12.21"

version := "0.1-SNAPSHOT"

// exportJars := true

Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

Compile / avroSourceDirectories += (Compile / sourceDirectory).value / "avro2"

import sbt.complete.DefaultParsers.spaceDelimited
import sbtcompat.PluginCompat._

@transient
lazy val checkGeneratedFiles = inputKey[Unit]("Check generated sources and compiled classes")

checkGeneratedFiles := Def.uncached {
  val paths = spaceDelimited("<class-path>").parsed
  val sources = (Compile / sourceManaged).value / "compiled_avro"
  val classes = (Compile / classDirectory).value
  paths.foreach { path =>
    val source = sources / (path + ".scala")
    val compiled = classes / (path + ".class")
    assert(source.isFile, "Missing generated source: " + source)
    assert(compiled.isFile, "Missing compiled class: " + compiled)
  }
}
