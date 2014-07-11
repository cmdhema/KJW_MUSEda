package com.museda.login;

import java.net.URLEncoder;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.museda.ParentActivity;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
import com.museda.encryption.Encrypt;
import com.museda.main.MuseMainActivity;
import com.museda.main.NormalMainActivity;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.passwordreset.PasswordResetActivity;
import com.museda.util.ErrorDialog;

public class LoginActivity extends ParentActivity implements OnClickListener, OnKeyListener, OnGetMethodProcessListener<UserData> {

	private ProgressDialog dialog;
	
	//Layout View
	private EditText idEt;
	private EditText passwordEt;
	private ImageView loginBtn;
	private TextView passwordLoseBtn;
	
	private LoginRequest request;
	
	private String id;
	private String password;
	
	private LoginData data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_login);

		idEt = (EditText) findViewById(R.id.login_id_et);
		passwordEt = (EditText) findViewById(R.id.login_password_et);
		loginBtn = (ImageView) findViewById(R.id.login_btn);
		passwordLoseBtn = (TextView) findViewById(R.id.lose_text);
		
		idEt.setPrivateImeOptions("defaultInputmode=english;");
		passwordEt.setPrivateImeOptions("defaultInputmode=english;");
		
		passwordEt.setOnKeyListener(this);
		loginBtn.setOnClickListener(this);
		passwordLoseBtn.setOnClickListener(this);
		
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		if(event.getAction()==KeyEvent.ACTION_DOWN)
			if(keyCode == KeyEvent.KEYCODE_ENTER) {
				id = idEt.getText().toString();
				password = passwordEt.getText().toString();

				data = new LoginData(id, password, "0");

				loginToServer(data);
				
				return true;
			}
				
		return false;
	}

	@Override
	public void onClick(View v) {
		if( v == loginBtn) {
			id = idEt.getText().toString();
			password = passwordEt.getText().toString();

			data = new LoginData(id, password, "0");

			loginToServer(data);
		}
		else if (v== passwordLoseBtn) {
			startActivity(new Intent(this, PasswordResetActivity.class));
		}
	}
	
	public void loginToServer(LoginData data) {
		
		request = new LoginRequest(new UserData(), data);
		request.setOnGetMethodProcessListener(this);
		
		NetworkModel.getInstance().getNetworkData(LoginActivity.this, request);
		
		dialog = ProgressDialog.show(this, "로그인 중입니다.", "잠시만 기다려주세요",true);
		
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<UserData> request) {
		Log.i("LoginActivity", "Login Success");
		UserData result = request.getResult();
		
		dialog.dismiss();
		
		SingletonData.getInstance().getUserData().put("UserData", result);
		
		if( result.userType.equals("0")) {
			Intent intent = new Intent(LoginActivity.this, NormalMainActivity.class);
			ComponentName cn = intent.getComponent();
			Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
			startActivity(mainIntent);
		} else {
			Intent intent = new Intent(LoginActivity.this, MuseMainActivity.class);
			ComponentName cn = intent.getComponent();
			Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
			startActivity(mainIntent);
		}

		try {
			SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("login", true);
			editor.putString("id", id);
			editor.putString("pw", URLEncoder.encode(Encrypt.encrypt(password), "UTF-8"));
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finish();
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();
		dialog.dismiss();
	}
}
