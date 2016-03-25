/**
 * 
 */
package clx.util.string;


/**
 * @author chulx
 *
 */
public enum ExtractDomain {
	INSTANCE;
	
	/**
	 * The primary purpose of extracting 'domain' from a url is to check if a url
	 * is duplicated. A simple string comparison is not good enough, for example:
	 * http://www.hp.com and www.hp.com are actually the same url.
	 * 
	 * the secondary purpose of extracting 'account' from some special urls is to
	 * get the account information so that a special service can retrieve the information. 
	 * For example, 
	 * 
	 *   https://twitter.com/taylorswift13, 
	 *   https://plus.google.com/+TaylorSwift/videos, 
	 *   https://www.facebook.com/TaylorSwift
	 *   http://taylorswift.tumblr.com/, 
	 *   http://www.weibo.com/taylorswiftvip, 
	 *   https://pa.linkedin.com/pub/taylor-swift/76/a40/377
	 *
	 * Considering two cases, how do we support the logic
	 *   . http://www.twitter.com and http://twitter.com/taylorswift13
	 *   
	 * In the 'sites' table:
	 *   host:    http://www.twitter.com  and http://twitter.com/taylorswift13
	 *   domain:  www.twitter.com         and twitter
	 *   account: NULL ?                  and taylorswift13
	 * 
	 * Q1. Can we get accounts for a special site ?
	 *     Yes,  with a different domain string, we can differentiate a normal web site and a twitter account;
	 * Q2. Can we check if duplicated url or not ?
	 *     Yes.
	 *     If the new url is a special account, we check both domain and account (case insensitive ?)
	 *     If the new url is a normal site, we check domain only.
	 * 
	 * @param url
	 * @return (type, account), type is one of facebook, feedburner, feedproxy, google+, twitter, weibo, tumblr, gawker, or empty/null which
	 * means it is a normal web site.
	 */
	public String [] extractDomainFromUrl (String url) {
		String [] domainAndAccount = new String [2];
		if (url != null && !url.isEmpty()) {			
			String site = url.contains("//") ? url.substring(url.indexOf("//") + "//".length()) : url;
			String [] segs = site.split("/");
			String host = site.contains("/") ? site.substring(0, site.indexOf("/")).toLowerCase() : site.toLowerCase();
			if (host.contains("feedburner.com")) {
				// special case : http://feeds.feedburner.com/at-gadget-en?format=xml
				if (segs.length > 1) {
					domainAndAccount[0] = "feedburner";
					// special case : http://feeds.feedburner.com/at-gadget-en?format=xml
					if (segs[1].contains("?")) {
						domainAndAccount[1] = segs[1].substring(0, segs[1].indexOf("?"));
					} else {
						domainAndAccount[1] = segs[1];
					}
				} else {
					// it is the feedburner.com itself
					domainAndAccount[0] = segs[0];
				}
			} else if (host.contains("gawker.com")) {
				// http://feeds.gawker.com/gizmodo/full
				if (segs.length > 1) {
					domainAndAccount[0] = "gawker";
					domainAndAccount[1] = segs[1];
				} else
					domainAndAccount[0] = segs[0];
			} else if (host.contains("feedproxy.google.com")) {
				if (segs.length >= 2) {
					if (segs[1].equalsIgnoreCase("~r")) {
						domainAndAccount[0] = "feedproxy";
						domainAndAccount[1] = segs[2];
					} else {
						domainAndAccount[0] = "feedproxy";
						domainAndAccount[1] = segs[1];
					}
				} else if (segs.length > 1) {
					domainAndAccount[0] = "feedproxy";
					domainAndAccount[1] = segs[1];
				} else
					domainAndAccount[0] = segs[0];
			} else if (host.startsWith("www.twitter.com") || host.startsWith("twitter.com")) {
				// https://twitter.com/retailingtoday
				// https://twitter.com/@outdoorretailer
				// https://twitter.com/#!/Luxuo
				// http://twitter.com/RetailConcepts/	
				
				// need to exclude cases (typically used in retweet url):
				/**
				 * a href="http://pic.twitter.com/t1oSv3PhFL" target="_blank"
				 * 
				 * user?user_id=27027979
				 * user?screen_name=MilitaryTimes
				 * tweet?in_reply_to=344527858015805440
				 * retweet?tweet_id=619175885854240768
				 * favorite?tweet_id=344527858015805440
				 * search?q=%23SBBenefit
				 * share?url=http%3A%2F%2Fbit.ly%2F1C0FgvZ&text=
				 * 
				 */
				if (segs.length > 2) {
					String a = segs[2].startsWith("@") ? segs[2].substring(1) : segs[2];
					if (!a.contains("=") && !a.contains("?")) {
						domainAndAccount[0] = "twitter";
						domainAndAccount[1] = a;
					}
				} else if (segs.length > 1) {
					String a = segs[1].startsWith("@") ? segs[1].substring(1) : segs[1];
					if (!a.contains("=") && !a.contains("?")) {
						domainAndAccount[0] = "twitter";
						domainAndAccount[1] = a;
					}
				} else
					domainAndAccount[0] = segs[0];
				
			} else if (host.contains("tumblr.com")) {
				if (host.toLowerCase().startsWith("tumblr.com") || host.toLowerCase().startsWith("www.tumblr.com")) {
					domainAndAccount[0] = host;
				} else {
					domainAndAccount[0] = "tumblr";
					domainAndAccount[1] = host.substring(0, host.indexOf("tumblr.com")-1);
				}
			} else if (host.contains("weibo.com")) {
				if (segs.length > 1) {
					domainAndAccount[0] = "weibo";
					domainAndAccount[1] = segs[1];
				} else
					domainAndAccount[0] = segs[0];
				
			} else if (host.contains("plus.google.com")) {
				if (segs.length > 1) {
					domainAndAccount[0] = "google+";
					domainAndAccount[1] = segs[1];
				} else
					domainAndAccount[0] = segs[0];
				
			} else if (host.contains("facebook.com")) {
				if (segs.length > 1) {
					domainAndAccount[0] = "facebook";
					domainAndAccount[1] = segs[1];
				} else
					domainAndAccount[0] = segs[0];
				
			} else {
				domainAndAccount[0] = segs[0];
			}
			
		}
		
		return domainAndAccount;
	}

}
