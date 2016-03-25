/**
 * 
 */
package clx.util.flickr;

import java.util.Date;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.interestingness.InterestingnessInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.stats.StatsInterface;
import com.flickr4java.flickr.stats.StatsSort;

/**
 * @author chulx
 *
 */
public final class FlickrUtil {
	// Private constructor. Prevents instantiation from other classes.
    private FlickrUtil() { }

    /**
     * Initializes singleton.
     *
     * {@link SingletonHolder} is loaded on the first execution of {@link Singleton#getInstance()} or the first access to
     * {@link SingletonHolder#INSTANCE}, not before.
     */
    private static class SingletonHolder {
            private static final FlickrUtil INSTANCE = new FlickrUtil();
    }

    public static FlickrUtil getInstance(String key, String secret) {
    		setFlickr(key, secret);
            return SingletonHolder.INSTANCE;
    }
 	
	private static Flickr flickr = null;
	
	public PhotoList<Photo> getMostViewed (Date date, int perPage, int page) {	
		if (flickr == null)
			return null;
		
		InterestingnessInterface ii = flickr.getInterestingnessInterface();
        try {
			return ii.getList(date, null, perPage, page);
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
	}

	public PhotoList<Photo> getInterestingPhotoList (Date date, int perPage, int page) {
		if (flickr == null)
			return null;

		StatsInterface ii = flickr.getStatsInterface();
        try {
			return ii.getPopularPhotos(date, StatsSort.views, perPage, page);
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
	}
	
	
	private static Flickr setFlickr(String KEY, String SECRET) {
		if (flickr == null) {
			REST rest = new REST();
			
	        //rest.setHost(Flickr.DEFAULT_HOST);
	        flickr = new Flickr(KEY, SECRET, rest);
	        //setAuth(Permission.READ);
		}
		return flickr;
	}
	
	
	protected void setAuth(Permission perms, String KEY, String SECRET) {
        Auth auth = new Auth();
        auth.setPermission(perms);
        auth.setToken(KEY);
        auth.setTokenSecret(SECRET);

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }
}
