name := "scavro-generation-test"

scalaVersion := "2.12.16"

Compile / sourceGenerators += (Compile / avroScalaGenerateScavro).taskValue

libraryDependencies ++= Seq(
  "org.oedura" %% "scavro" % "1.0.1"
)