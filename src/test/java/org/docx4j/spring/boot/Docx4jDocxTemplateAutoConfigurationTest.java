package org.docx4j.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for {@link Docx4jDocxTemplateAutoConfiguration} and {@link Docx4jDocxTemplateProperties}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class Docx4jDocxTemplateAutoConfigurationTest {

	private final ApplicationContextRunner runner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(Docx4jDocxTemplateAutoConfiguration.class));

	@Test
	void shouldNotCreateBeansWhenDisabled() {
		runner.run(context -> assertThat(context)
				.doesNotHaveBean(WordprocessingMLDocxTemplate.class)
				.doesNotHaveBean(WordprocessingMLPackageWriter.class));
	}

	@Test
	void shouldCreateDocxTemplateBeansWhenEnabled() {
		runner.withPropertyValues("docx4j.enabled=true")
				.run(context -> assertThat(context)
						.hasSingleBean(WordprocessingMLDocxTemplate.class)
						.hasSingleBean(WordprocessingMLDocxSaxTemplate.class)
						.hasSingleBean(WordprocessingMLDocxStAXTemplate.class)
						.hasSingleBean(ConversionHyperlinkHandler.class)
						.hasSingleBean(ConversionHTMLStyleElementHandler.class)
						.hasSingleBean(ConversionHTMLScriptElementHandler.class)
						.hasSingleBean(WordprocessingMLPackageExtractor.class)
						.hasSingleBean(WordprocessingMLPackageWriter.class)
						.hasSingleBean(WordprocessingMLTemplateWriter.class));
	}

	@Test
	void packageWriterShouldBeWiredWithHandlers() {
		runner.withPropertyValues("docx4j.enabled=true")
				.run(context -> {
					WordprocessingMLPackageWriter writer = context.getBean(WordprocessingMLPackageWriter.class);
					assertThat(writer.getHyperlinkHandler())
							.isSameAs(context.getBean(OutputConversionHyperlinkHandler.getHyperlinkHandler().getClass()));
					assertThat(writer.getScriptElementHandler()).isNotNull();
					assertThat(writer.getStyleElementHandler()).isNotNull();
				});
	}

	@Test
	void docxTemplatePropertiesDefaultsAndAccessors() {
		Docx4jDocxTemplateProperties props = new Docx4jDocxTemplateProperties();
		assertThat(Docx4jDocxTemplateProperties.PREFIX).isEqualTo("docx4j.template.docx");
		assertThat(props.getPlaceholderStart()).isEqualTo("${");
		assertThat(props.getPlaceholderEnd()).isEqualTo("}");
		assertThat(props.getInputEncoding()).isEqualTo(props.getOutputEncoding());
		assertThat(props.isAutoDelete()).isFalse();

		props.setPlaceholderStart("[[");
		props.setPlaceholderEnd("]]");
		props.setInputEncoding("GBK");
		props.setOutputEncoding("ISO-8859-1");
		props.setAutoDelete(true);
		assertThat(props.getPlaceholderStart()).isEqualTo("[[");
		assertThat(props.getPlaceholderEnd()).isEqualTo("]]");
		assertThat(props.getInputEncoding()).isEqualTo("GBK");
		assertThat(props.getOutputEncoding()).isEqualTo("ISO-8859-1");
		assertThat(props.isAutoDelete()).isTrue();
	}

}
