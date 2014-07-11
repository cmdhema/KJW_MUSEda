package com.museda.network;

import java.io.InputStream;
import java.net.URL;

public abstract class GetNetworkRequest<T> extends NetworkRequest{

	private T result;
	private OnGetMethodProcessListener<T> listener;
	
	public abstract boolean parsingGetRequest(InputStream is, T result);
	public abstract URL getServerURL();
	
	public GetNetworkRequest(T data) {
		this.result = data;
	}
	
	@Override
	public URL getURL() {
		return getServerURL();
	}

	@Override
	public boolean parsing(InputStream is) {
		return parsingGetRequest(is, result);
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

	@Override
	public void onSuccess() {
		listener.onGetMethodProcessSuccess(this);
	}

	@Override
	public void onError() {
		listener.onGetMethodProcessError(this);
	}

	public void setOnGetMethodProcessListener(OnGetMethodProcessListener<T> listener) {
		this.listener = listener;
	}
	
	public interface OnGetMethodProcessListener<T> {
		public void onGetMethodProcessSuccess(GetNetworkRequest<T> request);
		public void onGetMethodProcessError(GetNetworkRequest<T> request);
	}

	public T getResult() {
		return result;
	}
	
	
}
