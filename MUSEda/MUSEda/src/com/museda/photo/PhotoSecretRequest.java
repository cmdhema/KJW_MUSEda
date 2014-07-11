package com.museda.photo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.museda.PhotoData;
import com.museda.network.NetworkConstant;
import com.museda.network.PostNetworkRequest;

public class PhotoSecretRequest  extends PostNetworkRequest<PhotoData> {

	private PhotoData data;
	
	public PhotoSecretRequest(PhotoData list) {
		super(list);
		this.data = list;
		
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(	NetworkConstant.SERVER_URL + "picture/secret");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public boolean parsingPostReqeuest(InputStream is, PhotoData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Log.i("PhotoSecretRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			

		} catch (JSONException je) {
			Log.e("PhotoSecretRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
	}


	@Override
	public StringBuilder getRequestQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(data.museIdNum);
		sb.append("&picture_id=");
		sb.append(data.photoIdNum);
		sb.append("&secret=");
		sb.append(data.flag);
		
		return sb;
	}
}
