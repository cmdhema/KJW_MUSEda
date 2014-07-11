package com.museda.profile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.museda.UserData;
import com.museda.network.GetNetworkRequest;
import com.museda.network.NetworkConstant;
import com.museda.util.BuildQueryString;

public class UserDataRequest extends GetNetworkRequest<UserData> {

	private String serverURL;
	
	public UserDataRequest(UserData data, UserData requestData) {
		super(data);
		serverURL = NetworkConstant.SERVER_URL + "user/get?" + BuildQueryString.getUserDataQueryString(requestData);
		
		Log.i("UserDataRequest",serverURL);
	}


	@Override
	public URL getServerURL() {
		try {
			return new URL(serverURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean parsingGetRequest(InputStream is, UserData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Log.i("LoginRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if ( result.errorCode != 0)
				return false;
			
			JSONObject dataObject = jData.getJSONObject("data");
			result.userType = dataObject.getString("user_type");
			result.myAccount = dataObject.getString("show_id");
			result.myName = (dataObject.getString("name"));
			result.nationalCode = dataObject.getString("national");
			result.birth = dataObject.getString("birth");
			result.email = (dataObject.getString("email"));
			result.introduce = (dataObject.getString("introduce"));
			result.job = (dataObject.getString("job"));
			result.school = (dataObject.getString("school"));
			result.hobby = (dataObject.getString("hobby"));
			result.enjoy = (dataObject.getString("enjoy"));
			result.boast = (dataObject.getString("boast"));
			result.myMUSEIDNum = (dataObject.getInt("my_muse"));
			result.followFlag = dataObject.getString("my_following");
			result.followingCount = (dataObject.getInt("following_count"));
			result.followerCount = (dataObject.getInt("follower_count"));
			result.favoriteCount = (dataObject.getInt("favorites_count"));
			result.sendHeartCount = (dataObject.getInt("heart_count"));
			result.recvHeartCount = (dataObject.getInt("heart_recv_count"));
			result.recvMUSECount = (dataObject.getInt("muse_count"));
			result.todayCount = dataObject.getInt("today_count");
			result.coverPhotoPath = dataObject.getString("cover_path");
			
			JSONObject profilePhotoObject = dataObject.getJSONObject("delegate");
			result.profilePhotoPath = (profilePhotoObject.getString("url"));
			result.profileThumbPhotoPath = (profilePhotoObject.getString("thumb_url"));

			JSONObject myPhoto1Object = dataObject.getJSONObject("picture1");
			result.myPhoto1Path = (myPhoto1Object.getString("url"));
			result.myPhoto1ThumbPhotoPath = (myPhoto1Object.getString("thumb_url"));

			JSONObject myPhoto2Object = dataObject.getJSONObject("picture2");
			result.myPhoto2Path = (myPhoto2Object.getString("url"));
			result.myPhoto2ThumbPhotoPath = (myPhoto2Object.getString("thumb_url"));

			JSONObject myPhoto3Object = dataObject.getJSONObject("picture3");
			result.myPhoto3Path = (myPhoto3Object.getString("url"));
			result.myPhoto3ThumbPhotoPath = (myPhoto3Object.getString("thumb_url"));
			
		} catch (JSONException je) {
			Log.e("UserDataRequest", "JSON파싱중 에러 발생", je);
		}

		return true;
	}

}
