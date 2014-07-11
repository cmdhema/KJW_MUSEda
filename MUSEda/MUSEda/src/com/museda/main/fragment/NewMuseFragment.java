package com.museda.main.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
import com.museda.adapter.GridAdapter;
import com.museda.detail.MuseDetailActivity;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;

public class NewMuseFragment extends Fragment implements OnItemClickListener, OnGetMethodProcessListener<ArrayList<PhotoData>>, OnLastItemVisibleListener, OnRefreshListener<GridView> {

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");

	private MuseSearchRequest request;
	private PullToRefreshGridView pullToRefreshGridView;
	private GridView newMuseGridView;

	private PhotoData data;
	private ArrayList<PhotoData> photoList;
	
	private int lastPhotoId;
	private int responseGridSize;
	
	private GridAdapter adapter;
	
	private ProgressDialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_newmuse, container, false);

		pullToRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.newmuselist_gridview);
		pullToRefreshGridView.setOnLastItemVisibleListener(this);
		pullToRefreshGridView.setMode(Mode.PULL_FROM_START);
		pullToRefreshGridView.setOnRefreshListener(this);
		newMuseGridView = pullToRefreshGridView.getRefreshableView();

		newMuseGridView.setOnItemClickListener(this);
		newMuseGridView.setVerticalScrollBarEnabled(false);
		newMuseGridView.setSelector(new StateListDrawable());

		photoList = new ArrayList<PhotoData>();
		adapter = new GridAdapter(getActivity(), photoList);
		newMuseGridView.setAdapter(adapter);
		
		requestNewMuse(0);

		return view;
	}

	private void requestNewMuse(int photoId) {
		
		data = new PhotoData(userData.myIDNum, 90, photoId, "previous");
		request = new MuseSearchRequest(new ArrayList<PhotoData>(), data, "new");
		request.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(getActivity(), request);
		
		dialog = ProgressDialog.show(getActivity(), "사진을 불러오는 중입니다.", "잠시만 기다려주세요",true);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		Intent intent = new Intent(getActivity(), MuseDetailActivity.class);

		intent.putExtra("flag", "NewMuseFragment");
		intent.putExtra("photoId", photoList.get(position).photoIdNum);
		intent.putExtra("userId", photoList.get(position).museIdNum);
//		intent.putExtra("heartFlag", photoList.get(position).recvHeartFlag);
		intent.putExtra("photoPath", photoList.get(position).photoPath);
		intent.putExtra("width", photoList.get(position).photoWidth);
		intent.putExtra("height", photoList.get(position).photoHeight);

		startActivity(intent);
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		photoList.addAll( request.getResult() );
		adapter.notifyDataSetChanged();
		pullToRefreshGridView.onRefreshComplete();
		responseGridSize = request.getResult().size();
		
		dialog.dismiss();
	}

	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		photoList.removeAll(photoList);
		requestNewMuse(0);
	}
	

	@Override
	public void onLastItemVisible() {
		lastPhotoId = photoList.get(photoList.size()-1).photoIdNum;
		
		if(responseGridSize >= 90)
			requestNewMuse(lastPhotoId);
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {}

}
