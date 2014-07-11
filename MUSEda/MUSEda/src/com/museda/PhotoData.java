package com.museda;

import java.util.ArrayList;

import android.os.Environment;

public class PhotoData {

	public static final String imageDir = Environment.getExternalStorageDirectory() + "/MUSEda";
	
	//Request Query
	public int myIdNum;
	public int count;
	public int startPicNum;
	public String direction;
	public String flag;
	public int jumpCount;
	
	//Result Data
	public int resultCode;
	public int errorCode;
	public String date;
	public String museAccount;
	public String museName;
	public int heartCount;
	public int approve;
	public int recvHeartFlag;
	public int secretFlag;
	public int todayCount;
	public String favoriteFlag;

	//Result Muse Data
	public int museIdNum;
	public String profilephotoPath;
	public String profilePhotoThumbPath;
	
	//Result Picture Data
	public int photoIdNum;
	public String photoPath;
	public String photoThumbPath;
	public int photoWidth;
	public int photoHeight;
	
	//Result Data
	public int senderId;
	public String userType;
	
	public String sendPhotoPath;
	
	//Result MuseHeartActivity
	public ArrayList<PhotoData> sendHeartUserList;
	
	
	public PhotoData() {
		sendHeartUserList = new ArrayList<PhotoData>();
	}
	
	public PhotoData(int id, String account) {
		this.myIdNum = id;
		this.museAccount = account;
	}
	
	public PhotoData(int myId, int count, int jumpCount) {
		this.myIdNum = myId;
		this.count = count;
		this.jumpCount = jumpCount;
	}
	
	public PhotoData(int museId, int count, String date) {
		myIdNum = museId;
		this.count = count;
		this.date = date;
	}
	
	public PhotoData(int myId, int count, int photoNum, String direction) {
		this.myIdNum = myId;
		this.count = count;
		this.startPicNum = photoNum;
		this.direction = direction;
	}
	
	public PhotoData(int myId, int count, int photoNum, int jumpCount) {
		this.myIdNum = myId;
		this.count = count;
		this.photoIdNum = photoNum;
		this.jumpCount = jumpCount;
	}
	
		
	public PhotoData(int myId, int count, int photoNum, String direciton, String flag) {
		this.myIdNum = myId;
		this.count = count;
		this.startPicNum = photoNum;
		this.direction = direciton;
		this.flag = flag;
	}
	
	public PhotoData(int myId, int museId, int count, int photoNum, String direciton, String flag) {
		this.myIdNum = myId;
		this.museIdNum = museId;
		this.count = count;
		this.startPicNum = photoNum;
		this.direction = direciton;
		this.flag = flag;
	}

	//사진 비공개 전환 생성자
	public PhotoData(int id, int photoId) {
		this.myIdNum = id;
		this.photoIdNum = photoId;
	}
}
