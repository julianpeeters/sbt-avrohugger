package sbtavrohugger
package formats
package specific

import SpecificGeneratorTask.specificCaseClassGeneratorTask
import AvrohuggerSettings.{avroScalaCustomEnumStyle, avroScalaCustomNamespace, avroScalaCustomTypes}
import java.io.File

import sbt.Keys._
import sbt.{AllPassFilter, Compile, Configuration, NothingFilter, Setting, Task, TaskKey, inConfig}

object SpecificAvroSettings {

  val generateSpecific = TaskKey[Seq[File]]("generate-specific",
    "Generate Scala sources for the Avro files.")

  def getSettings(
    avroConfig: Configuration,
    inputDir: Setting[File],
    outputDir: Setting[File],
    classPath: Setting[Task[Classpath]]): Seq[Setting[_]] = {

    inConfig(avroConfig)(Seq[Setting[_]](
      inputDir,
      outputDir,
      classPath,
      avroScalaCustomTypes in avroConfig := Map.empty[String, Class[_]],
      avroScalaCustomNamespace in avroConfig := Map.empty[String, String],
      avroScalaCustomEnumStyle in avroConfig := Map.empty[String, String],
      generateSpecific <<= specificCaseClassGeneratorTask(avroConfig))) ++
        Seq[Setting[_]](
          sourceGenerators in Compile <+= (generateSpecific in avroConfig),
          managedSourceDirectories in Compile <+= (scalaSource in avroConfig),
          cleanFiles <+= (scalaSource in avroConfig),
          ivyConfigurations += avroConfig,
          includeFilter := AllPassFilter,
          excludeFilter := NothingFilter)
  }

}
