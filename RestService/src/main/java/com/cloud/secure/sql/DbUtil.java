package com.cloud.secure.sql;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.cloud.secure.pojo.searchresp.Image;
import com.cloud.secure.pojo.searchresp.ImageSet;

public class DbUtil {

	public static String getRandomId() {
		return UUID.randomUUID().toString();
	}

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static Timestamp convertStringToSql(final String dateStr)
			throws ParseException {
		return new Timestamp(format.parse(dateStr).getTime());
	}

	public static String convertTimestampToStringWithoutTime(final Timestamp ts) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH);
	}

	public static String convertTimestampToString(final Timestamp ts) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR)
				+ ":" + cal.get(Calendar.MINUTE) + ":"
				+ cal.get(Calendar.SECOND);
	}

	public static boolean isDifferentDay(final Timestamp ts1,
			final Timestamp ts2) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts1.getTime());
		final int day1 = cal.get(Calendar.DAY_OF_MONTH), month1 = cal
				.get(Calendar.MONTH), year1 = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(ts2.getTime());
		final int day2 = cal.get(Calendar.DAY_OF_MONTH), month2 = cal
				.get(Calendar.MONTH), year2 = cal.get(Calendar.YEAR);
		return (day1 != day2 || month1 != month2 || year1 != year2);

	}
	
	public static List<ImageSet> cloneImageSet(final List<ImageSet> list){
		List<ImageSet> clonedList=new ArrayList<ImageSet>();
		for(final ImageSet item:list)
			clonedList.add(item);
		return clonedList;
	}
	
	public static List<Image> cloneImage(final List<Image> list){
		List<Image> clonedList=new ArrayList<Image>();
		for(final Image item:list)
			clonedList.add(item);
		return clonedList;
	}
	
	public static Timestamp parseTimeStamp(final String ...key) throws ParseException{
		final String dateStr=key[0]+"-"+key[1]+"-"+key[2]+" "+key[3]+":"+key[4]+":"+key[5];
		return new Timestamp(format.parse(dateStr).getTime());
	}

}
