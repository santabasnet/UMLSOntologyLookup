package com.wiseyak.luceneapp

import java.util.Date

import com.wiseyak.indexing.{DocumentLookUp, Indexer}
import com.wiseyak.utility.{DataFlattener, DataUtils, PreProcessing}

import scala.util.Try

/**
  * This class is a part of the package com.wiseyak.luceneapp and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-17.
  */
object IndexApp {

    /**
      * Main application, that starts for indexing.
      */
    def main(args: Array[String]): Unit = {

        println("Indexing started .... " + new Date())

        /**
          * Clean previous data here.
          */
        Try {
            Indexer.indexWriter.deleteAll()
            Indexer.indexWriter.commit()
        }

        /**
          * Batch size to make commit.
          */
        val batchSize = 10000

        val finalDataSource = DataUtils
            .readLines(DataFlattener.finalOutputFile)
            .map(_.split(PreProcessing.delimiter).toList)

        var count = 1

        finalDataSource.take(100000)
            .grouped(batchSize)
            .foreach(batch => {
                batch.map(DocumentLookUp.buildIndexerMap)
                    .foreach(document => DocumentLookUp(document, Indexer.indexWriter).buildIndex)
                Indexer.indexWriter.commit()
                println("Count : " + count)
                count = count + 1
            })

        println("Completed. " + new Date())

    }

}
