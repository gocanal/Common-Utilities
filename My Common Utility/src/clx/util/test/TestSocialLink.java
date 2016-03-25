/**
 * Copyright 2014, Chu Li Xin
 * 
 * 
 * Jul 27, 2015  7:43:10 AM
 */
package clx.util.test;

import java.util.Set;

import org.jsoup.nodes.Document;

import clx.util.string.ExtractDomain;
import clx.util.web.DetectSocialLinks;
import clx.util.web.Downloader;

/**
 * @author lixin
 *
 */
public class TestSocialLink {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String [] urls = {
				"http://www.burrellesluce.com/"
		};
		
		for (String url : urls) {
			Document doc = Downloader.INSTANCE.getDocumentFromUrl(url, 0);
			if (doc != null) {
				Set<String> tlinks = DetectSocialLinks.INSTANCE.detectTwitter(doc);
				if (tlinks != null && !tlinks.isEmpty()) {
					for (String link : tlinks) {
						String [] account = ExtractDomain.INSTANCE.extractDomainFromUrl(link);
						System.out.println (account[0] + " : " + account[1]);
					}
				}
			}
		}

	}

}
