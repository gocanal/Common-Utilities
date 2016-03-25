/**
 * 
 */
package clx.util.network;

import java.net.ProxySelector;

import com.btr.proxy.search.ProxySearch;

/**
 * @author chulx
 *
 */
public enum Proxy {
	INSTANCE;
	
	/**
	 * detect and set the network proxy. this is needed if accessing behind a proxy, for example, 
	 * from office to the public Internet.
	 * 
	 */
	public void initNetworkProxy () {
		ProxySearch proxySearch = ProxySearch.getDefaultProxySearch();
		ProxySelector myProxySelector = proxySearch.getProxySelector();
		                
		ProxySelector.setDefault(myProxySelector);		
	}

}
