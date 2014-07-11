package com.museda.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class TimeUtil {

	public static String getConvertDay(String oriDay) {
		Log.i("getConverDay", "서버시간 : " + oriDay);
		String photoDay = getLocaleTime(oriDay).substring(0, 10);
		String convertDay = photoDay.substring(2, 10).replace('-', '.');

		return convertDay;
	}

	public static String getMuseMyPhotoDayInfo(String oriDay) {
		Log.i("getMuseMyPhotoDayInfo", "서버시간 : " + oriDay);
//		String photoTime = getLocaleTime(oriDay).substring(11);
		String photoTime = oriDay.substring(11);
		String converTime = photoTime.substring(0, 5);
		String indicator;
		String lastConverTime;
		String tempHour = converTime.substring(0, 2);
		int hour = Integer.parseInt(tempHour);

		if (hour > 25) {
			for (int temp = 25; temp < 32; temp++) {
				if (temp == hour) {
					hour = temp - 24;
					break;
				}
			}
		}

		if (hour < 12) {
			indicator = "am";
			lastConverTime = "at " + converTime + indicator;
		} else {
			String convertHour = String.valueOf(hour);
			indicator = "pm";
			if (hour >= 13) {
				hour -= 12;
				convertHour = String.valueOf(hour);
				if (convertHour.length() == 1) {
					convertHour = "0" + convertHour;
				}
			}
			lastConverTime = "at " + convertHour + ":" + photoTime.substring(3, 5) + indicator;

		}

		// Log.i("TimeTest", "Ori : " + oriDay + "TempHour : " + tempHour +
		// "ParseHour : " + hour + " Indicator : " + indicator + "Last : " +
		// lastConverTime);
		return lastConverTime;
	}

	public static String getTimeZone() {

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
		Date currentLocalTime = calendar.getTime();
		DateFormat date = new SimpleDateFormat("Z");
		String localTime = date.format(currentLocalTime);
		return localTime.substring(0, 3) + ":" + localTime.substring(3, 5);
	}
	
	public static String getPastTime(String time) {
		
		Log.i("TimeUtil", "서버 시간  : " + time);
		
		DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		DateFormat timeDiffFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		
		String localeTime;
		String currentTime;
//		String diffTime;
//		int pastDay;
//		int pastHour;
//		int pastMin;
		
		try {

			localeTime = time;
//			localeTime = getLocaleTime(time);
		
			currentTime = parser.format(System.currentTimeMillis());
			
//			diffTime = timeDiffFormat.format(parser.parse(currentTime).getTime() - parser.parse(localeTime).getTime());
//			Log.i("TimeUtil", "변환 현재 시간  : " + parser.parse(currentTime));
//			Log.i("TimeUtil", "변환 이전 시간  : " + parser.parse(localeTime));
			
//			Log.i("TimeUtil", "시간 차이  : " + (parser.parse(currentTime).getTime() - parser.parse(localeTime).getTime() ));
//			
//			Log.i("TimeUtil", "경과 시간  : " + diffTime);
//			Log.i("TimeUtil", "경과 시간  : " + diffTime.substring(0, 4) + "년 " + diffTime.substring(5, 7) + "월 " + diffTime.substring(8, 10) +"일 " + diffTime.substring(11, 13)+"시" + diffTime.substring(14, 16)+"분");
//			
//			pastDay = Integer.parseInt(diffTime.substring(8, 10));
//			pastHour = Integer.parseInt(diffTime.substring(11, 13));
//			pastMin = Integer.parseInt(diffTime.substring(14, 16));
			
			long diffSec = parser.parse(currentTime).getTime() - parser.parse(localeTime).getTime();
			
			if ( diffSec/1000 > 60*60*24*2 )
				return getConvertDay(time);
			else if ( diffSec/1000 > 60*60*24 )
				return "1 day ago";
			else if ( diffSec/(1000*60*60) > 0 && diffSec/(1000*60*60) < 24 )
				return diffSec/(1000*60*60) + "hours ago";
			else
				return diffSec/(1000*60) + "minutes ago";
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "오류";

	}

	public static String getLocaleTime(String oriTime) {
		
		SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		serverDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date date;
		String localeTime;
		
		try {
			date = serverDateFormat.parse(oriTime);
		
			TimeZone destTz = TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
			serverDateFormat.setTimeZone(destTz);
			localeTime = serverDateFormat.format(date);
			
			Log.i("TimeUtil", "getLocaleTime : " + localeTime);
		
			return localeTime;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "오류";
	}	
}
