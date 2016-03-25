/**
 * 
 */
package clx.util.test;

import clx.util.string.ExtractDomain;

/**
 * @author chulx
 *
 */
public class TestExtractDomain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String [] urls = {
				"http://www.burrellesluce.com/"
				/*
				"http://feeds.feedburner.com/SfusdNewsFeed",
				"http://feeds.feedburner.com/nydnrss/life-style/horoscopes",
				"http://feeds.feedburner.com/at-gadget-en?format=xml",
				"http://feeds.feedburner.com/YogaBuzz/",
				"http://feeds.gawker.com/gizmodo/full",
				"http://www.feedburner.com",
				"http://www.hp.com/index.html",
				"http://feedproxy.google.com/DigitalPhotographySchool",
				"http://www.hp.com/",
				"http://www.hp.com",
				"www.hp.com/index.html",
				"www.hp.com",
				"sina.cn",
				"baidu.com.cn",
				"https://twitter.com/taylorswift13",
				"https://twitter.com/@outdoorretailer",
				"https://twitter.com/#!/Luxuo",
				"http://twitter.com/RetailConcepts/",
				"http://www.tumblr.com/",
				"https://www.tumblr.com/explore/trending",
				"tumblr.com",
				"http://taylorswift.tumblr.com/",
				"http://m-e-t-a-p-h-y-s-i-c-s.tumblr.com/post/122368666248",
				"http://taylorswift.tumblr.com/",
				"http://www.weibo.com/taylorswiftvip",
				"http://plus.google.com",
				"https://plus.google.com/+TaylorSwift/videos",
				"https://www.facebook.com/TaylorSwift"
				*/
				};
		
		for (String url : urls) {
			String [] domainAndaccount = ExtractDomain.INSTANCE.extractDomainFromUrl(url);
			System.out.println ("[domain] " + domainAndaccount[0] + " [host] " + (domainAndaccount[1] == null ? "" : domainAndaccount[1]));
		}

	}

}
