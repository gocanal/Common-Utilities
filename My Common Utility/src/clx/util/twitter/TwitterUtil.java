/**
 * 
 */
package clx.util.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import clx.util.model.News;


/**
 * @author chulx
 * 
 * Twitter Access Rate Limit :
 *  https://dev.twitter.com/rest/public/rate-limiting
 *  
 *  Search: 180 queries per 15 minute
 *  
 */
public final class TwitterUtil {
	
	private static final int MAX_ITEM = 15;	
	private static Twitter twitter = null;
	
	/****************************************
	 * 
	 * Init
	 * 
	 ***************************************/
	// Private constructor. Prevents instantiation from other classes.
    private TwitterUtil() { }

    /**
     * Initializes singleton.
     *
     * {@link SingletonHolder} is loaded on the first execution of {@link Singleton#getInstance()} or the first access to
     * {@link SingletonHolder#INSTANCE}, not before.
     */
    private static class SingletonHolder {
            private static final TwitterUtil INSTANCE = new TwitterUtil();
    }

    public static TwitterUtil getInstance(String key, String secret, String token, String tokenSecret) {
    		setTwitter(key, secret, token, tokenSecret);
            return SingletonHolder.INSTANCE;
    }



	private static void setTwitter (String CONSUMER_KEY, String CONSUMER_SECRET, String TOKEN, String TOKEN_SECRET) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(CONSUMER_KEY);
		builder.setOAuthConsumerSecret(CONSUMER_SECRET);
		builder.setOAuthAccessToken(TOKEN);
		builder.setOAuthAccessTokenSecret(TOKEN_SECRET);
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();	
	}


	
	
	/****************************************
	 * 
	 * Rate Limit Status
	 * 
	 ***************************************/
	public void getRateLimitStatus () {
		try {
			Map<String, RateLimitStatus> limits = twitter.getRateLimitStatus();
			if (limits != null) {
				for (Map.Entry<String, RateLimitStatus> status : limits.entrySet()) {
					System.out.println ("[" + status.getKey() + "] " + "(limit) " + status.getValue().getLimit() + " (remaining) " + status.getValue().getRemaining() + " (reset) " + status.getValue().getSecondsUntilReset());
				}
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/****************************************
	 * 
	 * Users
	 * 
	 ***************************************/

	public List<User> getUsersByKeyword (String keyword) throws TwitterException {
		// indicate which page, starting from 0. each page has 20. maximum 50 pages 
		return twitter.searchUsers(keyword, 0);
	}
	
	public User getUserById (String id) throws TwitterException {
		return twitter.showUser(Long.valueOf(id));
	}
	
	public User getUserByName (String name) throws TwitterException {
		return twitter.showUser(name);
	}

	/****************************************
	 * 
	 * Posts
	 * 
	 ***************************************/
	
	public List<News> getPostsByUserId (String id, int max) throws TwitterException {
		List<News> result = new ArrayList<News>();
		ResponseList<Status> list = twitter.getUserTimeline(id, new Paging(1, max <= 0 ? MAX_ITEM : max));
		if (list != null && !list.isEmpty()) {
			for (Status status : list) {
				result.add (convertToNews (status));
			}
		}
		return result;
	}
	

	/**
	 * 
	 * @param keyword
	 * @param max twitter returns max 200 I think.
	 * @return
	 * @throws TwitterException 
	 */
	public List<News> getPostsByKeyword (String keyword, int max) throws TwitterException {
		QueryResult engine = twitter.search(new Query(keyword));
		
		List<News> result = new ArrayList<News>();
		
		List<Status> list = engine.getTweets();
		if (list != null && !list.isEmpty()) {			
			for (Status status : list) {
				result.add (convertToNews (status));
			}
		}
		
		return result;
	}
	
	private News convertToNews (Status entry) {
		News news = new News();

		// tweet does not have title, for filtering purpose, we use content, then in order for the 
		// client to render correctly, we need to indicate the news type
		news.setTitle(entry.getText());
		news.setAuthor(entry.getUser().getScreenName());
		news.setDate(entry.getCreatedAt());
		//news.setUrl(entry.getUrl());
		
		// text may not be HTML, may have URLs
		news.setContent (entry.getText());
		
		if (entry.getMediaEntities()!=null && entry.getMediaEntities().length>0) {
			for (MediaEntity media : entry.getMediaEntities()) {
				if (media.getType().equals("photo")) {
					if (media.getMediaURL()!=null) {
						news.setImageUrl(media.getMediaURL());
						break;
					}
				}
			}
		}
	
		
		return news;
	}
}
