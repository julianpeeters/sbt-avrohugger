Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

Compile / avroSpecificScalaSource := new java.io.File(s"${baseDirectory.value}/myoutputdir")

Compile / avroScalaSpecificCustomNamespace := Map("example"->"overridden")

organization := "com.julianpeeters"

name := "override-settings"

version := "0.1-SNAPSHOT"

crossScalaVersions := Seq("2.12.20", "2.13.15", "3.3.4")

libraryDependencies += "org.apache.avro" % "avro" % "1.11.4"

