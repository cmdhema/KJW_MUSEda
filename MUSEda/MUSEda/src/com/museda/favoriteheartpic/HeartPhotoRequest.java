package com.museda.favoriteheartpic;

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

import com.museda.PhotoData;
import com.museda.network.GetNetworkRequest;
import com.museda.network.NetworkConstant;
import com.museda.util.BuildQueryString;

public class HeartPhotoRequest extends GetNetworkRequest<ArrayList<PhotoData>>{

	private String serverURL;
	
	public HeartPhotoRequest(ArrayList<PhotoData> data, PhotoData requestData) {
		super(data);
		serverURL = NetworkConstant.SERVER_URL + "picture/list/myheart?" + BuildQueryString.getFavoriteListQueryString(requestData);
	}
	
	@Override
	public boolean parsingGetRequest(InputStream is, ArrayList<PhotoData> result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Log.i("HeeartPhotoRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			PhotoData resultData = new PhotoData();

			resultData.resultCode = jData.getInt("result");
			resultData.errorCode = jData.getInt("error");
			
			if(resultData.errorCode != 0)
				return false;
			
			JSONArray jsonArray = jData.getJSONArray("data");

			int jsonObjSize = jsonArray.length();

			for (int i = 0; i < jsonObjSize; i++) {

				JSONObject dataObject = jsonArray.getJSONObject(i);
				PhotoData userInfo = new PhotoData();

				userInfo.museIdNum = dataObject.getInt("id");
				userInfo.date = dataObject.getString("hdate");
				
				JSONObject photoObject = dataObject.getJSONObject("picture");
				userInfo.profilephotoPath = photoObject.getString("url");
				userInfo.favoriteFlag = photoObject.getString("favorites");
				userInfo.photoIdNum = photoObject.getInt("id");
				userInfo.photoThumbPath = photoObject.getString("thumb_url");
				userInfo.photoPath = photoObject.getString("url");
				userInfo.photoWidth = photoObject.getInt("width");
				userInfo.photoHeight = photoObject.getInt("height");
				result.add(userInfo);
			}
		} catch (JSONException je) {
			Log.e("getUserToMuseHeartListJSONRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
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

}
