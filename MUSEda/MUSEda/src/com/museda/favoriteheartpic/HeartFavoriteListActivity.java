package com.museda.favoriteheartpic;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.museda.ParentActivity;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.adapter.GridAdapter;
import com.museda.detail.MuseDetailActivity;
import com.museda.main.fragment.FollowPhotoRequest;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.profile.MuseProfileActivity;

public class HeartFavoriteListActivity extends ParentActivity implements OnItemClickListener, OnGetMethodProcessListener<ArrayList<PhotoData>>, OnRefreshListener<GridView>, OnLastItemVisibleListener {

	private UserData userInfo = SingletonData.getInstance().getUserData().get("UserData");

	private long currentTime = System.currentTimeMillis();
	
	private TitleBar header;
	private GridView photoView;	

	private PullToRefreshGridView pullToRefreshGridView;
	private ArrayList<PhotoData> photoList;
	
	//Intent로 넘어온 문자열 저장
	private String flag;
	
	private PhotoData data;
	
	private FollowPhotoRequest favoritePhotoRequest;
	private HeartPhotoRequest heartPhotoRequest;

	private int jumpCount = 200;

	private int responseGridSize;

	private GridAdapter adapter;

	private boolean isLastVisible;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_heartfavoritelist);
		
		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.normalmypage_list_gridview);
		pullToRefreshGridView.setOnLastItemVisibleListener(this);
		pullToRefreshGridView.setMode(Mode.DISABLED);
		pullToRefreshGridView.setOnRefreshListener(this);
		
		photoView = pullToRefreshGridView.getRefreshableView();
		
		header = (TitleBar) findViewById(R.id.layout_titlebar);
		photoView.setOnItemClickListener(this);

		flag = getIntent().getStringExtra("flag");
		header.setTitleText(flag);
		
		photoList = new ArrayList<PhotoData>();
		adapter = new GridAdapter(this, photoList);
		photoView.setAdapter(adapter);	
		
	
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		isLastVisible = false;
		requestData(flag, 0);
		
	}


	private void requestData(String flag, int startId) {
		data = new PhotoData(userInfo.myIDNum, jumpCount, startId);
		
		if(flag.equals("Favorite")) {
			favoritePhotoRequest = new FollowPhotoRequest(new ArrayList<PhotoData>(), "picture/list/favorites?", data);
			favoritePhotoRequest.setOnGetMethodProcessListener(this);
			NetworkModel.getInstance().getNetworkData(this, favoritePhotoRequest);
		} else if (flag.equals("Heart Pic")) {
			
			if ( currentTime != UserData.lastSetHeartedTime) {
				
				currentTime = UserData.lastSetHeartedTime;
				
				heartPhotoRequest = new HeartPhotoRequest(new ArrayList<PhotoData>(), data);
				heartPhotoRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().getNetworkData(this, heartPhotoRequest);		
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		if(flag.equals("Favorite")) {
			Intent intent = new Intent(this, MuseProfileActivity.class);
			intent.putExtra("flag", "HeartFavoriteListActivity");
			intent.putExtra("userId", photoList.get(position).museIdNum);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, MuseDetailActivity.class);
			intent.putExtra("heartFlag", 1);
			intent.putExtra("photoId", photoList.get(position).photoIdNum);
			intent.putExtra("userId", photoList.get(position).museIdNum);
			intent.putExtra("photoPath", photoList.get(position).photoPath);
			intent.putExtra("favoriteFlag", photoList.get(position).favoriteFlag);
			intent.putExtra("width", photoList.get(position).photoWidth);
			intent.putExtra("height", photoList.get(position).photoHeight);
			intent.putExtra("flag", "HeartFavoriteListActivity");
			startActivity(intent);
		}
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		
		if ( isLastVisible == false && photoList.size() > 0) 
			photoList.removeAll(photoList);
		
		photoList.addAll(request.getResult());
		adapter.notifyDataSetChanged();
		
		pullToRefreshGridView.onRefreshComplete();
	}


	@Override
	public void onLastItemVisible() {
		
		if(responseGridSize >= jumpCount)
			requestData(flag, jumpCount);		
		
		isLastVisible = true;
	}

	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		photoList.removeAll(photoList);
		requestData(flag, 0);
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
	}
}
