package org.docx4j.spring.boot;

import org.docx4j.Docx4J;
import org.docx4j.template.xhtml.WordprocessingMLHtmlTemplate;
import org.docx4j.template.xhtml.handler.DocumentHandler;
import org.docx4j.template.xhtml.handler.def.XHTMLDocumentHandler;
import org.docx4j.template.xhtml.io.WordprocessingMLPackageBuilder;
import org.jsoup.Jsoup;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the XHTML-to-Docx template engine, exposing the document handler, package
 * builder and {@link WordprocessingMLHtmlTemplate} bean, activated when {@code docx4j.enabled=true}
 * and Jsoup is on the classpath.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Docx4jDocxTemplateAutoConfiguration.class)
@ConditionalOnClass({ Docx4J.class, Jsoup.class, WordprocessingMLHtmlTemplate.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jProperties.class, Docx4jXhtmlTemplateProperties.class })
/**
 * <p>Auto-configuration for Docx4jXhtmlTemplateAutoConfiguration.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class Docx4jXhtmlTemplateAutoConfiguration {

	/** Provide the default {@link DocumentHandler} unless one already exists. @return the XHTML document handler */
	@Bean
	@ConditionalOnMissingBean
	/**
	 * <p>Document handler.</p>
	 * @return the result
	 */
	public DocumentHandler documentHandler() {
		return XHTMLDocumentHandler.getDocumentHandler();
	}

	/** Provide the default {@link WordprocessingMLPackageBuilder} unless one already exists. @return the package builder */
	@Bean
	@ConditionalOnMissingBean
	/**
	 * <p>Word m l package builder.</p>
	 * @return the result
	 */
	public WordprocessingMLPackageBuilder wordMLPackageBuilder() {
		return WordprocessingMLPackageBuilder.getWMLPackageBuilder();
	}

	/** Create the {@link WordprocessingMLHtmlTemplate} bean using the landscape and altChunk flags from properties. @param docx4jProperties docx4j properties @param templateProperties xhtml template properties @param documentHandler document handler @param wordMLPackageBuilder package builder @return a configured XHTML Docx template */
	@Bean
	public WordprocessingMLHtmlTemplate wmlHtmlTemplate(Docx4jProperties docx4jProperties,
			Docx4jXhtmlTemplateProperties templateProperties, DocumentHandler documentHandler,
			WordprocessingMLPackageBuilder wordMLPackageBuilder) {
		WordprocessingMLHtmlTemplate template = new WordprocessingMLHtmlTemplate(docx4jProperties.isLandscape(),
				docx4jProperties.isAltChunk());
		template.setDocHandler(documentHandler);
		template.setWordMLPackageBuilder(wordMLPackageBuilder);
		return template;
	}

}
