package com.wiseyak.luceneapp

import com.wiseyak.indexing.DocumentLookUp
import com.wiseyak.search.Searcher

/**
  * This class is a part of the package com.wiseyak.luceneapp and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-17.
  */
object SearchApp {

    def main(args: Array[String]) : Unit = {
        val fieldName = DocumentLookUp.PREFERRED_TERM
        val queryText = "joint pain"
        //val queryText = "1,2-dipalmitoylphosphatidylcholine"
        Searcher.execute(fieldName, queryText)
    }

}
