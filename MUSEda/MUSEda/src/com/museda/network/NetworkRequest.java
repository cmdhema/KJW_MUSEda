package com.museda.network;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public abstract class NetworkRequest {

	public Context context;
	public HttpURLConnection conn;
	public boolean isCanceled;

	private boolean mResult;

	public abstract URL getURL();

	public abstract boolean parsing(InputStream is);

	public abstract String getRequestMethod();

	public abstract void onSuccess();

	public abstract void onError();

	public int getConnectionTimeout() {
		return 30000;
	}

	public int getReadTimeout() {
		return 30000;
	}

	public void setRequestHeader(HttpURLConnection conn) {

	}

	public void setOutput(HttpURLConnection conn) {
		conn.setDoOutput(true);
		conn.setDoInput(true);
	}

	public void setRequestProertpy(HttpURLConnection conn) {
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	}

	public void process(InputStream is) {
		mResult = parsing(is);
	}

	public void setCancel() {
		isCanceled = true;
		if (conn != null)
			conn.disconnect();
	}

	public void sendResult() {
		if (isCanceled)
			return;
		if (mResult)
			onSuccess();
		else
			onError();
	}

	public void sendError(int code) {
		if (isCanceled)
			return;
		if (!mResult)
			onError();
	}

	public boolean isCancel() {
		return isCanceled;
	}
}
