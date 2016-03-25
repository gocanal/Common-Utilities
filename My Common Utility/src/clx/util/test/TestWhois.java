/**
 * 
 */
package clx.util.test;

import clx.util.network.Proxy;
import clx.util.web.GetWebSiteWhois;

/**
 * @author chulx
 *
 */
public class TestWhois {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String [] sites = {
			"weibo.com",
			"http://yandex.ru",
			"yahoo.co.jp"
		};
		
		Proxy.INSTANCE.initNetworkProxy();
		
		for (String site : sites) {
			System.out.println (GetWebSiteWhois.INSTANCE.getCountry(site));
		}

	}

}
