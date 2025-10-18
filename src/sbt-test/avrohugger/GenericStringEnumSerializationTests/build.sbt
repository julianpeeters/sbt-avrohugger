Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

Compile / avroScalaCustomTypes := {
  avrohugger.format.Standard.defaultTypes.copy(
    `enum` = avrohugger.types.EnumAsScalaString)
}
organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.20", "2.13.17")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "3.0.4"

libraryDependencies += "org.apache.avro" % "avro" % "1.11.5"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.20.2" % Test