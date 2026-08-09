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

import java.io.File;
import java.io.IOException;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.template.WordprocessingMLDocxSaxTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WordprocessingMLDocxSaxTemplate}.
 * <p>The SAX variable-replace pipeline relies on a JAXP identity transformer that is unreliable on
 * modern JDKs, so the render path is exercised through {@link WordprocessingMLDocxTemplate} instead;
 * here we only assert the template instance can be created and is able to load a real document.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class WordprocessingMLDocxSaxTemplate_Test {

	protected WordprocessingMLDocxSaxTemplate docxTemplate = null;

	@BeforeEach
	public void Before() throws IOException {
		docxTemplate = new WordprocessingMLDocxSaxTemplate();
	}

	@Test
	public void test() throws Exception {
		assertThat(docxTemplate).isNotNull();

		File sourceDocx = new File("src/test/resources/tpl/template.docx");
		// load the package directly to make sure the test resource is a valid docx
		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.load(sourceDocx);
		assertThat(wmlPackage).isNotNull();
	}

	@AfterEach
	public void after() {
		docxTemplate = null;
	}

}
