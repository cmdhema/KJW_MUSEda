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

import com.museda.network.NetworkConstant;
import com.museda.network.PostNetworkRequest;

public class MuseFollowRequest extends PostNetworkRequest<MuseFollowRequestData>{

	private MuseFollowRequestData requestData;
	private String query;
	
	public MuseFollowRequest(MuseFollowRequestData data, MuseFollowRequestData requestData, String query) {
		super(data);
		this.requestData = requestData;
		this.query = query;
	}

	@Override
	public boolean parsingPostReqeuest(InputStream is, MuseFollowRequestData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Log.i("MuseFollowRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			

		} catch (JSONException je) {
			Log.e("MuseFollowRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
	}

	@Override
	public StringBuilder getRequestQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(requestData.myIDNum);
		sb.append("&muse_id=");
		sb.append(requestData.museIDNum);
		sb.append("&action=");
		sb.append(requestData.flag);
		Log.i("MuseFollowRequest", sb.toString());
		return sb;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + query);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
