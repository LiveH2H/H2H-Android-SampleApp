/**
 * 
 */
package com.mosai.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author hedewen creattime：2013-4-10下午3:59:56 日期时间工具类，进行各种日期时间格式的转化以及格式化
 */
@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {
	
	public static Locale formatlocale = Locale.getDefault();
	
	public final static int TIME_DAY_MILLISECOND = 86400000;
	//
	// 定义时间日期显示格式
	// /
	private final static String DATE_FORMAT = "yyyy-MM-dd";

	private final static String DATE_FORMAT_CN = "yyyy年MM月dd日";

	private final static String TIME_FORMAT_MILL = "yyyy-MM-dd HH:mm:ss.SSS";
	
	private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final static String TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";

	private final static String MONTH_FORMAT = "yyyy-MM";

	private final static String MONTH_DAY_FORMAT = "MM月dd";

	private final static String DAY_FORMAT = "yyyyMMdd";

	// private final static String TIME_FORMAT_MILLI = "yyyy-MM-dd
	// HH:mm:ss.SSS";

	/**
	 * 取得当前系统时间，返回java.util.Date类型
	 * 
	 * @see Date
	 * @return java.util.Date 返回服务器当前系统时间
	 */
	public static Date getCurrDate() {
		return new Date();
	}
	
	/**
	 * 取得当前系统时间的小时
	 * @return
	 */
	public static int getHourOfCurrDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 取得当前系统时间的分钟
	 * @return
	 */
	public static int getMinuteOfCurrDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MINUTE);
	}
	
	/**
	 * 取得当前系统时间的秒钟
	 * @return
	 */
	public static int getSOfCurrDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.SECOND);
	}
	

	/**
	 * 取得当前系统时间戳
	 * 
	 * @see java.sql.Timestamp
	 * @return java.sql.Timestamp 系统时间戳
	 */
	public static java.sql.Timestamp getCurrTimestamp() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * 返回当前时间是上午还是下午
	 * 
	 * @see Calendar.AM 0
	 * @see Calendar.PM 1
	 * @author lenghao
	 * @createTime 2008-8-2 下午04:22:07
	 * @return
	 */
	public static Integer getCurrDateAMorPM() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.AM_PM);
	}

	/**
	 * 得到格式化后的日期，格式为yyyy-MM-dd，如2006-02-15
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的日期，默认格式为为yyyy-MM-dd，如2006-02-15
	 */
	public static String getFormatDate(Date currDate) {
		return getFormatDate(currDate, DATE_FORMAT);
	}

	/**
	 * 得到格式化后的日期，格式为yyyy-MM-dd，如2006-02-15
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(Date)
	 * @return Date 返回格式化后的日期，默认格式为为yyyy-MM-dd，如2006-02-15
	 */
	public static Date getFormatDateToDate(Date currDate) {
		return getFormatDate(getFormatDate(currDate));
	}

	/**
	 * 得到格式化后的日期，格式为yyyy年MM月dd日，如2006年02月15日
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的日期，默认格式为yyyy年MM月dd日，如2006年02月15日
	 */
	public static String getFormatDate_CN(Date currDate) {
		return getFormatDate(currDate, DATE_FORMAT_CN);
	}

	/**
	 * 得到格式化后的日期，格式为yyyy年MM月dd日，如2006年02月15日
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate_CN(String)
	 * @return Date 返回格式化后的日期，默认格式为yyyy年MM月dd日，如2006年02月15日
	 */
	public static Date getFormatDateToDate_CN(Date currDate) {
		return getFormatDate_CN(getFormatDate_CN(currDate));
	}

	/**
	 * 得到格式化后的日期，格式为yyyy-MM-dd，如2006-02-15
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(String, String)
	 * @return Date 返回格式化后的日期，默认格式为yyyy-MM-dd，如2006-02-15
	 */
	public static Date getFormatDate(String currDate) {
		return getFormatDate(currDate, DATE_FORMAT);
	}

	/**
	 * 得到格式化后的日期，格式为yyyy年MM月dd日，如2006年02月15日
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(String, String)
	 * @return 返回格式化后的日期，默认格式为yyyy年MM月dd日，如2006年02月15日
	 */
	public static Date getFormatDate_CN(String currDate) {
		return getFormatDate(currDate, DATE_FORMAT_CN);
	}

	/**
	 * 根据格式得到格式化后的日期
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @param format
	 *            日期格式，如yyyy-MM-dd
	 * @see SimpleDateFormat#format(Date)
	 * @return String 返回格式化后的日期，格式由参数<code>format</code>
	 *         定义，如yyyy-MM-dd，如2006-02-15
	 */
	public static String getFormatDate(Date currDate, String format) {
		if (currDate == null) {
			return "";
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(format,formatlocale);
			return dtFormatdB.format(currDate);
		} catch (Exception e) {
			dtFormatdB = new SimpleDateFormat(DATE_FORMAT,formatlocale);
			try {
				return dtFormatdB.format(currDate);
			} catch (Exception ex) {
			}
		}
		return null;
	}
	
	/**
	 * 根据格式得到格式化后的日期
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @param format
	 *            日期格式，如yyyy-MM-dd
	 * @see SimpleDateFormat#format(Date)
	 * @return String 返回格式化后的日期，格式由参数<code>format</code>
	 *         定义，如yyyy-MM-dd，如2006-02-15
	 */
	public static String getFormatDate(Date currDate, String format,Locale locale) {
		if (currDate == null) {
			return "";
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(format,locale);
			return dtFormatdB.format(currDate);
		} catch (Exception e) {
			dtFormatdB = new SimpleDateFormat(DATE_FORMAT,locale);
			try {
				return dtFormatdB.format(currDate);
			} catch (Exception ex) {
			}
		}
		return null;
	}

	/**
	 * 得到格式化后的时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 * 
	 * @param currDate
	 *            要格式化的时间
	 * @see #getFormatDateTime(Date, String)
	 * @return String 返回格式化后的时间，默认格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 */
	public static String getFormatDateTime(Date currDate) {
		return getFormatDateTime(currDate, TIME_FORMAT);
	}

	/**
	 * 得到格式化后的时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 * 
	 * @param currDate
	 *            要格式环的时间
	 * @see #getFormatDateTime(String)
	 * @return Date 返回格式化后的时间，默认格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 */
	public static Date getFormatDateTimeToTime(Date currDate) {
		return getFormatDateTime(getFormatDateTime(currDate));
	}

	/**
	 * 得到格式化后的时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 * 
	 * @param currDate
	 *            要格式化的时间
	 * @see #getFormatDateTime(String, String)
	 * @return Date 返回格式化后的时间，默认格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 */
	public static Date getFormatDateTime(String currDate) {
		return getFormatDateTime(currDate, TIME_FORMAT);
	}

	/**
	 * 得到格式化后的时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 * 
	 * @param currDate
	 *            要格式化的时间
	 * @see #getFormatDateTime(Date, String)
	 * @return String 返回格式化后的时间，默认格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 */
	public static String getFormatDateTime_CN(Date currDate) {
		return getFormatDateTime(currDate, TIME_FORMAT_CN);
	}

	/**
	 * 得到格式化后的时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 * 
	 * @param currDate
	 *            要格式化的时间
	 * @see #getFormatDateTime_CN(String)
	 * @return Date 返回格式化后的时间，默认格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 */
	public static Date getFormatDateTimeToTime_CN(Date currDate) {
		return getFormatDateTime_CN(getFormatDateTime_CN(currDate));
	}

	/**
	 * 得到格式化后的时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 * 
	 * @param currDate
	 *            要格式化的时间
	 * @see #getFormatDateTime(String, String)
	 * @return Date 返回格式化后的时间，默认格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 */
	public static Date getFormatDateTime_CN(String currDate) {
		return getFormatDateTime(currDate, TIME_FORMAT_CN);
	}

	/**
	 * 根据格式得到格式化后的时间
	 * 
	 * @param currDate
	 *            要格式化的时间
	 * @param format
	 *            时间格式，如yyyy-MM-dd HH:mm:ss
	 * @see SimpleDateFormat#format(Date)
	 * @return String 返回格式化后的时间，格式由参数<code>format</code>定义，如yyyy-MM-dd HH:mm:ss
	 */
	public static String getFormatDateTime(Date currDate,
			String format) {
		if (currDate == null) {
			return "";
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(format,formatlocale);
			return dtFormatdB.format(currDate);
		} catch (Exception e) {
			dtFormatdB = new SimpleDateFormat(TIME_FORMAT,formatlocale);
			try {
				return dtFormatdB.format(currDate);
			} catch (Exception ex) {
			}
		}
		return "";
	}

	/**
	 * 根据格式得到格式化后的日期
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @param format
	 *            日期格式，如yyyy-MM-dd
	 * @see SimpleDateFormat#parse(String)
	 * @return Date 返回格式化后的日期，格式由参数<code>format</code>
	 *         定义，如yyyy-MM-dd，如2006-02-15
	 */
	public static Date getFormatDate(String currDate, String format) {
		if (currDate == null) {
			return null;
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(format,formatlocale);
			return dtFormatdB.parse(currDate);
		} catch (Exception e) {
			dtFormatdB = new SimpleDateFormat(DATE_FORMAT,formatlocale);
			try {
				return dtFormatdB.parse(currDate);
			} catch (Exception ex) {
			}
		}
		return null;
	}

	public static String getFormatDate(String currDate,
			String formatofcurrDate, String format) {
		if (currDate == null) {
			return null;
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(formatofcurrDate,formatlocale);
			return getFormatDate(dtFormatdB.parse(currDate), format);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据格式得到格式化后的时间
	 * 
	 * @param currDate
	 *            要格式化的时间
	 * @param format
	 *            时间格式，如yyyy-MM-dd HH:mm:ss
	 * @see SimpleDateFormat#parse(String)
	 * @return Date 返回格式化后的时间，格式由参数<code>format</code>定义，如yyyy-MM-dd HH:mm:ss
	 */
	public static Date getFormatDateTime(String currDate, String format) {
		if (currDate == null) {
			return null;
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(format,formatlocale);
			return dtFormatdB.parse(currDate);
		} catch (Exception e) {
			dtFormatdB = new SimpleDateFormat(TIME_FORMAT,formatlocale);
			try {
				return dtFormatdB.parse(currDate);
			} catch (Exception ex) {
			}
		}
		return null;
	}

	/**
	 * 得到本日的上月时间 如果当日为2007-9-1,那么获得2007-8-1
	 */
	public static String getDateBeforeMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到本日的前几个月时间 如果number=2当日为2007-9-1,那么获得2007-7-1
	 */
	public static String getDateBeforeMonth(int number) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -number);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到某日的前几个月时间 如果number=2某日为2007-9-1,那么获得2007-7-1
	 */
	public static String getDateBeforeMonth(Date curDate, int number) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		cal.add(Calendar.MONTH, -number);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	public static long getDaysOfDates(Date first, Date second) {
		Date d1 = getFormatDateTime(getFormatDate(first), DATE_FORMAT);
		Date d2 = getFormatDateTime(getFormatDate(second), DATE_FORMAT);

		long mils = d1.getTime() - d2.getTime();

		return mils / (TIME_DAY_MILLISECOND);
	}

	/**
	 * 获得两个Date型日期之间相差的天数（第2个减第1个）
	 * 
	 * @param Date
	 *            first, Date second
	 * @return int 相差的天数
	 */
	public static int getDaysBetweenDates(Date first, Date second) {
		Date d1 = getFormatDateTime(getFormatDate(first), DATE_FORMAT);
		Date d2 = getFormatDateTime(getFormatDate(second), DATE_FORMAT);

		Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);

		return mils.intValue();
	}

	/**
	 * 获得两个String型日期之间相差的天数（第2个减第1个）
	 * 
	 * @param String
	 *            first, String second
	 * @return int 相差的天数
	 */
	public static int getDaysBetweenDates(String first, String second) {
		Date d1 = getFormatDateTime(first, DATE_FORMAT);
		Date d2 = getFormatDateTime(second, DATE_FORMAT);

		Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);

		return mils.intValue();
	}

	/**
	 * @author lenghao
	 * @createTime 2008-8-5 下午01:57:09
	 * @param first
	 * @param second
	 * @return 获取两个Date之间的天数的列表
	 */
	public static List<Date> getDaysListBetweenDates(Date first, Date second) {
		List<Date> dateList = new ArrayList<Date>();
		Date d1 = getFormatDateTime(getFormatDate(first), DATE_FORMAT);
		Date d2 = getFormatDateTime(getFormatDate(second), DATE_FORMAT);
		if (d1.compareTo(d2) > 0) {
			return dateList;
		}
		do {
			dateList.add(d1);
			d1 = getDateBeforeOrAfter(d1, 1);
		} while (d1.compareTo(d2) <= 0);
		return dateList;
	}

	/**
	 * @author hedewen
	 * @param first
	 * @param second
	 * @return 获取两个Date之间的列表
	 */
	public static List<String> getDaysStrListBetweenDates(Date first,
			Date second) {
		List<String> dateList = new ArrayList<String>();
		Date d1 = getFormatDateTime(getFormatDate(first), DATE_FORMAT);
		Date d2 = getFormatDateTime(getFormatDate(second), DATE_FORMAT);
		if (d1.compareTo(d2) > 0) {
			return dateList;
		}
		do {
			dateList.add(getFormatDate(d1));
			d1 = getDateBeforeOrAfter(d1, 1);
		} while (d1.compareTo(d2) <= 0);
		return dateList;
	}

	/**
	 * @author hedewen
	 * @return 获取当前月的列表
	 */
	public static List<String> getDaysStrListCurMonths(String datetime) {
		List<String> dateList = new ArrayList<String>();
		Date d1 = getFormatDate(getFirstDayOfMonth(getFormatDate(datetime)));
		Date d2 = getFormatDate(getLastDayOfMonth(getFormatDate(datetime)));
		if (d1.compareTo(d2) > 0) {
			return dateList;
		}
		do {
			dateList.add(getFormatDate(d1));
			d1 = getDateBeforeOrAfter(d1, 1);
		} while (d1.compareTo(d2) <= 0);
		return dateList;
	}

	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		// String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	/**
	     *
	     *
	     */
	public static String getDateBeforeDay() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到格式化后的当前系统日期，格式为yyyy-MM-dd，如2006-02-15
	 * 
	 * @see #getFormatDate(Date)
	 * @return String 返回格式化后的当前服务器系统日期，格式为yyyy-MM-dd，如2006-02-15
	 */
	public static String getCurrDateStr() {
		return getFormatDate(getCurrDate());
	}

	/**
	 * 得到格式化后的当前系统时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 * 
	 * @see #getFormatDateTime(Date)
	 * @return String 返回格式化后的当前服务器系统时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15
	 *         15:23:45
	 */
	public static String getCurrDateTimeStr() {
		return getFormatDateTime(getCurrDate());
	}
	
	public static String getCurrDateTimeStrMill(){
		return 	getFormatDateTime(getCurrDate(), TIME_FORMAT_MILL);
	}

	/**
	 * 得到格式化后的当前系统日期，格式为yyyy年MM月dd日，如2006年02月15日
	 * 
	 * @see #getFormatDate(Date, String)
	 * @return String 返回当前服务器系统日期，格式为yyyy年MM月dd日，如2006年02月15日
	 */
	public static String getCurrDateStr_CN() {
		return getFormatDate(getCurrDate(), DATE_FORMAT_CN);
	}
	/**
	 * 得到格式化后的当前系统时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 * @return
	 */
	public static String getCurrDateTimeStr_CN2() {
		return getFormatDateTime(getCurrDate(), TIME_FORMAT);
	}
	/**
	 * 得到格式化后的当前系统时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 * 
	 * @see #getFormatDateTime(Date, String)
	 * @return String 返回格式化后的当前服务器系统时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日
	 *         15:23:45
	 */
	public static String getCurrDateTimeStr_CN() {
		return getFormatDateTime(getCurrDate(), TIME_FORMAT_CN);
	}

	/**
	 * 得到系统当前日期的前或者后几天
	 * 
	 * @param iDate
	 *            如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数
	 * @see Calendar#add(int, int)
	 * @return Date 返回系统当前日期的前或者后几天
	 */
	public static Date getDateBeforeOrAfter(int iDate) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, iDate);
		return cal.getTime();
	}

	/**
	 * 得到日期的前或者后几天
	 * 
	 * @param iDate
	 *            如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数
	 * @see Calendar#add(int, int)
	 * @return Date 返回参数<code>curDate</code>定义日期的前或者后几天
	 */
	public static Date getDateBeforeOrAfter(Date curDate, int iDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		cal.add(Calendar.DAY_OF_MONTH, iDate);
		return cal.getTime();
	}

	/**
	 * 得到格式化后的月份，格式为yyyy-MM，如2006-02
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的月份，格式为yyyy-MM，如2006-02
	 */
	public static String getFormatMonth(Date currDate) {
		return getFormatDate(currDate, MONTH_FORMAT);
	}

	public static String getFormatMonth(Date currDate, String format) {
		return getFormatDate(currDate, format);
	}

	/**
	 * 得到格式化后的日，格式为yyyyMMdd，如20060210
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的日，格式为yyyyMMdd，如20060210
	 */
	public static String getFormatDay(Date currDate) {
		return getFormatDate(currDate, DAY_FORMAT);
	}

	/**
	 * 得到格式化后的月份，格式为MM月dd，如02月01
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的月份，格式为MM月dd，如02月01
	 */
	public static String getFormatDAY(Date currDate) {
		return getFormatDate(currDate, MONTH_DAY_FORMAT);
	}

	/**
	 * 得到格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see Calendar#getMinimum(int)
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 */
	public static String getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到格式化后的下月第一天，格式为yyyy-MM-dd，如2006-02-01
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see Calendar#getMinimum(int)
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的下月第一天，格式为yyyy-MM-dd，如2006-02-01
	 */
	public static String getFirstDayOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, +1);
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see Calendar#getMinimum(int)
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 */
	public static String getFirstDayOfMonth(Date currDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see Calendar#getMinimum(int)
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
	 */
	public static String getLastDayOfMonth(Date currDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see Calendar#getMinimum(int)
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
	 */
	public static String getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 得到日期的前或者后几小时
	 * 
	 * @param iHour
	 *            如果要获得前几小时日期，该参数为负数； 如果要获得后几小时日期，该参数为正数
	 * @see Calendar#add(int, int)
	 * @return Date 返回参数<code>curDate</code>定义日期的前或者后几小时
	 */
	public static Date getDateBeforeOrAfterHours(Date curDate, int iHour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		cal.add(Calendar.HOUR_OF_DAY, iHour);
		return cal.getTime();
	}

	/**
	 * 判断日期是否在当前周内
	 * 
	 * @param curDate
	 * @param compareDate
	 * @return
	 */
	public static boolean isSameWeek(Date curDate, Date compareDate) {
		if (curDate == null || compareDate == null) {
			return false;
		}

		Calendar calSun = Calendar.getInstance();
		calSun.setTime(getFormatDateToDate(curDate));
		calSun.set(Calendar.DAY_OF_WEEK, 1);

		Calendar calNext = Calendar.getInstance();
		calNext.setTime(calSun.getTime());
		calNext.add(Calendar.DATE, 7);

		Calendar calComp = Calendar.getInstance();
		calComp.setTime(compareDate);
		if (calComp.after(calSun) && calComp.before(calNext)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断两个时间是否在同一天
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public static boolean isSameDate(Date firstDate,Date secondDate){
		if (firstDate == null || secondDate == null) {
			return false;
		}
		Calendar calFirst = Calendar.getInstance();
		calFirst.setTime(firstDate);

		Calendar calSecond = Calendar.getInstance();
		calSecond.setTime(secondDate);
		
		return (calFirst.get(Calendar.DAY_OF_MONTH) == calSecond.get(Calendar.DAY_OF_MONTH))
				&& (calFirst.get(Calendar.MONTH) == calSecond.get(Calendar.MONTH))
				&& (calFirst.get(Calendar.YEAR) == calSecond.get(Calendar.YEAR));
		
	}

	/**
	 * 时间查询时,结束时间的 23:59:59
	 */
	public static String addDateEndfix(String datestring) {
		if ((datestring == null) || datestring.equals("")) {
			return null;
		}
		return datestring + " 23:59:59";
	}

	/**
	 * 返回格式化的日期
	 * 
	 * @param datePre
	 *            格式"yyyy-MM-dd 23:59:59";
	 * @return
	 */
	public static Date getFormatDateEndfix(String dateStr) {
		dateStr = addDateEndfix(dateStr);
		return getFormatDateTime(dateStr);
	}

	/**
	 * 返回格式化的日期
	 * 
	 * @param datePre
	 *            格式"yyyy-MM-dd HH:mm:ss";
	 * @return
	 */
	public static Date formatEndTime(String datePre) {
		if (datePre == null)
			return null;
		String dateStr = addDateEndfix(datePre);
		return getFormatDateTime(dateStr);
	}

	// date1加上compday天数以后的日期与当前时间比较，如果大于当前时间返回true，否则false
	public static Boolean compareDay(Date date1, int compday) {
		if (date1 == null)
			return false;
		Date dateComp = getDateBeforeOrAfter(date1, compday);
		Date nowdate = new Date();
		if (dateComp.after(nowdate))
			return true;
		else
			return false;
	}

	/**
	 * 进行时段格式转换，对于输入的48位的01串，将进行如下操作： <li>
	 * 1.先将输入中每个0变成两个0，每个1变成2个1，形成一个96位的二进制串。</li> <li>
	 * 2.将上述的96位的二进制串分成3组，每组32位。</li> <li>3.将每个32位的二进制串转换成一个8位的16进制串。</li> <li>
	 * 4.将3个8位的16进制串合并成一个串，中间以","分割。</li>
	 * 
	 * @param timespan
	 *            一个48位的二进制串，如：
	 *            "011111111011111111111111111111111111111111111110"
	 * @return 一个16进制串，每位间以","分割。如："3fffcfff,ffffffff,fffffffc"
	 */
	public static String convertBinaryTime2Hex(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String ret = "";
		String tmp = "";
		for (int i = 0; i < timespan.length(); i++) {
			tmp += timespan.charAt(i);
			tmp += timespan.charAt(i);
			// tmp += i;
			if ((i + 1) % 16 == 0) {
				if (!ret.equals("")) {
					ret += ",";
				}
				Long t = Long.parseLong(tmp, 2);
				String hexStr = Long.toHexString(t);
				if (hexStr.length() < 8) {
					int length = hexStr.length();
					for (int n = 0; n < 8 - length; n++) {
						hexStr = "0" + hexStr;
					}
				}

				ret += hexStr;
				tmp = "";
			}
		}

		return ret;
	}

	/**
	 * 进行时段格式转换，将输入的26位的2进制串转换成48位的二进制串。
	 * 
	 * @param timespan
	 *            一个16进制串，每位间以","分割。如："3fffcfff,ffffffff,fffffffc"
	 * @return 一个48位的二进制串，如："011111111011111111111111111111111111111111111110"
	 */
	public static String convertHexTime2Binary(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String tmp = "";
		String ret = "";
		String[] strArr = timespan.split(",");
		for (int i = 0; i < strArr.length; i++) {
			String binStr = Long.toBinaryString(Long.parseLong(strArr[i], 16));
			if (binStr.length() < 32) {
				int length = binStr.length();
				for (int n = 0; n < 32 - length; n++) {
					binStr = "0" + binStr;
				}
			}
			tmp += binStr;
		}

		for (int i = 0; i < 48; i++) {
			ret += tmp.charAt(i * 2);
		}

		return ret;
	}

	/**
	 * 进行时段格式转换，将输入的32位的10进制串转换成48位的二进制串。
	 * 
	 * @param timespan
	 *            一个16进制串，每位间以","分割。如："1234567890,1234567890,1234567890c"
	 * @return 一个48位的二进制串，如："011111111011111111111111111111111111111111111110"
	 */
	public static String convertDecTime2Binary(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String tmp = "";
		String ret = "";
		String[] strArr = timespan.split(",");
		for (int i = 0; i < strArr.length; i++) {
			String binStr = Long.toBinaryString(Long.parseLong(strArr[i], 10));
			if (binStr.length() < 32) {
				int length = binStr.length();
				for (int n = 0; n < 32 - length; n++) {
					binStr = "0" + binStr;
				}
			}
			tmp += binStr;
		}

		for (int i = 0; i < 48; i++) {
			ret += tmp.charAt(i * 2);
		}

		return ret;
	}

	/**
	 * 进行时段格式转换，对于输入的48位的01串，将进行如下操作： <li>
	 * 1.先将输入中每个0变成两个0，每个1变成2个1，形成一个96位的二进制串。</li> <li>
	 * 2.将上述的96位的二进制串分成3组，每组32位。</li> <li>3.将每个32位的二进制串转换成一个10位的10进制串。</li> <li>
	 * 4.将3个8位的16进制串合并成一个串，中间以","分割。</li>
	 * 
	 * @param timespan
	 *            一个48位的二进制串，如：
	 *            "011111111011111111111111111111111111111111111110"
	 * @return 一个16进制串，每位间以","分割。如："1234567890,1234567890,1234567890"
	 */
	public static String convertBinaryTime2Dec(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String ret = "";
		String tmp = "";
		for (int i = 0; i < timespan.length(); i++) {
			tmp += timespan.charAt(i);
			tmp += timespan.charAt(i);
			// tmp += i;
			if ((i + 1) % 16 == 0) {
				if (!ret.equals("")) {
					ret += ",";
				}
				Long t = Long.parseLong(tmp, 2);
				String decStr = Long.toString(t);
				if (decStr.length() < 10) {
					int length = decStr.length();
					for (int n = 0; n < 10 - length; n++) {
						decStr = "0" + decStr;
					}
				}

				ret += decStr;
				tmp = "";
			}
		}

		return ret;
	}

	/**
	 * 计算指定日期+addMonth月+15号 返回格式"2008-02-15"
	 * 
	 * @param date
	 * @param addMonth
	 * @param monthDay
	 * @return
	 */
	public static String genericSpecdate(Date date, int addMonth, int monthDay) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, addMonth);
		cal.set(Calendar.DAY_OF_MONTH, monthDay);
		return getFormatDate(cal.getTime(), DATE_FORMAT);
	}

	/**
	 * 获得以今天为单位若干天以前或以后的日期的标准格式"Wed Feb 20 00:00:00 CST 2008"，是0点0分0秒。
	 * 
	 * @param idx
	 * @return
	 */
	public static Date getDateBeforeOrAfterV2(int idx) {
		return getDateBeforeOrAfter(getFormatDateToDate(getCurrDate()), idx);
	}

	/**
	 * 获得给定时间若干秒以前或以后的日期的标准格式。
	 * 
	 * @param curDate
	 * @param seconds
	 * @return curDate
	 */
	public static Date getSpecifiedDateTimeBySeconds(Date curDate, int seconds) {
		long time = (curDate.getTime() / 1000) + seconds;
		curDate.setTime(time * 1000L);
		return curDate;
	}

	/**
	 * 获得给定日期当天23点59分59秒的标准格式。
	 * 
	 * @param curDate
	 * @return curDate
	 */
	public static Date getSpecifiedDateTime_235959(Date curDate) {
		return getSpecifiedDateTimeBySeconds(getFormatDateToDate(curDate),
				24 * 60 * 60 - 1);
	}

	public static String getSpecifiedDateTime_month(Date curDate) {
		return getFormatDateTime(curDate, "MM.dd");
	}

	// change by bbq
	public static final String dtSimple = "yyyy-MM-dd";

	// private static final Object[] Date = null;

	/**
	 * alahan add 20050825 获取传入时间相差的日期
	 * 
	 * @param dt
	 *            传入日期，可以为空
	 * @param diff
	 *            需要获取相隔diff天的日期 如果为正则取以后的日期，否则时间往前推
	 * @return
	 */
	public static String getDiffStringDate(Date dt, int diff) {
		Calendar ca = Calendar.getInstance();

		if (dt == null) {
			ca.setTime(new Date());
		} else {
			ca.setTime(dt);
		}

		ca.add(Calendar.DATE, diff);
		return dtSimpleFormat(ca.getTime());
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static final String dtSimpleFormat(Date date) {
		if (date == null) {
			return "";
		}

		return getFormat(dtSimple).format(date);
	}

	// SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final DateFormat getFormat(String format) {
		return new SimpleDateFormat(format,formatlocale);
	}

	/**
	 * 取得多个日期中间隔的最大天数
	 * 
	 * @author Alvise
	 * @param startDateAndEndDate
	 * @return
	 */
	public static int maxContinuousDays(Date[][] startDateAndEndDate) {
		// 冒泡排序
		for (int i = 0; i < startDateAndEndDate.length - 1; i++) {
			for (int j = 0; j < startDateAndEndDate.length - i - 1; j++) {
				if (DateTimeUtil.getDaysBetweenDates(
						startDateAndEndDate[j + 1][0],
						startDateAndEndDate[j][0]) > 0) {
					Date[] tempDate = startDateAndEndDate[j];
					startDateAndEndDate[j] = startDateAndEndDate[j + 1];
					startDateAndEndDate[j + 1] = tempDate;
				}
			}
		}

		// for (int i = 0; i < startDateAndEndDate.length; i++) {
		// if (startDateAndEndDate[i][0] == null)
		// break;
		// System.out.println(DateTimeUtil.getFormatDate(
		// startDateAndEndDate[i][0]) + ","
		// + DateTimeUtil.getFormatDate(startDateAndEndDate[i][1]));
		// }
		//
		// System.out.println(
		// "===========================================");

		// 合并连续的时间段
		int j = 0;
		Date[][] startDateAndEndDateNew = new Date[startDateAndEndDate.length][2];
		for (int i = 0; i < startDateAndEndDateNew.length; i++) {
			if (j >= startDateAndEndDate.length)
				break;

			startDateAndEndDateNew[i] = startDateAndEndDate[j];
			j++;
			while (j < startDateAndEndDate.length) {
				if (DateTimeUtil
						.getDaysBetweenDates(startDateAndEndDateNew[i][1],
								startDateAndEndDate[j][0]) > 0) {
					break;
				} else if (DateTimeUtil
						.getDaysBetweenDates(startDateAndEndDateNew[i][1],
								startDateAndEndDate[j][1]) > 0) {
					startDateAndEndDateNew[i][1] = startDateAndEndDate[j][1];
					j++;
				} else if (DateTimeUtil
						.getDaysBetweenDates(startDateAndEndDateNew[i][1],
								startDateAndEndDate[j][1]) <= 0) {
					j++;
				}

			}
		}

		// for (int i = 0; i < startDateAndEndDateNew.length; i++) {
		// if (startDateAndEndDateNew[i][0] == null)
		// break;
		// System.out.println(DateTimeUtil.getFormatDate(startDateAndEndDateNew[i][0])
		// + ","
		// + DateTimeUtil.getFormatDate(startDateAndEndDateNew[i][1]));
		// }

		// 选择法排序
		int maxDays = 0;
		for (int i = 0; i < startDateAndEndDateNew.length - 1; i++) {
			Date curEndDate = startDateAndEndDateNew[i][1];
			Date nextStartDate = startDateAndEndDateNew[i + 1][0];
			if (curEndDate == null || nextStartDate == null) {
				break;
			}

			int temDays = DateTimeUtil.getDaysBetweenDates(curEndDate,
					nextStartDate);
			if (temDays > maxDays) {
				maxDays = temDays;
			}
		}
		return maxDays;
	}

	/**
	 * 取得多个日期中间隔的最大天数,这里的参数是用 ","和";"分割的字符字符串例如 "2008-08-03,2008-08-04;"
	 * 
	 * @author Alvise
	 * @param startDateAndEndDate
	 * @return
	 */
	public static int maxContinuousDays(String dateStr) {
		String[] seDate = dateStr.split(";");
		Date[][] startDateAndEndDate = new Date[seDate.length][2];

		for (int i = 0; i < seDate.length; i++) {
			String[] tempDate = seDate[i].split(",");
			startDateAndEndDate[i][0] = DateTimeUtil.getFormatDate(tempDate[0]);
			startDateAndEndDate[i][1] = DateTimeUtil.getFormatDate(tempDate[1]);
		}

		return maxContinuousDays(startDateAndEndDate);

	}

	/**
	 * 判断时间段1和时间段2是否有交集
	 * 
	 * @param begintimeOne
	 * @param endtimeOne
	 * @param begintimeTwo
	 * @param endtimeTwo
	 * @return true:有交集,false:没有交集
	 */
	public static boolean isConfilct(String begintimeOne, String endtimeOne,
			String begintimeTwo, String endtimeTwo) {
		Date beginOne = getFormatDate(begintimeOne);
		Date endOne = getFormatDate(endtimeOne);
		Date beginTwo = getFormatDate(begintimeTwo);
		Date endTwo = getFormatDate(endtimeTwo);
		if ((beginOne.compareTo(beginTwo) <= 0 && endOne.compareTo(beginTwo) >= 0)
				|| (beginOne.compareTo(endTwo) <= 0 && endOne.compareTo(endTwo) >= 0)
				|| (beginTwo.compareTo(beginOne) <= 0 && endTwo
						.compareTo(beginOne) >= 0)
				|| (beginTwo.compareTo(endOne) <= 0 && endTwo.compareTo(endOne) >= 0)) {
			return true;
		}
		return false;
	}
	/**
	 * 当期系统的前n天的时间格式
	 * @param length
	 * @return
	 */
	public static String getDateStringBeforeToday(int length){
		long time = System.currentTimeMillis()-length*24*60*60*1000L;
		Date date = new Date(time);
		return getFormatDate(date);
	}
	/**
	 * 获取系统当前时间(精确到时) eg: 2015-09-29 10:00:00
	 */
	public static String getCurrentTimeInHour(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.format(new Date(calendar.getTimeInMillis()));
	}
	/**
	 * 获取当前时间的前/后几个小时(精确到时) 
	 * eg:2015-09-29 10:00:00 
	 * 输入（1） 输出 2015-09-29 11:00:00
	 * 输入（-1）输出 2015-09-29 09:00:00
	 * @param diff
	 * @return
	 */
	public static String getCurrentTimeAfterInHour(int diff){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, hour += diff);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.format(new Date(calendar.getTimeInMillis()));
	}
	/**
	 * 获取任意时间的前/后几个小时(精确到时)
	 * @param time
	 * @param diff
	 * @return
	 */
	public static String getTimeAfterInHour(String time,int diff){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(sdf.parse(time).getTime());
			calendar.set(Calendar.HOUR_OF_DAY, (calendar.get(Calendar.HOUR_OF_DAY))+diff);
			return sdf.format(new Date(calendar.getTimeInMillis()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 获取任意时间的前/后几个小时(精确到时)
	 * @param time
	 * @param diff
	 * @return
	 */
	public static String getTimeAfterInDay(String time,int diff){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(sdf.parse(time).getTime());
			calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR))+diff);
			return sdf.format(new Date(calendar.getTimeInMillis()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
