/**
 * 
 */
package clx.util.web;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author chulx
 *
 */
public enum DetectSocialLinks {
	INSTANCE;
	
	public Set<String> detectTwitter (Document doc) {
		Set<String> links = new HashSet<String>();

		Elements urls = doc.select("a");
		for (Element link : urls) {
			String href = link.attr("href");
			String target = link.attr("target");
			if (href != null && target.contains("blank")) {
				if (href.contains("twitter.com"))
					links.add(href);
			}
		}
		// for twitter special case
		Elements metas = doc.select("meta");
		if (metas != null) {
			for (Element meta : metas) {
				String name = meta.attr("name");
				String content = meta.attr("content");
				if (name != null && name.equalsIgnoreCase("twitter:site") && content != null && !content.isEmpty()) {
					links.add(content);
					break;
				}
			}
		}

		return links;
	}

	public Set<String> detectGooglePlus (Document doc) {
		Set<String> links = new HashSet<String>();

		Elements urls = doc.select("a");
		for (Element link : urls) {
			String href = link.attr("href");
			String target = link.attr("target");
			if (href != null && target.contains("blank")) {
				if (href.contains("plus.google.com"))
					links.add(href);
			}
		}

		return links;
	}
	

	public Set<String> detectFacebook (Document doc) {
		Set<String> links = new HashSet<String>();

		Elements urls = doc.select("a");
		for (Element link : urls) {
			String href = link.attr("href");
			String target = link.attr("target");
			if (href != null && target.contains("blank")) {
				if (href.contains("www.facebook.com"))
					links.add(href);
			}
		}

		return links;
	}

	public Set<String> detectTumblr (Document doc) {
		Set<String> links = new HashSet<String>();

		Elements urls = doc.select("a");
		for (Element link : urls) {
			String href = link.attr("href");
			String target = link.attr("target");
			if (href != null && target.contains("blank")) {
				if (href.contains("tumblr.com"))
					links.add(href);
			}
		}

		return links;
	}


}
