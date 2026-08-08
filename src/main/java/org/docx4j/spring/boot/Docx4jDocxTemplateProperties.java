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

import org.docx4j.template.Docx4jConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Docx template engine, bound to the {@code docx4j.template.docx.*} prefix.
 * <p>Configures placeholder delimiters, input/output character encoding and whether intermediate template
 * files are deleted automatically after rendering.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(Docx4jDocxTemplateProperties.PREFIX)
public class Docx4jDocxTemplateProperties {

	public static final String PREFIX = "docx4j.template.docx";

	/** Variable placeholder start token; default is {@code ${}. */
	private String placeholderStart = "${";
	/** Variable placeholder end token; default is {@code }}. */
	private String placeholderEnd = "}";
	/** Character encoding used when reading template documents; default is UTF-8. */
	private String inputEncoding = Docx4jConstants.DEFAULT_CHARSETNAME;
	/** Character encoding used for the rendered output document; default is UTF-8. */
	private String outputEncoding = Docx4jConstants.DEFAULT_CHARSETNAME;
	/** Whether to automatically delete intermediate template files. */
	private boolean autoDelete = false;

	/** Return the variable placeholder start token. @return the start token */
	public String getPlaceholderStart() {
		return placeholderStart;
	}

	/** Set the variable placeholder start token. @param placeholderStart the start token */
	public void setPlaceholderStart(String placeholderStart) {
		this.placeholderStart = placeholderStart;
	}

	/** Return the variable placeholder end token. @return the end token */
	public String getPlaceholderEnd() {
		return placeholderEnd;
	}

	/** Set the variable placeholder end token. @param placeholderEnd the end token */
	public void setPlaceholderEnd(String placeholderEnd) {
		this.placeholderEnd = placeholderEnd;
	}

	/** Return the template document input encoding. @return the input encoding */
	public String getInputEncoding() {
		return inputEncoding;
	}

	/** Set the template document input encoding. @param inputEncoding the input encoding */
	public void setInputEncoding(String inputEncoding) {
		this.inputEncoding = inputEncoding;
	}

	/** Return the rendered document output encoding. @return the output encoding */
	public String getOutputEncoding() {
		return outputEncoding;
	}

	/** Set the rendered document output encoding. @param outputEncoding the output encoding */
	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}

	/** Return whether intermediate template files are deleted automatically. @return true if auto-deleted */
	public boolean isAutoDelete() {
		return autoDelete;
	}

	/** Set whether intermediate template files are deleted automatically. @param autoDelete true to auto-delete */
	public void setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

}
