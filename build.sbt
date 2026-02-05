organization := "com.julianpeeters"
description := "Sbt plugin for compiling Avro to Scala"
version := "2.16.0"
versionScheme := Some("semver-spec")

enablePlugins(SbtPlugin)

(run / fork) := true
(run / outputStrategy) := Some(StdoutOutput)

crossScalaVersions := Seq("2.12.21", "3.8.1")
scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Werror"
)

libraryDependencies ++= Seq(
  "com.julianpeeters" %% "avrohugger-core" % "2.16.0",
  "com.julianpeeters" %% "avrohugger-filesorter" % "2.16.0",
  "io.spray" %% "spray-json" % "1.3.6",
  "org.specs2" %% "specs2-core" % "4.20.2" % "test")

sbtPluginPublishLegacyMavenStyle := false
publishTo := localStaging.value
licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
homepage := Some(url(s"https://github.com/julianpeeters/${name.value}"))
pomExtra := (
  <scm>
    <url>git://github.com/julianpeeters/sbt-avrohugger.git</url>
    <connection>scm:git://github.com/julianpeeters/sbt-avrohugger.git</connection>
  </scm>
  <developers>
    <developer>
      <id>julianpeeters</id>
      <name>Julian Peeters</name>
      <url>http://github.com/julianpeeters</url>
    </developer>
  </developers>)

scriptedBufferLog := false
