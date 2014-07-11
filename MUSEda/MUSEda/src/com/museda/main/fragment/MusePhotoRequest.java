package com.museda.main.fragment;

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

public class MusePhotoRequest extends GetNetworkRequest<ArrayList<PhotoData>> {

	private String serverURL;

	public MusePhotoRequest(ArrayList<PhotoData> data, PhotoData requestData) {
		super(data);
		serverURL = NetworkConstant.SERVER_URL + "picture/list/mymuse?" + BuildQueryString.getMusePhotoQueryString(requestData); 
		
		Log.i("MusePhotoRequest", serverURL);
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
//		Log.i("MusePhotoRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			PhotoData resultInfo = new PhotoData();
			resultInfo.resultCode = jData.getInt("result");
			resultInfo.errorCode = jData.getInt("error");
			
			if(resultInfo.errorCode != 0)
				return false;
			
//			userDataList.add(resultInfo);

			JSONArray jsonArray = jData.getJSONArray("data");

			int jsonObjSize = jsonArray.length();

			for (int i = 0; i < jsonObjSize; i++) {
				JSONObject dataObject = jsonArray.getJSONObject(i);
				PhotoData userInfo = new PhotoData();

				userInfo.museAccount = (dataObject.getString("show_id"));
				userInfo.museName = (dataObject.getString("name"));
				userInfo.date = (dataObject.getString("pdate"));
				userInfo.heartCount = (dataObject.getInt("heart_count"));
				userInfo.recvHeartFlag = (dataObject.getInt("my_heart"));
				userInfo.approve = dataObject.getInt("approve");
				userInfo.secretFlag = dataObject.getInt("secret");
				userInfo.todayCount = dataObject.getInt("today_count");
				
				
				JSONObject museObject = dataObject.getJSONObject("muse");
				userInfo.museIdNum = (museObject.getInt("id"));
				userInfo.profilephotoPath = (museObject.getString("url"));
				userInfo.profilePhotoThumbPath = (museObject.getString("thumb_url"));

				JSONObject photoObject = dataObject.getJSONObject("picture");
				userInfo.photoIdNum = (photoObject.getInt("id"));
				userInfo.photoPath = (photoObject.getString("url"));
				userInfo.photoThumbPath = (photoObject.getString("thumb_url"));
				userInfo.photoWidth = photoObject.getInt("width");
				userInfo.photoHeight = photoObject.getInt("height");

				result.add(userInfo);
			}
		} catch (JSONException je) {
			Log.e("getSearchViewMuseInfoJSONRequest", "JSON파싱중 에러 발생", je);
		}

		return true;
	}

}
