/**
 * 
 */
package clx.util.test;

import java.io.IOException;
import java.util.List;

import clx.util.googleplus.GooglePlusUtil;

import com.google.api.services.plus.model.Person;

/**
 * @author chulx
 *
 */
public class TestGooglePlus {
	private static String appName, clientId, clientSecret, accessToken, refreshToken;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// check if google plus account is ok to have a + sign
		String [] accounts = {"111325568466741513319", "+justdial", "justdial"}; 
		for (String name : accounts)
			searchPerson (name);

	}
	
	private static void searchPerson (String name) throws IOException {
		System.out.println (" ----- " + name + " -----");
		if (name.matches("[0-9]+")) {
			Person p = GooglePlusUtil.getInstance(appName, clientId, clientSecret, accessToken, refreshToken).searchPersonById(name);
			if (p != null)
				System.out.println (p.getId() + " : " + p.getDisplayName() + " : " + p.getUrl());
				
			return;
		}
			
		List<Person> users = GooglePlusUtil.getInstance(appName, clientId, clientSecret, accessToken, refreshToken).searchPersonByName(name, 1);
		if (users != null) {
			for (Person p : users) {
				System.out.println (p.getId() + " : " + p.getDisplayName() + " : " + p.getUrl());
			}
		} else {
			System.out.println ("did not find");
		}
		
	}

}
