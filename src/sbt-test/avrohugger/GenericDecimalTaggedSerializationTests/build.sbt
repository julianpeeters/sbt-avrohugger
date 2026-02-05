Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.21","2.13.18")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

resolvers += "Apache Snapshots" at "https://repository.apache.org/content/groups/snapshots"

libraryDependencies += "org.apache.avro" % "avro" % "1.13.0-SNAPSHOT"

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.9.1"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.20.2" % Test

Compile / avroScalaCustomTypes := {
  avrohugger.format.Standard.defaultTypes.copy(
    decimal = avrohugger.types.ScalaBigDecimalWithPrecision(None))
}