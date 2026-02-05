name := "specific-generation-test"

Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

scalaVersion := "2.12.21"

libraryDependencies += "org.apache.avro" % "avro" % "1.12.1"