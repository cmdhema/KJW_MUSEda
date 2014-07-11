package com.museda.detail;

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

public class HeartMoreRequest extends GetNetworkRequest<ArrayList<PhotoData>>{

	private String serverURL;
	
	public HeartMoreRequest(ArrayList<PhotoData> data, PhotoData requestData) {
		super(data);
		
		serverURL = NetworkConstant.SERVER_URL + "muse/list/heartuser?" + BuildQueryString.getRecvHeartUserQueryString(requestData);
	}

	@Override
	public boolean parsingGetRequest(InputStream is, ArrayList<PhotoData> result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf = new StringBuilder();
		String line = "";
		
		try {
			while ( ( line = br.readLine() ) != null )
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i("HeartMoreRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			PhotoData data = new PhotoData();
			data.resultCode = jData.getInt("result");
			data.errorCode = jData.getInt("error");
			
			if ( data.errorCode != 0)
				return false;
			
			JSONArray photoDataList = jData.getJSONArray("data");
			for ( int i = 0; i < photoDataList.length(); i++) {
				JSONObject photoData = photoDataList.getJSONObject(i);
				PhotoData objData = new PhotoData();
				objData.userType = photoData.getString("user_type");
				objData.senderId = photoData.getInt("id");
				objData.profilePhotoThumbPath = photoData.getString("thumb_url");
				
				result.add(objData);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
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
