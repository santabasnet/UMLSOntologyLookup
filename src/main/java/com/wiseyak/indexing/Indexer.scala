package com.wiseyak.indexing

import java.nio.file.Paths

import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory

import scala.util.Try

/**
  * This class is a part of the package com.wiseyak.indexing and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-19.
  */
case class Indexer(indexDirectory: String) {

    /**
      * Creates an index writer.
      *
      * @return anIndexWriter
      */
    def getWriter: IndexWriter = {
        val indexDirectory = FSDirectory.open(Paths.get(Indexer.indexDirectory))
        val writerConfiguration = new IndexWriterConfig(DocumentLookUp.fieldWiseAnalyzers)
        new IndexWriter(indexDirectory, writerConfiguration)
    }

}

/**
  * Helper Object.
  */
object Indexer {

    /**
      * Root Directory.
      */
    val rootDirectory: String = "resources"

    /**
      * Data path.
      */
    val dataDirectory: String = rootDirectory + "/data"

    /**
      * Index path.
      */
    val indexDirectory: String = rootDirectory + "/index"

    /**
      * Initialize Index writer.
      */
    var indexWriter = Indexer(indexDirectory).getWriter

    /**
      * Returns index writer.
      *
      * @return indexWriter
      */
    def writer: IndexWriter = if (indexWriter == null || !indexWriter.isOpen) {
        Indexer.indexWriter = Indexer(indexDirectory).getWriter
        Indexer.indexWriter
    } else Indexer.indexWriter

    /**
      * Close index writer here.
      */
    def close: Unit = Try(Indexer.indexWriter.close())

}
