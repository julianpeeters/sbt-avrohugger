sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.11.8", "2.12.4")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "1.9.0"

libraryDependencies += "org.apache.avro" % "avro" % "1.7.7"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6"

avroScalaCustomTypes in Compile := {
  avrohugger.format.Standard.defaultTypes.copy(
    decimal = avrohugger.types.ScalaBigDecimalWithPrecision)
}