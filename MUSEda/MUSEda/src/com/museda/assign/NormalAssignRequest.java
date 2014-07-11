package com.museda.assign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.museda.UserData;
import com.museda.encryption.Encrypt;
import com.museda.network.NetworkConstant;
import com.museda.network.PostNetworkRequest;

public class NormalAssignRequest extends PostNetworkRequest<UserData>{

	private UserData data;
	
	public NormalAssignRequest(UserData data) {
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
		Log.i("NormalAssignRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			

		} catch (JSONException je) {
			Log.e("NormalAssignRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
	}

	@Override
	public StringBuilder getRequestQueryString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("show_id=");
			sb.append(data.myAccount);
			sb.append("&pw=");
			sb.append(URLEncoder.encode(Encrypt.encrypt(data.password), "UTF-8"));
	//		sb.append(data.password);
			sb.append("&name=");
			sb.append(data.myName);
			sb.append("&user_type=");
			sb.append(data.userType);
			sb.append("&national=");
			sb.append(data.nationalCode);
			sb.append("&email=");
			sb.append(data.email);
			sb.append("&phone_type=");
			sb.append("0");
		} catch (Exception e) {
			e.printStackTrace();
		}
			return sb;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + "user/new");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
