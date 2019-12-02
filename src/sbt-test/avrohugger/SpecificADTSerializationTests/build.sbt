sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue

organization := "com.julianpeeters"

name := "datatype-specific-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.11.11", "2.12.4", "2.13.1")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

avroScalaCustomTypes in Compile := {
  avrohugger.format.SpecificRecord.defaultTypes.copy(
    protocol = avrohugger.types.ScalaADT)
}

libraryDependencies += "org.apache.avro" % "avro" % "1.9.1"

libraryDependencies += "org.apache.avro" % "avro-ipc-netty" % "1.9.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.6.0" % Test