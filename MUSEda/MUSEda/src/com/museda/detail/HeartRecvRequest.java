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

public class HeartRecvRequest extends GetNetworkRequest<ArrayList<PhotoData>> {

	private String serverURL;

	public HeartRecvRequest(ArrayList<PhotoData> data, PhotoData requestData) {
		super(data);
		serverURL = NetworkConstant.SERVER_URL + "muse/list/heartuser?" + BuildQueryString.getRecvHeartUserQueryString(requestData);
	}

	@Override
	public boolean parsingGetRequest(InputStream is, ArrayList<PhotoData> result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf = new StringBuilder();
		String line = "";

		try {
			while ((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		Log.i("HeartRecvRequest",jsonBuf.toString());

		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			PhotoData resultData = new PhotoData();

			resultData.resultCode = jData.getInt("result");

			if ( jData.getInt("error") != 0 )
				return false;
			

			JSONArray jsonArray = jData.getJSONArray("data");

			int jsonObjSize = jsonArray.length();

			for (int i = 0; i < jsonObjSize; i++) {

				JSONObject dataObject = jsonArray.getJSONObject(i);
				PhotoData userInfo = new PhotoData();

				userInfo.senderId = dataObject.getInt("id");
				userInfo.profilePhotoThumbPath = dataObject.getString("thumb_url");
				userInfo.userType = dataObject.getString("user_type");
				result.add(userInfo);
			}
		} catch (JSONException je) {
			Log.e("HeartRecvRequest", "JSON파싱중 에러 발생", je);
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
