package com.museda;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.museda.assign.AssignTutorialActivity;
import com.museda.encryption.Encrypt;
import com.museda.experiance.ExperianceActivity;
import com.museda.login.LoginActivity;
import com.museda.login.LoginData;
import com.museda.login.LoginRequest;
import com.museda.main.MuseMainActivity;
import com.museda.main.NormalMainActivity;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.util.ErrorDialog;
import com.museda.util.views.VideoSurfaceView;

public class MainActivity extends ParentActivity implements OnClickListener, OnGetMethodProcessListener<UserData> {

	/*
	 * SharedPreference String id, String pw, boolean tuto, boolean autoLogin
	 */

	// 뒤로가기 두번 눌러서 종료에 쓰이는 변수
	private long m_startTime;
	private long m_endTime;
	private boolean m_isPressedBackButton;

	private ProgressDialog dialog;

	// 배경화면 재생을 위한 SurfaceView
	private VideoSurfaceView videoView;

	// Layout View
	private RelativeLayout videoFrame;
	private RelativeLayout buttonLayout;
	private TextView loginBtn;
	private ImageView experianceBtn;
	private ImageView assignBtn;

	private LoginRequest request;

	private SharedPreferences userSP;
	
	private String userId;
	private String userPw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_intro);

		videoFrame = (RelativeLayout) findViewById(R.id.videolayout);
		buttonLayout = (RelativeLayout) findViewById(R.id.button_layout);
		loginBtn = (TextView) findViewById(R.id.login_btn);
		assignBtn = (ImageView) findViewById(R.id.assign_btn);
		experianceBtn = (ImageView) findViewById(R.id.experiance_btn);

		assignBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		experianceBtn.setOnClickListener(this);

		videoView = new VideoSurfaceView(getApplicationContext(), "MainActivity");
		videoFrame.addView(videoView);
		buttonLayout.bringToFront();

		checkAutoLogin();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!videoView.getmPlayer().isPlaying())
			videoView.getmPlayer().start();

		Log.i("MainActivity", "onResume()");
		buttonLayout.setVisibility(View.VISIBLE);
	}

	public void checkAutoLogin() {

		userSP = getSharedPreferences("info", MODE_PRIVATE);

		try {
			userId = userSP.getString("id", "none");
			userPw = Encrypt.decrypt(URLDecoder.decode(userSP.getString("pw", "none"), "UTF-8"));

			boolean autoLogin = userSP.getBoolean("login", false);
			if (autoLogin) {
				dialog = ProgressDialog.show(this, "로그인 중입니다.", "잠시만 기다려주세요", true);

				loginRequest();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loginRequest() {
		
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				LoginData data = new LoginData(userId, userPw, "0");
				request = new LoginRequest(new UserData(), data);
				request.setOnGetMethodProcessListener(MainActivity.this);
				NetworkModel.getInstance().getNetworkData(MainActivity.this, request);
			}
		};
		
		handler.sendEmptyMessageDelayed(0, 1500);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			startActivity(new Intent(this, LoginActivity.class));
			buttonLayout.setVisibility(View.GONE);
			break;
		case R.id.experiance_btn:
			startActivity(new Intent(this, ExperianceActivity.class));
			break;
		case R.id.assign_btn:
			startActivity(new Intent(this, AssignTutorialActivity.class));
			buttonLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<UserData> request) {
		dialog.dismiss();
		UserData result = request.getResult();

		SingletonData.getInstance().getUserData().put("UserData", result);

		if (result.userType.equals("0")) {
			Intent intent = new Intent(MainActivity.this, NormalMainActivity.class);
			ComponentName cn = intent.getComponent();
			Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
			startActivity(mainIntent);
		} else {
			Intent intent = new Intent(MainActivity.this, MuseMainActivity.class);
			ComponentName cn = intent.getComponent();
			Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
			startActivity(mainIntent);
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("MainActivity", "onRestart()");
		videoView.getmPlayer().start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("MainActivity", "onStop()");
		videoView.getmPlayer().pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.i("MainActivity", "onDestroy()");
		videoView.getmPlayer().stop();
	}

	@Override
	public void onBackPressed() {
		m_endTime = System.currentTimeMillis();

		if (m_endTime - m_startTime > 2000)
			m_isPressedBackButton = false;
		if (m_isPressedBackButton == false) {
			m_isPressedBackButton = true;
			m_startTime = System.currentTimeMillis();
			Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
		} else {
			finish();
			// System.exit(0);
			// android.os.Process.killProcess(android.os.Process.myPid());
//			ActivityManager am  = (ActivityManager)getSystemService(Activity.ACTIVITY_SERVICE);
//		    am.killBackgroundProcesses(getPackageName());
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();
		dialog.dismiss();
	}
}
