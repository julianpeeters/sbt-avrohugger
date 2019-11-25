


sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue

lazy val module = (project in file("."))
  .settings(
    name := "standard-generation-test",
    commonSettings,
  ).aggregate(`submodule`)
  

lazy val `submodule` = (project in file("submodule"))
  .settings(
    commonSettings
  )
  
lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  libraryDependencies += "org.apache.avro" % "avro" % "1.9.1",
  libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6",
  libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "3.8.6"
)

