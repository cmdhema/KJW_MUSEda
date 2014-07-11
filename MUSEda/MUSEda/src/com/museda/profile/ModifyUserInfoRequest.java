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
import com.museda.network.NetworkConstant;
import com.museda.network.PostNetworkRequest;

public class ModifyUserInfoRequest extends PostNetworkRequest<UserData>{

	private UserData data;
	
	public ModifyUserInfoRequest(UserData data) {
		super(data);
		this.data = data;
	}

	@Override
	public boolean parsingPostReqeuest(InputStream is, UserData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			

		} catch (JSONException je) {
			Log.e("ModifyNormalInfoRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
	}

	@Override
	public StringBuilder getRequestQueryString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("user_id=");
		sb.append(data.userIdNum);
		sb.append("&name=");
		sb.append(data.myName);
		sb.append("&birth=");
		sb.append(data.birth);
		sb.append("&national=");
		sb.append(data.nationalCode);
		sb.append("&introduce=");
		sb.append(data.introduce);
		sb.append("&email=");
		sb.append(data.email);
		
		Log.i("ModifyUserINfoRequest", sb.toString());
		
		return sb;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + "user/edit/default");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
