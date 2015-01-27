import sbt._
import Keys._
import ScriptedPlugin._


object build extends Build {
    val sbtAvrohugger = Project(
        id = "sbt-avrohugger",
        base = file("."),
        settings = Defaults.defaultSettings ++ scriptedSettings ++ Seq[Project.Setting[_]](
            organization := "com.julianpeeters",
            version := "0.0.1-SNAPSHOT",
            sbtPlugin := true,
            resolvers += Resolver.file("Local Ivy Repository", file("/home/julianpeeters/.ivy2/local/"))(Resolver.ivyStylePatterns),
            libraryDependencies ++= Seq(
                "com.julianpeeters" % "avrohugger-core_2.10" % "0.0.1",
                 "org.apache.avro" % "avro" % "1.7.4"

            ),
            scalaVersion := "2.10.4",
            scalacOptions in Compile ++= Seq("-deprecation"),
            description := "Sbt plugin for compiling Avro sources",

            publishMavenStyle := false
        )
    )
}
