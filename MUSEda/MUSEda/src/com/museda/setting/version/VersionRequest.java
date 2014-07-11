package com.museda.setting.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.museda.network.GetNetworkRequest;
import com.museda.network.NetworkConstant;

public class VersionRequest extends GetNetworkRequest<AppData>{

	public VersionRequest(AppData data) {
		super(data);
	}

	@Override
	public boolean parsingGetRequest(InputStream is, AppData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf = new StringBuilder();
		String line = "";
		
		try {
			while ( ( line = br.readLine()) != null )
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.i("VersionRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			
			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			JSONObject versionData = jData.getJSONObject("data");
			
			result.versionCode = versionData.getString("app_version");

			if ( result.errorCode != 0)
				return false;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return true;
		
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + "server/info");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
