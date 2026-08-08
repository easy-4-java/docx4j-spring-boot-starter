package org.docx4j.spring.boot.utils;

/**
 * String utility that extends Spring's {@link org.springframework.util.StringUtils} with a tokenizer
 * tuned for comma/semicolon/whitespace-delimited lists such as font aliases.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class StringUtils extends org.springframework.util.StringUtils{

	/**
	 * Any number of these characters are considered delimiters between
	 * multiple context config paths in a single String value.
	 */
	public static String CONFIG_LOCATION_DELIMITERS = ",; \t\n";


	/** Tokenize the given string using the default delimiters, trimming and discarding empty tokens. @param str the string to tokenize @return the token array */
	public static String[] tokenizeToStringArray(String str) {
		return tokenizeToStringArray(str, CONFIG_LOCATION_DELIMITERS, true, true);
	}

}
