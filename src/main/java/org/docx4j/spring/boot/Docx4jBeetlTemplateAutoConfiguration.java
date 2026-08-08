package org.docx4j.spring.boot;

import org.beetl.core.GroupTemplate;
import org.docx4j.Docx4J;
import org.docx4j.template.beetl.WordprocessingMLBeetlTemplate;
import org.docx4j.template.xhtml.WordprocessingMLHtmlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the Beetl-based Docx template engine, activated when {@code docx4j.enabled=true}
 * and the Beetl {@link GroupTemplate} and {@link WordprocessingMLBeetlTemplate} classes are present.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Docx4jXhtmlTemplateAutoConfiguration.class)
@ConditionalOnClass({ Docx4J.class, GroupTemplate.class , WordprocessingMLBeetlTemplate.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jProperties.class, Docx4jBeetlTemplateProperties.class })
public class Docx4jBeetlTemplateAutoConfiguration {

	/** Create the {@link WordprocessingMLBeetlTemplate} bean backed by the optional Beetl {@link GroupTemplate}. @param docx4jProperties docx4j properties @param templateProperties beetl template properties @param wmlHtmlTemplate shared XHTML template @param groupTemplate optional Beetl group template @return a configured Beetl Docx template */
	@Bean
	public WordprocessingMLBeetlTemplate wmlBeetlTemplate(
			Docx4jProperties docx4jProperties,
			Docx4jBeetlTemplateProperties templateProperties,
			WordprocessingMLHtmlTemplate wmlHtmlTemplate,
			@Autowired(required = false) GroupTemplate groupTemplate) {
		WordprocessingMLBeetlTemplate template = new WordprocessingMLBeetlTemplate(wmlHtmlTemplate);
		template.setEngine(groupTemplate);
		return template;
	}

}
