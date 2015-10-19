package sbtavrohugger

import sbt.SettingKey

// These settings are shared by multiple formats. For some reason they don't get picked up with the 
// other settings, so the user must import them specifically in order to use them.
object AvrohuggerSettings  {
  val scalaCustomTypes = SettingKey[Map[String, String]]("scala-custom-types", "Customize Avro to Scala type map by type")
  val scalaCustomNamespace = SettingKey[Map[String, String]]("scala-custom-namespace", "Custom namespace of generated Scala wrapper code")
}
