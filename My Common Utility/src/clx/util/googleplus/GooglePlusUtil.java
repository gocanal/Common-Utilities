/**
 * 
 */
package clx.util.googleplus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import clx.util.model.News;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Activity.PlusObject.Attachments;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;



/**
 * @author chulx
 *
 */
public final class GooglePlusUtil {
	
	private static Plus plus = null;
	
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	
	private static final long MAX_PERSON = 10;

	/****************************************
	 * 
	 * initialization
	 * 
	 ***************************************/
	private GooglePlusUtil() { }
	
    /**
     * Initializes singleton.
     *
     * {@link SingletonHolder} is loaded on the first execution of {@link Singleton#getInstance()} or the first access to
     * {@link SingletonHolder#INSTANCE}, not before.
     */
    private static class SingletonHolder {
            private static final GooglePlusUtil INSTANCE = new GooglePlusUtil();
    }

    public static GooglePlusUtil getInstance(String appName, String clientId, String clientSecret, String accessToken, String refreshToken) {
    		setPlus(appName, clientId, clientSecret, accessToken, refreshToken);
            return SingletonHolder.INSTANCE;
    }


	private static void setPlus (String appName, String clientId, String clientSecret, String accessToken, String refreshToken) {
		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			MemoryDataStoreFactory dataStoreFactory = new MemoryDataStoreFactory();
		    // authorization
		    // Credential credential = authorize(httpTransport, dataStoreFactory);
			GoogleCredential credential = authorizeByAccessToken (httpTransport, clientId, clientSecret, accessToken, refreshToken);
		    // set up global Plus instance
		    plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(appName).build();		    
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static GoogleCredential authorizeByAccessToken (HttpTransport httpTransport, String clientId, String clientSecret, String accessToken, String refreshToken) {

		GoogleCredential credential = new GoogleCredential.Builder()
			.setTransport(httpTransport)
			.setJsonFactory(JSON_FACTORY)
			.setClientSecrets(clientId, clientSecret)
			.build();
		credential.setAccessToken(accessToken);
		//credential.setExpirationTimeMilliseconds(1412046110052l);
		credential.setRefreshToken(refreshToken);
		return credential;
	}
	
	/** Authorizes the installed application to access user's protected data.
	 * this will open a browser, then click 'accept' 
	 * 
	 * this should be used for the first time only
	 * 
	 * @param httpTransport
	 * @param dataStoreFactory
	 * @return
	 * @throws Exception
	 */
	private Credential authorize(HttpTransport httpTransport, MemoryDataStoreFactory dataStoreFactory) throws Exception {
		// load client secrets
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	        new InputStreamReader(GooglePlusUtil.class.getResourceAsStream("client_secrets.json")));
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	      System.out.println(
	          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=plus "
	          + "into plus-cmdline-sample/src/main/resources/client_secrets.json");
	      System.exit(1);
	    }
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(PlusScopes.PLUS_ME))
	    	.setDataStoreFactory(dataStoreFactory).build();
	    // authorize
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}		


	
	/****************************************
	 * 
	 * User
	 * @throws IOException 
	 * 
	 ***************************************/	
	public List<Person> searchPersonByName (String name, long max) throws IOException {
		long m = (max <= 0) ? 1 : ((max > MAX_PERSON) ? MAX_PERSON : max);
		return plus.people().search(name).setMaxResults(m).execute().getItems();
	}

	public Person searchPersonById (String id) throws IOException {
		return plus.people().get(id).execute();
	}

	
	/****************************************
	 * 
	 * Posts
	 * 
	 ***************************************/
	public List<News> downloadPostByPerson (String gid) {
		try {
			// default download 10, max 20
			Plus.Activities.List list = plus.activities().list(gid, "public");
			ActivityFeed feed = list.execute();
			List<Activity> activities = feed.getItems();
			if (activities != null && !activities.isEmpty()) {
				List<News> result = new ArrayList<News>();
				for (Activity a : activities) {
					result.add(convertToNews (a));
				}
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private News convertToNews (Activity entry) {
		News news = new News();
		news.setTitle(entry.getTitle());
		news.setAuthor(entry.getActor().getDisplayName());
		news.setDate(new Date (entry.getPublished().getValue()));
		news.setUrl(entry.getUrl());
		
		String c = entry.getObject().getContent();
		if (c == null)
			c = "";
		news.setContent (c);

		List<Attachments> attachments = entry.getObject().getAttachments();
		if (attachments != null && !attachments.isEmpty()) {
			for (Attachments at : attachments) {
				if (at.getObjectType().equals("article")) {
					if (at.getDisplayName()!=null && !at.getDisplayName().isEmpty())
						news.setTitle(at.getDisplayName());						
					if (at.getFullImage()!=null)
						news.setImageUrl(at.getFullImage().getUrl());
					if (at.getContent()!=null)
						news.setContent(at.getContent());
					if (at.getUrl() != null)
						news.setUrl (at.getUrl());
					break;
				} else if (at.getObjectType().equals("video")) {
					if (at.getDisplayName()!=null && !at.getDisplayName().isEmpty())
						news.setTitle(at.getDisplayName());
					if (at.getImage() != null && at.getImage().getUrl()!=null)
						news.setImageUrl(at.getImage().getUrl());
					if (at.getContent()!=null)
						news.setContent(at.getContent());
					if (at.getUrl() != null)
						news.setUrl (at.getUrl());
				} else if (at.getObjectType().equals("photo")) {
					if (at.getImage()!=null && at.getImage().getUrl()!=null)
						news.setImageUrl(at.getImage().getUrl());
				} else if (at.getObjectType().equals("album")) {
					// TODO
				}
				/*
				Image image = at.getImage();
				if (image != null) {
					news.setImageurl(image.getUrl());
					break;
				}
				*/
			}
		}
		
		return news;
	}
}
