package com.museda.util;

import android.util.Log;


public class DateUtil {

	public static String getMonth(int month) {
		if ( month == 1)
			return "January";
		else if ( month == 2)
			return "February";
		else if ( month == 3)
			return "March";
		else if ( month == 4)
			return "April";
		else if ( month == 5)
			return "May";
		else if ( month == 6)
			return "June";
		else if ( month == 7)
			return "July";
		else if ( month == 8)
			return "August";
		else if ( month == 9)
			return "September";
		else if ( month == 10)
			return "October";
		else if ( month == 11)
			return "November";
		else 
			return "December";
	}
	
	public static int getMonth(String date) {
		
		if ( date.startsWith("January"))
			return 1;
		else if ( date.startsWith("February"))
			return 2;
		else if ( date.startsWith("March"))
			return 3;
		else if ( date.startsWith("April"))
			return 4;
		else if ( date.startsWith("May"))
			return 5;
		else if ( date.startsWith("June"))
			return 6;
		else if ( date.startsWith("July"))
			return 7;
		else if ( date.startsWith("August"))
			return 8;
		else if ( date.startsWith("September"))
			return 9;
		else if ( date.startsWith("October"))
			return 10;
		else if ( date.startsWith("November"))
			return 11;
		else
			return 12;
	}
	
	public static int getDay(String date) {
		return Integer.parseInt(date.substring(8, 10));
	}
	
	public static String getBirthDate(int m, int d) {
		String month;
		String day;
		
		if( m < 10 ) 
			month = "0" + m;
		else 
			month = m + "";
		
		if( d < 10 )
			day = "0" + d;
		else
			day = d+"";
		
		return "1999" + "-" + month + "-" + day;
	}
	
	public static String getDisplayBirthDate(String birth) {
		Log.i("DateUtil", birth);
		String month = getMonth(Integer.parseInt(birth.substring(5, 7)));
		String day = birth.substring(8, 10);
		
		return month + " " + day;
	}
	
}
