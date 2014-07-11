package com.museda.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.museda.follow.UserListData;
import com.museda.views.UserListView;

public class UserListAdapter extends BaseAdapter {

	ArrayList<UserListData> dataList;
	Context context;
	
	public UserListAdapter(ArrayList<UserListData> data, Context context) {
		dataList = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return (UserListData) dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserListView view;
		
		if(convertView == null)
			view = new UserListView(context);
		else
			view = (UserListView) convertView;
		
		view.setView(dataList.get(position));
		
		return view;
	}

}
