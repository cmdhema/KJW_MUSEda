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

public class ModifyNormalInfoRequest extends PostNetworkRequest<UserData>{

	private UserData data;
	
	public ModifyNormalInfoRequest(UserData data) {
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
//		Log.i("ModifyNormalInfoRequest", jsonBuf.toString());
		
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
		sb.append("&job=");
		sb.append(data.job);
		sb.append("&school=");
		sb.append(data.school);
		sb.append("&hobby=");
		sb.append(data.hobby);
		sb.append("&enjoy=");
		sb.append(data.enjoy);
		sb.append("&boast=");
		sb.append(data.boast);
		
		return sb;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL +"user/edit/detail");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
}
