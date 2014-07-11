package com.museda.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.museda.PhotoData;
import com.museda.views.MuseHeartView;
import com.museda.views.MuseHeartView.OnHeartUserClickListener;

public class MuseHeartAdapter extends BaseAdapter implements OnHeartUserClickListener {

	private OnHeartUserClickListener onItemClickListener;
	
	private Context context;
	private ArrayList<PhotoData> userList;
	
	public MuseHeartAdapter(Context context, ArrayList<PhotoData> photoList) {
		this.context = context;
		this.userList = photoList;
	}

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return (PhotoData) userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		MuseHeartView view;
		
		if(convertView == null)
			view = new MuseHeartView(context);
		else
			view = (MuseHeartView) convertView;
		
		view.setView(userList.get(position), position);
		view.setOnPhotoItemClickListener(this);
		
		return view;
	}
	
	public void setOnPhotoItemClickListener(OnHeartUserClickListener listener) {
		this.onItemClickListener = listener;
	}

	@Override
	public void onHeartUserListClick(View view, int position, int userIndex) {
		if(onItemClickListener != null)
			onItemClickListener.onHeartUserListClick(view, position, userIndex); 		
	}

}
