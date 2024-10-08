
name := "standard-generation-test"

scalaVersion := "2.12.20"

// exportJars := true

Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

Compile / avroSourceDirectories += (Compile / sourceDirectory).value / "avro2"
