package com.museda.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkModel {

	// 싱글톤을 위한 instance
	private static NetworkModel instance;

	// 네트워크 요청을 관리할 HashMap
	public HashMap<Context, ArrayList<NetworkRequest>> mRequestMap = new HashMap<Context, ArrayList<NetworkRequest>>();

	// 이미지를 로딩할 쓰레드를 최대 5가까지 설정
	public static final int MAX_THREAD_COUNT = 5;

	// 싱글톤 Instance를 얻음
	public static NetworkModel getInstance() {
		if (instance == null)
			instance = new NetworkModel();

		return instance;
	}

	public NetworkModel() {

	}

	// 서버에서 정보를 얻어오는 요청을 처리하는 쓰레드 실행
	public void getNetworkData(Context context, NetworkRequest request) {
		request.context = context;
		ArrayList<NetworkRequest> list = mRequestMap.get(context);

		if (list == null) { // 현재 네트워크 요청이 없는 경우
			list = new ArrayList<NetworkRequest>();
			mRequestMap.put(context, list);
		}
		list.add(request);

		// 정보를 얻어오는 쓰레드 실행
		new GetInfoAsyncTask(request).execute(request);

	}

	// 서버에 정보를 보내는 요청을 처리하는 쓰레드 실행
	public void sendNetworkData(Context context, NetworkRequest request) {
		request.context = context;
		ArrayList<NetworkRequest> list = mRequestMap.get(context);

		if (list == null) { // 현재 네트워크 요청이 없는 경우
			list = new ArrayList<NetworkRequest>();
			mRequestMap.put(context, list);
		}
		list.add(request);

		// 정보를 보내는 쓰레드 실행
		new SendToServerAsyncTask(request).execute(request);

	}

	public void sendPhotoData(Context context, NetworkRequest request) {
		request.context = context;
		ArrayList<NetworkRequest> list = mRequestMap.get(context);

		if (list == null) { // 현재 네트워크 요청이 없는 경우
			list = new ArrayList<NetworkRequest>();
			mRequestMap.put(context, list);
		}
		list.add(request);

		// 정보를 보내는 쓰레드 실행
		new SendToServerWithMultiPartTask().execute(request);
	}

	public void cleanUpRequest(Context context) {
		ArrayList<NetworkRequest> list = mRequestMap.get(context);
		if (list != null) {
			for (NetworkRequest request : list) {
				request.setCancel();
			}
		}
	}

	private class GetInfoAsyncTask extends AsyncTask<NetworkRequest, Integer, Boolean> {

		NetworkRequest mRequest;

		public GetInfoAsyncTask(NetworkRequest request) {
			mRequest = request;
		}

		@Override
		protected Boolean doInBackground(NetworkRequest... params) {
			NetworkRequest request = params[0];
			mRequest = request;
			URL url = request.getURL();
			int retry = 3;

			// 최대 3회까지 재 접속을 시도한다.
			while (retry > 0) {
				try {
					// GET 방식으로 접속하기 위한 설정
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod(request.getRequestMethod());
					conn.setConnectTimeout(request.getConnectionTimeout());
					conn.setReadTimeout(request.getReadTimeout());
					System.setProperty("http.keepAlive", "false");
					// 사용자가 종룓했을 경우 작업 종료
					if (request.isCancel())
						return false;

					// 실제 접속
					int resCode = conn.getResponseCode();
					if (request.isCancel())
						return false;

					// 접속에 성공했을 경우
					if (resCode == HttpURLConnection.HTTP_OK) {
						InputStream is = conn.getInputStream();

						request.process(is);
						return true;
					} else
						return false;
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 접속 실패했을 경우 재전송 횟수 1 감소
				retry--;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result)
				mRequest.sendResult();
			else
				mRequest.sendError(0);

			ArrayList<NetworkRequest> list = mRequestMap.get(mRequest.context);
			list.remove(mRequest);

			super.onPostExecute(result);
		}

	}
	
	@SuppressWarnings("rawtypes")
	private class SendToServerAsyncTask extends AsyncTask<NetworkRequest, Integer, Boolean> {

		PostNetworkRequest mRequest;

		public SendToServerAsyncTask(NetworkRequest request) {
//			mRequest = request;
		}

		@Override
		protected Boolean doInBackground(NetworkRequest... params) {

			NetworkRequest request = params[0];
			mRequest = (PostNetworkRequest) request;
			URL url = request.getURL();
			int retry = 3;

			// 최대 3회까지 재 접속을 시도한다.
			while (retry > 0) {
				try {
					// POST 방식으로 접속하기 위한 설정
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod(request.getRequestMethod());
					conn.setConnectTimeout(request.getConnectionTimeout());
					conn.setReadTimeout(request.getReadTimeout());
					conn.setDoOutput(true);
					conn.setDoInput(true);
					request.setRequestProertpy(conn);
					request.setOutput(conn);

					DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
					wr.write(mRequest.getRequestQueryString().toString().getBytes());
					wr.flush();
					wr.close();

					// 사용자가 종룓했을 경우 작업 종료
					if (request.isCancel())
						return false;

					// 실제 접속
					int resCode = conn.getResponseCode();
					if (request.isCancel())
						return false;
					// 접속에 성공했을 경우
					if (resCode == HttpURLConnection.HTTP_OK) {
						InputStream is = conn.getInputStream();
						request.process(is);
						Log.i("Send Object", "Success");
						return true;
					} else {
						Log.i("Send Object", resCode + "");
//						Log.e("JsonTest", mObject.toString());
						return false;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 접속 실패했을 경우 재전송 횟수 1 감소
				retry--;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result)
				mRequest.sendResult();
			else
				mRequest.sendError(0);

			ArrayList<NetworkRequest> list = mRequestMap.get(mRequest.context);
			list.remove(mRequest);

			super.onPostExecute(result);
		}
	}

	@SuppressWarnings("rawtypes")
	private class SendToServerWithMultiPartTask extends AsyncTask<NetworkRequest, Integer, Boolean> {

		MultiPartNetworkRequest mRequest;

		@Override
		protected Boolean doInBackground(NetworkRequest... params) {

			NetworkRequest request = params[0];
			mRequest = (MultiPartNetworkRequest) request;
			DefaultHttpClient httpClient = null;

			int retry = 3;

			while (retry > 0) {
				try {
					httpClient = new DefaultHttpClient();
					HttpPost upLoadPost = new HttpPost(mRequest.getServerURL().toString());
					upLoadPost.setHeader("Connection", "Keep-Alive");
					upLoadPost.setHeader("Accept_Charset", "UTF-8");
					upLoadPost.setHeader("enctype", "multipart/form-data");
					MultipartEntity multiEntity = ((MultiPartNetworkRequest) request).getMultipartEntity();

					upLoadPost.setEntity(multiEntity);

					if (request.isCancel())
						return false;

					HttpResponse httpResponse = httpClient.execute(upLoadPost);

					if (request.isCancel())
						return false;

					int resultCode = httpResponse.getStatusLine().getStatusCode();

					if (request.isCancel())
						return false;

					if (resultCode == HttpStatus.SC_OK || resultCode == HttpStatus.SC_ACCEPTED) {
						HttpEntity responseBody = null;
						responseBody = httpResponse.getEntity();
						InputStream is = responseBody.getContent();
						request.process(is);
						
						Log.i("HttpHelperHandlerClass", "업로드 성공");

						return true;
					} else {
						Log.i("HttpHelperHandlerClass", "업로드 실패" + resultCode);
						return false;
					}

				} catch (Exception e) {
					Log.e("UPLOAD", "업로드중 문제 발생", e);
				} finally {
					httpClient.getConnectionManager().shutdown();
				}

				retry--;

			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result)
				mRequest.sendResult();
			else
				mRequest.sendError(0);

			ArrayList<NetworkRequest> list = mRequestMap.get(mRequest.context);
			list.remove(mRequest);

			super.onPostExecute(result);
		}
	}
}
