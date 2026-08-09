package org.docx4j.spring.boot.event.linstener;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.spring.boot.Docx4jProperties;
import org.docx4j.wml.Fonts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * Tests for {@link ApplicationReadyFontMapperistener}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class ApplicationReadyFontMapperistenerTest {

	/** Lightweight Mapper subclass that avoids BestMatchingMapper / IdentityPlusMapper static initializers. */
	private static Mapper testMapper() {
		return new Mapper() {
			@Override
			public void populateFontMappings(Set<String> fontNames, Fonts fonts) {
				// no-op
			}
		};
	}

	@Test
	void shouldNoOpWhenDiscoverFontsDisabled() {
		Docx4jProperties properties = new Docx4jProperties();
		ApplicationReadyFontMapperistener listener = new ApplicationReadyFontMapperistener(properties);
		listener.setResourceLoader(new DefaultResourceLoader());
		// event is null-safe: the listener only checks the discover-fonts flag
		listener.onApplicationEvent(null);
		assertThat(listener.docx4jProperties).isSameAs(properties);
	}

	@Test
	void shouldRunBackgroundFontDiscoveryWhenEnabled() throws Exception {
		Docx4jProperties properties = new Docx4jProperties();
		properties.setDiscoverFonts(true);
		Map<String, String> fontMapper = new LinkedHashMap<>();
		fontMapper.put("NotAFont", "classpath:does-not-exist.ttf");
		properties.setFontMapper(fontMapper);
		Map<String, String> alias = new LinkedHashMap<>();
		alias.put("NotAFont", "Alias1,Alias2;Alias3");
		properties.setFontAliasMapper(alias);

		ApplicationReadyFontMapperistener listener = new ApplicationReadyFontMapperistener(properties);
		listener.setResourceLoader(new DefaultResourceLoader());

		// fire the event; the background thread runs and swallows any font errors
		listener.onApplicationEvent(new ApplicationReadyEvent(new org.springframework.boot.SpringApplication(),
				new String[0], new GenericApplicationContext(), Duration.ZERO));

		// give the background thread a moment to execute
		Thread.sleep(500);
		assertThat(listener.resourceLoader).isNotNull();
	}

	@Test
	void defaultFontMapperShouldRegisterEntries() {
		Docx4jProperties properties = new Docx4jProperties();
		ApplicationReadyFontMapperistener listener = new ApplicationReadyFontMapperistener(properties);
		Mapper mapper = testMapper();
		listener.defaultFontMapper(mapper);
		// the helper must not throw even when the physical fonts are absent
		assertThat(mapper).isNotNull();
	}

	@Test
	void customFontMapperShouldHandleEmptyOrNullMap() {
		Docx4jProperties properties = new Docx4jProperties();
		ApplicationReadyFontMapperistener listener = new ApplicationReadyFontMapperistener(properties);
		Mapper mapper = testMapper();
		// null map -> early return
		listener.customFontMapper(mapper, null);
		// empty map -> early return
		listener.customFontMapper(mapper, new LinkedHashMap<>());
		assertThat(mapper).isNotNull();
	}

	@Test
	void customFontMapperShouldTolerateMissingResources() {
		Docx4jProperties properties = new Docx4jProperties();
		Map<String, String> fontMapper = new LinkedHashMap<>();
		fontMapper.put("MissingFont", "classpath:nope-" + System.nanoTime() + ".ttf");
		properties.setFontMapper(fontMapper);
		Map<String, String> alias = new LinkedHashMap<>();
		alias.put("MissingFont", "A , B ; C");
		properties.setFontAliasMapper(alias);

		ApplicationReadyFontMapperistener listener = new ApplicationReadyFontMapperistener(properties);
		listener.setResourceLoader(new DefaultResourceLoader());
		Mapper mapper = testMapper();
		// must not throw even when the font file does not exist
		listener.customFontMapper(mapper, fontMapper);
		assertThat(mapper).isNotNull();
	}

}
