/**
 * 
 */
package clx.util.test;

import java.util.List;

import clx.util.file.FileUtil;
import clx.util.string.ExtractDomain;

/**
 * @author chulx
 *
 */
public class TestExtractDomainFromFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> lines = FileUtil.INSTANCE.readTextFileAsLines("./top-1m.csv");
		for (String url : lines) {
			
			String [] segs = url.split(",");
			String [] domainAndaccount = ExtractDomain.INSTANCE.extractDomainFromUrl(segs[1]);
			System.out.println ("[domain] " + domainAndaccount[0] + " [host] " + (domainAndaccount[1] == null ? "" : domainAndaccount[1]));
		}

	}

}
