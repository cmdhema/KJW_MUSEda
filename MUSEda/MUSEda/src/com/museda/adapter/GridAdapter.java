package com.museda.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.museda.PhotoData;
import com.museda.views.GridItemView;

public class GridAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<PhotoData> data;
	
	public GridAdapter(Context context, ArrayList<PhotoData> data) {
		mContext = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return (PhotoData)data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		GridItemView view;
		
		if(convertView == null) {
			view = new GridItemView(mContext);
		} else {
			view = (GridItemView) convertView;
		}
		
		view.setData(data.get(position));
		
		return view;
	}

}
