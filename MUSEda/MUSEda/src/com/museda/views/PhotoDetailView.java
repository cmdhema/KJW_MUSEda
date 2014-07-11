package com.museda.views;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.util.TimeUtil;

public class PhotoDetailView {

	AQuery aq;
	
	public PhotoDetailView(AQuery aq) {
		this.aq = aq;
	}

	public void setPhotoView(PhotoData data) {
		
		aq.id(R.id.museview_heartnum_tv).text("¢¾" + data.heartCount);
		aq.id(R.id.museview_day_tv).text(TimeUtil.getPastTime(data.date));

		if (data.recvHeartFlag == 0) {
			aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
		} else {
			aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.btn_heart_selector);		
		}
	}

	public void setHeartRecvPhoto(ArrayList<PhotoData> userList, ArrayList<ImageView> photoViewList, int heartCount) {

		aq.id(R.id.heart_iv).visibility(View.VISIBLE);
		aq.id(R.id.heartnum_tv).visibility(View.VISIBLE);
		aq.id(R.id.heartnum_tv).text(heartCount + "");

		for (int i = 0; i < 7; i++) 
			aq.id(photoViewList.get(i)).visibility(View.INVISIBLE);

		if( heartCount > 6 )
			aq.id(photoViewList.get(6)).visibility(View.VISIBLE);
		
		for (int i = 0; i < userList.size(); i++) {
			
			if ( i == 6 )
				break;
			
			aq.id(photoViewList.get(i)).visibility(View.VISIBLE);
			if (userList.get(i).profilePhotoThumbPath.length() > 0)
				aq.id(photoViewList.get(i)).image(userList.get(i).profilePhotoThumbPath);
			else {
				aq.id(photoViewList.get(i)).image(R.drawable.signin_man_btn);
			}
		}
		
	}
}
