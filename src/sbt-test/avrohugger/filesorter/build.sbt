name := "filesorter-test"

Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue

scalaVersion := "3.3.6"