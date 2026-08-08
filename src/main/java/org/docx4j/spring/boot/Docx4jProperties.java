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

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Docx4j Spring Boot starter, bound to the {@code docx4j.*} prefix.
 * <p>Controls the starter enable flag, altChunk support, landscape orientation, font discovery and the
 * font-name-to-location and font-name-to-alias mappings used to resolve (and fix Chinese garbled) fonts.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(Docx4jProperties.PREFIX)
public class Docx4jProperties {

	public static final String PREFIX = "docx4j";

	/** Whether to enable Docx4j auto-configuration. */
	private boolean enabled = false;
	/** Whether to enable Docx4j altChunk support. */
	private boolean altChunk = false;
	private boolean landscape = false;
	/** Whether to enable Docx4j font discovery. */
	private boolean discoverFonts = false;

	/** Mapping of font name to physical font location used by the font mapper. */
	private Map<String /* Font Name */, String /* Location */> fontMapper = new LinkedHashMap<String, String>();
	/** Mapping of font name to one or more aliases; used to fix garbled Chinese characters. */
	private Map<String /* Font Name */, String /* Font Alias */> fontAliasMapper = new LinkedHashMap<String, String>();

	/** Return whether Docx4j auto-configuration is enabled. @return true if enabled */
	public boolean isEnabled() {
		return enabled;
	}

	/** Set whether Docx4j auto-configuration is enabled. @param enabled true to enable */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/** Return whether altChunk support is enabled. @return true if enabled */
	public boolean isAltChunk() {
		return altChunk;
	}

	/** Set whether altChunk support is enabled. @param altChunk true to enable */
	public void setAltChunk(boolean altChunk) {
		this.altChunk = altChunk;
	}

	/** Return whether landscape orientation is enabled. @return true if landscape */
	public boolean isLandscape() {
		return landscape;
	}

	/** Set whether landscape orientation is enabled. @param landscape true for landscape */
	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}

	/** Return whether font discovery is enabled. @return true if enabled */
	public boolean isDiscoverFonts() {
		return discoverFonts;
	}

	/** Set whether font discovery is enabled. @param discoverFonts true to enable */
	public void setDiscoverFonts(boolean discoverFonts) {
		this.discoverFonts = discoverFonts;
	}

	/** Return the font-name-to-location mapping. @return the font mapper map */
	public Map<String, String> getFontMapper() {
		return fontMapper;
	}

	/** Set the font-name-to-location mapping. @param fontMapper the font mapper map */
	public void setFontMapper(Map<String, String> fontMapper) {
		this.fontMapper = fontMapper;
	}

	/** Return the font-name-to-alias mapping. @return the font alias mapper map */
	public Map<String, String> getFontAliasMapper() {
		return fontAliasMapper;
	}

	/** Set the font-name-to-alias mapping. @param fontAliasMapper the font alias mapper map */
	public void setFontAliasMapper(Map<String, String> fontAliasMapper) {
		this.fontAliasMapper = fontAliasMapper;
	}

}
