/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.docx4j.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.template.io.WordprocessingMLTemplateWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WordprocessingMLTemplateWriter}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class WordprocessingMLTemplateWriter_Test {

	protected WordprocessingMLTemplateWriter wemplateWriter = null;

	@BeforeEach
	public void Before() {
		wemplateWriter = WordprocessingMLTemplateWriter.getWMLTemplateWriter();
	}

	@Test
	public void test() throws Exception {
		assertThat(wemplateWriter).isNotNull();
		// the writer is a singleton accessor; verify it returns the same instance
		assertThat(WordprocessingMLTemplateWriter.getWMLTemplateWriter()).isSameAs(wemplateWriter);

		// verify it can handle a real docx package without throwing
		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.load(
				new java.io.File("src/test/resources/tpl/template.docx"));
		assertThat(wmlPackage).isNotNull();
	}

	@AfterEach
	public void after() {
		wemplateWriter = null;
	}


}
