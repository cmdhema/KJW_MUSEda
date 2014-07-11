package com.museda.views;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.util.ScreenSize;
import com.museda.util.TimeUtil;

public class DetailView {

	AQuery aq;

	public DetailView(AQuery aq) {
		this.aq = aq;
	}
	
	public void setDetailView(ArrayList<PhotoData> data, ArrayList<ImageView> photoViewList) {
		
		if(data.size()<4)
			aq.id(R.id.photolist2_layout).visibility(View.GONE);	
		
		if(data.size()==6)
			photoViewList.get(5).setVisibility(View.VISIBLE);
		
		for (int i = 0; i < data.size(); i++){
			photoViewList.get(i).setVisibility(View.VISIBLE);
			
			if(i==0) {
				aq.id(R.id.photo1).image(data.get(0).photoThumbPath);
			} if (i==1) {
				aq.id(R.id.photo2).image(data.get(1).photoThumbPath);
			} if (i==2) {
				aq.id(R.id.photo3).image(data.get(2).photoThumbPath);
			} if (i==3) {
				aq.id(R.id.photo4).image(data.get(3).photoThumbPath);
			} if (i==4){
				aq.id(R.id.photo5).image(data.get(4).photoThumbPath);
			}
		}
		
		aq.id(R.id.museview_pasttime_tv).text(TimeUtil.getPastTime(data.get(0).date));
		aq.id(R.id.museview_porfile_iv).image(data.get(0).profilePhotoThumbPath);
		aq.id(R.id.museview_name_tv).text(data.get(0).museName);
		aq.id(R.id.museview_id_tv).text(data.get(0).museAccount);
		aq.id(R.id.museview_heartnum_tv).text( "¢½" + data.get(0).heartCount + "");
		
		if(data.get(0).recvHeartFlag == 1) 
			aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.btn_heart_selector);
		else
			aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);		
		
	}

	public void setPhotoIndexInfo(ArrayList<PhotoData> photoList, int index) {
		aq.id(R.id.museview_heartnum_tv).text("¢½" + photoList.get(index).heartCount);
		float imageRatio = (float)photoList.get(index).photoHeight/(float)photoList.get(index).photoWidth;
		aq.id(R.id.museview_iv).getImageView().getLayoutParams().width = (int) ScreenSize.getScreenWidth(aq.getContext());
		aq.id(R.id.museview_iv).getImageView().getLayoutParams().height = (int) (ScreenSize.getScreenWidth(aq.getContext()) * imageRatio);
		aq.id(R.id.museview_iv).image(photoList.get(index).photoPath, true, true, 0, 0, null, AQuery.FADE_IN);
		
		if(photoList.get(index).recvHeartFlag == 1) 
			aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.btn_heart_selector);
		else
			aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);	
		
//		if (photoList.get(index).favoriteFlag.equals("true"))
//			aq.id(R.id.favorite_iv).getImageView().setImageResource(R.drawable.page_userid_fav2);
//		else
//			aq.id(R.id.favorite_iv).getImageView().setImageResource(R.drawable.page_userid_fav1);
	}

	public void setImage(float photoHeight, float photoWidth, String photoPath) {
		float imageRatio = (float) photoHeight / (float) photoWidth;
		aq.id(R.id.museview_iv).getImageView().getLayoutParams().width = (int) (ScreenSize.getScreenWidth(aq.getContext()));
		aq.id(R.id.museview_iv).getImageView().getLayoutParams().height = (int) ((ScreenSize.getScreenWidth(aq.getContext())) * imageRatio);
		aq.id(R.id.museview_iv).image(photoPath);
	}
	
}
