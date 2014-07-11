package com.museda.detail;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.adapter.HeartMoreAdapter;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.profile.MuseProfileActivity;
import com.museda.profile.NormalProfileActivity;

public class HeartMoreActivity extends Activity implements OnGetMethodProcessListener<ArrayList<PhotoData>>, OnItemClickListener, OnLastItemVisibleListener {

	private UserData myInfo = SingletonData.getInstance().getUserData().get("UserData");

	private PullToRefreshGridView pullToRefreshGridView;
	private GridView gridView;
	private TitleBar titleBar;
	
	private int photoIdNum;
	
	private ArrayList<PhotoData> photoList;
	
	private HeartMoreAdapter adapter;

	private int responseGridSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_heartmore);
	
		titleBar = (TitleBar) findViewById(R.id.titlebar);
		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.heart_grid);
		pullToRefreshGridView.setOnLastItemVisibleListener(this);
		pullToRefreshGridView.setMode(Mode.DISABLED);
		
		gridView = pullToRefreshGridView.getRefreshableView();
		gridView.setOnItemClickListener(this);
		titleBar.setTitleText("Heart");
		
		photoList = new ArrayList<PhotoData>();
		adapter = new HeartMoreAdapter(this, photoList);
		gridView.setAdapter(adapter);
		
		photoIdNum = getIntent().getIntExtra("photoId", -1);
		
		requestHeartList(0);

	}

	private void requestHeartList(int jumpCount) {

		PhotoData requestData = new PhotoData(myInfo.myIDNum, 90, photoIdNum, jumpCount);
		HeartMoreRequest request = new HeartMoreRequest(new ArrayList<PhotoData>(), requestData);
		request.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, request);
	
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		photoList.addAll( request.getResult() );
		adapter.notifyDataSetChanged();
		responseGridSize = request.getResult().size();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		String userType = photoList.get(position).userType;
		int userId = photoList.get(position).senderId;
		
		if(userType.equals("0")) {
			Intent intent = new Intent(this, NormalProfileActivity.class);
			intent.putExtra("flag", "MuseHeartActivity");
			intent.putExtra("userId", userId);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, MuseProfileActivity.class);
			intent.putExtra("flag", "MuseHeartActivity");
			intent.putExtra("userId", userId);
			startActivity(intent);
		}
	}

	@Override
	public void onLastItemVisible() {
		
		if(responseGridSize >= 90)
			requestHeartList(responseGridSize);
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
	}
}
