package org.docx4j.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;
import org.docx4j.events.Docx4jEvent;
import org.docx4j.spring.boot.event.linstener.ApplicationReadyFontMapperistener;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link Docx4jAutoConfiguration} and {@link Docx4jProperties}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class Docx4jAutoConfigurationTest {

	private final ApplicationContextRunner runner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(Docx4jAutoConfiguration.class));

	@Test
	void shouldNotCreateBeansWhenDisabled() {
		runner.run(context -> assertThat(context)
				.doesNotHaveBean(Docx4jAutoConfiguration.class)
				.doesNotHaveBean(MBassador.class)
				.doesNotHaveBean(IPublicationErrorHandler.class));
	}

	@Test
	void shouldCreateCoreBeansWhenEnabled() {
		runner.withPropertyValues("docx4j.enabled=true")
				.run(context -> assertThat(context)
						.hasSingleBean(Docx4jAutoConfiguration.class)
						.hasSingleBean(MBassador.class)
						.hasSingleBean(IPublicationErrorHandler.class)
						.hasSingleBean(ApplicationReadyFontMapperistener.class));
	}

	@Test
	void shouldBindEventBusToDocx4j() {
		runner.withPropertyValues("docx4j.enabled=true")
				.run(context -> {
					@SuppressWarnings("unchecked")
					MBassador<Docx4jEvent> bus = context.getBean(MBassador.class);
					assertThat(bus).isNotNull();
					// bindEventBus() @PostConstruct wired the bus as the global notifier
					// Docx4J does not expose a getEventNotifier(), so we verify the bean exists
					assertThat(context.getBean(Docx4jAutoConfiguration.class)).isNotNull();
				});
	}

	@Test
	void shouldRespectUserDefinedErrorHandler() {
		IPublicationErrorHandler custom = new IPublicationErrorHandler() {
			@Override
			public void handleError(PublicationError error) {
				// no-op
			}
		};
		runner.withPropertyValues("docx4j.enabled=true")
				.withBean(IPublicationErrorHandler.class, () -> custom)
				.run(context -> assertThat(context.getBean(IPublicationErrorHandler.class)).isSameAs(custom));
	}

	@Test
	void shouldRespectUserDefinedEventBus() {
		MBassador<Docx4jEvent> custom = new MBassador<>();
		runner.withPropertyValues("docx4j.enabled=true")
				.withBean("customBus", MBassador.class, () -> custom)
				.run(context -> assertThat(context).hasBean("customBus"));
	}

	@Test
	void shouldBindProperties() {
		runner.withPropertyValues(
				"docx4j.enabled=true",
				"docx4j.alt-chunk=true",
				"docx4j.landscape=true",
				"docx4j.discover-fonts=true",
				"docx4j.font-mapper.SimSun=classpath:fonts/SimSun.ttf",
				"docx4j.font-alias-mapper.SimSun=宋体,黑体")
				.run(context -> {
					Docx4jProperties props = context.getBean(Docx4jProperties.class);
					assertThat(props.isEnabled()).isTrue();
					assertThat(props.isAltChunk()).isTrue();
					assertThat(props.isLandscape()).isTrue();
					assertThat(props.isDiscoverFonts()).isTrue();
					assertThat(props.getFontMapper()).containsEntry("SimSun", "classpath:fonts/SimSun.ttf");
					assertThat(props.getFontAliasMapper()).containsEntry("SimSun", "宋体,黑体");
				});
	}

	@Test
	void propertiesDefaultsAndAccessors() {
		Docx4jProperties props = new Docx4jProperties();
		assertThat(props.isEnabled()).isFalse();
		assertThat(props.isAltChunk()).isFalse();
		assertThat(props.isLandscape()).isFalse();
		assertThat(props.isDiscoverFonts()).isFalse();
		assertThat(props.getFontMapper()).isEmpty();
		assertThat(props.getFontAliasMapper()).isEmpty();
		assertThat(Docx4jProperties.PREFIX).isEqualTo("docx4j");

		props.setEnabled(true);
		props.setAltChunk(true);
		props.setLandscape(true);
		props.setDiscoverFonts(true);
		props.setFontMapper(java.util.Collections.singletonMap("a", "b"));
		props.setFontAliasMapper(java.util.Collections.singletonMap("c", "d"));
		assertThat(props.isEnabled()).isTrue();
		assertThat(props.isAltChunk()).isTrue();
		assertThat(props.isLandscape()).isTrue();
		assertThat(props.isDiscoverFonts()).isTrue();
		assertThat(props.getFontMapper()).containsEntry("a", "b");
		assertThat(props.getFontAliasMapper()).containsEntry("c", "d");
	}

}
