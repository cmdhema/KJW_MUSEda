package com.museda.network;

import java.io.InputStream;
import java.net.URL;

public abstract class PostNetworkRequest<T> extends NetworkRequest{

	private T result;
	
	private OnPostMethodProcessListener<T> listener;
	
	public abstract boolean parsingPostReqeuest(InputStream is, T result);
	public abstract StringBuilder getRequestQueryString();
	public abstract URL getServerURL();

	
	public T getResult() {
		return result;
	}
	
	public PostNetworkRequest(T data) {
		this.result = data;
	}

	@Override
	public URL getURL() {
		return getServerURL();
	}

	@Override
	public boolean parsing(InputStream is) {
		return parsingPostReqeuest(is, result);
	}

	@Override
	public String getRequestMethod() {
		return "POST";
	}

	@Override
	public void onSuccess() {
		listener.onPostMethodProcessSuccess(this);
	}

	@Override
	public void onError() {
		listener.onPostMethodProcessError(this);
	}
	
	public void setOnPostMethodProcessListener(OnPostMethodProcessListener<T> listener) {
		this.listener = listener;
	}

	public interface OnPostMethodProcessListener<T> {
		public void onPostMethodProcessSuccess(PostNetworkRequest<T> request);
		public void onPostMethodProcessError(PostNetworkRequest<T> request);
	}
}
