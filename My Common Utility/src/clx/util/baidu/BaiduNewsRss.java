/**
 * 
 */
package clx.util.baidu;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import clx.util.date.DateUtil;
import clx.util.model.News;
import clx.util.web.Downloader;

/**
 * @author chulx
 *
 */
public enum BaiduNewsRss {
	INSTANCE;
	
	private static final String NEWSRSS_URL = "http://news.baidu.com/ns?tn=newsrss&sr=0&cl=2&rn=20&ct=0&word=";
	
	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public List<News> downloadNewsByKeyword (String keyword) {
		/* 
		 * it has unique RSS format that Rome can not properly handle (for the pubDate field only)		 
		 * for example:
		/* <pubDate> <![CDATA[ Thu, 25 Jun 2015 17:45:04 ]]> </pubDate>
		 * 
		 */
		
		// return RssUtil.INSTANCE.download(NEWSRSS_URL+keyword);
		
		List<News> result = new ArrayList<News>();
		
		try {
			Document doc = Downloader.INSTANCE.getXmlDocumentFromUrl(NEWSRSS_URL + URLEncoder.encode(keyword, "UTF-8"), 0);
			if (doc != null) {
				for (Element item : doc.select("item")) {
			        result.add(convertToNews(item));
			    }
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;		
	}

	private News convertToNews (Element item) {
		News news = new News();
		news.setTitle(item.select("title").first().text());
		news.setContent(item.select("description").first().text());	
		news.setUrl(item.select("link").first().text());
		news.setAuthor(item.select("author").first().text());
		news.setDate(DateUtil.INSTANCE.convertStringToDate(item.select("pubDate").first().text()));
		return news;
	}

}
