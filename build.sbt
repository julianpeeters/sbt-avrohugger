ThisBuild / organization := "com.julianpeeters"
ThisBuild / description := "Sbt plugin for compiling Avro to Scala"
ThisBuild / version := "2.15.0-SNAPSHOT"
ThisBuild / versionScheme := Some("semver-spec")

enablePlugins(SbtPlugin)

(Global / run / fork) := true
(Global / run / outputStrategy) := Some(StdoutOutput)

ThisBuild /  crossScalaVersions := Seq("2.12.20", "3.7.3")
ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Werror"
)

ThisBuild / libraryDependencies ++= Seq(
  "com.julianpeeters" %% "avrohugger-core" % "2.14.0",
  "com.julianpeeters" %% "avrohugger-filesorter" % "2.14.0",
  "io.spray" %% "spray-json" % "1.3.6",
  "org.specs2" %% "specs2-core" % "4.20.2" % "test")

ThisBuild / sbtPluginPublishLegacyMavenStyle := false
ThisBuild / publishTo := localStaging.value
ThisBuild / licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url(s"https://github.com/julianpeeters/${name.value}"))
ThisBuild / pomExtra := (
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

ThisBuild / scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}
ThisBuild / scriptedBufferLog := false
