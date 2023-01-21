Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

Compile / avroSpecificScalaSource := new java.io.File(s"${baseDirectory.value}/myoutputdir")

Compile / avroScalaSpecificCustomNamespace := Map("example"->"overridden")

organization := "com.julianpeeters"

name := "override-settings"

version := "0.1-SNAPSHOT"

crossScalaVersions := Seq("2.12.17", "2.13.10", "3.1.3", "3.2.1")

libraryDependencies += "org.apache.avro" % "avro" % "1.11.1"

