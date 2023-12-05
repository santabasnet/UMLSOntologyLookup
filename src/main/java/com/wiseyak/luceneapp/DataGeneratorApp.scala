package com.wiseyak.luceneapp

import com.wiseyak.utility.{DataCleaner, DataFlattener}

/**
  * This class is a part of the package com.wiseyak.luceneapp and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-22.
  */
object DataGeneratorApp {

    /**
      * Data generation started here.
      * 1. It takes input of four files of UMLS data.
      * 2. Transforms these data using given criteria to four cleaned files.
      *     a. cleaned_umls_short.txt
      *     b. cleaned_umls_semantic_type.txt
      *     c. cleaned_umls_concept_information.txt
      *     d. cleaned_umls_concept_relations.txt
      * 3. Combines these 4 cleaned files to final_output.txt by merging and flattening
      * strategy.
      * *
      *
      * @param args
      */
    def main(args: Array[String]): Unit = {

        println("\nStep 1: ")
        DataCleaner.performCleaning()
        Thread.sleep(100)

        println("\nStep 2: ")
        DataFlattener.perform()

        /**
          * Total generated data.
          * 4,908,596
          */
    }

}
