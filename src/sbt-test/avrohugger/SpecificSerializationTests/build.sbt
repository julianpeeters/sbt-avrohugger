sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue

organization := "com.julianpeeters"

name := "datatype-specific-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.4")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.julianpeeters" %% "avrohugger-core" % "1.0.0-RC11-SNAPSHOT"

libraryDependencies += "org.apache.avro" % "avro" % "1.8.2"

libraryDependencies += "org.apache.avro" % "avro-ipc" % "1.8.2"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6" % "test"