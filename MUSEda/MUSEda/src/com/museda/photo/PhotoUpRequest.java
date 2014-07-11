package com.museda.photo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.museda.UserData;
import com.museda.network.MultiPartNetworkRequest;
import com.museda.network.NetworkConstant;

public class PhotoUpRequest extends MultiPartNetworkRequest<UserData> {

	private UserData data;
	
	public PhotoUpRequest(UserData list) {
		super(list);
		this.data = list;
	}

	@Override
	public MultipartEntity getMultipartEntity() {

		MultipartEntity multiEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, null);
		File file = new File(data.photoPath);
		FileBody fileBody = new FileBody(file);
		Charset chars = Charset.forName("UTF-8");

		try {
			multiEntity.addPart("show_id", new StringBody(data.myAccount, chars));
			multiEntity.addPart("muse_id", new StringBody(data.myIDNum+"", chars));
			multiEntity.addPart("picture", fileBody);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return multiEntity;
	}

	@Override
	public boolean parsingMultiPartRequest(InputStream is, UserData result) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonBuf =  new StringBuilder();
		String line = "";
		
		try {
			while((line = br.readLine()) != null)
				jsonBuf.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Log.i("PhotoUpRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());

			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			

		} catch (JSONException je) {
			Log.e("PhotoUpRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + "picture/add");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
