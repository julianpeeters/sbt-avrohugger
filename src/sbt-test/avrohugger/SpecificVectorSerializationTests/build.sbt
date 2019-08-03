sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue

avroScalaSpecificCustomTypes in Compile := {
  avrohugger.format.SpecificRecord.defaultTypes.copy(
    array = avrohugger.types.ScalaVector,
    date = avrohugger.types.JavaSqlDate,
    timestampMillis = avrohugger.types.JavaSqlTimestamp)
}
organization := "com.julianpeeters"

name := "datatype-specific-vector-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.4")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "org.apache.avro" % "avro" % "1.9.0"

libraryDependencies += "org.apache.avro" % "avro-ipc" % "1.8.2"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6"