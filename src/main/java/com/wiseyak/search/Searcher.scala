package com.wiseyak.search

import java.nio.file.Paths

import com.wiseyak.indexing.{DocumentLookUp, Indexer}
import org.apache.lucene.facet.FacetsCollector
import org.apache.lucene.facet.sortedset.{DefaultSortedSetDocValuesReaderState, SortedSetDocValuesFacetCounts}
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, MatchAllDocsQuery, Query}
import org.apache.lucene.store.FSDirectory

/**
  * This class is a part of the package com.wiseyak.search and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-22.
  */
object Searcher {

    /**
      * Index Searcher.
      */
    private val keywordSearcher: IndexSearcher = createKeywordSearcher

    /**
      * Build and execute search query for the given text.
      *
      * @param fieldName, given search field name, determines the analyzer strategy.
      * @param text     , query text.
      */
    def execute(fieldName: String, text: String) = {
        val analyzer = DocumentLookUp.analyzerOfField(fieldName)
        val query: Query = new QueryParser(fieldName, DocumentLookUp.ngramAnalyzer).parse(text)
        println("\nSearching for: " + query.toString(fieldName))
        println()

        val maxResults = 10
        //val groupingSearch = new GroupingSearch(DocumentLookUp.CONCEPT_UNIQUE_IDENTIFIER)
        //val topDocs = keywordSearcher.search(query, maxResults)
        /*topDocs.scoreDocs.toList.foreach(scoreDoc => {
            val document = keywordSearcher.doc(scoreDoc.doc)
            println(document.getField(fieldName).stringValue())
        })*/


        val state = new DefaultSortedSetDocValuesReaderState(keywordSearcher.getIndexReader)
        val facetsCollector = new FacetsCollector()
        FacetsCollector.search(keywordSearcher, new MatchAllDocsQuery(), 1000, facetsCollector)
        val facets = new SortedSetDocValuesFacetCounts(state, facetsCollector)
        val facetsResult = facets.getTopChildren(maxResults, DocumentLookUp.SEMANTIC_TYPE)

        (0 until maxResults).foreach(index => {
            val labelAndValue = facetsResult.labelValues(index)
            println(String.format("%s (%s)", labelAndValue.label, labelAndValue.value))
        })

        keywordSearcher.getIndexReader.close()
    }


    /**
      * Creates the keyword search object.
      *
      * @return indexSearcher
      */
    private def createKeywordSearcher: IndexSearcher = {
        val indexDirectory = FSDirectory.open(Paths.get(Indexer.indexDirectory))
        val indexReader = DirectoryReader.open(indexDirectory)
        new IndexSearcher(indexReader)
    }

}
