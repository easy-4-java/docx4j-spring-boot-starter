package org.docx4j.spring.boot;

import java.io.IOException;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.ConversionHTMLScriptElementHandler;
import org.docx4j.convert.out.ConversionHTMLStyleElementHandler;
import org.docx4j.convert.out.ConversionHyperlinkHandler;
import org.docx4j.template.WordprocessingMLDocxSaxTemplate;
import org.docx4j.template.WordprocessingMLDocxStAXTemplate;
import org.docx4j.template.WordprocessingMLDocxTemplate;
import org.docx4j.template.handler.OutputConversionHTMLScriptElementHandler;
import org.docx4j.template.handler.OutputConversionHTMLStyleElementHandler;
import org.docx4j.template.handler.OutputConversionHyperlinkHandler;
import org.docx4j.template.io.WordprocessingMLPackageExtractor;
import org.docx4j.template.io.WordprocessingMLPackageWriter;
import org.docx4j.template.io.WordprocessingMLTemplateWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the Docx template engines (default, SAX and StAX) and the shared conversion
 * handlers, package extractor/writer and template writer, activated when {@code docx4j.enabled=true}.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Docx4jAutoConfiguration.class)
@ConditionalOnClass({ Docx4J.class, WordprocessingMLDocxTemplate.class })
@ConditionalOnProperty(prefix = Docx4jProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ Docx4jDocxTemplateProperties.class })
public class Docx4jDocxTemplateAutoConfiguration {

	protected static Logger LOG = LoggerFactory.getLogger(Docx4jDocxTemplateAutoConfiguration.class);

	/** Create the default {@link WordprocessingMLDocxTemplate} bean. @return a new Docx template @throws IOException if initialisation fails */
	@Bean
	public WordprocessingMLDocxTemplate wmlDocxTemplate()
			throws IOException {
		WordprocessingMLDocxTemplate template = new WordprocessingMLDocxTemplate();
		return template;
	}

	/** Create the SAX-based {@link WordprocessingMLDocxSaxTemplate} bean. @return a new SAX Docx template @throws IOException if initialisation fails */
	@Bean
	public WordprocessingMLDocxSaxTemplate wmlDocxSaxTemplate()
			throws IOException {
		WordprocessingMLDocxSaxTemplate template = new WordprocessingMLDocxSaxTemplate();
		return template;
	}

	/** Create the StAX-based {@link WordprocessingMLDocxStAXTemplate} bean. @return a new StAX Docx template @throws IOException if initialisation fails */
	@Bean
	public WordprocessingMLDocxStAXTemplate wmlDocxStAXTemplate()
			throws IOException {
		WordprocessingMLDocxStAXTemplate template = new WordprocessingMLDocxStAXTemplate();
		return template;
	}

	/** Provide the default {@link ConversionHyperlinkHandler} unless one already exists. @return the hyperlink handler */
	@Bean
	@ConditionalOnMissingBean
	public ConversionHyperlinkHandler hyperlinkHandler() {
		return OutputConversionHyperlinkHandler.getHyperlinkHandler();
	}

	/** Provide the default {@link ConversionHTMLStyleElementHandler} unless one already exists. @return the style element handler */
	@Bean
	@ConditionalOnMissingBean
	public ConversionHTMLStyleElementHandler styleElementHandler() {
		return OutputConversionHTMLStyleElementHandler.getStyleElementHandler();
	}

	/** Provide the default {@link ConversionHTMLScriptElementHandler} unless one already exists. @return the script element handler */
	@Bean
	@ConditionalOnMissingBean
	public ConversionHTMLScriptElementHandler scriptElementHandler() {
		return OutputConversionHTMLScriptElementHandler.getScriptElementHandler();
	}

	/** Provide the default {@link WordprocessingMLPackageExtractor} unless one already exists. @return the package extractor */
	@Bean
	@ConditionalOnMissingBean
	public WordprocessingMLPackageExtractor wmlPackageExtractor() {
		return WordprocessingMLPackageExtractor.getWMLPackageExtractor();
	}

	/** Provide the default {@link WordprocessingMLPackageWriter} wired with the conversion handlers unless one already exists. @param hyperlinkHandler hyperlink handler @param scriptElementHandler script element handler @param styleElementHandler style element handler @return the configured package writer */
	@Bean
	@ConditionalOnMissingBean
	public WordprocessingMLPackageWriter wmlPackageWriter(ConversionHyperlinkHandler hyperlinkHandler,
			ConversionHTMLScriptElementHandler scriptElementHandler,
			ConversionHTMLStyleElementHandler styleElementHandler) {
		WordprocessingMLPackageWriter wmlPackageWriter = WordprocessingMLPackageWriter.getWMLPackageWriter();
		wmlPackageWriter.setHyperlinkHandler(hyperlinkHandler);
		wmlPackageWriter.setScriptElementHandler(scriptElementHandler);
		wmlPackageWriter.setStyleElementHandler(styleElementHandler);
		return wmlPackageWriter;
	}

	/** Provide the default {@link WordprocessingMLTemplateWriter} unless one already exists. @return the template writer */
	@Bean
	@ConditionalOnMissingBean
	public WordprocessingMLTemplateWriter wmlTemplateWriter() {
		return WordprocessingMLTemplateWriter.getWMLTemplateWriter();
	}

}
