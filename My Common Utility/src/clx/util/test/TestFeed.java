/**
 * 
 */
package clx.util.test;

import java.util.List;

import clx.util.feed.RssUtil;
import clx.util.model.News;
import clx.util.network.Proxy;

/**
 * @author chulx
 *
 */
public class TestFeed {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String urls [] = {
				//"http://www.bibase.com/feed.rss",
				//"http://www.kioskmarketplace.com/rss/",
				"http://news.google.com/?output=rss"
		};
		
		Proxy.INSTANCE.initNetworkProxy();
		
		for (String url : urls) {
			List<News> news = RssUtil.INSTANCE.download(url);
			if (news != null) {
				System.out.println ("total news : " + news.size());
			}
		}

	}

}
