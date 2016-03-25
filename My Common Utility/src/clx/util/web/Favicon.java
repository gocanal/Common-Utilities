/**
 * 
 */
package clx.util.web;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author chulx
 *
 */
public enum Favicon {
	INSTANCE;
	
	public String getFaviconUrl (String url) {
		String furl = null;
		Document doc = Downloader.INSTANCE.getDocumentFromUrl(url, 0);
		if (doc != null) {
			Element element = doc.head().select("link[rel*=icon]").first();
			if (element != null) {
				furl = element.attr("abs:href");
			} else {
				element = doc.head().select("meta[itemprop=image]").first();
				if (element != null) {
					furl = element.attr("abs:content");
				}
			} 
		}
		
		return furl;
	}

}
