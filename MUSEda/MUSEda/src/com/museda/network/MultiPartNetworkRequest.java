package com.museda.network;

import java.io.InputStream;
import java.net.URL;

import org.apache.http.entity.mime.MultipartEntity;

public abstract class MultiPartNetworkRequest<T> extends NetworkRequest {

	private T result;
	
	private OnMultiPartProcessListener<T> listener;
	
	public abstract MultipartEntity getMultipartEntity();
	public abstract boolean parsingMultiPartRequest(InputStream is, T result);
	public abstract URL getServerURL();
	
	public MultiPartNetworkRequest(T list) {
		this.result = list;
	}
	
	public T getResult() {
		return result;
	}

	public void setOnMultiPartProcessListener(OnMultiPartProcessListener<T> listener) {
		this.listener = listener;
	}

	public interface OnMultiPartProcessListener<T> {
		public void onMultiPartProcessSuccess(MultiPartNetworkRequest<T> request);
		public void onMultiPartProcessError(MultiPartNetworkRequest<T> request);
	}

	@Override
	public URL getURL() {
		return getServerURL();
	}
	
	@Override
	public boolean parsing(InputStream is) {
		return parsingMultiPartRequest(is, result);
	}

	
	@Override
	public String getRequestMethod() {
		return "POST";
	}
	
	@Override
	public void onSuccess() {
		listener.onMultiPartProcessSuccess(this);
	}
	
	@Override
	public void onError() {
		listener.onMultiPartProcessError(this);
	}
}
