package com.museda.heart;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.museda.ParentActivity;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.adapter.MuseHeartAdapter;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.profile.MuseProfileActivity;
import com.museda.profile.NormalProfileActivity;
import com.museda.views.MuseHeartView.OnHeartUserClickListener;

public class MuseHeartActivity extends ParentActivity implements OnGetMethodProcessListener<ArrayList<PhotoData>>, OnHeartUserClickListener, OnScrollListener {

	private UserData userInfo;

	private TitleBar header;

	private ListView heartList;

	private HeartCountRequest heartCountRequest;
	
	private ArrayList<PhotoData> userList;

	private View listFooter;
	private MuseHeartAdapter adapter;
	
	private String lastDate;
	private int responseListSize;
	private boolean lockListView;
	
	private int count = 35;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_museheart);

		listFooter = getLayoutInflater().inflate(R.layout.layout_listfooter, null);
		
		heartList = (ListView) findViewById(R.id.heartview_listview);
		heartList.setFocusable(true);
		heartList.setDivider(null);
		// listview.setDividerHeight(20);
		header = (TitleBar) findViewById(R.id.titlebar);
		header.setTitleText("Heart");
		header.setSettingBtnVisible(false);

		heartList.setOnScrollListener(this);
		
		userList = new ArrayList<PhotoData>();
		adapter = new MuseHeartAdapter(this, userList);
		adapter.setOnPhotoItemClickListener(this);
		heartList.setAdapter(adapter);
		

		userInfo = SingletonData.getInstance().getUserData().get("UserData");

		requestHeartData("");
		
	}

	@Override
	protected void onResume() {
		super.onResume();


	}

	private void requestHeartData(String date) {

		lockListView = true;
		
		PhotoData requestData = new PhotoData(userInfo.myIDNum, count, date);
		heartCountRequest = new HeartCountRequest(new ArrayList<PhotoData>(), requestData);
		heartCountRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, heartCountRequest);
		
	}
	
	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		userList.addAll(request.getResult());
		adapter.notifyDataSetChanged();
		
		lockListView = false;
		
		if ( request.getResult().size() > 0)
			lastDate = request.getResult().get(request.getResult().size()-1).date;
	}

	@Override
	public void onHeartUserListClick(View view, int position, int userIndex) {
		String userType = userList.get(position).sendHeartUserList.get(userIndex).userType;
		int userId = userList.get(position).sendHeartUserList.get(userIndex).senderId;
		
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
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int count = totalItemCount - visibleItemCount;
		
		if(firstVisibleItem >= count && totalItemCount != 0 && lockListView == false ) {
			Log.i("MuseHeartActivity", "Loading next items");
			if(responseListSize >= this.count) {
				heartList.addFooterView(listFooter);
				requestHeartData(lastDate);
			} else
				heartList.removeFooterView(listFooter);
		}  
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
		// TODO Auto-generated method stub
		
	}
}
