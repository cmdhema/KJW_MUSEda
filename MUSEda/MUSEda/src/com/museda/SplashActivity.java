package com.museda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.museda.util.FileUtil;
import com.urqa.clientinterface.URQAController;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_splash);

		URQAController.InitializeAndStartSession(this, "0D097162");

		FileUtil.makeDirectory();
		
		initialize();
		
	}

	private void initialize() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				finish(); // 액티비티 종료
				startActivity(new Intent(SplashActivity.this,MainActivity.class));
			}
		};

		handler.sendEmptyMessageDelayed(0, 2000); // ms, 3초후 종료시킴
	}
	
}
