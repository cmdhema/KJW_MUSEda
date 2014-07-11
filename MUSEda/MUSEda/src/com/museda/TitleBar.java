package com.museda;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBar extends RelativeLayout implements OnClickListener{

	public ImageView backBtn;
	public ImageView doneBtn;
	public ImageView searchBtn;
	public ImageView settingBtn;

	public ImageView titleBtn;
	public TextView titleText;
	
	Context mContext;
	RelativeLayout layout;

	public TitleBar(Context context) {
		super(context);
		init(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		
		mContext = context;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.layout_titlebar, this,true);
		
		titleText = (TextView) findViewById(R.id.title_tv);
		layout = (RelativeLayout) findViewById(R.id.layout);
		backBtn = (ImageView) findViewById(R.id.btn_back);
		doneBtn = (ImageView) findViewById(R.id.btn_done);
		settingBtn = (ImageView) findViewById(R.id.btn_setting);
		searchBtn = (ImageView) findViewById(R.id.btn_search);
		
		titleBtn = (ImageView) findViewById(R.id.iv_title_bar);
		
		backBtn.setOnClickListener(this);
		doneBtn.setOnClickListener(this);
	}
	
	public void setBackGround() {
		layout.setBackgroundResource(R.color.gray);
	}
	
	public void setTitleImage(int resourceId) {
		titleBtn.setBackgroundResource(resourceId);
	}
			
	public void setTitleText(String title) {
		titleText.setText(title);
	}
	
	public void setTitleBtnVisible(boolean isVisible) {
		if(isVisible)
			titleBtn.setVisibility(View.VISIBLE);
		else
			titleBtn.setVisibility(View.INVISIBLE);
	}	
	
	public void setBackBtnVisible(boolean isVisible) {
		if(isVisible)
			backBtn.setVisibility(View.VISIBLE);
		else
			backBtn.setVisibility(View.INVISIBLE);
	}
	
	public void setDoneBtnVisible(boolean isVisible) {
		if(isVisible)
			doneBtn.setVisibility(View.VISIBLE);
		else
			doneBtn.setVisibility(View.INVISIBLE);
	}
	
	public void setSearchBtnVisible(boolean isVisible) {
		if(isVisible)
			searchBtn.setVisibility(View.VISIBLE);
		else
			searchBtn.setVisibility(View.INVISIBLE);
	}
	
	public void setSettingBtnVisible(boolean isVisible) {
		if(isVisible)
			settingBtn.setVisibility(View.VISIBLE);
		else
			settingBtn.setVisibility(View.INVISIBLE);
	}
	
	public ImageView getDoneBtnView(){
		return doneBtn;
	}
	
	public ImageView getBackBtnView(){
		return backBtn;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_back:
			( (Activity) mContext).finish();
			((Activity)mContext).overridePendingTransition(R.anim.from_left_out,R.anim.from_left_in );
			break;
		case R.id.btn_done:
			( (Activity) mContext).finish();
			((Activity)mContext).overridePendingTransition(R.anim.from_top_out,R.anim.from_top_in );
			break;
		}
	}

}
