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

public class PhotoRemoveRequest extends PostNetworkRequest<PhotoData> {

	private PhotoData data;
	
	public PhotoRemoveRequest(PhotoData list) {
		super(list);
		this.data = list;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + "picture/delete");
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
//		Log.i("PhotoRemoveRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			

		} catch (JSONException je) {
			Log.e("PhotoRemoveRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
	}


	@Override
	public StringBuilder getRequestQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append("muse_id=");
		sb.append(data.myIdNum);
		sb.append("&picture_id=");
		sb.append(data.photoIdNum);
		
		return sb;
	}

}
