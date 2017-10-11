name := "specific-generation-test"

sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue

libraryDependencies += "org.apache.avro" % "avro" % "1.7.7"