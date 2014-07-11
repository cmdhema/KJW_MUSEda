package com.museda.views;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.util.ScreenSize;
import com.museda.util.TimeUtil;

public class PhotoView extends FrameLayout implements OnClickListener{

	private OnPhotoItemClickListener onItemClickListener;
	
	private TextView heartNumTv;
	private TextView nameTv;
	private TextView idTv;
	private TextView pastTimeTv;
	
	private ImageView photoIv;
	private ImageView profileIv;
	private ImageView heartBtn;
	
	private AQuery aq;
	
	private int position;
	
	public PhotoView(Context context) {
		super(context);

		aq = new AQuery(context);
		
		LayoutInflater.from(context).inflate(R.layout.layout_normalview_row, this);
		
		heartBtn = (ImageView) findViewById(R.id.museview_heart_btn);
		photoIv = (ImageView) findViewById(R.id.museview_iv);
		profileIv = (ImageView) findViewById(R.id.museview_profile_iv);
		nameTv = (TextView) findViewById(R.id.museview_name_tv);
		idTv = (TextView) findViewById(R.id.museview_id_tv);
		heartNumTv = (TextView) findViewById(R.id.museview_heartnum_tv);	
		pastTimeTv = (TextView) findViewById(R.id.museview_pasttime_tv);
		
		heartBtn.setOnClickListener(this);
		photoIv.setOnClickListener(this);
		profileIv.setOnClickListener(this);
		nameTv.setOnClickListener(this);
		idTv.setOnClickListener(this);
		heartNumTv.setOnClickListener(this);
		
	}

	public void setView(PhotoData data, int position) {
		this.position = position;
		
		float imageRatio = (float) data.photoHeight	/ (float) data.photoWidth;
		aq.id(photoIv).getImageView().getLayoutParams().width = (int) (ScreenSize.getScreenWidth(getContext()));
		aq.id(photoIv).getImageView().getLayoutParams().height = (int) ((ScreenSize.getScreenWidth(getContext())) * imageRatio);
		aq.id(photoIv).image(data.photoPath);
		aq.id(pastTimeTv).text(TimeUtil.getPastTime(data.date));
		aq.id(nameTv).text(data.museName);
		aq.id(idTv).text(data.museAccount);
		aq.id(heartNumTv).text("¢½" + data.heartCount);
		aq.id(profileIv).image(data.profilePhotoThumbPath);
		
		if(data.recvHeartFlag == 0 ){
			aq.id(heartBtn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
		} else {
			aq.id(heartBtn).getImageView().setImageResource(R.drawable.btn_heart_selector);			
		}
	}

	public void setOnPhotoItemClickListener(OnPhotoItemClickListener listener) {
		this.onItemClickListener = listener;
	}

	@Override
	public void onClick(View v) {
		if(onItemClickListener != null)
			onItemClickListener.onItemClick(v, position);
	}

	public void changeHeartStatus(ArrayList<PhotoData> photoList, int photoIndex) {
		int heartFlag = photoList.get(photoIndex).recvHeartFlag;
		
		if(heartFlag == 0) {
			photoList.get(photoIndex).recvHeartFlag = 1;
			aq.id(R.id.museview_heart_btn).getImageView().setImageResource(R.drawable.btn_heart_selector);
		} else {
			photoList.get(photoIndex).recvHeartFlag = 0;
			aq.id(R.id.museview_heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
		}
	}

}
