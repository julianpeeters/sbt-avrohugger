Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.20", "2.13.15")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "3.0.4"

libraryDependencies += "org.apache.avro" % "avro" % "1.11.4"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.20.2" % Test

Compile / avroScalaCustomTypes := {
  avrohugger.format.Standard.defaultTypes.copy(
    union = avrohugger.types.OptionalShapelessCoproduct)
}