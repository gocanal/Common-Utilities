/**
 * 
 */
package clx.util.string;

/**
 * @author chulx
 *
 */
public enum UrlUtil {
	INSTANCE;
	
	/**
	 * adding leading http if not there
	 * @param url
	 * @return original url or with leading 'http://' added.
	 */
	public String checkHttpPrefix (String url) {
		// a very special case http.com (non exist actually but was part of the Alexa top 1M urls)
		
		if (url != null && !url.contains("://")) {
			return "http://"+url;
		}
		
		return url;
	}

	public String checkHttpsPrefix (String url) {
		if (url != null && !url.contains("://")) {
			return "https://"+url;
		}
		
		return url;
		
	}
	/**
	 * remove query parameter string. for example, "abc?q=123" will be "abc".
	 * @param url
	 * @return
	 */
	public String removeQueryString (String url) {
		if (url != null && url.contains("?")) {
			return url.substring(0, url.indexOf("?"));
		}		
		return url;
	}
	
	
}
