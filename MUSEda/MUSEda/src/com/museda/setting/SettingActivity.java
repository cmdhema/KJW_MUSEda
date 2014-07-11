package com.museda.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.museda.ParentActivity;
import com.museda.R;
import com.museda.TitleBar;
import com.museda.setting.account.AccountSettingActivity;
import com.museda.setting.noti.NotiListActivity;
import com.museda.setting.tutorial.TutorialActivity;
import com.museda.setting.version.VersionActivity;
import com.museda.util.ResultDialog;

public class SettingActivity extends ParentActivity implements OnClickListener {

	private TextView noti;
	private TextView center;
	private TextView help;
	private TextView version;
	private TextView intro;
	private TextView account;
	private TextView library;
	private TextView helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_setting);
		
		TitleBar header;
		noti = (TextView)findViewById(R.id.noti_tv);
		center = (TextView)findViewById(R.id.center_tv);
		help = (TextView)findViewById(R.id.help_tv);
		version = (TextView)findViewById(R.id.version_tv);
		intro = (TextView)findViewById(R.id.intro_tv);
		account = (TextView)findViewById(R.id.account_tv);
		library = (TextView)findViewById(R.id.library_tv);
		helper = (TextView)findViewById(R.id.helper_tv);
		
		header = (TitleBar) findViewById(R.id.titlebar);
		
		header.setTitleText("설정");
		
		noti.setOnClickListener(this);
		center.setOnClickListener(this);
		help.setOnClickListener(this);
		version.setOnClickListener(this);
		intro.setOnClickListener(this);
		account.setOnClickListener(this);
		library.setOnClickListener(this);
		helper.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if (v==noti) {
			startActivity(new Intent(this, NotiListActivity.class));
		} else if ( v == center) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData((Uri.fromParts("mailto", "cs@museda.com", null)));
			startActivity(intent);
		} else if ( v == help) {
			startActivity(new Intent(this, TutorialActivity.class));
		} else if ( v == version) {
			startActivity(new Intent(this, VersionActivity.class));
		} else if ( v == intro) {
			startActivity(new Intent(this, IntroMovieActivity.class));
		} else if ( v == account) {
			startActivity(new Intent(this, AccountSettingActivity.class));
		} else if ( v == library) {
			new ResultDialog(this, "오픈소스", getResources().getString(R.string.opensource)).show();
		} else if ( v == helper) {
			new ResultDialog(this, "도움을 준 사람", getResources().getString(R.string.assistance)).show();	
		}
		
	}

	
}
