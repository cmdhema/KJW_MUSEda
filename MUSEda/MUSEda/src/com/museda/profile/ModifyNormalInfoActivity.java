package com.museda.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.museda.ParentActivity;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.util.ErrorDialog;

public class ModifyNormalInfoActivity extends ParentActivity implements OnClickListener, OnPostMethodProcessListener<UserData> {

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");
	
	private TitleBar header;
	
	private EditText jobEt;
	private EditText schoolEt;
	private EditText hobbyEt;
	private EditText likeEt;
	private EditText skillEt;

	private TextView jobTextNumTv;
	private TextView schoolTextNumTv;
	private TextView hobbyTextNumTv;
	private TextView likeTextNumTv;
	private TextView skillTextNumTv;
	
	private String job;
	private String school;
	private String hobby;
	private String like;
	private String skill;
	
	private ImageView saveBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_modifynormalinfo);

		header = (TitleBar) findViewById(R.id.titlebar);
		jobEt = (EditText) findViewById(R.id.modify_job_et);
		schoolEt = (EditText) findViewById(R.id.modify_school_et);
		hobbyEt = (EditText) findViewById(R.id.modify_hobby_et);
		likeEt = (EditText) findViewById(R.id.modify_like_et);
		skillEt = (EditText) findViewById(R.id.modify_skill_et);
		jobTextNumTv = (TextView) findViewById(R.id.modify_job_textlength_tv);
		schoolTextNumTv = (TextView) findViewById(R.id.modify_school_textlength_tv);
		hobbyTextNumTv = (TextView) findViewById(R.id.modify_hobby_textlength_tv);
		likeTextNumTv = (TextView) findViewById(R.id.modify_like_textlength_tv);
		skillTextNumTv = (TextView) findViewById(R.id.modify_skill_textlength_tv);
		saveBtn = (ImageView) findViewById(R.id.modify_save_btn);
		
		header.setTitleText("My Profile");
	
		jobEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int textLength = jobEt.getText().toString().length();
				if(textLength > 40) {
					showModifyDialog("40자를 넘길 수 없습니다.");
				} else 
					jobTextNumTv.setText(textLength + "");
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		schoolEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int textLength = schoolEt.getText().toString().length();
				if(textLength > 40) {
					showModifyDialog("40자를 넘길 수 없습니다.");
				} else 
					schoolTextNumTv.setText(textLength + "");
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		hobbyEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int textLength = hobbyEt.getText().toString().length();
				if(textLength > 40) {
					showModifyDialog("40자를 넘길 수 없습니다.");
				} else 
					hobbyTextNumTv.setText(textLength + "");
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		likeEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int textLength = likeEt.getText().toString().length();
				if(textLength > 40) {
					showModifyDialog("40자를 넘길 수 없습니다.");
				} else 
					likeTextNumTv.setText(textLength + "");
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		skillEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int textLength = skillEt.getText().toString().length();
				if(textLength > 40) {
					showModifyDialog("40자를 넘길 수 없습니다.");
				} else 
					skillTextNumTv.setText(textLength + "");
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		
		saveBtn.setOnClickListener(this);
		
		setUserInfo();
	}

	private void setUserInfo() {
		jobEt.setText(userData.job);
		schoolEt.setText(userData.school);
		hobbyEt.setText(userData.hobby);
		likeEt.setText(userData.enjoy);
		skillEt.setText(userData.boast);
	}

	public void showModifyDialog(String message) {
		new AlertDialog.Builder(ModifyNormalInfoActivity.this).setTitle("정보 수정").setMessage(message).setNeutralButton("확인", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
			
		}).show();
	}

	@Override
	public void onClick(View v) {
		if( v == saveBtn) {
			job = jobEt.getText().toString();
			school = schoolEt.getText().toString();
			hobby = hobbyEt.getText().toString();
			like = likeEt.getText().toString();
			skill = skillEt.getText().toString();
			
			if ( job.length() < 1)
				job = "비공개 입니다.";
			if ( school.length() < 1)
				school = "비공개 입니다.";
			if ( hobby.length() < 1)
				hobby = "비공개 입니다.";
			if ( like.length() < 1)
				like = "비공개 입니다.";
			if ( skill.length() < 1)
				skill = "비공개 입니다.";
			
			UserData modifyData = new UserData(userData.myIDNum, job, school, hobby, like, skill);
			ModifyNormalInfoRequest request = new ModifyNormalInfoRequest(modifyData);
			request.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, request);
		}
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		showModifyDialog("정보 수정 성공");
		
		userData.job = job;
		userData.school = school;
		userData.hobby = hobby;
		userData.enjoy = like;
		userData.boast = skill;
		
		UserData.profileModify = true;
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();		
	}

	
}
