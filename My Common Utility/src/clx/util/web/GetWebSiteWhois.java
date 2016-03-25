/**
 * 
 */
package clx.util.web;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author chulx
 * 
 * not all can be retrieved, for example, no info on yahoo.com.jp, yandex.ru
 *
 */
public enum GetWebSiteWhois {
	INSTANCE;
	
	private static final String BASE_URL = "http://www.whois.com/whois/";
	
	public String getCountry (String url) {
		String country = null;
		
		Document doc = Downloader.INSTANCE.getDocumentFromUrl(BASE_URL + (url.contains("//") ? url.substring(url.indexOf("//") + "//".length()) : url), 0);
		if (doc != null) {
			Element div = doc.select("div#registrarData").first();
			if (div != null) {				
				String registra = div.text();
				if (registra != null && !registra.isEmpty()) {
					int index = registra.indexOf("Registrant Country:");
					if (index > 0) {
						registra = registra.substring(registra.indexOf("Registrant Country:") + "Registrant Country:".length());
						String [] segs = registra.split(" ");
						return segs[1];
					}
				}
			}
		}
		
		return country;
	}

}
