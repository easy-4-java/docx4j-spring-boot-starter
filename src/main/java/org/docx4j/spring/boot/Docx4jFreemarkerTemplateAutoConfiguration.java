package org.docx4j.spring.boot;

import org.docx4j.Docx4J;
import org.docx4j.template.freemarker.WordprocessingMLFreemarkerTemplate;
import org.docx4j.template.xhtml.WordprocessingMLHtmlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the Freemarker-based Docx template engine, activated when
 * {@code docx4j.enabled=true} and Freemarker's {@link freemarker.template.Configuration} and
 * {@link WordprocessingMLFreemarkerTemplate} classes are present.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Docx4jXhtmlTemplateAutoConfiguration.class)
@ConditionalOnClass({ Docx4J.class, freemarker.template.Configuration.class , WordprocessingMLFreemarkerTemplate.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jProperties.class, Docx4jFreemarkerTemplateProperties.class })
/**
 * <p>Auto-configuration for Docx4jFreemarkerTemplateAutoConfiguration.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class Docx4jFreemarkerTemplateAutoConfiguration {

	/** Create the {@link WordprocessingMLFreemarkerTemplate} bean backed by the optional Freemarker configuration. @param docx4jProperties docx4j properties @param templateProperties freemarker template properties @param wmlHtmlTemplate shared XHTML template @param configuration optional Freemarker configuration @return a configured Freemarker Docx template */
	@Bean
	public WordprocessingMLFreemarkerTemplate wmlFreemarkerTemplate(
			Docx4jProperties docx4jProperties,
			Docx4jFreemarkerTemplateProperties templateProperties,
			WordprocessingMLHtmlTemplate wmlHtmlTemplate,
			@Autowired(required = false) freemarker.template.Configuration configuration) {
		WordprocessingMLFreemarkerTemplate template = new WordprocessingMLFreemarkerTemplate(wmlHtmlTemplate);
		template.setEngine(configuration);
		return template;
	}

}
