package org.docx4j.spring.boot.event.linstener;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.docx4j.fonts.BestMatchingMapper;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.spring.boot.Docx4jAutoConfiguration;
import org.docx4j.spring.boot.Docx4jProperties;
import org.docx4j.spring.boot.utils.StringUtils;
import org.docx4j.template.fonts.FontMapperHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;

/**
 * {@link ApplicationListener} that initialises the Docx4j font mapper once the application is ready.
 * <p>When font discovery is enabled it spawns a background thread that chooses an
 * {@link IdentityPlusMapper} on Windows (or systems with Microsoft fonts installed) or a
 * {@link BestMatchingMapper} (Panose-based) on other systems, then applies the default Chinese font
 * mappings and any user-supplied custom font/alias mappings.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class ApplicationReadyFontMapperistener
		implements ApplicationListener<ApplicationReadyEvent>, ResourceLoaderAware {

	protected static Logger LOG = LoggerFactory.getLogger(Docx4jAutoConfiguration.class);
	protected ResourceLoader resourceLoader;
	protected final Docx4jProperties docx4jProperties;

	/** Create a listener bound to the given Docx4j properties. @param docx4jProperties docx4j properties */
	public ApplicationReadyFontMapperistener(Docx4jProperties docx4jProperties) {
		this.docx4jProperties  = docx4jProperties;
	}

	/** When font discovery is enabled, asynchronously build and register the Docx4j font mapper. @param event the application ready event */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		if(docx4jProperties.isDiscoverFonts()) {
			// 使用独立线程初始化字体信息
			new Thread() {
				
				@Override
				public void run() {
					try {

						// Set up font mapper (optional)

						// example of mapping font Times New Roman which doesn't have certain Arabic
						// glyphs
						// eg Glyph "ي" (0x64a, afii57450) not available in font
						// "TimesNewRomanPS-ItalicMT".
						// eg Glyph "ج" (0x62c, afii57420) not available in font
						// "TimesNewRomanPS-ItalicMT".
						// to a font which does
						PhysicalFonts.get("Arial Unicode MS");

						final String osName = System.getProperty("os.name");
						if (osName.startsWith("Windows")) {
							/*
							 * 
							 * This mapper automatically maps document fonts for which the exact font is
							 * physically available. Think of this as an identity mapping. For this reason,
							 * it will work best on Windows, or a system on which Microsoft fonts have been
							 * installed. （此映射器自动映射确切可用的文档字体，将此视为标识映射；基于这个原因，它在Windows系统或安装了微软字体库的系统运行的更好。）
							 * You can manually add your own additional mappings if you wish.
							 * 如果需要，你可以手动添加自己的字体映射
							 * 
							 * 1、 自动检测系统上可用的字体 PhysicalFonts.discoverPhysicalFonts();
							 * 
							 */
							Mapper fontMapper = new IdentityPlusMapper();
							defaultFontMapper(fontMapper);
							customFontMapper(fontMapper, docx4jProperties.getFontMapper());
							FontMapperHolder.setFontMapper(fontMapper);
						} else {
							/*
							 * This mapper uses Panose to guess the physical font which is a closest fit for
							 * the font used in the document. （这个映射器使用Panose算法猜测最适合这个文档使用的物理字体。）
							 * 
							 * Panose是一种依照字体外观来进行分类的方法。我们可以通过PANOSE体系将字体的外观特征进行整理，并且与其它字体归类比较。
							 * Panose的原形在1985年由Benjamin
							 * Bauermeister开发，当时一种字体由7位16进制数字定义，现在则发展为10位，也就是字体的十种特征。这每一位数字都给出了它定义的一种视觉外观的量度
							 * ，如笔划的粗细或是字体衬线的样式等。 Panose定义的范围：Latin Text，Latin Script，Latin
							 * Decorative，Iconographic，Japanese Text，Cyrillic Text，Hebrew。
							 * 
							 * It is most likely to be suitable on Linux or OSX systems which don't have
							 * Microsoft's fonts installed. （它很可能适用于没有安装Microsoft字体的Linux或OSX系统。）
							 * 
							 * 1、获取Microsoft字体我们需要这些：a.在Microsoft平台上，嵌入PDF输出; b. docx4all - 所有平台 - 填充字体下拉列表
							 * setupMicrosoftFontFilenames(); 2、 自动检测系统上可用的字体
							 * PhysicalFonts.discoverPhysicalFonts();
							 * 
							 */
							Mapper fontMapper = new BestMatchingMapper();
							defaultFontMapper(fontMapper);
							customFontMapper(fontMapper, docx4jProperties.getFontMapper());
							FontMapperHolder.setFontMapper(fontMapper);
						}

					} catch (Exception e) {
					}
				};
			}.start();
		}
	}

	/** Apply the user-supplied font-name-to-location mappings (and their aliases) to the font mapper. @param fontMapper the font mapper to populate @param fontMap the font name to location mapping */
	protected void customFontMapper(Mapper fontMapper, Map<String, String> fontMap) {
		if (CollectionUtils.isEmpty(fontMap)) {
			return;
		}
		// 遍历自定义的字体库信息
		Iterator<Entry<String, String>> ite = fontMap.entrySet().iterator();
		while (ite.hasNext()) {

			Entry<String, String> entry = ite.next();

			try {
				// 获取字体库
				PhysicalFont physicalFont = PhysicalFonts.get(entry.getKey());
				if (physicalFont == null) {
					// 加载字体文件（解决linux环境下无中文字体问题）
					Resource resource = resourceLoader.getResource(entry.getValue());
					if (resource.exists()) {
						PhysicalFonts.addPhysicalFonts(entry.getKey(), resource.getURI());
					}
					LOG.debug("Add PhysicalFont " + entry.getKey());
				}

				// 设置字体名对应的字体库
				fontMapper.put(entry.getKey(), physicalFont);
				// 设置字体别名对应的字体库，支持多个别名
				String fontAliasArr = docx4jProperties.getFontAliasMapper().get(entry.getKey());
				if (StringUtils.hasText(fontAliasArr)) {
					for (String fontAlias : StringUtils.tokenizeToStringArray(fontAliasArr)) {
						fontMapper.put(fontAlias, physicalFont);
					}
				}

			} catch (Exception e) {
				LOG.debug("Add PhysicalFont Fail : Ingore");
			}

		}

	}

	/** Register the built-in mappings for common Chinese fonts to resolve Chinese character compatibility. @param fontMapper the font mapper to populate */
	protected void defaultFontMapper(Mapper fontMapper) {

		// 进行中文字体兼容处理
		fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
		fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
		fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
		fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
		fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
		fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
		fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
		fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
		fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
		fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
		fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
		fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
		fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
		fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
		
	}

	/** Inject the {@link ResourceLoader} used to resolve custom font file locations. @param resourceLoader the resource loader */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
