import sbtavrohugger.AvrohuggerSettings.{
  avroScalaCustomNamespace,
  avroScalaCustomTypes
}

sbtavrohugger.SbtAvrohugger.specificAvroSettings

(scalaSource in avroConfig) := new java.io.File("myoutputdir")

(avroScalaCustomNamespace in avroConfig) := Map("example"->"overridden")

avroScalaCustomTypes in sbtavrohugger.SbtAvrohugger.avroConfig := Map("array" -> classOf[Vector[_]])

organization := "com.julianpeeters"

name := "override-settings"

version := "0.1-SNAPSHOT"

