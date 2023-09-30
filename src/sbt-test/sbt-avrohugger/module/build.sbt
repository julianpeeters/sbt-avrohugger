


// Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

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
  scalaVersion := "2.12.18",
  libraryDependencies += "org.apache.avro" % "avro" % "1.11.3",
  libraryDependencies += "org.specs2" %% "specs2-core" % "4.20.2" % Test,
  libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.6.0" % Test
)

