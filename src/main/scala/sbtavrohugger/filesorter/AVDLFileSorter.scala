package sbtavrohugger.filesorter

import java.io.File

import scala.annotation.tailrec
import scala.io.Source

/**
  * The order in which avsc files are compiled depends on the underlying file
  * system (under OSX its is alphabetical, under some linux distros it's not).
  * This is an issue when you have a record type that is used in different
  * other types. This ensures that dependent types are compiled in the
  * correct order.
  * Created by Jon Morra on 2/7/17.
  */
object AVDLFileSorter {
  def sortSchemaFiles(filesTraversable: Traversable[File]): Seq[File] = {
    val files = filesTraversable.toSeq
    val importsMap = files.map{ file =>
      (file.getCanonicalFile, getImports(file))
    }.toMap

    @tailrec def addFiles(processedFiles: Seq[File], remainingFiles: Seq[File], processedImports: Set[File]): Seq[File] = {
      if (remainingFiles.isEmpty)
        processedFiles
      else {
        val fileGroups = remainingFiles.groupBy{ file =>
          importsMap(file).forall(processedImports.contains)
        }
        val newFiles = fileGroups.getOrElse(true, Seq.empty)
        val toBeProcessedFiles = fileGroups.getOrElse(false, Seq.empty)
        addFiles(processedFiles ++ newFiles, toBeProcessedFiles, newFiles.toSet)
      }
    }
    val result = addFiles(Seq.empty, files, Set.empty)
    result
  }

  // TODO This should be replaced by letting AVRO compile the IDL files directly, but I'm not sure how to do that now.
  private[this] val importPattern = """\s*import\s+idl\s+"([^"]+)"\s*;\s*""".r

  private[this] def getImports(file: File): Vector[File] = {
    val source = Source.fromFile(file)
    try {
      source.getLines().collect{
        case importPattern(currentImport) => new File(file.getParentFile, currentImport).getCanonicalFile
      }.toVector
    }
    finally source.close()
  }
}
