/**
 * 
 */
package com.springyweb.elasticsearch.index.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * @author si<si@springyweb.com>
 * 
 * Prepends the count of the token in the stream to the term stored in the index 
 * 
 */
public class TokenCountFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private int count = 0;
	
	public TokenCountFilter(final TokenStream input) {
		super(input);
	}
	
	/**
	 * Prepend the value of ${count} to each token and increment ${count}
	 */
	@Override
	public final boolean incrementToken() throws IOException {
		boolean increment = false;
		if (input.incrementToken()) {
			final char[] count = String.valueOf(this.count++).toCharArray();
			termAtt.resizeBuffer(termAtt.length() + count.length);
			termAtt.setLength(termAtt.length() + count.length);
			System.arraycopy(termAtt.buffer(), 0, termAtt.buffer(), count.length, termAtt.length());
			System.arraycopy(count, 0, termAtt.buffer(), 0, count.length);
			increment = true;
		}
		return increment;
	}
	
	@Override 
	public void end() throws IOException {
		super.end();
		count = 0;
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		count = 0;
	}
}