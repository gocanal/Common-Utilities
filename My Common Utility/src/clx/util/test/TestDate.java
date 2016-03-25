/**
 * 
 */
package clx.util.test;

import java.util.Date;

import clx.util.date.DateUtil;

/**
 * @author chulx
 *
 */
public class TestDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date()));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2014-1900, 5, 29, 3, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2013-1900, 6, 29, 4, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2015-1900, 6, 29, 4, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2015-1900, 7, 29, 4, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2016-1900, 6, 29, 4, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2015-1900, 5, 23, 4, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2015-1900, 6, 2, 4, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2015-1900, 5, 29, 16, 0, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2015-1900, 5, 29, 16, 9, 0)));
		System.out.println (DateUtil.INSTANCE.convertDateToDuratingString(new Date(2015-1900, 5, 30, 18, 15, 0)));

	}

}
