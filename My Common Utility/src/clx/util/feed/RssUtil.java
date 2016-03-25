/**
 * 
 */
package clx.util.feed;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import clx.util.date.DateUtil;
import clx.util.model.News;
import clx.util.string.UrlUtil;

import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.modules.mediarss.MediaModule;
import com.rometools.modules.mediarss.types.MediaContent;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;



/**
 * @author chulx
 *
 */
public enum RssUtil {
	INSTANCE;

	/****************************************
	 * 
	 * extract feed links
	 * 
	 ***************************************/
	
	/**
	 * Parse the web page to exract RSS links
	 * 
	 * @param doc
	 * @return
	 */
	public List<String> getRssLinks (Document doc) {
		List<String> urls = new ArrayList<String>();
		
        Elements links = doc.select("link");

		for (Element link : links) {
			if (isFeedLink (link)) {
				String l = link.attr("abs:href");
				if (isNewLink (l, urls))
					urls.add(l);
			}
		}
		
		if (urls.isEmpty()) {
			Elements hrefs = doc.select("a[href]");		
		
			for (Element e : hrefs) {
				String link = e.attr("abs:href");
				String slink = link.toLowerCase();
				// assume there should not be many links with rss or xml
				if ( (slink.contains("rss") || slink.contains("xml") || slink.contains("feed") || slink.contains("feedburner.com")) && isNewLink (slink, urls) ) {
					urls.add(link);
				}
			}
		}

		// need to verify
		if (urls != null) {
			for (int i = urls.size()-1; i>=0; i--) {
				if (!isFeedUrl (urls.get(i)))
					urls.remove(i);
			}
		}
		
		return urls;

	}
	
	private boolean isFeedLink (Element link) {
		String rel = link.attr("rel");
		String type = link.attr("type");
		if ( (rel != null && rel.equals("alternate")) && (type != null && (type.contains("rss") || type.contains("atom"))))
			return true;
		else
			return false;
	}
	
	private boolean isNewLink (String link, List<String> links) {
		if (links == null || links.isEmpty())
			return true;
		for (String l : links) {
			if (l.equalsIgnoreCase(link))
				return false;
		}
		
		return true;
	}
	
	private boolean isFeedUrl (String url) {

		List<SyndEntry> feeds = getFeedEntries (url);
		if (feeds != null && !feeds.isEmpty())
			return true;
		else
			return false;
	}
	
	private List<SyndEntry> getFeedEntries (String url) {
		try {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(new URL(UrlUtil.INSTANCE.checkHttpPrefix(url))));
			return feed.getEntries();
		} catch (IllegalArgumentException | FeedException | IOException e) {
			System.out.println ("WRONG FEED URL : " + url);
			//e.printStackTrace();
		}
		
		return null;
	}

	/****************************************
	 * 
	 * download feeds
	 * 
	 ***************************************/
	
	
	public List<News> download (String url) {
		List<News> result = new ArrayList<News>();
		
		try {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(new URL(UrlUtil.INSTANCE.checkHttpPrefix(url))));
			Date buildDate = feed.getPublishedDate();
			List<SyndEntry> entries = feed.getEntries();			
			if (entries != null) {
				for (SyndEntry entry : entries) {
					News news = convertToNews (entry, buildDate);
					if (news != null)
						result.add(news);
				}
			}
		} catch (IllegalArgumentException | FeedException | IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private News convertToNews (SyndEntry entry, Date date) {
		News news = new News ();
		
		Date publishedDate = DateUtil.INSTANCE.convertStringToDate(entry.getPublishedDate() == null ? null : entry.getPublishedDate().toString());
		news.setDate(publishedDate == null ? date : publishedDate);
		
		news.setAuthor(entry.getAuthor());		
		news.setUrl(entry.getLink());
		news.setContent(entry.getDescription() == null || entry.getDescription().getValue() == null ? null : entry.getDescription().getValue());
		news.setTitle(entry.getTitle());

		// check image or video
		String imageUrl = null;
		Document imageDoc = Jsoup.parseBodyFragment(news.getContent());
    	Elements images = imageDoc == null ? null : imageDoc.select("img");
    	
    	// TODO need to check if the image is already referenced
    	// check if the content has embedded image
    	if (images != null && !images.isEmpty()) {
    		boolean found = false;
    		for (org.jsoup.nodes.Element image : images) {
    			// google has embedded image url "//url...", this cause problems !
    			String url = image.attr("src");
    			if (url != null && url.startsWith("//")) {
    				url = url.substring("//".length());
    				image.attr("src", url);
    				// TODO : how to update news content ?
    			}
    			String w = image.attr("width");
    			String h = image.attr("height");
    			if (w == null && h == null) {
    				found = true;
    				break;
    			}
    			if ( (w != null && !w.equals("1")) || (h != null && !h.equals("1")) ) {
    				found = true;
    				break;
    			}
    		}
    		if (!found)
    			images = null;
    	}
    	
    	if (images == null || images.isEmpty()) {
    		// there are a few possibilities
    		List<SyndEnclosure> enc = entry.getEnclosures();
    		if (enc != null && !enc.isEmpty()) {
    			for (SyndEnclosure se : enc) {
    				if ( (se.getUrl()!=null && (se.getUrl().endsWith("jpg") || se.getUrl().endsWith("png")) ) ||
    						(se.getUrl()!=null && se.getType()!=null && se.getType().startsWith("image")) ) {
    					imageUrl = se.getUrl();
    					//System.out.println (imageUrl+"\n");
    					break;
    				}
    			}
    		}
    		
    		if (imageUrl == null) {
    			MediaEntryModule m = (MediaEntryModule) entry.getModule(MediaModule.URI );
    			MediaContent mc = m == null || m.getMediaContents() == null || m.getMediaContents().length <= 0 ? null : m.getMediaContents()[0];
    			if (mc != null) {
    				String u = mc.getReference() == null ? null : mc.getReference().toString();
    				if (u != null) {
    					if (u.contains("?")) {
    						u = u.substring(0, u.indexOf("?")).toLowerCase();       						
    					}
    					if (u.endsWith("jpeg") || u.endsWith("jpg") || u.endsWith("png") || u.endsWith("gif"))
    						imageUrl = u;
    				}
    				
    			}
    		}
    	}
    	
    	if (imageUrl != null)
    		news.setImageUrl(imageUrl);		
		
		return news;
	}
}
