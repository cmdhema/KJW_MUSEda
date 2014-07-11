package com.museda.profile;

public class MuseFollowRequestData {


	public int myIDNum;
	public int museIDNum;
	public String flag;
	public int resultCode;
	public int errorCode;
	
	public MuseFollowRequestData(int myIdNum, int museIdNum, String flag) {
		this.myIDNum = myIdNum;
		this.museIDNum = museIdNum;
		this.flag = flag;
	}

	public MuseFollowRequestData() {

	}
}
