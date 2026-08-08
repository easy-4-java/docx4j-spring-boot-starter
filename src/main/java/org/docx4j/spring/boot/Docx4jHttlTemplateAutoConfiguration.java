package org.docx4j.spring.boot;

import org.docx4j.Docx4J;
import org.docx4j.template.httl.WordprocessingMLHttlTemplate;
import org.docx4j.template.xhtml.WordprocessingMLHtmlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import httl.Engine;

/**
 * Auto-configuration for the HTTL-based Docx template engine, activated when {@code docx4j.enabled=true}
 * and the HTTL {@link Engine} and {@link WordprocessingMLHttlTemplate} classes are present.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Docx4jXhtmlTemplateAutoConfiguration.class)
@ConditionalOnClass({ Docx4J.class, Engine.class , WordprocessingMLHttlTemplate.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jProperties.class, Docx4jHttlTemplateProperties.class })
public class Docx4jHttlTemplateAutoConfiguration {

	/** Create the {@link WordprocessingMLHttlTemplate} bean backed by the optional HTTL {@link Engine}. @param docx4jProperties docx4j properties @param templateProperties httl template properties @param wmlHtmlTemplate shared XHTML template @param engine optional HTTL engine @return a configured HTTL Docx template */
	@Bean
	public WordprocessingMLHttlTemplate wmlHttlTemplate(
			Docx4jProperties docx4jProperties,
			Docx4jHttlTemplateProperties templateProperties,
			WordprocessingMLHtmlTemplate wmlHtmlTemplate,
			@Autowired(required = false) Engine engine) {
		WordprocessingMLHttlTemplate template = new WordprocessingMLHttlTemplate(wmlHtmlTemplate);
		template.setEngine(engine);
		return template;
	}

}
