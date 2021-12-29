name := "scavro-generation-test"

scalaVersion := "2.12.15"

sourceGenerators in Compile += (avroScalaGenerateScavro in Compile).taskValue

libraryDependencies ++= Seq(
  "org.oedura" %% "scavro" % "1.0.1"
)