package com.museda.follow;

public class UserListData {

	//Request Query
	public int myIdNum;
	public int count;
	public String direction;
	public int jumpCount;
	
	public int resultCode;
	public int errorCode;
	
	public String userType="1";
	public int userIdNum;
	public String userAccount;
	public String userId;
	
	public String userName;
	public String date;
	public String national;
	
	public String introduce;
	public String profilePhoto;
	public String profileThumbPhoto;
	
	public UserListData() {

	}

	public UserListData(int myId, int count, int jumpCount) {
		this.myIdNum = myId;
		this.count = count;
		this.jumpCount = jumpCount;
	}
	
	public UserListData(String museId, int count, int jumpCount) {
		this.userId = museId;
		this.count = count;
		this.jumpCount = jumpCount;
	}
	
	
	public UserListData(int myId, int count, int userId, String direction) {
		this.myIdNum = myId;
		this.userIdNum = userId;
		this.count = count;
		this.direction = direction;
	}
	
	
}
