package sbtavrohugger
package settings

import tasks.SpecificGeneratorTask.specificCaseClassGeneratorTask
import java.io.File

import sbt.Keys._
import sbt.{
  Compile,
  Configuration,
  Setting,
  Task,
  TaskKey,
  inConfig
}

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
      generateSpecific <<= specificCaseClassGeneratorTask(avroConfig))) ++ Seq[Setting[_]](
      sourceGenerators in Compile <+= (generateSpecific in avroConfig),
      managedSourceDirectories in Compile <+= (scalaSource in avroConfig),
      cleanFiles <+= (scalaSource in avroConfig),
      ivyConfigurations += avroConfig)
  }

}
