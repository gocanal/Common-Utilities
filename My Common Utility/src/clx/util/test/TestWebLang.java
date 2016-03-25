/**
 * 
 */
package clx.util.test;

import clx.util.network.Proxy;
import clx.util.web.DetectWebLanguage;

/**
 * @author chulx
 *
 */
public class TestWebLang {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String [] sites = {
				//"http://www.baidu.com",
				//"http://google.com",
				//"http://qq.com", // time out
				//"https://twitter.com", // lang=msa ?
				"http://www.weibo.com", // fi ? see http://www.zhihu.com/question/24690249, JS redirect problem
				"http://yahoo.co.jp",
				"http://yandex.ru"
		};
		
		Proxy.INSTANCE.initNetworkProxy();
		
		for (String url : sites) {
			System.out.println (url);
			System.out.println (DetectWebLanguage.INSTANCE.getLanguageAndDescription(url));
		}

	}

}
