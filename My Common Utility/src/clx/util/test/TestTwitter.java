/**
 * 
 */
package clx.util.test;

import java.util.List;

import clx.util.model.News;
import clx.util.network.Proxy;
import clx.util.twitter.TwitterUtil;

/**
 * @author chulx
 *
 */
public class TestTwitter {
	private static String key, secret, token, tokenSecret;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Proxy.INSTANCE.initNetworkProxy();
		
		getLimit();
		/*
		List<News> posts = TwitterUtil.getInstance().getPostsByUserId("Luxuo", 0);
		if (posts != null) {
			for (News post : posts) {
				System.out.println (post.getContent());
			}
		}
		*/
	}

	private static void getLimit () {
		TwitterUtil.getInstance(key, secret, token, tokenSecret).getRateLimitStatus();
	}
}
