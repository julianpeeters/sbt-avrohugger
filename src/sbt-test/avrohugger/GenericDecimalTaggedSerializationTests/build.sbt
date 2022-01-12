sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq( "2.12.15","2.13.8")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "org.apache.avro" % "avro" % "1.9.1"

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.9.1"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.6.0" % Test

avroScalaCustomTypes in Compile := {
  avrohugger.format.Standard.defaultTypes.copy(
    decimal = avrohugger.types.ScalaBigDecimalWithPrecision(None))
}