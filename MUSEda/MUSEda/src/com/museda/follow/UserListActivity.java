package com.museda.follow;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.museda.ParentActivity;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.adapter.UserListAdapter;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.profile.MuseProfileActivity;
import com.museda.profile.NormalProfileActivity;
import com.museda.search.SearchRequest;

public class UserListActivity extends ParentActivity implements OnItemClickListener, OnGetMethodProcessListener<ArrayList<UserListData>>, OnScrollListener {

//	private long currentTime = System.currentTimeMillis();
	
	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");

	private TitleBar header;

	private ListView userListView;

	private String flag;

	private UserListRequest request;
	private FollowingRequestData followingRequest;
	private SearchRequest searchRequest;

	private UserListData data;

	private ArrayList<UserListData> photoList;
	
	private String searchMuseId;

//	private View listFooter;

	private boolean lockListView;

	private int RequestCount = 30;
	private int responseListSize;
	private UserListAdapter adapter;

	private int lastUserId;
	
	private boolean isLastVisible;
	
	private ProgressDialog dialog;

	private int jumpCount;

	private final int MuseRequest = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_userlist);

//		listFooter = getLayoutInflater().inflate(R.layout.layout_listfooter, null);
		
		userListView = (ListView) findViewById(R.id.userlist_listview);
		header = (TitleBar) findViewById(R.id.titlebar);
		
		header.setTitleText(getIntent().getStringExtra("flag"));
		flag = getIntent().getStringExtra("flag");
		searchMuseId = getIntent().getStringExtra("searchId");
		
		userListView.setOnScrollListener(this);
		userListView.setOnItemClickListener(this);
		
		photoList = new ArrayList<UserListData>();
		adapter = new UserListAdapter(photoList, this);
		userListView.setAdapter(adapter);

		requestUserList(flag, 0, 0);
		
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		isLastVisible = false;
		
		Log.i("UserListActivity", "Resume");

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int count = totalItemCount - visibleItemCount;
		
		if(firstVisibleItem >= count && totalItemCount != 0 && lockListView == false ) {
			Log.i("UserListActivity", "Loading next items");
			
			if(responseListSize >= 30) {
				jumpCount += responseListSize;
//				userListView.addFooterView(listFooter);
				requestUserList(flag, lastUserId, jumpCount);
			}
//			} else
//				userListView.removeFooterView(listFooter);
			
			isLastVisible = true;
		}  
	}
	
	private void requestUserList(final String flag, int startId, int jumpCount) {
		
		lockListView = true;
		
		if (flag.equals("Muse Follower")) {
			data = new UserListData(userData.myIDNum, RequestCount, startId, "next");
			request = new UserListRequest(new ArrayList<UserListData>(), data, "muse/list/mymuse");
			request.setOnGetMethodProcessListener(UserListActivity.this);
			NetworkModel.getInstance().getNetworkData(UserListActivity.this, request);
		} else if (flag.equals("Follower")) {
			data = new UserListData(userData.myIDNum, RequestCount, startId, "next");
			request = new UserListRequest(new ArrayList<UserListData>(), data, "muse/list/follower");
			request.setOnGetMethodProcessListener(UserListActivity.this);
			NetworkModel.getInstance().getNetworkData(UserListActivity.this, request);
		} else if (flag.equals("Search")) {
			data = new UserListData(searchMuseId, RequestCount, jumpCount);
			searchRequest = new SearchRequest(new ArrayList<UserListData>(), data, "muse/find");
			searchRequest.setOnGetMethodProcessListener(UserListActivity.this);
			NetworkModel.getInstance().getNetworkData(UserListActivity.this, searchRequest);
		} else if (flag.equals("Following")) {
			data = new UserListData(userData.myIDNum, RequestCount, jumpCount);
			followingRequest = new FollowingRequestData(new ArrayList<UserListData>(), data, "user/list/following");
			followingRequest.setOnGetMethodProcessListener(UserListActivity.this);
			NetworkModel.getInstance().getNetworkData(UserListActivity.this, followingRequest);
		}

		dialog = ProgressDialog.show(this, "목록을 불러오는 중입니다.", "잠시만 기다려주세요.");
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		int userId = photoList.get(position).userIdNum;
		if (photoList.get(position).userType.equals("0") || flag.equals("Muse Follower")) { // 일반 회원
			Intent intent = new Intent(this, NormalProfileActivity.class);
			intent.putExtra("userId", userId);
			intent.putExtra("flag", "UserListActivity");
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, MuseProfileActivity.class);
			intent.putExtra("userId", userId);
			intent.putExtra("flag", "UserListActivity");
			startActivityForResult(intent, MuseRequest );
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {
		case MuseRequest :
			final MuseProfileActivity museActivity = new MuseProfileActivity();
			MuseProfileActivity.OnMuseUpdateCallback museCallback = new MuseProfileActivity.OnMuseUpdateCallback() {
				
				@Override
				public void onMuseUpdated(boolean updated) {
				}

				@Override
				public void onFollowUpdated(boolean updated) {
					if ( updated ) {
						requestUserList(flag, 0, 0);
//						museActivity.setFollowingUpdated(false);
					}
				}
			};
			museActivity.setOnMuseUpdateCallback(museCallback);
			museActivity.isMuseUpdated();
			
		}
	}


	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<UserListData>> request) {

		responseListSize = request.getResult().size();
		
		if ( responseListSize > 0)
			lastUserId = request.getResult().get(request.getResult().size()-1).userIdNum;
		if ( isLastVisible == false && photoList.size() > 0)
			photoList.clear();

		photoList.addAll(request.getResult());
		
		lockListView = false;
		adapter.notifyDataSetChanged();
		
		dialog.dismiss();
	
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
	
	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<UserListData>> request) {
		dialog.dismiss();
	}
}