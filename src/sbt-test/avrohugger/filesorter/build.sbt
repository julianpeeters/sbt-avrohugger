name := "filesorter-test"

Compile / sourceGenerators += (Compile / avroScalaGenerate).taskValue
