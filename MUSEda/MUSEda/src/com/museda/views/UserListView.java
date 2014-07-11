package com.museda.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.R;
import com.museda.follow.UserListData;
import com.museda.util.views.RoundedImageView;

public class UserListView extends FrameLayout {

	private AQuery aq;
	private RoundedImageView profileIv;
	private TextView nameTv;
	private TextView idTv;
	private TextView nationalTv;
	
	public UserListView(Context context) {
		super(context);
		aq = new AQuery(context);
		
		LayoutInflater.from(context).inflate(R.layout.layout_userlist_row, this);
		
		profileIv = (RoundedImageView)findViewById(R.id.userlist_profile_iv);
		nameTv = (TextView) findViewById(R.id.userlist_name_tv);
		idTv = (TextView) findViewById(R.id.userlist_id_tv);
		nationalTv = (TextView) findViewById(R.id.userlist_national_tv);
		
	}

	public void setView(UserListData data) {
		
		aq.id(profileIv).image(data.profileThumbPhoto);
		aq.id(nameTv).text(data.userName);
		aq.id(idTv).text(data.userAccount);
		aq.id(nationalTv).text("Seoul, Korea");
	}
}
