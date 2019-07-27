


// sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue

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
  libraryDependencies += "org.apache.avro" % "avro" % "1.8.2",
  libraryDependencies += "org.specs2" %% "specs2-core" % "4.6.0" % Test,
  libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.6.0" % Test
)

