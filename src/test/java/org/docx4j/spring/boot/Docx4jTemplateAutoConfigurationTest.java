package org.docx4j.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.docx4j.template.beetl.WordprocessingMLBeetlTemplate;
import org.docx4j.template.freemarker.WordprocessingMLFreemarkerTemplate;
import org.docx4j.template.httl.WordprocessingMLHttlTemplate;
import org.docx4j.template.jetbrick.WordprocessingMLJetbrickTemplate;
import org.docx4j.template.rythm.WordprocessingMLRythmTemplate;
import org.docx4j.template.thymeleaf.WordprocessingMLThymeleafTemplate;
import org.docx4j.template.velocity.WordprocessingMLVelocityTemplate;
import org.docx4j.template.webit.WordprocessingMLWebitTemplate;
import org.docx4j.template.xhtml.WordprocessingMLHtmlTemplate;
import org.docx4j.template.xhtml.handler.DocumentHandler;
import org.docx4j.template.xhtml.io.WordprocessingMLPackageBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests for the template-engine auto-configurations (xhtml + Beetl/Freemarker/HTTL/Jetbrick/Rythm/
 * Thymeleaf/Velocity/Webit) and the {@code *TemplateProperties} holders.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class Docx4jTemplateAutoConfigurationTest {

	private final ApplicationContextRunner runner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(
					Docx4jDocxTemplateAutoConfiguration.class,
					Docx4jXhtmlTemplateAutoConfiguration.class,
					Docx4jBeetlTemplateAutoConfiguration.class,
					Docx4jFreemarkerTemplateAutoConfiguration.class,
					Docx4jHttlTemplateAutoConfiguration.class,
					Docx4jJetbrickTemplateAutoConfiguration.class,
					Docx4jRythmTemplateAutoConfiguration.class,
					Docx4jThymeleafTemplateAutoConfiguration.class,
					Docx4jVelocityTemplateAutoConfiguration.class,
					Docx4jWebitTemplateAutoConfiguration.class));

	@Test
	void shouldNotCreateBeansWhenDisabled() {
		runner.run(context -> assertThat(context)
				.doesNotHaveBean(WordprocessingMLHtmlTemplate.class)
				.doesNotHaveBean(WordprocessingMLBeetlTemplate.class));
	}

	@Test
	void shouldCreateAllTemplateBeansWhenEnabled() {
		runner.withPropertyValues("docx4j.enabled=true")
				.run(context -> assertThat(context)
						.hasSingleBean(DocumentHandler.class)
						.hasSingleBean(WordprocessingMLPackageBuilder.class)
						.hasSingleBean(WordprocessingMLHtmlTemplate.class)
						.hasSingleBean(WordprocessingMLBeetlTemplate.class)
						.hasSingleBean(WordprocessingMLFreemarkerTemplate.class)
						.hasSingleBean(WordprocessingMLHttlTemplate.class)
						.hasSingleBean(WordprocessingMLJetbrickTemplate.class)
						.hasSingleBean(WordprocessingMLRythmTemplate.class)
						.hasSingleBean(WordprocessingMLThymeleafTemplate.class)
						.hasSingleBean(WordprocessingMLVelocityTemplate.class)
						.hasSingleBean(WordprocessingMLWebitTemplate.class));
	}

	@Test
	void htmlTemplateShouldReflectAltChunkAndLandscape() {
		runner.withPropertyValues("docx4j.enabled=true", "docx4j.alt-chunk=true", "docx4j.landscape=true")
				.run(context -> assertThat(context).hasSingleBean(WordprocessingMLHtmlTemplate.class));
	}

	@Test
	void allTemplatePropertiesPrefixes() {
		assertThat(Docx4jBeetlTemplateProperties.PREFIX).isEqualTo("docx4j.template.beetl");
		assertThat(Docx4jFreemarkerTemplateProperties.PREFIX).isEqualTo("docx4j.template.freemarker");
		assertThat(Docx4jHttlTemplateProperties.PREFIX).isEqualTo("docx4j.template.httl");
		assertThat(Docx4jJetbrickTemplateProperties.PREFIX).isEqualTo("docx4j.template.jetbrick");
		assertThat(Docx4jRythmTemplateProperties.PREFIX).isEqualTo("docx4j.template.rythm");
		assertThat(Docx4jThymeleafTemplateProperties.PREFIX).isEqualTo("docx4j.template.thymeleaf");
		assertThat(Docx4jVelocityTemplateProperties.PREFIX).isEqualTo("docx4j.template.velocity");
		assertThat(Docx4jWebitTemplateProperties.PREFIX).isEqualTo("docx4j.template.webit");
		assertThat(Docx4jXhtmlTemplateProperties.PREFIX).isEqualTo("docx4j.template.xhtml");
	}

	@Test
	void shouldInstantiatePropertiesHolders() {
		assertThat(new Docx4jBeetlTemplateProperties()).isNotNull();
		assertThat(new Docx4jFreemarkerTemplateProperties()).isNotNull();
		assertThat(new Docx4jHttlTemplateProperties()).isNotNull();
		assertThat(new Docx4jJetbrickTemplateProperties()).isNotNull();
		assertThat(new Docx4jRythmTemplateProperties()).isNotNull();
		assertThat(new Docx4jThymeleafTemplateProperties()).isNotNull();
		assertThat(new Docx4jVelocityTemplateProperties()).isNotNull();
		assertThat(new Docx4jWebitTemplateProperties()).isNotNull();
		assertThat(new Docx4jXhtmlTemplateProperties()).isNotNull();
	}

}
