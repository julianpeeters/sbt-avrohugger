import avrohugger.types.{OptionScala3UnionType, ScalaADT}

Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

scalaVersion := "3.3.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies += "org.apache.avro" % "avro" % "1.11.5"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.20.2" % Test

Compile / avroScalaSpecificCustomTypes := {
  avrohugger.format.Standard.defaultTypes.copy(
    union = OptionScala3UnionType,
    protocol = ScalaADT
  )
}