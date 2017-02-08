package sbtavrohugger.filesorter

import java.io.File

import scala.io.Source

/**
  * Created by jon.morra on 2/7/17.
  */
object AVDLFileSorter {
  def sortSchemaFiles(filesTraversable: Traversable[File]): Seq[File] = {
    val files = filesTraversable.toSeq
    val importsMap = files.map{ file =>
      (file.getCanonicalFile, getImports(file))
    }.toMap

    def addSchema(currentFiles: Seq[File], processedImports: Set[File]): Seq[File] = {
      val initialValue = (Seq.empty[File], Seq.empty[File])
      val (newFiles, remainingFiles) = currentFiles.foldLeft(initialValue){ case((n, r), acc) =>
          val imports = importsMap(acc)
          if (imports.forall(processedImports.contains)) {
            (n :+ acc, r)
          }
          else {
            (n, r :+ acc)
          }
      }
      if (remainingFiles.isEmpty)
        newFiles
      else
        newFiles ++ addSchema(remainingFiles, newFiles.toSet)
    }
    val result = addSchema(files, Set.empty)
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
