package com.wiseyak.indexing

import java.util

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.core.KeywordAnalyzer
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document._
import org.apache.lucene.facet.FacetsConfig
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.util.BytesRef

/**
  * This class is a part of the package com.wiseyak.indexing and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-17.
  */
case class DocumentLookUp(fieldData: Map[String, String], indexWriter: IndexWriter) {

    /**
      * Builds an index from the map data.
      * The data contains field name associated with field value.
      */
    def buildIndex: Option[Long] = buildDocument.map(document => {
        indexWriter.addDocument(document)
    })

    /**
      * Prepare document.
      */
    private def buildDocument: Option[Document] = if (!isValid) None else {
        val document: Document = new Document()

        document.add(new SortedSetDocValuesFacetField(DocumentLookUp.CONCEPT_UNIQUE_IDENTIFIER, fieldData(DocumentLookUp.CONCEPT_UNIQUE_IDENTIFIER)))
        document.add(new SortedSetDocValuesFacetField(DocumentLookUp.WORD_NAME, fieldData(DocumentLookUp.WORD_NAME)))
        //document.add(new TextField(DocumentLookUp.WORD_NAME, fieldData(DocumentLookUp.WORD_NAME), Field.Store.YES))

        document.add(new SortedSetDocValuesFacetField(DocumentLookUp.SEMANTIC_TYPE_IDENTIFIER, fieldData(DocumentLookUp.SEMANTIC_TYPE_IDENTIFIER)))
        document.add(new SortedSetDocValuesFacetField(DocumentLookUp.SEMANTIC_TYPE, fieldData(DocumentLookUp.SEMANTIC_TYPE)))

        document.add(new SortedSetDocValuesFacetField(DocumentLookUp.UNIQUE_IDENTIFIER_CODE, fieldData(DocumentLookUp.UNIQUE_IDENTIFIER_CODE)))
        document.add(new SortedSetDocValuesFacetField(DocumentLookUp.PREFERRED_TERM, fieldData(DocumentLookUp.PREFERRED_TERM)))

        document.add(new TextField(DocumentLookUp.RELATIONSHIP_LABEL, fieldData(DocumentLookUp.RELATIONSHIP_LABEL), Field.Store.YES))
        document.add(new TextField(DocumentLookUp.CONCEPT_UNIQUE_SECOND_IDENTIFIER, fieldData(DocumentLookUp.CONCEPT_UNIQUE_SECOND_IDENTIFIER), Field.Store.YES))

        document.add(new SortedSetDocValuesField(DocumentLookUp.CONCEPT_UNIQUE_IDENTIFIER, new BytesRef(DocumentLookUp.CONCEPT_UNIQUE_IDENTIFIER)))
        document.add(new SortedSetDocValuesField(DocumentLookUp.SEMANTIC_TYPE, new BytesRef(DocumentLookUp.SEMANTIC_TYPE)))
        document.add(new SortedSetDocValuesField(DocumentLookUp.SEMANTIC_TYPE_IDENTIFIER, new BytesRef(DocumentLookUp.SEMANTIC_TYPE_IDENTIFIER)))
        document.add(new SortedSetDocValuesField(DocumentLookUp.PREFERRED_TERM, new BytesRef(DocumentLookUp.PREFERRED_TERM)))

        Some(DocumentLookUp.facetConfiguration.build(document))
    }

    /**
      * Is valid data.
      */
    private def isValid: Boolean = fieldData.keySet.forall(DocumentLookUp.availableFields.contains(_))

}

/**
  * Utility for the companion object.
  */
object DocumentLookUp {

    /**
      * Field name literals.
      */
    val CONCEPT_UNIQUE_IDENTIFIER = "conceptUniqueIdentifier"
    val WORD_NAME = "wordName"
    val SEMANTIC_TYPE_IDENTIFIER = "semanticTypeIdentifier"
    val SEMANTIC_TYPE = "semanticType"
    val UNIQUE_IDENTIFIER_CODE = "uniqueIdentifierCode"
    val PREFERRED_TERM = "stringValue"
    val RELATIONSHIP_LABEL = "relationshipLabel"
    val CONCEPT_UNIQUE_SECOND_IDENTIFIER = "uniqueIdentifierOfSecondConcept"
    val FACETS_PREFIX = "facets"

