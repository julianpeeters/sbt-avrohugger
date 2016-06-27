package sbtavrohugger

import sbt.SettingKey

// These settings are shared by multiple formats. For some reason they don't get picked up with the 
// other settings, so the user must import them specifically in order to use them.
object AvrohuggerSettings  {
  val avroScalaCustomTypes = SettingKey[Map[String, Class[_]]]("avro-scala-custom-types", "Customize Avro to Scala type map by type")
  val avroScalaCustomNamespace = SettingKey[Map[String, String]]("avro-scala-custom-namespace", "Custom namespace of generated Scala code")
  val avroScalaCustomEnumStyle = SettingKey[Map[String, String]]("avro-scala-custom-enum-style", "Custom enum style of generated Scala wrapper code")
}
