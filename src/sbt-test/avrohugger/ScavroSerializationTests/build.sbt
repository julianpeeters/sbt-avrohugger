organization := "com.julianpeeters"

name := "datatype-scavro-serializaton-tests"

version := "0.5-SNAPSHOT"

crossScalaVersions := Seq("2.10.6", "2.11.11")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

Test / scalacOptions ++= Seq("-Yrangepos")

Compile / sourceGenerators += (Compile / avroScalaGenerateScavro).taskValue

Compile / avroScalaScavroCustomTypes := {
  avrohugger.format.Scavro.defaultTypes.copy(
    array = avrohugger.types.ScalaList)
}

Compile / avroScalaScavroCustomNamespace := Map("model.v2" -> "scavromodelv2", "model" -> "scavromodel", "test" -> "scavrotest")

libraryDependencies ++= Seq(
  "org.oedura" %% "scavro" % "1.0.3",
  "org.apache.avro" % "avro-ipc-netty" % "1.9.1",
  "org.specs2" %% "specs2-core" % "3.8.6"
)
