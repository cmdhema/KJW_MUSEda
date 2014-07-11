package com.museda.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;

public class MuseHeartView extends FrameLayout implements OnClickListener {

	private OnHeartUserClickListener onItemClickListener;
	
	private ArrayList<ImageView> photoImageViewList;

	private static final int[] UserImageViewID = { R.id.photo1, R.id.photo2, R.id.photo3, R.id.photo4, R.id.photo5, R.id.photo6, R.id.photo7, R.id.photo8,
			R.id.photo9, R.id.photo10, R.id.photo11, R.id.photo12, R.id.photo13, R.id.photo14, R.id.photo15, R.id.photo16, R.id.photo17, R.id.photo18,
			R.id.photo19, R.id.photo20, R.id.photo21, R.id.photo22, R.id.photo23, R.id.photo24, R.id.photo25, R.id.photo26, R.id.photo27, R.id.photo28,
			R.id.photo29, R.id.photo30, R.id.photo31, R.id.photo32, R.id.photo33, R.id.photo34, R.id.more };

	private AQuery aq;

	private int position;
	
	private TextView dateTv;
	private TextView heartCountTv;
	
	public MuseHeartView(Context context) {
		super(context);

		aq = new AQuery(context);

		LayoutInflater.from(context).inflate(R.layout.layout_museheart_row, this);

		dateTv = (TextView) findViewById(R.id.heartview_day_tv);
		heartCountTv = (TextView) findViewById(R.id.heartview_heartnum_tv);
		
		photoImageViewList = new ArrayList<ImageView>();
		
		for (int id : UserImageViewID) {
			ImageView imageView = (ImageView) findViewById(id);
			imageView.setOnClickListener(this);
			photoImageViewList.add(imageView);
		}
	}

	public void setView(PhotoData data, int position) {

		this.position = position;
		
		aq.id(dateTv).text(data.date);
		aq.id(heartCountTv).text(data.heartCount+"");
	
		for (int i = 0; i < data.sendHeartUserList.size(); i++) {
			aq.id(photoImageViewList.get(i)).visible();
			if (data.sendHeartUserList.get(i).profilePhotoThumbPath.length() > 0)
				aq.id(photoImageViewList.get(i)).image(data.sendHeartUserList.get(i).profilePhotoThumbPath);
			else {
				if ( aq.id(photoImageViewList.get(i)).getImageView() == null)
					Log.i("MuseHeartView","ImageView is Null");
				aq.id(photoImageViewList.get(i)).image(R.drawable.signin_man_btn);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int userIndex = photoImageViewList.indexOf(v);
		if(onItemClickListener != null)
			onItemClickListener.onHeartUserListClick(v, position, userIndex);
	}

	public void setOnPhotoItemClickListener(OnHeartUserClickListener listener) {
		this.onItemClickListener = listener;
	}

	public interface OnHeartUserClickListener {
		public void onHeartUserListClick(View view, int position, int userIndex);
	}
}
