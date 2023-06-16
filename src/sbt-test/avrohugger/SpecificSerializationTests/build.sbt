Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

organization := "com.julianpeeters"

name := "datatype-specific-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.18", "2.13.11", "3.1.3", "3.2.1")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

Compile / avroScalaSpecificCustomTypes := {
  avrohugger.format.SpecificRecord.defaultTypes.copy(
    decimal = avrohugger.types.ScalaBigDecimal(Some(BigDecimal.RoundingMode.HALF_EVEN)))
}

libraryDependencies += "org.apache.avro" % "avro" % "1.11.1"

libraryDependencies += "org.apache.avro" % "avro-ipc-netty" % "1.9.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.16.1" % Test
