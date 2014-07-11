package com.museda.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.util.views.RoundedImageView;

public class HeartMoreAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<PhotoData> data;
	
	public HeartMoreAdapter( Context context, ArrayList<PhotoData> list) {
		this.data = list;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int index) {
		return (PhotoData) data.get(index); 
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		RoundedImageView imageView;
		
		if(convertView == null) {
			imageView = new RoundedImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(5, 5, 5, 5);
		}
		else {
			imageView = (RoundedImageView) convertView;
		}
		
		AQuery aq = new AQuery(context);
//		aq.id(imageView).image(data.get(position).profilePhotoThumbPath);
		
		if (data.get(position).profilePhotoThumbPath.length() > 0)
			aq.id(imageView).image(data.get(position).profilePhotoThumbPath);
		else {
			aq.id(imageView).image(R.drawable.signin_man_btn);
		}
		
		return imageView;
	}

}
