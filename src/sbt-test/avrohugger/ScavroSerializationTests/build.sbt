organization := "com.julianpeeters"

name := "datatype-scavro-serializaton-tests"

version := "0.5-SNAPSHOT"

crossScalaVersions := Seq("2.10.6", "2.11.11")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

scalacOptions in Test ++= Seq("-Yrangepos")

sourceGenerators in Compile += (avroScalaGenerateScavro in Compile).taskValue

avroScalaScavroCustomTypes in Compile := {
  avrohugger.format.Scavro.defaultTypes.copy(
    array = avrohugger.types.ScalaList)
}

avroScalaScavroCustomNamespace in Compile := Map("model.v2" -> "scavromodelv2", "model" -> "scavromodel", "test" -> "scavrotest")

libraryDependencies ++= Seq(
  "org.oedura" %% "scavro" % "1.0.3",
  "org.apache.avro" % "avro-ipc-netty" % "1.9.1",
  "org.specs2" %% "specs2-core" % "3.8.6"
)