    /**
      * Required Data Fields.
      */
    val availableFields = List(
        CONCEPT_UNIQUE_IDENTIFIER,
        WORD_NAME,
        SEMANTIC_TYPE_IDENTIFIER,
        SEMANTIC_TYPE,
        UNIQUE_IDENTIFIER_CODE,
        PREFERRED_TERM,
        RELATIONSHIP_LABEL,
        CONCEPT_UNIQUE_SECOND_IDENTIFIER)

    /**
      * Utility that builds a field data to be indexed.
      *
      * @param items, field values to be indexed.
      * @return mapOfFieldData
      */
    def buildIndexerMap(items: List[String]): Map[String, String] =
        availableFields.zip(items).map(entry => entry._1 -> entry._2).toMap

    /**
      * Standard Analyzer.
      */
    val standardAnalyzer: StandardAnalyzer = new StandardAnalyzer()

    /**
      * Keyword Analyzer.
      */
    val keyWordAnalyzer: KeywordAnalyzer = new KeywordAnalyzer()

    /**
      * NGram Analyzer for a word.
      */
    val ngramAnalyzer: NGramAnalyzer = new NGramAnalyzer()

    /**
      * All the analyzers map.
      */
    val allAnalyzers: util.HashMap[String, Analyzer] = {
        val analyzerPerField = new java.util.HashMap[String, Analyzer]()

        analyzerPerField.put(CONCEPT_UNIQUE_IDENTIFIER, keyWordAnalyzer)
        analyzerPerField.put(WORD_NAME, ngramAnalyzer)

        analyzerPerField.put(SEMANTIC_TYPE_IDENTIFIER, keyWordAnalyzer)
        analyzerPerField.put(SEMANTIC_TYPE, ngramAnalyzer)

        analyzerPerField.put(UNIQUE_IDENTIFIER_CODE, keyWordAnalyzer)
        analyzerPerField.put(PREFERRED_TERM, ngramAnalyzer)

        analyzerPerField.put(RELATIONSHIP_LABEL, keyWordAnalyzer)
        analyzerPerField.put(CONCEPT_UNIQUE_SECOND_IDENTIFIER, keyWordAnalyzer)
        analyzerPerField
    }

    /**
      * Facets configuration for some fields.
      *
      * @return facetsConfiguration
      */
    def facetConfiguration: FacetsConfig = {
        val facetsConfig = new FacetsConfig()
        facetsConfig.setMultiValued(CONCEPT_UNIQUE_IDENTIFIER, true)
        facetsConfig.setMultiValued(WORD_NAME, true)
        facetsConfig.setMultiValued(SEMANTIC_TYPE, true)
        facetsConfig.setMultiValued(SEMANTIC_TYPE_IDENTIFIER, true)
        facetsConfig.setMultiValued(UNIQUE_IDENTIFIER_CODE, true)
        facetsConfig.setMultiValued(PREFERRED_TERM, true)
        facetsConfig
    }

    /**
      * Returns the analyzer of the given field name.
      */
    def analyzerOfField(fieldName: String): Analyzer =
        allAnalyzers.getOrDefault(fieldName.toUpperCase, standardAnalyzer)

    /**
      * Factory that supports for lookup data.
      *
      * @param fieldData, map of field data.
      * @param writer   , an index writer created and ready to write.
      * @return keyLookUp instance.
      */
    def buildWith(fieldData: Map[String, String], writer: IndexWriter): DocumentLookUp = DocumentLookUp(fieldData, writer)

    /**
      * Extract per-field analyzer.
      * Needs to build for all the fields.
      */
    def fieldWiseAnalyzers: Analyzer = new PerFieldAnalyzerWrapper(standardAnalyzer, allAnalyzers)
}

