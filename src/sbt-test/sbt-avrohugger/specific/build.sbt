name := "specific-generation-test"

Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

scalaVersion := "2.12.18"

libraryDependencies += "org.apache.avro" % "avro" % "1.11.3"