package com.museda.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.museda.PhotoData;
import com.museda.views.MuseAlbumView;
import com.museda.views.OnPhotoItemClickListener;

public class MuseAlbumListAdapter extends BaseAdapter implements OnPhotoItemClickListener{

	private ArrayList<PhotoData> photoList;
	private Context context;
	private OnPhotoItemClickListener onItemClickListener;
	
	public MuseAlbumListAdapter(Context context, ArrayList<PhotoData> list) {
		this.context = context;
		photoList = list;
	}

	@Override
	public int getCount() {
		return photoList.size();
	}

	@Override
	public Object getItem(int position) {
		return (PhotoData) photoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MuseAlbumView view;
		
		if(convertView == null)
			view = new MuseAlbumView(context);
		else
			view = (MuseAlbumView) convertView;
		
		view.setView(photoList.get(position), position);
		view.setOnPhotoItemClickListener(this);
		
		return view;
	}
	
	public void setOnPhotoItemClickListener(OnPhotoItemClickListener listener) {
		this.onItemClickListener = listener;
	}
	
	@Override
	public void onItemClick(View view, int position) {
		if(onItemClickListener != null)
			onItemClickListener.onItemClick(view, position); 
	}

}
