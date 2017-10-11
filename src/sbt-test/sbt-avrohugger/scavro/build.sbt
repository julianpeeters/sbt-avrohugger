name := "scavro-generation-test"

sourceGenerators in Compile += (avroScalaGenerateScavro in Compile).taskValue

sbtavro.SbtAvro.avroSettings

version in sbtavro.SbtAvro.avroConfig := "1.8.0"

libraryDependencies ++= Seq(
  "org.oedura" %% "scavro" % "1.0.1"
)