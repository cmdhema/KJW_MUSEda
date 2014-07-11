package com.museda.setting.noti;

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

import com.museda.network.GetNetworkRequest;
import com.museda.network.NetworkConstant;

public class NotiRequest extends GetNetworkRequest<ArrayList<NotiData>>{

	public NotiRequest(ArrayList<NotiData> data) {
		super(data);
	}

	@Override
	public boolean parsingGetRequest(InputStream is, ArrayList<NotiData> result) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Log.i("MuseSearchRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			NotiData resultData = new NotiData();

			resultData.resultCode = jData.getInt("result");
			resultData.errorCode = jData.getInt("error");
			
			if ( resultData.errorCode != 0)
				return false;
//			result.add(resultData);

			JSONArray jsonArray = jData.getJSONArray("data");

			int jsonObjSize = jsonArray.length();
			
			for (int i = 0; i < jsonObjSize; i++) {

				JSONObject dataObject = jsonArray.getJSONObject(i);
				NotiData resultContentData = new NotiData();

				resultContentData.id = dataObject.getInt("noti_id");
				resultContentData.date = dataObject.getString("ndate");
				resultContentData.title = dataObject.getString("title");
				resultContentData.content = dataObject.getString("content");
				
				result.add(resultContentData);

			}
		} catch (JSONException je) {
			Log.e("NotiRequest", "JSON파싱중 에러 발생", je);
		}

		return true;
	}	

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + "noti/list");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
