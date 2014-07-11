package com.museda.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CountryUtil {
	
	public static String getISO2CountryCode() {
		return Locale.getDefault().getCountry();
	}
	
	public static String getISO2CountryCode(String country) {
		return getCountryListMap().get(country).getCountry();
	}
	
	public static String getISO3CountryCode(String iso2Code) {
		return getCountryListMap().get(iso2Code).getISO3Country();
	}
	
	public static String getCountryFullName(String iso2Code) {
		Locale obj = new Locale("", iso2Code);
		return obj.getDisplayCountry(Locale.US);
	}
	
	public static ArrayList<String> getCountryList() {
		String[] locales = Locale.getISOCountries();
		ArrayList<String> countryList = new ArrayList<String>();
		for ( int i = 0; i< locales.length; i++ ) {
			Locale obj = new Locale("", locales[i]);
			countryList.add(obj.getDisplayCountry(Locale.US));
		}
	
		return countryList;
	}
	
	public static HashMap<String, Locale> getCountryListMap() {
		
		HashMap<String, Locale> map = new HashMap<String, Locale>();
		
		String[] locales = Locale.getISOCountries();
		for ( String code : locales ) {
			Locale obj = new Locale("", code);
			map.put(obj.getDisplayCountry(Locale.US), obj);
		}
		
		return map;
	}
}
