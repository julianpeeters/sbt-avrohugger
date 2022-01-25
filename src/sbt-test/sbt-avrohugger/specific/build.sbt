name := "specific-generation-test"

Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

scalaVersion := "2.12.15"

libraryDependencies += "org.apache.avro" % "avro" % "1.11.0"