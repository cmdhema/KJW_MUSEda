package com.museda.favoriteheartpic;

public class HeartFavoriteData {

	public int resultCode;
	public int errorCode;
	
	public int myIdNum;
	public int museIdNum;
	public int photoIdNum;
	public String flag;
	
	public HeartFavoriteData() {
		
	}
	
	public HeartFavoriteData(int myId, int museId, int pictureId, String heartFlag) {
		myIdNum = myId;
		museIdNum = museId;
		photoIdNum = pictureId;
		flag = heartFlag;
	}
	
	public HeartFavoriteData(int myId, int pictureId, String heartFlag) {
		myIdNum = myId;
		photoIdNum = pictureId;
		flag = heartFlag;
	}	
	
}
