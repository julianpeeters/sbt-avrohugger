name := "standard-generation-test"

sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue