package org.docx4j.spring.boot;


import org.docx4j.Docx4J;
import org.docx4j.events.Docx4jEvent;
import org.docx4j.spring.boot.event.linstener.ApplicationReadyFontMapperistener;
import org.docx4j.template.bus.error.Slf4jLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;

/**
 * Core Docx4j auto-configuration that wires the Docx4j event bus, registers an SLF4J publication
 * error handler and the font-mapper listener, activated when {@code docx4j.enabled=true}.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ Docx4J.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jProperties.class })
public class Docx4jAutoConfiguration {

	/** Provide a default SLF4J-backed {@link IPublicationErrorHandler} unless one already exists. @return an Slf4jLogger */
	@Bean
	@ConditionalOnMissingBean
	public IPublicationErrorHandler errorHandler() {
		return new Slf4jLogger();
	}

	/** Provide a default {@link MBassador} event bus for {@link Docx4jEvent} unless one already exists,
	 *  and bind it to {@link Docx4J} as its global event notifier. @param errorHandler publication error handler @return a new MBassador event bus */
	@Bean
	@ConditionalOnMissingBean
	public MBassador<Docx4jEvent> eventbus(IPublicationErrorHandler errorHandler) {
		MBassador<Docx4jEvent> bus = new MBassador<>(errorHandler);
		Docx4J.setEventNotifier(bus);
		return bus;
	}

	/** Register the {@link ApplicationReadyFontMapperistener} that initialises the Docx4j font mapper. @param docx4jProperties docx4j properties @return the font mapper listener */
	@Bean
	public ApplicationReadyFontMapperistener fontMapperistener(Docx4jProperties docx4jProperties) {
		return new ApplicationReadyFontMapperistener(docx4jProperties);
	}

}
