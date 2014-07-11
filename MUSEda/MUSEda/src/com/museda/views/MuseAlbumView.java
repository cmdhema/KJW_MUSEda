package com.museda.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.util.ScreenSize;
import com.museda.util.TimeUtil;

public class MuseAlbumView extends FrameLayout implements OnClickListener{

	private AQuery aq;
	
	private LinearLayout infoLayout;
	private TextView heartNumTv;
	private ImageView photoIv;
	private ImageView approveIv;
	private TextView day;
	private TextView time;

	private OnPhotoItemClickListener onItemClickListener;

	private int position;
	
	public MuseAlbumView(Context context) {
		super(context);
		aq = new AQuery(context);
		
		LayoutInflater.from(context).inflate(R.layout.layout_musealbum_row, this);
		
		infoLayout = (LinearLayout) findViewById(R.id.myphoto_row_layout);
		heartNumTv = (TextView) findViewById(R.id.myphoto_heartnum);
		photoIv = (ImageView) findViewById(R.id.myphoto_iv);
		day = (TextView) findViewById(R.id.myphoto_day);
		time = (TextView) findViewById(R.id.myphoto_time);
		approveIv = (ImageView) findViewById(R.id.album_approve_iv);
		
		photoIv.setOnClickListener(this);
	}
	
	public void setView(PhotoData data, int position) {
		
		this.position = position;
		
		if(data == null)
			infoLayout.setVisibility(View.GONE);
		
		aq.id(heartNumTv).text(" " + data.heartCount + "명이 사진을 좋아합니다");
		
		float imageRatio = (float) data.photoHeight / (float) data.photoWidth;
		aq.id(photoIv).getImageView().getLayoutParams().width = (int) (ScreenSize.getScreenWidth(getContext()) * 0.86);
		aq.id(photoIv).getImageView().getLayoutParams().height = (int) ((ScreenSize.getScreenWidth(getContext()) * 0.86) * imageRatio);
		aq.id(approveIv).getImageView().getLayoutParams().width = (int) (ScreenSize.getScreenWidth(getContext()) * 0.86);
		aq.id(approveIv).getImageView().getLayoutParams().height = (int) ((ScreenSize.getScreenWidth(getContext()) * 0.86) * imageRatio);
		
		aq.id(photoIv).image(data.photoPath, true, true, 0, 0, null, AQuery.FADE_IN);
		
		aq.id(day).text(TimeUtil.getConvertDay(data.date));
		aq.id(time).text(" " + TimeUtil.getMuseMyPhotoDayInfo(data.date));
		
		if( data.approve == -1 )
			aq.id(approveIv).getImageView().setVisibility(View.VISIBLE);
		else 
			aq.id(approveIv).getImageView().setVisibility(View.GONE);
	}

	public void setOnPhotoItemClickListener(OnPhotoItemClickListener listener) {
		this.onItemClickListener = listener;
	}
	
	@Override
	public void onClick(View v) {
		if(onItemClickListener != null)
			onItemClickListener.onItemClick(v, position);
	}

}
