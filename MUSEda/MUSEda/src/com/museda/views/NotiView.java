package com.museda.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.museda.R;
import com.museda.setting.noti.NotiData;
import com.museda.util.TimeUtil;

public class NotiView extends FrameLayout implements OnClickListener {

	private TextView content;
	private TextView title;
	private TextView date;
	
	private boolean isShow;
	
	public NotiView(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.layout_notilist_row, this);
		
		content = (TextView) findViewById(R.id.noti_content);
		title = (TextView) findViewById(R.id.noti_title);
		date = (TextView) findViewById(R.id.noti_date);
		
		title.setOnClickListener(this);
	}
	
	public void setNoti(NotiData data) {
		content.setText(data.content);
		title.setText(data.title);
		date.setText(TimeUtil.getConvertDay(data.date));
	}

	@Override
	public void onClick(View v) {
		
		if(isShow) {
			isShow = false;
			content.setVisibility(View.GONE);
		}
		else {
			isShow = true;
			content.setVisibility(View.VISIBLE);
		}
	}
	
}
