/**
 * 
 */
package clx.util.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import clx.util.string.UrlUtil;

/**
 * @author chulx
 *
 */
public enum Downloader {
	INSTANCE;
	
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
	private static final int TIMEOUT = 10 *1000; // default 10 seconds
	
	private static final int BUFFER_SIZE = 4096;
	
	
	public String post (String url, Map<String, String>data, int timeout) {
		int ts = timeout <= 0 ? TIMEOUT : timeout;
		try {
			Document document = Jsoup.connect(UrlUtil.INSTANCE.checkHttpPrefix(url))
				.userAgent(USER_AGENT)
				.followRedirects(true)
				.timeout(ts)
				.data(data)
				.ignoreContentType(true)
				.post();
			return (document == null ? null : document.body().html());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	
	
	/**
	 * 
	 * @param url just pure string, not url encoded
	 * @param timeout millis
	 * @return
	 * @throws IOException 
	 */
	public Document getDocumentFromUrl (String url, int timeout) {
		int ts = timeout <= 0 ? TIMEOUT : timeout;
		try {
			return Jsoup.connect(UrlUtil.INSTANCE.checkHttpPrefix(url))
					.userAgent(USER_AGENT)
					.followRedirects(true)
					.timeout(ts)
					.get();
		} catch (IllegalArgumentException e) {
			System.out.println ("ERROR : " + url + " : " + e.getMessage() == null ? "unknown error when downloading web page" : e.getMessage());
			if (e instanceof IllegalCharsetNameException) {
				try {
					Response con = Jsoup.connect(UrlUtil.INSTANCE.checkHttpPrefix(url))
							.userAgent(USER_AGENT)
							.followRedirects(true)
							.timeout(ts)
							.execute();
					if (con != null)
						return Jsoup.parse(con.body(), ((IllegalCharsetNameException)e).getCharsetName());
				} catch (IOException ee) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (MalformedURLException e) {
			System.out.println ("ERROR : " + url + " : " + e.getMessage() == null ? "unknown error when downloading web page" : e.getMessage());			
		} catch (SSLHandshakeException e) {
			// PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
			// this is because the site uses self signed certificate. we can ignore it but it may introduce man-in-the-middle attack
			//switching to https does not work for this case
			try {
				return Jsoup.connect(UrlUtil.INSTANCE.checkHttpsPrefix(url))
						.userAgent(USER_AGENT)
						.followRedirects(true)
						.timeout(ts)
						.get();
			} catch (IOException e1) {
				System.out.println ("ERROR : " + url + " : " + e.getMessage() == null ? "unknown error when downloading web page" : e.getMessage());
			}
		} catch (IOException e) {
			System.out.println ("ERROR : " + url + " : " + e.getMessage() == null ? "unknown error when downloading web page" : e.getMessage());
			//e.printStackTrace();
		} 
		
		return null;
	}
	
	public Document getXmlDocumentFromUrl (String url, int timeout) {
		int ts = timeout <= 0 ? TIMEOUT : timeout;
		try {
			return Jsoup.connect(UrlUtil.INSTANCE.checkHttpPrefix(url))
					.userAgent(USER_AGENT)
					.followRedirects(true)
					.timeout(ts)
					.ignoreContentType(true)
					.parser(Parser.xmlParser())
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * typically this is used when the site returns json string
	 * 
	 * @param url
	 * @param timeout millis
	 * @return
	 */
	public String getBodyStringFromUrl (String url, int timeout) {
		int ts = timeout <= 0 ? TIMEOUT : timeout;
		try {
			Document doc = Jsoup.connect(UrlUtil.INSTANCE.checkHttpPrefix(url))
							.userAgent(USER_AGENT)
							.followRedirects(true)
							.timeout(ts)
							.ignoreContentType(true)
							.get();
			if (doc != null)
				return doc.body().html();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	public String getTextAttachmentFromUrl (String fileurl) {
		String data = null;
		
		byte[] response = getAttachmentFromUrl (fileurl);
		if (response != null) {
			try {
				data = new String (response, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        return data;

	}
	
	/**
	 * download binary attachment from url
	 * 
	 * @param url
	 */
	public byte[] getAttachmentFromUrl (String fileurl) {
		byte[] data = null;
		
        URL url;
        HttpURLConnection httpConn = null;
		try {
			url = new URL(UrlUtil.INSTANCE.checkHttpPrefix(fileurl));
	        httpConn = (HttpURLConnection) url.openConnection();
	        int responseCode = httpConn.getResponseCode();
	 
	        // always check HTTP response code first
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            String fileName = "";
	            String disposition = httpConn.getHeaderField("Content-Disposition");
	            String contentType = httpConn.getContentType();
	            int contentLength = httpConn.getContentLength();
	 
	            if (disposition != null) {
	                // extracts file name from header field
	                int index = disposition.indexOf("filename=");
	                if (index > 0) {
	                    fileName = disposition.substring(index + 10,
	                            disposition.length() - 1);
	                }
	            } else {
	                // extracts file name from URL
	                fileName = fileurl.substring(fileurl.lastIndexOf("/") + 1,
	                		fileurl.length());
	            }
	 
	            System.out.println("Content-Type = " + contentType);
	            System.out.println("Content-Disposition = " + disposition);
	            System.out.println("Content-Length = " + contentLength);
	            System.out.println("fileName = " + fileName);
	 
	            // opens input stream from the HTTP connection
	            InputStream inputStream = httpConn.getInputStream();
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            
	            int bytesRead = -1;
	            byte[] buffer = new byte[BUFFER_SIZE];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	            	 out.write(buffer, 0, bytesRead);
	            }

	            inputStream.close();
	            out.close();
	            data = out.toByteArray();
	            
	        } else {
	            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
	        }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpConn != null)
				httpConn.disconnect();
		}

		return data;
	}
}
