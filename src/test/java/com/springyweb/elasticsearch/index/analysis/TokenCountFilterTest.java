/**
 * 
 */
package com.springyweb.elasticsearch.index.analysis;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.path.PathHierarchyTokenizer;
import org.junit.Test;

/**
 * @author si
 *
 */
public class TokenCountFilterTest {

    private class TestAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer source = new PathHierarchyTokenizer(reader);
	     TokenStream filter = new TokenCountFilter(source);
	     return new TokenStreamComponents(source, filter);

	}   
    }
    
    /**
     * Create a string of ${pathElementCount} path elements
     * and asserts that each path in the hierarchy is prepended with its depth.
     * 
     * e.g given the input a/a/a/a
     * 
     * We would expect the output
     * 
     * 0/a
     * 1/a/a
     * 2/a/a/a
     * 3/a/a/a/a
     * @throws IOException 
     * 
     * @throws Exception
     */
    @Test
    public void testTokens() throws IOException {
      int pathElementCount = 10;
      StringBuffer input = new StringBuffer();
      String[] output = new String[pathElementCount];
      String pathElement = "/a";
      for(int i = 0; i< pathElementCount; i++) {
		  input.append(pathElement);
		  output[i] = i + input.toString(); 
      }
      
      Analyzer testAnalyzer = new TestAnalyzer();
      BaseTokenStreamTestCase.assertAnalyzesTo(testAnalyzer, input.toString(),output);
    }
}