package com.museda.heart;

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

public class HeartCountRequest extends GetNetworkRequest<ArrayList<PhotoData>>{

//	private PhotoData requestData;
	private String serverURL;
	
	public HeartCountRequest(ArrayList<PhotoData> data, PhotoData requestData) {
		super(data);
//		this.requestData = requestData;
		
		serverURL = NetworkConstant.SERVER_URL + "muse/list/date/heartuser25?" + BuildQueryString.getHeartUserListQueryString(requestData);
		
		Log.i("HeartCountRequest",serverURL);
		
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
		
//		Log.i("HeartCountRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			
			PhotoData resultData = new PhotoData();
			resultData.resultCode = jData.getInt("result");
			resultData.errorCode = jData.getInt("error");

			if(resultData.errorCode != 0)
				return false;
			
			JSONArray jsonArray = jData.getJSONArray("data");

//			result.add(resultData);

			int jsonObjSize = jsonArray.length();
			
			for (int i = 0; i < jsonObjSize; i++) {

				JSONObject dataObject = jsonArray.getJSONObject(i);
				PhotoData dataResult = new PhotoData();

				dataResult.date = dataObject.getString("date");
				dataResult.heartCount = dataObject.getInt("count");

				JSONArray dataArray = dataObject.getJSONArray("user");
				int userObjSize = dataArray.length();
				
				dataResult.sendHeartUserList = new ArrayList<PhotoData>();
				
				for(int j=0;j<userObjSize;j++) {
					PhotoData userInfo = new PhotoData();
					JSONObject userObject = dataArray.getJSONObject(j);
					userInfo.senderId = userObject.getInt("id");
					userInfo.userType = userObject.getString("user_type");
					userInfo.profilePhotoThumbPath = userObject.getString("thumb_url");
					
					dataResult.sendHeartUserList.add(userInfo);
				}
				
				result.add(dataResult);
				
			}
		} catch (JSONException je) {
			Log.e("HeartCountRequest", "JSON파싱중 에러 발생", je);
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
