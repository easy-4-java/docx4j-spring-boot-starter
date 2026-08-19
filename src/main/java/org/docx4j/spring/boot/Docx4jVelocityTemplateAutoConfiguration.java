package org.docx4j.spring.boot;

import org.apache.velocity.app.VelocityEngine;
import org.docx4j.Docx4J;
import org.docx4j.template.velocity.WordprocessingMLVelocityTemplate;
import org.docx4j.template.xhtml.WordprocessingMLHtmlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

/**
 * Auto-configuration for the Velocity-based Docx template engine, activated when
 * {@code docx4j.enabled=true} and the {@link VelocityEngine} and
 * {@link WordprocessingMLVelocityTemplate} classes are present.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Docx4jXhtmlTemplateAutoConfiguration.class)
@ConditionalOnClass({ Docx4J.class, TemplateEngine.class , WordprocessingMLVelocityTemplate.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jProperties.class, Docx4jVelocityTemplateProperties.class })
/**
 * <p>Auto-configuration for Docx4jVelocityTemplateAutoConfiguration.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class Docx4jVelocityTemplateAutoConfiguration {

	/** Create the {@link WordprocessingMLVelocityTemplate} bean backed by the optional {@link VelocityEngine}. @param docx4jProperties docx4j properties @param templateProperties velocity template properties @param wmlHtmlTemplate shared XHTML template @param engine optional Velocity engine @return a configured Velocity Docx template */
	@Bean
	public WordprocessingMLVelocityTemplate wmlVelocityTemplate(
			Docx4jProperties docx4jProperties,
			Docx4jVelocityTemplateProperties templateProperties,
			WordprocessingMLHtmlTemplate wmlHtmlTemplate,
			@Autowired(required = false) VelocityEngine engine) {
		WordprocessingMLVelocityTemplate template = new WordprocessingMLVelocityTemplate(wmlHtmlTemplate);
		template.setEngine(engine);
		return template;
	}

}
