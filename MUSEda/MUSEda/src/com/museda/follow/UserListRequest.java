package com.museda.follow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.museda.network.GetNetworkRequest;
import com.museda.network.NetworkConstant;
import com.museda.util.BuildQueryString;

public class UserListRequest extends GetNetworkRequest<ArrayList<UserListData>>{

	private String serverURL;

	public UserListRequest(ArrayList<UserListData> data, UserListData requestData, String flag) {
		super(data);
		
		serverURL = NetworkConstant.SERVER_URL + flag + "?"+ BuildQueryString.getFollowerListQueryString(requestData);
			
		Log.i("UserListRequest",serverURL);
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
	public boolean parsingGetRequest(InputStream is, ArrayList<UserListData> result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		Log.i("UserListRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			UserListData resultInfo = new UserListData();
			resultInfo.resultCode = jData.getInt("result");
			resultInfo.errorCode = jData.getInt("error");
			
			if ( resultInfo.errorCode != 0)
				return false;
//			jsonAllList.add(resultInfo);

			JSONArray jsonArray = jData.getJSONArray("data");

			int jsonObjSize = jsonArray.length();

			for (int i = 0; i < jsonObjSize; i++) {
				JSONObject dataObject = jsonArray.getJSONObject(i);
				UserListData userInfo = new UserListData();

				userInfo.myIdNum = dataObject.getInt("id");
				userInfo.userIdNum = dataObject.getInt("user_id");
				userInfo.userAccount = dataObject.getString("show_id");
				userInfo.userName = dataObject.getString("name");
				userInfo.userType = dataObject.getString("user_type");
				userInfo.date = dataObject.getString("date");
				userInfo.national = dataObject.getString("national");
				userInfo.introduce = dataObject.getString("introduce");
				
				JSONObject photoObject = dataObject.getJSONObject("picture");
				userInfo.profilePhoto = photoObject.getString("url");
				userInfo.profileThumbPhoto = photoObject.getString("thumb_url");

				result.add(userInfo);
			}
		} catch (JSONException je) {
			Log.e("getSearchViewMuseInfoJSONRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;

	}
}
