name := "specific-generation-test"

sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue

scalaVersion := "2.12.4"

libraryDependencies += "org.apache.avro" % "avro" % "1.7.7"