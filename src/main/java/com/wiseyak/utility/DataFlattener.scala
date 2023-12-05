package com.wiseyak.utility

import java.util.Date

import scala.collection.mutable

/**
  * This class is a part of the package com.wiseyak.utility and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-21.
  */
object DataFlattener {

    /**
      * Final output file.
      */
    val finalOutputFile = "resources/data/final_output.txt"

    /**
      * Flatten the umls data and write the final output to the file.
      */
    def perform(): Unit = {

        println("\n1. Data loading started ...\t\t\t\t" + new Date())

        /**
          * Short source of cleaned data.
          */
        val umlsShortSource = DataUtils
            .readLines(DataCleaner.cleanedUmlsShortFile)
            .map(_.split(PreProcessing.delimiter).toList)
            .map(items => if (items.size == 1) items :+ PreProcessing.emptyEntry else items)
            .foldLeft(mutable.Map[String, String]())((result, item) => result += (item.head -> item.last))
            .toMap

        /**
          * Needs to iterate for flattening.
          */
        val umlsSemanticTypeSource = DataUtils
            .readLines(DataCleaner.cleanedUmlsSemanticTypeFile)
            .map(_.split(PreProcessing.delimiter).toList)
        //umlsSemanticTypeSource.take(10).foreach(println)

        val umlsConceptInformationSource = DataUtils
            .readLines(DataCleaner.cleanedUmlsConceptInformationFile)
            .map(_.split(PreProcessing.delimiter).toList)
            .map(items => items.head -> items.drop(1))
            .toMap
        //umlsConceptInformationSource.take(10).foreach(println)

        /**
          * Builds map of umls CUI with their list of parents.
          * CUI -> List of CUIs
          */
        val umlsConceptRelationSource = DataUtils
            .readLines(DataCleaner.cleanedUmlsConceptRelationsFile)
            .map(_.split(PreProcessing.delimiter).toList)
            .foldLeft(mutable.Map[String, List[String]]())((result, item) => {
                val parents = result.getOrElse(item.head, List())
                result += item.head -> (parents :+ item.last)
            }).toMap
        //umlsConceptRelationSource.take(100).foreach(println)

        println("\n2. Data loading completed.\t\t\t\t" + new Date())

        //1. First expansion: UMLs concept to attach word field.
        val semanticTypeWithWord = umlsSemanticTypeSource.map(semanticType => {
            List(semanticType.head, umlsShortSource.getOrElse(semanticType.head, PreProcessing.emptyEntry)) ++ semanticType.tail
        })

        //2. Second expansion: attach umls concept information for the result 1.
        val semanticTypeInformation = semanticTypeWithWord.map(umlsWithType => {
            umlsWithType ++ umlsConceptInformationSource.getOrElse(umlsWithType.head, List(PreProcessing.emptyEntry, PreProcessing.emptyEntry))
        })

        //3. Attach parental relation.
        val semanticTypeInformationRelation = semanticTypeInformation.flatMap(umlsWithTypeInfo => {
            val parentalRelation = umlsConceptRelationSource.getOrElse(umlsWithTypeInfo.head, List(PreProcessing.emptyEntry))
            parentalRelation.map(item => umlsWithTypeInfo ++ List(PreProcessing.parent, item))
        })

        println("\n3. Data flattening completed.\t\t\t" + new Date())

        //4. Perform duplicate filter.
        println("\n4. Removing duplicate entries ...\t\t" + new Date())

        //5. Write final output.
        val updatedData: mutable.HashSet[Int] = new mutable.HashSet[Int]()
        val finalOutput: Iterator[String] = semanticTypeInformationRelation
            .map(items => {
                val code = items.mkString.hashCode
                if (updatedData.contains(code)) None else {
                    updatedData.add(code)
                    Some(items)
                }
            }).filter(_.isDefined).map(_.get.mkString("|"))

        DataUtils.writeTextLines(finalOutput, finalOutputFile)

        println("\n5. Data written successfully.\t\t\t" + new Date())

    }

}
