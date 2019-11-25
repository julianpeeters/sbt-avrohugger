organization := "com.julianpeeters"

name := "override-settings"

scalaVersion := "2.12.4"

version := "0.1-SNAPSHOT"

sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue
sourceGenerators in Test += (avroScalaGenerate in Test).taskValue

(avroScalaSource in Compile) := new java.io.File(s"${baseDirectory.value}/myoutputdir")
(avroScalaSource in Test) := new java.io.File(s"${baseDirectory.value}/mytestoutputdir")

(avroSourceDirectories in Compile) += new java.io.File(s"${baseDirectory.value}/src/main/myavro")
(avroSourceDirectories in Compile) += new java.io.File(s"${baseDirectory.value}/src/main/myavro2")

(avroSourceDirectories in Test) += new java.io.File(s"${baseDirectory.value}/src/test/avro2")

(avroScalaCustomNamespace in Compile) := Map("example"->"overridden", "example2"->"overridden2")

avroScalaCustomTypes in Compile := {
  avrohugger.format.Standard.defaultTypes.copy(
    array = avrohugger.types.ScalaVector,
    union = avrohugger.types.OptionalShapelessCoproduct)
}


libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.apache.avro" % "avro" % "1.9.1"
)



