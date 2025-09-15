Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

organization := "com.julianpeeters"

name := "datatype-specific-serializaton-tests"

version := "0.4-SNAPSHOT"

<<<<<<< HEAD
crossScalaVersions := Seq("2.12.20", "3.3.4")
=======
crossScalaVersions := Seq("2.12.20", "2.13.16")
>>>>>>> 5cc349be2d0d3f8311a6d47077b1adb26e1f4452

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

Compile / avroScalaCustomTypes := {
  avrohugger.format.SpecificRecord.defaultTypes.copy(
    protocol = avrohugger.types.ScalaADT)
}

libraryDependencies += "org.apache.avro" % "avro" % "1.11.4"

libraryDependencies += "org.apache.avro" % "avro-ipc-netty" % "1.9.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.20.2" % Test