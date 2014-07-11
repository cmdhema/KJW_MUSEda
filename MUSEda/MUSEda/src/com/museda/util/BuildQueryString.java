package com.museda.util;

import com.museda.PhotoData;
import com.museda.UserData;
import com.museda.favoriteheartpic.HeartFavoriteData;
import com.museda.follow.UserListData;

public class BuildQueryString {
	
	public static StringBuilder sendFavoriteQueryString(HeartFavoriteData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(data.myIdNum);
		sb.append("&picture_id=");
		sb.append(data.photoIdNum);
		sb.append("&favorites=");
		sb.append(data.flag);
		
		return sb;
	}
	
	public static StringBuilder getMusePhotoQueryString(PhotoData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(data.myIdNum);
		sb.append("&muse_id=");
		sb.append(data.museIdNum);
		sb.append("&count=");
		sb.append(data.count);
		sb.append("&direction=");
		sb.append(data.direction);
		sb.append("&picture_id=");
		sb.append(data.startPicNum);
		sb.append("&include_not_approve=");
		sb.append(data.flag);
		
		return sb;
	}
	
	public static StringBuilder getFollowPhotoQueryString(PhotoData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(data.myIdNum);
		sb.append("&count=");
		sb.append(data.count);
		sb.append("&direction=");
		sb.append(data.direction);
		sb.append("&picture_id=");
		sb.append(data.startPicNum);
		sb.append("&include_not_approve=");
		sb.append(data.flag);
		
		return sb;
	}
	
	public static StringBuilder getFollowerListQueryString(UserListData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("muse_id=");
		sb.append(data.myIdNum);
		sb.append("&count=");
		sb.append(data.count);
		sb.append("&direction=");
		sb.append(data.direction);
		sb.append("&id=");
		sb.append(data.userIdNum);
		return sb;
	}
	
	public static StringBuilder getSearchListQueryString(UserListData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("find_name=");
		sb.append(data.userId);
		sb.append("&count=");
		sb.append(data.count);
		sb.append("&jump_count=");
		sb.append(data.jumpCount);
		return sb;
	}
	
	public static StringBuilder getFollowingListQueryString(UserListData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(data.myIdNum);
		sb.append("&count=");
		sb.append(data.count);
		sb.append("&jump_count=");
		sb.append(data.jumpCount);
		return sb;
	}
	
	public static StringBuilder getFavoriteListQueryString(PhotoData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(data.myIdNum);
		sb.append("&count=");
		sb.append(data.count);
		sb.append("&jump_count=");
		sb.append(data.jumpCount);
		return sb;
	}
	
	public static StringBuilder getUserDataQueryString(UserData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("call_id=");
		sb.append(data.myIDNum);
		sb.append("&user_id=");
		sb.append(data.userIdNum);
		return sb;
	}
	
	public static StringBuilder getRecvHeartUserQueryString(PhotoData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("muse_id=");
		sb.append(data.myIdNum);
		sb.append("&picture_id=");
		sb.append(data.photoIdNum);
		sb.append("&count=");
		sb.append(data.count);
		sb.append("&jump_count=");
		sb.append(data.jumpCount);
		return sb;		
	}
	
	public static StringBuilder getHeartUserListQueryString(PhotoData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("muse_id=");
		sb.append(data.myIdNum);
		sb.append("&count=");
		sb.append(data.count);
//		sb.append("&date=");
//		sb.append(data.date);
		sb.append("&time_zone=");
		sb.append(TimeUtil.getTimeZone());
		return sb;		
	}
	
	
	
}
