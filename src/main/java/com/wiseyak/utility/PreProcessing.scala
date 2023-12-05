package com.wiseyak.utility

import scala.collection.immutable.HashSet
import scala.util.Try

/**
  * This class is a part of the package com.wiseyak.utility and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-20.
  */
object PreProcessing {

    /**
      * Empty Value
      */
    val emptyEntry = "_"

    /**
      * Line delimiter.
      */
    val delimiter = '|'

    /**
      * Parent relation.
      */
    val parent = "PAR"

    /**
      * Fields name with their position places in the UMLs flat data.
      */
    private val umlsShortSourceFields = Map[String, Int](
        "word" -> 1,
        "conceptUniqueIdentifier" -> 2
    )

    /**
      * Defines all the umls short terms file indices for the fields.
      */
    private val umlsShortFieldIndices = umlsShortSourceFields.values.toList.sorted

    /**
      * Defines all the umls semantic type fields in the given data file.
      */
    private val umlsSemanticTypeFields = Map[String, Int](
        "conceptUniqueIdentifier" -> 0,
        "semanticTypeIdentifier" -> 1,
        "semanticType" -> 3
    )

    /**
      * Defines all the semantic type indices in the fields.
      */
    private val umlsSemanticTypeIndices = umlsSemanticTypeFields.values.toList.sorted

    /**
      * Defines all the concept information fields in the data to be indexed.
      */
    private val umlsConceptInformationFields = Map[String, Int](
        "conceptUniqueIdentifier" -> 0,
        "languageOfTerm" -> 1,
        "termStatus" -> 2,
        "uniqueIdentifierCode" -> 3,
        "typeString "-> 4,
        "indicationOfAUIPreference" -> 6,
        "sourceAbbreviation" -> 11,
        "stringValue" -> 14
    )

    /**
      * Defines all the indices of concept information from the concept relation fields.
      */
    private val umlsConceptInformationIndices = umlsConceptInformationFields.values.toList.sorted

    /**
      * Concept relation fields.
      */
    private val umlsConceptRelationFields = Map[String, Int](
        "conceptUniqueIdentifier" -> 0,
        "relationshipLabel" -> 3,
        "uniqueIdentifierOfSecondConcept" -> 4
    )

    /**
      * Defines all the concept sources to be indexed.
      */
    private val umlsConceptSources = HashSet("ICD10AE", "ICD10AMAE", "ICD10AM", "ICD10CM", "ICD10PCS", "ICD10", "SNOMEDCT_US", "MSH", "RXNORM", "MTH")

    /**
      * Defines all the relations criteria to be indexed.
      */
    private val umlsRelationsCriteria = HashSet("PAR")

    /**
      * Defines all the indices of concept relations from the concept relation fields.
      */
    private val umlsConceptRelationIndices = umlsConceptRelationFields.values.toList.sorted

    /**
      * Transform and filters the fields.
      * Field 1 -> word
      * Field 2 -> conceptUniqueIdentifier
      */
    def shortUMLsSource: Iterator[List[String]] = DataUtils.umlsShortSource
        .map(_.split(delimiter).toList)
        .map(items => items.indices.collect { case index if umlsShortFieldIndices.contains(index) => items(index) })
        .map(items => List(items.last, PreProcessing.leftNumericTrim(items.head)))
        .filter(_.head.nonEmpty)

    /**
      * It generates all the semantic types data to be indexed.
      *
      * @return iteratorOfSemanticTypes
      */
    def umlsSemanticTypeSource: Iterator[List[String]] = DataUtils.umlsSemanticTypeSource
        .map(_.split(delimiter).toList)
        .map(items => items.indices.collect { case index if umlsSemanticTypeIndices.contains(index) => items(index) })
        .map(_.toList)

    /**
      * It generates all the concept information to be indexed.
      * The concept information should be passed the given criteria.
      *
      * @return iteratorOfConceptInformation
      */
    def umlsConceptInformationSource: Iterator[List[String]] = DataUtils.umlsConceptInformationSource
        .map(_.split(delimiter))
        .filter(informationCriteria)
        .map(items => items.indices.collect { case index if umlsConceptInformationIndices.contains(index) => items(index) })
        .map(_.toList)

    /**
      * It generates all the concept information that passes the given criteria(parent for now)
      * from the concept relation.
      *
      * @return iteratorOfConceptRelation
      */
    def umlsConceptRelationSource: Iterator[List[String]] = {
        DataUtils.umlsConceptRelationSource
            .map(line => line.split(delimiter))
            .filter(items => Try(items(3) == parent).getOrElse(false))
            .map(items => items.indices.collect { case index if umlsConceptRelationIndices.contains(index) => items(index) })
            .map(_.toList)
    }

    /**
      * Filters the numeric part in the given string appears to be prefixed.
      *
      * @param textInput, given text to be cleaned.
      * @return filtersPrefixedNumericPart
      */
    private def leftNumericTrim(textInput: String): String = textInput.dropWhile(_.isDigit)

    /**
      * Sets criteria to filter the concept information.
      *
      * @param items, list of items to be checked for information criteria.
      * @return true if the concept passes the given criteria.
      */
    private def informationCriteria(items: Array[String]): Boolean =
        items(1) == "ENG" && items(2) == "P" && items(4) == "PF" && items(6) == "Y" && umlsConceptSources.contains(items(11))

    /**
      * Filter information.
      */
    private def filterBySourceOfIndices(source: Iterator[String], indices: List[Int]): Iterator[List[String]] = source
        .map(_.split(delimiter).toList)
        .map(items => items.indices.collect { case index if indices.contains(index) => items(index) })
        .map(_.toList)

}
