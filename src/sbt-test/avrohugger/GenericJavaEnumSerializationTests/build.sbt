sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue

avroScalaCustomTypes in Compile := {
  avrohugger.format.Standard.defaultTypes.copy(
    enum = avrohugger.types.JavaEnum)
}
organization := "com.julianpeeters"

name := "datatype-avro-serializaton-tests"

version := "0.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.4")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ywarn-value-discard")

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor == 12 =>
      libraryDependencies.value ++ Seq("com.sksamuel.avro4s" %% "avro4s-core" % "3.0.4")
    case Some((2, scalaMajor)) if scalaMajor == 10 =>
      libraryDependencies.value ++ Seq("com.sksamuel.avro4s" %% "avro4s-core" % "1.2.2")
    case _ => Seq()
  }
}

libraryDependencies += "org.apache.avro" % "avro" % "1.9.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6" % Test