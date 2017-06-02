name := "scavro-generation-test"

sbtavrohugger.SbtAvrohugger.scavroSettings

sbtavro.SbtAvro.avroSettings

version in sbtavro.SbtAvro.avroConfig := "1.8.0"

libraryDependencies ++= Seq(
  "org.oedura" %% "scavro" % "1.0.1"
)