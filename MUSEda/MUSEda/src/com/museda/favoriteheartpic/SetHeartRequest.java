package com.museda.favoriteheartpic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.museda.network.NetworkConstant;
import com.museda.network.PostNetworkRequest;

public class SetHeartRequest extends PostNetworkRequest<HeartFavoriteData> {

	private String serverURL;
	private HeartFavoriteData requestData;
	
	public SetHeartRequest(HeartFavoriteData data, String flag) {
		super(data);
		serverURL = NetworkConstant.SERVER_URL + "picture/" + flag;
		this.requestData = data;
		
		Log.i("SetHeartRequest", serverURL);
	}


	@Override
	public boolean parsingPostReqeuest(InputStream is, HeartFavoriteData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Log.i("SetHeartRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			

		} catch (JSONException je) {
			Log.e("SetHeartRequest", "JSON파싱중 에러 발생", je);
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


	@Override
	public StringBuilder getRequestQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(requestData.myIdNum);
		sb.append("&muse_id=");
		sb.append(requestData.museIdNum);
		sb.append("&picture_id=");
		sb.append(requestData.photoIdNum);
		sb.append("&heart=");
		sb.append(requestData.flag);
		
		return sb;
	}


}