package org.docx4j.spring.boot.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StringUtils}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class StringUtilsTest {

	@Test
	void shouldTokenizeByComma() {
		assertThat(StringUtils.tokenizeToStringArray("a,b,c")).containsExactly("a", "b", "c");
	}

	@Test
	void shouldTokenizeBySemicolon() {
		assertThat(StringUtils.tokenizeToStringArray("a;b;c")).containsExactly("a", "b", "c");
	}

	@Test
	void shouldTokenizeByWhitespace() {
		assertThat(StringUtils.tokenizeToStringArray("a b\tc\nd")).containsExactly("a", "b", "c", "d");
	}

	@Test
	void shouldTrimAndDiscardEmptyTokens() {
		assertThat(StringUtils.tokenizeToStringArray(" a , , b ,  ")).containsExactly("a", "b");
	}

	@Test
	void shouldHandleNullAndBlank() {
		assertThat(StringUtils.tokenizeToStringArray(null)).isEmpty();
		assertThat(StringUtils.tokenizeToStringArray("")).isEmpty();
		assertThat(StringUtils.tokenizeToStringArray(" , ; ")).isEmpty();
	}

	@Test
	void shouldExtendSpringStringUtils() {
		StringUtils utils = new StringUtils();
		assertThat(utils).isInstanceOf(org.springframework.util.StringUtils.class);
		assertThat(StringUtils.CONFIG_LOCATION_DELIMITERS).isEqualTo(",; \t\n");
	}

}
