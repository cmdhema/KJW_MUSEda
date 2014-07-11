package com.museda.setting.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.museda.MainActivity;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;

public class AccountSettingActivity extends FragmentActivity implements OnClickListener {

	private UserData userInfo = SingletonData.getInstance().getUserData().get("UserData");

	private TitleBar header;
	private TextView nameTv;
	private TextView emailTv;
	private TextView pwChangeTv;
	private TextView nameSetTv;
	private TextView emailSetTv;
	private Button logoutBtn;
	private TextView signOutTv;
	
	private Fragment editFragment;
	private Fragment pwFragment;

	private Bundle flagBundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_account);
	
		header = (TitleBar) findViewById(R.id.titlebar);
		nameTv = (TextView) findViewById(R.id.account_name_tv);
		nameSetTv = (TextView) findViewById(R.id.account_name_set_tv);
		emailTv = (TextView) findViewById(R.id.account_email_tv);
		emailSetTv = (TextView) findViewById(R.id.account_email_set_tv);
		pwChangeTv = (TextView) findViewById(R.id.account_pwchange_tv);
		logoutBtn = (Button) findViewById(R.id.account_logout_btn);
		signOutTv = (TextView) findViewById(R.id.account_signout_tv);
		
		nameTv.setOnClickListener(this);
		emailTv.setOnClickListener(this);
		pwChangeTv.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		signOutTv.setOnClickListener(this);
		
		header.setBackBtnVisible(true);
		header.setDoneBtnVisible(false);
		header.setTitleText("계정");
		nameSetTv.setText(userInfo.myName);
		emailSetTv.setText(userInfo.email);
		
		flagBundle = new Bundle();
		
		if (null == editFragment)
			editFragment = new AccountEditFragment();
		if (null == pwFragment)
			pwFragment = new AccountPwEditFragment();
	}

	@Override
	public void onClick(View v) {
		if ( v == nameTv) {
			flagBundle.putString("flag", "이름");
			editFragment.setArguments(flagBundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.account_container, editFragment).commit();
			
		} else if (  v == emailTv) {
			flagBundle.putString("flag", "이메일");
			editFragment.setArguments(flagBundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.account_container, editFragment).commit();
		} else if (v == pwChangeTv) {
			getSupportFragmentManager().beginTransaction().replace(R.id.account_container, pwFragment).commit();
		} else if ( v == signOutTv) {
			showSignOutDialog();
		} else if ( v == logoutBtn ) {
			showLogOutDialog();
		}
	}

	private void showLogOutDialog() {
		new AlertDialog.Builder(this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putBoolean("login", false);
				editor.commit();
				Intent intent = new Intent(AccountSettingActivity.this, MainActivity.class);
				if ( Build.VERSION.SDK_INT < 11)
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				else
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
				UserData.photoModify = true;
				UserData.profileModify = false;
			}
		}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).show();
	}

	private void showSignOutDialog() {
		new AlertDialog.Builder(this).setTitle("정말 뮤즈다를 탈퇴하시겠습니까?").setMessage("회원님의 모든 정보가 삭제됩니다. 정말 뮤즈다를 탈퇴하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//TODO 탈퇴 코드 넣기
				SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("id", "none");
				editor.putString("pw", "none");
				editor.putBoolean("login", false);
				editor.commit();
				Intent intent = new Intent(AccountSettingActivity.this, MainActivity.class);
				if ( Build.VERSION.SDK_INT < 11)
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				else
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				UserData.photoModify = true;
				UserData.profileModify = false;
				
			}
		}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).show();
	}

	
}
