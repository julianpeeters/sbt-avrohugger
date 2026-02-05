
name := "standard-generation-test"

scalaVersion := "2.12.21"

version := "0.1-SNAPSHOT"

// exportJars := true

Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

Compile / avroSourceDirectories += (Compile / sourceDirectory).value / "avro2"
