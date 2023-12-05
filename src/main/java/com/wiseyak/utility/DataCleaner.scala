package com.wiseyak.utility

import java.util.Date

/**
  * This class is a part of the package com.wiseyak.utility and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-21.
  */
object DataCleaner {

    /**
      * Cleaned umls short file name.
      */
    val cleanedUmlsShortFile: String = "resources/data/cleaned_umls_short.txt"

    /**
      * Cleaned umls semantic type file name.
      */
    val cleanedUmlsSemanticTypeFile: String = "resources/data/cleaned_umls_semantic_type.txt"

    /**
      * Cleaned umls concept information file name.
      */
    val cleanedUmlsConceptInformationFile: String = "resources/data/cleaned_umls_concept_information.txt"

    /**
      * Cleaned umls concept relations file name.
      */
    val cleanedUmlsConceptRelationsFile = "resources/data/cleaned_umls_concept_relations.txt"

    /**
      * Perform cleaning here.
      */
    def performCleaning(): Unit = {

        println("\n Data Cleaning Started ...")

        println("\n\t1. Cleaning short umls file started ... at " + new Date())
        /**
          * All the short umls data.
          * Total Entries: 44,750,130
          */
        val shortData: Iterator[String] = PreProcessing.shortUMLsSource.map(_.mkString("|"))
        DataUtils.writeTextLines(shortData, cleanedUmlsShortFile)
        println("\t\tFile write completed at " + new Date())

        println("\n\t2. Cleaning umls semantic file file started ... at " + new Date())
        /**
          * All the semantic type information.
          * Total Entries: 3,962,617
          */
        val umlsSemanticTypeData = PreProcessing.umlsSemanticTypeSource.map(_.mkString("|"))
        DataUtils.writeTextLines(umlsSemanticTypeData, cleanedUmlsSemanticTypeFile)
        println("\t\tFile write completed at " + new Date())

        println("\n\t3. Cleaning short umls concept information file started ... at " + new Date())
        /**
          * All the concept information.
          * Total Entries: 627,645
          */
        val umlsConceptInformation = PreProcessing.umlsConceptInformationSource.map(_.mkString("|"))
        DataUtils.writeTextLines(umlsConceptInformation, cleanedUmlsConceptInformationFile)
        println("\t\tFile write completed at " + new Date())

        println("\n\t4. Cleaning short umls concept relation file started ... at " + new Date())
        /**
          * All the concept relations.
          * Total entries: 5,155,917
          */
        val umlsConceptRelations = PreProcessing.umlsConceptRelationSource.map(_.mkString("|"))
        DataUtils.writeTextLines(umlsConceptRelations, cleanedUmlsConceptRelationsFile)
        println("\t\tFile write completed at " + new Date())

        println("\nData cleaning Completed Successfully.\n")
    }

}
