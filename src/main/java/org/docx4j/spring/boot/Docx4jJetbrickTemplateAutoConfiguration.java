package org.docx4j.spring.boot;

import org.docx4j.Docx4J;
import org.docx4j.template.jetbrick.WordprocessingMLJetbrickTemplate;
import org.docx4j.template.xhtml.WordprocessingMLHtmlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jetbrick.template.JetEngine;

/**
 * Auto-configuration for the Jetbrick-based Docx template engine, activated when
 * {@code docx4j.enabled=true} and the Jetbrick {@link JetEngine} and
 * {@link WordprocessingMLJetbrickTemplate} classes are present.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Docx4jXhtmlTemplateAutoConfiguration.class)
@ConditionalOnClass({ Docx4J.class, JetEngine.class , WordprocessingMLJetbrickTemplate.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jProperties.class, Docx4jJetbrickTemplateProperties.class })
public class Docx4jJetbrickTemplateAutoConfiguration {

	/** Create the {@link WordprocessingMLJetbrickTemplate} bean backed by the optional Jetbrick {@link JetEngine}. @param docx4jProperties docx4j properties @param templateProperties jetbrick template properties @param wmlHtmlTemplate shared XHTML template @param engine optional Jetbrick engine @return a configured Jetbrick Docx template */
	@Bean
	public WordprocessingMLJetbrickTemplate wmlJetbrickTemplate(
			Docx4jProperties docx4jProperties,
			Docx4jJetbrickTemplateProperties templateProperties,
			WordprocessingMLHtmlTemplate wmlHtmlTemplate,
			@Autowired(required = false) JetEngine engine) {
		WordprocessingMLJetbrickTemplate template = new WordprocessingMLJetbrickTemplate(wmlHtmlTemplate);
		template.setEngine(engine);
		return template;
	}

}
