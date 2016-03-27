/**
 * 
 */
package clx.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;

/**
 * @author chulx
 * various Date format
 * 
 * Thu May 21 04:54:07 SGT 2015 : EEE MMM dd HH:mm:ss z yyyy
 * 2015-02-14 15:53:13 : yyyy-dd-MM HH:mm:ss (common in Chinese web sites)
 * Fri, 29 May 2015 13:20:03 GMT : EEE, dd MMM yyyy HH:mm:ss z
 * 30-May-2015 08:00 : (telegraph) : dd-MMM-yyyy HH:mm
 * Thu, 28 May 2015 09:56:17 +0000 : EEE, dd MMM yyyy HH:mm:ss Z
 * 2015-05-30T13:31:19Z : yyyy-MM-dd'T'HH:mm:ss'Z'
 * Thursday, September 18, 2014 : EEEE, MMMM dd, yyyy
 * <![CDATA[ 2015-05-21T01:16:30.000Z ]]>
 * 
 */
public enum DateUtil {
	INSTANCE;
	
	private static final String [] dateFormats = {
		"yyyy-MM-dd",
		"EEE MMM dd HH:mm:ss z yyyy",
		"yyyy-dd-MM HH:mm:ss",
		"EEE, dd MMM yyyy HH:mm:ss zzz",
		"dd-MMM-yyyy HH:mm",
		"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
		"EEE, dd MMM yyyy HH:mm:ss Z",
		"EEE, dd MMM yyyy HH:mm",
		"yyyy-MM-dd'T'HH:mm:ss'Z'",
		"EEEE, MMMM dd, yyyy",
		"yyyy:MM:dd HH:mm:ss"};

	public Date convertStringToDate (String d) {
		Date date = null;

		if (d != null && !d.isEmpty()) {
			
			if (d.startsWith("<![CDATA[")) {
				d = d.substring("<![CDATA[".length());
				if (d.endsWith("]]>"))
					d = d.substring(0, d.length()-3);			
			}
			
			// remove leading and trailing space
			d = d.trim();

			for (String format : dateFormats) {
				DateFormat formatter = new SimpleDateFormat(format);
				try {
					date = formatter.parse(d);
					break;
				} catch (ParseException e) {
				}				
			}
			
			if (date == null)
				System.out.println ("[ERROR] can not parse date string : " + d);

		}
		
		return date; 
	}

	public String convertDateToString (Date date, String format) {
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	/**
	 * 
	 * @param start start time
	 * @param end end time
	 * @return h H : m M : s S : ms MS
	 */
	public String getElapsedString (Date start, Date end) {
		String result = "0 ms";
		if (start != null && end != null) {
			
			long diff = end.getTime() - start.getTime();
			long h = diff / (1000 * 60 * 60);
			long m = (diff - h * (1000 * 60 * 60))/ (1000 * 60);
			long s = (diff - h * (1000 * 60 * 60) - m * (1000 * 60))/1000;
			long ms = diff - h * (1000 * 60 * 60) - m * (1000 * 60) - s * 1000;
			result = h + " h " + m + " min " + s + " sec " + ms + " ms";
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param date
	 * @return a few possibilities
	 *   y year(s) ago/later
	 *   m month(s) ago/later
	 *   d day(s) ago/later
	 *   h hour(s) ago/later
	 *   m minute(s) ago/later
	 *   s second(s) ago/later
	 */
	public String convertDateToDuratingString (Date d) {
		DateTime today = new DateTime();
		DateTime date = new DateTime(d);
		int months = Months.monthsBetween(date, today).getMonths();
		if (months <= -24) {
			return -months/12 + " years later";
		} else if (months <= -12) {
			// TODO - may want to return Y years M months
			return "1 year later";
		} else if (months < -1) {
			return -months + " months later";
		} else if (months == -1) {
			return "1 month later";
		} else if (months >= 24) {
			// TODO - may want to return Y years M months
			return months/12 + " years ago";
		} else if (months >= 12) {
			return "1 year ago";
		} else if (months > 1) {
			return months + " months ago";
		} else if (months == 1) {
			return "1 month ago";
		}
		
		int weeks = Weeks.weeksBetween(date, today).getWeeks();
		if (weeks < -1) {
			return -weeks + " weeks later";
		} else if (weeks == -1) {
			return "1 week later";
		} else if (weeks > 1) {
			return weeks + " weeks ago";
		} else if (weeks == 1) {
			return "1 week ago";
		}
		
		int days = Days.daysBetween(date, today).getDays();
		if (days < -1) {
			return -days + " days later";
		} else if (days == -1) {
			return "1 day later";
		} else if (days > 1) {
			return days + " days ago";
		} else if (days == 1) {
			return "1 fay ago";
		}
		
		int hours = Hours.hoursBetween(date, today).getHours();
		if (hours < -1) {
			return -hours + " hours later";
		} else if (hours == -1) {
			return "1 hour later";
		} else if (hours > 1) {
			return hours + " hours ago";
		} else if (hours == 1) {
			return "1 hour ago";
		}
		
		int minutes = Minutes.minutesBetween(date, today).getMinutes();
		if (minutes < -1) {
			return -minutes + " minutes later";
		} else if (minutes == -1) {
			return "1 minute later";
		} else if (minutes > 1) {
			return minutes + " minutes ago";
		} else if (minutes == 1) {
			return "1 minute ago";
		}
		
		int seconds = Seconds.secondsBetween(date, today).getSeconds();
		if (seconds < -1) {
			return -seconds + " seconds later";
		} else if (seconds > 1) {
			return seconds + " seconds ago";
		}
			
		return "now";
	}
	
	public int getDaysBetween (Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return -1;
		
		return Days.daysBetween(new DateTime (d1), new DateTime (d2)).getDays();
	}
}
