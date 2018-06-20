
name := "standard-generation-test"

scalaVersion := "2.12.4"

sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue
avroSourceDirectories in Compile += (sourceDirectory in Compile).value / "avro2"