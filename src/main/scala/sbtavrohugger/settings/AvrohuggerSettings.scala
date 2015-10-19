package sbtavrohugger
package settings

import sbt.SettingKey

object AvrohuggerSettings  {
  val scalaCustomTypes = SettingKey[Map[String, String]]("scala-custom-types", "Customize Avro to Scala type map by type")
  val scalaCustomNamespace = SettingKey[Map[String, String]]("scala-custom-namespace", "Custom namespace of generated Scala wrapper code")
}
