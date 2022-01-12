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

crossScalaVersions := Seq("2.12.15", "2.13.8")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "org.apache.avro" % "avro" % "1.9.1"

libraryDependencies += "org.apache.avro" % "avro-ipc-netty" % "1.9.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.6.0" % Test