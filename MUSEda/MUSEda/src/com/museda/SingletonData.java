package com.museda;

import java.util.HashMap;

public class SingletonData {

	private static SingletonData instance = null;
	
	private HashMap<String, UserData> userData;

	private SingletonData() {
		userData = new HashMap<String, UserData>();
	}
	
	public static SingletonData getInstance(){
		if( null == instance )
			instance = new SingletonData();
		return instance;
	}

	public HashMap<String, UserData> getUserData() {
		return userData;
	}
	
}
