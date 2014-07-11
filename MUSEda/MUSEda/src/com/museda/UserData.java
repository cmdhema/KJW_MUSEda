package com.museda;


public class UserData {

	public static long lastSetHeartedTime;
	
	public static boolean profileModify = false;
	public static boolean photoModify = true;
	
	//Request ����
	public int userIdNum;
	
	
	//Result Code
	public int resultCode;
	public int errorCode;
	
	public int myIDNum;
	public String myAccount;
	public String myName;
	public String password;
	public String userType;
	public String nationalCode;
	public String city;
	public String birth = "����� �Դϴ�.";
	public String email;
	public String job = "����� �Դϴ�.";
	public String school = "����� �Դϴ�.";
	public String hobby = "����� �Դϴ�.";
	public String enjoy = "����� �Դϴ�.";
	public String boast = "����� �Դϴ�.";
	public String introduce = "�ȳ��ϼ���.";
	public int myMUSEIDNum;
	public String followFlag;
	
	public int followingCount;
	public int followerCount;
	public int favoriteCount;
	public int sendHeartCount;
	public int recvHeartCount;
	public int recvMUSECount;
	public int todayCount;
	
	public String myPhoto1Path;
	public String myPhoto1ThumbPhotoPath;
	public String myPhoto2Path;
	public String myPhoto2ThumbPhotoPath;
	public String myPhoto3Path;
	public String myPhoto3ThumbPhotoPath;
	public String coverPhotoPath;
	public String profilePhotoPath;
	public String profileThumbPhotoPath;
	public String museProfilePhotoPath;
	public String museProfileThumbPhotoPath;
	
	public int photoIndex;
	public String photoFlag;
	public String photoPath;
	public String resultPhotoPath;
	public String resultPhotoThumbPath;
	
	public String assignDate;
	
	//Ʃ�丮�� �ó� �Ⱥó�
	public int tutoFlag;
	
	public String newPassword;


	public UserData() {

	}
	
	public UserData(int myId, int userId) {
		this.myIDNum = myId;
		this.userIdNum = userId;
	}
	
	//��й�ȣ ����
	public UserData (String id, String oldPw, String newPw) {
		this.myAccount = id;
		this.password = oldPw;
		this.newPassword = newPw;
	}

	//ȸ������ ������
	public UserData ( String id, String pw, String name, String email, String country, String photoUri, String userType) {
		this.myAccount = id;
		this.password = pw;
		this.myName = name;
		this.email = email;
		this.nationalCode = country;
		this.profilePhotoPath = photoUri;
		this.userType = userType;
	}
	
	//ȸ�� ���� ���� ������
	public UserData ( int id, String name, String birth, String national, String introduce) {
		this.userIdNum = id;
		this.myName = name;
		this.birth = birth;
		this.nationalCode = national;
		this.introduce = introduce;
	}
	
	//�Ϲ� ȸ�� ������ ���� ������
	public UserData (int id, String job, String school, String hobby, String like, String skill){
		this.userIdNum = id;
		this.school = school;
		this.job = job;
		this.hobby = hobby;
		this.enjoy = like;
		this.boast = skill;
	}
	
	//������ ���� ���� ������
	public UserData(int id, int index, String flag) {
		this.userIdNum = id;
		this.photoIndex = index;
		this.photoFlag = flag;
	}
	
	//������, Ŀ�� ���� ���ε� ������
	public UserData(int id, int index, String flag, String imagePath) {
		this.userIdNum = id;
		this.photoIndex = index;
		this.photoFlag = flag;
		this.photoPath = imagePath;
	}
	
	//Ŀ������ ������ ������
	public UserData(int id, String flag) {
		this.userIdNum = id;
		this.photoFlag = flag;
	}

	//���� ���ε�
	public UserData(int myIDNum, String myAccount, String imagePath) {
		this.myIDNum = myIDNum;
		this.myAccount = myAccount;
		this.photoPath = imagePath;
	}
}
