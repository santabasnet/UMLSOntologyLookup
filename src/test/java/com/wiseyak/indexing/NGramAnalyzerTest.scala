package com.wiseyak.indexing

import java.io.StringReader

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

import scala.collection.mutable
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
object NGramAnalyzerTest {

    /**
      * Test App.
      */
    def main(args: Array[String]): Unit = {
        val analyzer = new NGramAnalyzer()
        val givenText = "1,2-dipalmitoylphosphatidylcholine"
        val result = analyze(analyzer, givenText)
        println("Tokens : " + result)
    }

    /**
      * Perform text analysis using NGramAnalyzer.
      *
      * @param analyzer
      * @param text
      * @return listOfString
      */
    private def analyze(analyzer: NGramAnalyzer, text: String): List[String] = Try {
        var result = new mutable.ListBuffer[String]()
        val tokenStream = analyzer.tokenStream(null, new StringReader(text))
        tokenStream.reset()
        while (tokenStream.incrementToken()) {
            result += tokenStream.getAttribute(classOf[CharTermAttribute]).toString
        }
        result.toList
    }.getOrElse(List[String]())


}
