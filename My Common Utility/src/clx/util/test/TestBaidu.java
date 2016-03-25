/**
 * 
 */
package clx.util.test;

import java.util.List;

import clx.util.baidu.BaiduNewsRss;
import clx.util.model.News;
import clx.util.network.Proxy;

/**
 * @author chulx
 *
 */
public class TestBaidu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Proxy.INSTANCE.initNetworkProxy();

		List<News> news = BaiduNewsRss.INSTANCE.downloadNewsByKeyword("stock");
		if (news != null) {
			System.out.println ("downloaded : " + news.size());
		}
	}

}
