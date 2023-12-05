package com.wiseyak.utility

import java.io._
import java.nio.charset.{CharsetDecoder, CodingErrorAction}

import scala.io.{Codec, Source}

/**
  * This class is a part of the package com.wiseyak.utility and the package
  * is a part of the project keylookup.
  *
  * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
  * https://www.integratedict.com.np
  *
  * Created by Santa on 2020-07-17.
  */
object DataUtils {

    /**
      * Source file name.
      */
    private val shortSourceFile = "C:/WiseYak/data/MRXW_ENG.RRF"

    /**
      * Semantic type file name.
      */
    private val semanticTypeFileName = "C:/WiseYak/data/umls/MRSTY.RRF"

    /**
      * Concept information file name.
      */
    private val conceptInformationFileName = "C:/WiseYak/data/umls/MRCONSO.RRF"

    /**
      * Concept relation file name.
      */
    private val conceptRelationFileName = "C:/WiseYak/data/umls/MRREL.RRF"

    /**
      * UMLS Source File Name.
      */
    private val umlsSourceFile = "C:/WiseYak/data/umls"

    /**
      * UTF 8 decoder that is used to decode the relation file.
      */
    val utf8Decoder: CharsetDecoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.IGNORE)

    val UTF8: String = Codec.UTF8.name

    /**
      * Iterator of short source file.
      *
      * @return umlsDelimitedText
      */
    def umlsShortSource: Iterator[String] = Source.fromFile(shortSourceFile, UTF8).getLines()

    /**
      * UMLs Semantic Source file.
      *
      * @return semanticTypesLines, an iterator.
      */
    def umlsSemanticTypeSource: Iterator[String] = Source.fromFile(semanticTypeFileName, UTF8).getLines()

    /**
      * UMLs concept information source file.
      *
      * @return conceptInformationLines, an iterator.
      */
    def umlsConceptInformationSource: Iterator[String] = Source.fromFile(conceptInformationFileName, UTF8).getLines()

    /**
      * UMLs concept relation source file.
      *
      * @return conceptRelationLines, an iterator.
      */
    def umlsConceptRelationSource: Iterator[String] = Source.fromFile(conceptRelationFileName)(utf8Decoder).getLines()

    /**
      * Numeric text.
      */
    def isNumeric(textInput: String): Boolean = textInput.forall(_.isDigit)

    /**
      * Write text with line by line.
      *
      * @param linesIterator, lines text to write in the file.
      * @param fileName     , given output file.
      */
    def writeTextLines(linesIterator: Iterator[String], fileName: String): Unit = {
        val outputStreamWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), UTF8))
        try {
            linesIterator.foreach(line => {
                outputStreamWriter.write(line)
                outputStreamWriter.newLine()
            })
        } finally {
            outputStreamWriter.close()
        }
    }

    /**
      * Read lines of the file content.
      *
      * @param fileName
      * @return linesIterator
      */
    def readLines(fileName: String): Iterator[String] = Source.fromFile(fileName, UTF8).getLines()
}
