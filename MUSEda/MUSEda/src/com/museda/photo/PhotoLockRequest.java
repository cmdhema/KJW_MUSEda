package com.museda.photo;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.museda.PhotoData;
import com.museda.network.NetworkConstant;
import com.museda.network.PostNetworkRequest;

public class PhotoLockRequest extends PostNetworkRequest<PhotoData>{

	private PhotoData data;
	
	public PhotoLockRequest(PhotoData data) {
		super(data);
		this.data = data;
	}

	@Override
	public boolean parsingPostReqeuest(InputStream is, PhotoData result) {
		return true;
	}

	@Override
	public StringBuilder getRequestQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append("user_id=");
		sb.append(data.myIdNum);
		sb.append("&picture_id=");
		sb.append(data.photoIdNum);
		sb.append("&secret=");
		sb.append(data.flag);
		
		return sb;
	}

	@Override
	public URL getServerURL() {
		try {
			return new URL(NetworkConstant.SERVER_URL + "picture/secret");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
