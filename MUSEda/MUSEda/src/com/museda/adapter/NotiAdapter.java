package com.museda.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.museda.setting.noti.NotiData;
import com.museda.views.NotiView;

public class NotiAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NotiData> list;
	
	public NotiAdapter(Context context, ArrayList<NotiData> dataList) {
		
		this.context = context;
		this.list = dataList;
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NotiData getItem(int position) {
		return (NotiData) list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		NotiView view = null;
		
		if ( convertView == null)
			view = new NotiView(context);
		else
			convertView = (NotiView) convertView;
		
		view.setNoti(list.get(position));
		
		return view;
	}

	
}
