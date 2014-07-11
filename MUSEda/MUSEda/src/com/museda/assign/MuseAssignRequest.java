package com.museda.assign;

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
import com.museda.encryption.Encrypt;
import com.museda.network.MultiPartNetworkRequest;
import com.museda.network.NetworkConstant;

public class MuseAssignRequest extends MultiPartNetworkRequest<UserData> {

	private UserData data;

	public MuseAssignRequest(UserData list) {
		super(list);
		this.data = list;
	}

	@Override
	public MultipartEntity getMultipartEntity() {

		MultipartEntity multiEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, null);
		File file = new File(data.profilePhotoPath);
		FileBody fileBody = new FileBody(file);
		Charset chars = Charset.forName("UTF-8");

		try {
			multiEntity.addPart("show_id", new StringBody(data.myAccount, chars));
			multiEntity.addPart("pw", new StringBody(Encrypt.encrypt(data.password), chars));
//			multiEntity.addPart("pw", new StringBody(data.password, chars));
			multiEntity.addPart("name", new StringBody(data.myName, chars));
			multiEntity.addPart("user_type", new StringBody(data.userType + "", chars));
			multiEntity.addPart("national", new StringBody(data.nationalCode, chars));
			multiEntity.addPart("email", new StringBody(data.email, chars));
			multiEntity.addPart("phone_type", new StringBody("0", chars));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		multiEntity.addPart("profile_picture", fileBody);

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
		Log.i("MuseAssignRequest", jsonBuf.toString());
		
		try {
			JSONObject jData = new JSONObject(jsonBuf.toString());
			
			result.resultCode = jData.getInt("result");
			result.errorCode = jData.getInt("error");
			
			if(result.errorCode != 0)
				return false;
			
		} catch (JSONException je) {
			Log.e("MuseAssignRequest", "JSON파싱중 에러 발생", je);
		}
		
		return true;
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
