Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.18", "2.13.11")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "3.0.4"

libraryDependencies += "org.apache.avro" % "avro" % "1.11.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.16.1" % Test

Compile / avroScalaCustomTypes := {
  avrohugger.format.Standard.defaultTypes.copy(
    union = avrohugger.types.OptionalShapelessCoproduct)
}