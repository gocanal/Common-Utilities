/**
 * 
 */
package clx.util.web;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author chulx
 *
 */
public enum ExtractWebDescription {
	INSTANCE;
	
	public String extractDescription (Document doc) {
		String description = null;
		if (doc != null) {
			// <meta name="description" content=""> is more reliable then Title
			Elements metas = doc.select("meta");
			if (metas != null) {
				for (Element meta : metas) {
					String name = meta.attr("name");
					if (name != null && "description".equalsIgnoreCase(name)) {
						description = meta.attr("content");
						break;
					}					
				}
			}
			
			if (description == null || description.isEmpty()) {	
				description = doc.select("title").first() == null ? null : doc.select("title").first().text();
			}
		}
		
		return description;
	}
}
