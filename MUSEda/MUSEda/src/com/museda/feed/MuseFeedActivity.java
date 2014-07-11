package com.museda.feed;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.museda.ParentActivity;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.adapter.PhotoListAdapter;
import com.museda.detail.PhotoDetailActivity;
import com.museda.favoriteheartpic.HeartFavoriteData;
import com.museda.favoriteheartpic.SetHeartRequest;
import com.museda.main.fragment.FollowPhotoRequest;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.profile.MuseProfileActivity;
import com.museda.util.ErrorDialog;
import com.museda.views.OnPhotoItemClickListener;

public class MuseFeedActivity extends ParentActivity implements OnRefreshListener<ListView>, OnPhotoItemClickListener, OnGetMethodProcessListener<ArrayList<PhotoData>>, OnPostMethodProcessListener<HeartFavoriteData>, OnScrollListener {

	private final int MUSE_OK = 100;
	
	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");
	private PhotoData requestData;

	private ProgressDialog dialog;
	
	private SetHeartRequest heartRequest;
	private FollowPhotoRequest followingRequest;

	private PullToRefreshListView pullToListView;
	private ListView listview;

	private TitleBar header;
	private ArrayList<PhotoData> photoList;
	private int photoIndex;
	
	private int lastPhotoId;
	private int responseListSize;
	private boolean lockListView;
	
	private PhotoListAdapter adapter;
	
//	private View listFooter;
	
	private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_musefeed);

//		listFooter = getLayoutInflater().inflate(R.layout.layout_listfooter,null);
		
		header = (TitleBar) findViewById(R.id.titlebar);
		pullToListView = (PullToRefreshListView) findViewById(R.id.muse_feed_list);
		pullToListView.setVerticalScrollBarEnabled(false);
		pullToListView.setFocusable(true);
		pullToListView.setOnRefreshListener(this);
		listview = pullToListView.getRefreshableView();
		listview.setOnScrollListener(this);
		
		photoList = new ArrayList<PhotoData>();
		adapter = new PhotoListAdapter(this, photoList);
		adapter.setOnPhotoItemClickListener(this);
		listview.setAdapter(adapter);
		
		header.setTitleText("Feed");
		header.setSettingBtnVisible(false);
		
		aq = new AQuery(this);
		
		requestFeedData(0);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		int count = totalItemCount - visibleItemCount;
		
		if(firstVisibleItem >= count && totalItemCount != 0 && lockListView == false ) {
			Log.i("MuseFeedActivity", "Loading next items");
			if(responseListSize >= 10) {
//				listview.addFooterView(listFooter);
				requestFeedData(lastPhotoId);
			}
//			else
//				listview.removeFooterView(listFooter);
		}  
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		photoList.removeAll(photoList);
		requestFeedData(0);
	}

	private void requestFeedData(int photoId) {
		
		lockListView = true;
		
		requestData = new PhotoData(userData.myIDNum, 10, photoId, "previous", "false");
		followingRequest = new FollowPhotoRequest(new ArrayList<PhotoData>(),  "picture/list/following?", requestData);
		followingRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, followingRequest);
		
		dialog = ProgressDialog.show(this, "사진 목록을 불러오는 중입니다.", "잠시만 기다려주세요.");
		
	}
	
	@Override
	public void onItemClick(View view, int position) {
		this.photoIndex = position;
		switch (view.getId()) {
		case R.id.museview_iv: {
			Intent intent = new Intent(this, PhotoDetailActivity.class);
			intent.putExtra("photoPath", photoList.get(photoIndex).photoPath);
			intent.putExtra("flag", "MuseFeedActivity");
			intent.putExtra("userId", photoList.get(photoIndex).museIdNum);
			startActivity(intent);
			break;
		}
		case R.id.museview_heart_btn: {
			int heartFlag = photoList.get(position).recvHeartFlag;
			HeartFavoriteData data = null;
			if (heartFlag == 0)
				data = new HeartFavoriteData(userData.myIDNum, photoList.get(position).museIdNum, photoList.get(position).photoIdNum, "true");
			else
				data = new HeartFavoriteData(userData.myIDNum, photoList.get(position).museIdNum, photoList.get(position).photoIdNum, "false");

			heartRequest = new SetHeartRequest(data, "heart");
			heartRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, heartRequest);
			break;
		}
		case R.id.museview_profile_iv:
		case R.id.museview_name_tv:
		case R.id.museview_id_tv:
			Intent intent = new Intent(this, MuseProfileActivity.class);
			intent.putExtra("userId", photoList.get(position).museIdNum);
			intent.putExtra("flag", "NormalViewFragment");
			startActivityForResult(intent, MUSE_OK);
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch ( requestCode ) {
		case MUSE_OK :
			final MuseProfileActivity museActivity = new MuseProfileActivity();
			MuseProfileActivity.OnMuseUpdateCallback museCallback = new MuseProfileActivity.OnMuseUpdateCallback() {
				
				@Override
				public void onMuseUpdated(boolean updated) {
				}

				@Override
				public void onFollowUpdated(boolean updated) {
					if ( updated ) {
						photoList.clear();
						requestFeedData(0);
//						museActivity.setFollowingUpdated(false);
					}
				}
			};
			museActivity.setOnMuseUpdateCallback(museCallback);
			museActivity.isMuseUpdated();
		}
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<HeartFavoriteData> request) {
		int heartFlag = photoList.get(photoIndex).recvHeartFlag;
		
		if(heartFlag == 0) {
			photoList.get(photoIndex).recvHeartFlag = 1;
			aq.id(R.id.museview_heart_btn).getImageView().setImageResource(R.drawable.heart);
		} else {
			photoList.get(photoIndex).recvHeartFlag = 0;
			aq.id(R.id.museview_heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
		}		
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {

		dialog.dismiss();
		
		this.photoList.addAll(request.getResult());

		lockListView = false;
		adapter.notifyDataSetChanged();
		responseListSize = request.getResult().size();
		pullToListView.onRefreshComplete();
		
		if ( photoList.size() > 0)
			lastPhotoId = this.photoList.get(photoList.size()-1).photoIdNum;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<HeartFavoriteData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();		
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
		dialog.dismiss();
	}

}
