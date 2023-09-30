Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue

Compile / avroScalaSpecificCustomTypes := {
  avrohugger.format.SpecificRecord.defaultTypes.copy(
    array = avrohugger.types.ScalaVector,
    date = avrohugger.types.JavaSqlDate,
    timestampMillis = avrohugger.types.JavaSqlTimestamp,
    union = avrohugger.types.OptionShapelessCoproduct)
}
organization := "com.julianpeeters"

name := "datatype-specific-vector-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.18", "2.13.12", "3.3.0")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies += "org.apache.avro" % "avro" % "1.11.3"

libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "3.0.4"

libraryDependencies += "org.apache.avro" % "avro-ipc-netty" % "1.9.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.20.2" % Test