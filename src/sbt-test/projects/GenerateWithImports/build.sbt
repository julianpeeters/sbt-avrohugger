import sbtavrohugger.SbtAvrohugger.imports

sbtavrohugger.SbtAvrohugger.specificAvroSettings

organization := "com.julianpeeters"

name := "generate-with-imports"

version := "0.1"

(imports in avroConfig) ++= {
  Seq(
    (sourceDirectory in avroConfig).value / "b.avsc"
  )
}
