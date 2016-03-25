/**
 * 
 */
package clx.util.test;

import java.util.Set;

import org.jsoup.nodes.Document;

import clx.util.string.ExtractDomain;
import clx.util.web.DetectSocialLinks;
import clx.util.web.Downloader;

/**
 * @author chulx
 *
 */
public class TestExtractGooglePlusAccount {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String [] data = {
				" boundless.com", // g+ 404 error
				"calcionapoli24.it", // g+ 404 error
				"instant-gaming.com", // g+ 500 error
				"idg.se", // g+ 500 error
				"http://www.infopraca.pl/", // g+ return 404 which is correct
				"manualsonline.com", // g+ returns 500
				"Ssrn.com", // throw IllegalArgumentException : protocol http, host = null
				"lodash.com",
				"http.com", // malformed url exception
				"Triplicate.com", // charset encoding, not using UTF-8
				"hasjob.co" // https case
		};

		for (String url : data) {
			process (url);
		}
	}

	private static void process (String url) {
		Document doc = Downloader.INSTANCE.getDocumentFromUrl(url, 0);
		if (doc != null) {
			Set<String> glinks = DetectSocialLinks.INSTANCE.detectGooglePlus(doc);
			if (glinks != null && !glinks.isEmpty()) {
				for (String link : glinks) {
					System.out.println (link);
					String [] account = ExtractDomain.INSTANCE.extractDomainFromUrl(link);
					if (account[1] != null) {
						System.out.println (account[1]);
					}
				}
			}
		}
	}
}
