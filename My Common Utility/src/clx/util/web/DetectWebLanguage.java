/**
 * 
 */
package clx.util.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import clx.util.string.CharsetDetector;

/**
 * @author chulx
 * 
 * Several ways:
 * . check <html lang="">
 *    example: en and en_US for English, ru for Russia. 
 *    But not all sites use this (Baidu.com does not use this)
 * . check <meta name="description" content="日本最大級...">
 * . check <title>
 *   Seems that almost all have it. But it is possible that it is English but the page is in different language
 * . check url country code if there is one
 *   But there are English sites in China for example...
 * . check body content
 *   this is likely the most accurate
 *
 * We are likely interested only in:
 *   zh, zh-Hans, zh-Hant, en, en-GB, en-US, en-au
 *   
 *   
 * https://en.wikipedia.org/wiki/Language_identification has documented libraries and web services
 */
public enum DetectWebLanguage {
	INSTANCE;
	
	

	public String detectWebLanguage (String url) {
		String lang = null;
		Document doc = Downloader.INSTANCE.getDocumentFromUrl(url, 0);
		if (doc != null) {
			lang = doc.select("html").first() == null ? null : doc.select("html").first().attr("lang");
			if (lang != null && !lang.isEmpty()) {
				return lang;
			}
			
			// <meta name="description" content=""> is more reliable then Title
			Elements metas = doc.select("meta");
			if (metas != null) {
				for (Element meta : metas) {
					String name = meta.attr("name");
					if (name != null && "description".equalsIgnoreCase(name)) {
						String content = meta.attr("content");
						if (content != null && !content.isEmpty())
							return CharsetDetector.getInstance().detect(content);
					}
				}
			}
			
			lang = doc.select("title").first() == null ? null : doc.select("title").first().text();
			if (lang != null && !lang.isEmpty())
				lang = CharsetDetector.getInstance().detect(lang);
			
			if (lang == null || lang.isEmpty())
				lang = CharsetDetector.getInstance().detect(doc.select("body").first().text());
		}
		
		return lang;
	}
	
	/**
	 * 
	 * @param url
	 * @return [language, description]
	 */
	public String [] getLanguageAndDescription (String url) {
		String [] langDes = new String [2];
		
		String lang = null;
		String description = null;
		
		Document doc = Downloader.INSTANCE.getDocumentFromUrl(url, 0);
		if (doc != null) {
			lang = doc.select("html").first() == null ? null : doc.select("html").first().attr("lang");
			
			// <meta name="description" content=""> is more reliable then Title
			Elements metas = doc.select("meta");
			if (metas != null) {
				for (Element meta : metas) {
					String name = meta.attr("name");
					if (name != null && "description".equalsIgnoreCase(name)) {
						description = meta.attr("content");
					}					
				}
			}
			
			if (description == null || description.isEmpty()) {	
				description = doc.select("title").first() == null ? null : doc.select("title").first().text();
			}
			
			if (lang == null || lang.isEmpty()) {
				if (description != null && !description.isEmpty()) {			
					//lang = CharsetDetector.getInstance().detect(description);
					lang = detectLanguageWithLangDetectorSite(description);
				} else {
					//lang = CharsetDetector.getInstance().detect(doc.select("body").first().text());
					lang = detectLanguageWithLangDetectorSite (doc.select("body").first().text());
				}
			}
		}
		
		langDes[0] = lang;
		langDes[1] = description;
		
		return langDes;
	}
	
	/**
	 * using http://www.en.langdetector.com/getlangs.php
	 * 
	 * but with around 50 accesses within 10 min ?
	 * 
	 * POST
	 *   input=1
	 *   contents=
	 *   language=eng
	 * 
	 * RETURN
	 *   <script type="text/javascript" language="Javascript">
		 parent.document.getElementById("loading").style.display="block";</script><span style="font-family: arial, helvetica, verdana; color: green; font-size: 9pt; font-weight: bolder;">Chinese<br />Response time: 0.66 seconds<br /></span><script type="text/javascript" language="Javascript">
		 parent.document.getElementById("loading").style.display="none";</script>
		 
	 * @param s
	 * @return
	 */
	public String detectLanguageWithLangDetectorSite (String s) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("input", "1");
		try {
			data.put("contents", URLEncoder.encode(s, "UTF-8"));
			data.put("language", "eng");
			
			String body = Downloader.INSTANCE.post("http://www.en.langdetector.com/getlangs.php", data, 0);
			if (body != null) {
				String [] split = body.split("<br");
				int i = split[0].lastIndexOf(">");
				if (i > 0)
					return split[0].substring(i+1);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}
