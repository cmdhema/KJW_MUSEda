package com.museda.views;

import java.util.ArrayList;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.util.TimeUtil;

public class PopularTopView {

	private Animation fromRight;
	private Animation fromLeft;
	private Animation fadeIn;
	
	private AQuery aq;
	
	public PopularTopView(AQuery aquery) {
		aq = aquery;
		
		fromRight = AnimationUtils.loadAnimation(aq.getContext(), R.anim.from_right_out);
		fromLeft = AnimationUtils.loadAnimation(aq.getContext(), R.anim.from_left_out);
		fadeIn = AnimationUtils.loadAnimation(aq.getContext(), R.anim.fade_in);
		
	}

	public void setView(ArrayList<PhotoData> data, int photoIndex, int animFlag) {
		Animation animation = null;
		int heartFlag = data.get(photoIndex).recvHeartFlag;
		
		if(animFlag == -1) {
			animFlag = R.anim.from_left_out;
			animation = fromLeft;
		}
		else if (animFlag == 0) {
			animFlag = AQuery.FADE_IN;
			animation = fadeIn;
		}
		else {
			animFlag = R.anim.from_right_out;
			animation = fromRight;
		}
		
		aq.id(R.id.musesearch_iv).progress(R.id.progress).image(data.get(photoIndex).photoPath, true, true, 0, 0,	null, AQuery.FADE_IN);
		aq.id(R.id.musesearch_porfile_iv).progress(R.id.progress2).image(data.get(photoIndex).profilePhotoThumbPath, true, true, 0, 0, null, animFlag);
		aq.id(R.id.musesearch_heartnum_tv).text(data.get(photoIndex).heartCount+"");
		aq.id(R.id.musesearch_id_tv).text(data.get(photoIndex).museAccount);
		aq.id(R.id.musesearch_id_tv).getTextView().startAnimation(animation);
		aq.id(R.id.musesearch_name_tv).text(data.get(photoIndex).museName);
		aq.id(R.id.musesearch_name_tv).getTextView().startAnimation(animation);
		aq.id(R.id.musesearch_pasttime_tv).text(TimeUtil.getPastTime(data.get(photoIndex).date));
		aq.id(R.id.musesearch_pasttime_tv).getTextView().startAnimation(animation);
		
		if(heartFlag == 0 ){
			aq.id(R.id.musesearch_heartcount_iv).getImageView().setImageResource(R.drawable.heartcount_greyheart);
			aq.id(R.id.musesearch_heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
		} else {
			aq.id(R.id.musesearch_heartcount_iv).getImageView().setImageResource(R.drawable.heartcount_pinkheart);
			aq.id(R.id.musesearch_heart_btn).getImageView().setImageResource(R.drawable.btn_heart_selector);			
		}
		
	}

	public void changeHeartStatus(ArrayList<PhotoData> photoList, int photoIndex) {
		int heartFlag = photoList.get(photoIndex).recvHeartFlag;
		
		if(heartFlag == 0) {
			photoList.get(photoIndex).recvHeartFlag = 1;
			photoList.get(photoIndex).heartCount++;
			aq.id(R.id.musesearch_heartcount_iv).getImageView().setImageResource(R.drawable.heartcount_pinkheart);
			aq.id(R.id.musesearch_heart_btn).getImageView().setImageResource(R.drawable.btn_heart_selector);
		} else {
			photoList.get(photoIndex).heartCount--;
			photoList.get(photoIndex).recvHeartFlag = 0;
			aq.id(R.id.musesearch_heartcount_iv).getImageView().setImageResource(R.drawable.heartcount_greyheart);
			aq.id(R.id.musesearch_heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
		}
		
		aq.id(R.id.musesearch_heartnum_tv).text(photoList.get(photoIndex).heartCount+"");
	}

	public void setInit() {
		aq.id(R.id.progress).visibility(View.GONE);
		aq.id(R.id.progress2).visibility(View.GONE);
	}
}
