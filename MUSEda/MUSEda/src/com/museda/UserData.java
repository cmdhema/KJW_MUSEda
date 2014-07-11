package com.museda;


public class UserData {

	public static long lastSetHeartedTime;
	
	public static boolean profileModify = false;
	public static boolean photoModify = true;
	
	//Request 변수
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
	public String birth = "비공개 입니다.";
	public String email;
	public String job = "비공개 입니다.";
	public String school = "비공개 입니다.";
	public String hobby = "비공개 입니다.";
	public String enjoy = "비공개 입니다.";
	public String boast = "비공개 입니다.";
	public String introduce = "안녕하세요.";
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
	
	//튜토리올 봤나 안봤나
	public int tutoFlag;
	
	public String newPassword;


	public UserData() {

	}
	
	public UserData(int myId, int userId) {
		this.myIDNum = myId;
		this.userIdNum = userId;
	}
	
	//비밀번호 변경
	public UserData (String id, String oldPw, String newPw) {
		this.myAccount = id;
		this.password = oldPw;
		this.newPassword = newPw;
	}

	//회원가입 생성자
	public UserData ( String id, String pw, String name, String email, String country, String photoUri, String userType) {
		this.myAccount = id;
		this.password = pw;
		this.myName = name;
		this.email = email;
		this.nationalCode = country;
		this.profilePhotoPath = photoUri;
		this.userType = userType;
	}
	
	//회원 정보 수정 생성자
	public UserData ( int id, String name, String birth, String national, String introduce) {
		this.userIdNum = id;
		this.myName = name;
		this.birth = birth;
		this.nationalCode = national;
		this.introduce = introduce;
	}
	
	//일반 회원 상세정보 수정 생성자
	public UserData (int id, String job, String school, String hobby, String like, String skill){
		this.userIdNum = id;
		this.school = school;
		this.job = job;
		this.hobby = hobby;
		this.enjoy = like;
		this.boast = skill;
	}
	
	//프로필 사진 삭제 생성자
	public UserData(int id, int index, String flag) {
		this.userIdNum = id;
		this.photoIndex = index;
		this.photoFlag = flag;
	}
	
	//프로필, 커버 사진 업로드 생성자
	public UserData(int id, int index, String flag, String imagePath) {
		this.userIdNum = id;
		this.photoIndex = index;
		this.photoFlag = flag;
		this.photoPath = imagePath;
	}
	
	//커버사진 삭제용 생성자
	public UserData(int id, String flag) {
		this.userIdNum = id;
		this.photoFlag = flag;
	}

	//사진 업로드
	public UserData(int myIDNum, String myAccount, String imagePath) {
		this.myIDNum = myIDNum;
		this.myAccount = myAccount;
		this.photoPath = imagePath;
	}
}
