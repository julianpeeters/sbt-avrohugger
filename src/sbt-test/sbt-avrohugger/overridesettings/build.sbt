organization := "com.julianpeeters"

name := "override-settings"

scalaVersion := "2.12.4"

version := "0.1-SNAPSHOT"

sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue

(avroScalaSource in Compile) := new java.io.File(s"${baseDirectory.value}/myoutputdir")

(avroSourceDirectory in Compile) := new java.io.File(s"${baseDirectory.value}/src/main/myavro")

(avroScalaCustomNamespace in Compile) := Map("example"->"overridden")

avroScalaCustomTypes in Compile := {
  avrohugger.format.Standard.defaultTypes.copy(
    array = avrohugger.types.ScalaVector,
    union = avrohugger.types.ShapelessCoproduct)
}


libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.apache.avro" % "avro" % "1.7.7"
)



