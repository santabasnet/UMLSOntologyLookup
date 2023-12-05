package com.wiseyak.indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * This class is a part of the package com.wiseyak.indexing and the package
 * is a part of the project keylookup.
 * <p>
 * Integrated ICT Pvt. Ltd. Jwagal, Lalitpur, Nepal.
 * https://www.integratedict.com.np
 * <p>
 * Created by Santa on 2020-07-18.
 */
public class NGramAnalyzer extends Analyzer {
    
    private static final int minGram = 3;
    private static final int maxGram = 5;
    
    /**
     * Default constructor.
     */
    public NGramAnalyzer() {
    }
    
    /**
     * This is the product name analyzer.
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new StandardTokenizer();
        TokenStream lowerCaseFilter = new LowerCaseFilter(source);
        TokenStream result = new NGramTokenFilter(lowerCaseFilter, minGram, maxGram, true);
        return new TokenStreamComponents(source, result);
    }
}
