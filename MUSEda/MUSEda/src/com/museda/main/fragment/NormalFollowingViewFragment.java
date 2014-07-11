package com.museda.main.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
import com.museda.adapter.PhotoListAdapter;
import com.museda.detail.PhotoDetailActivity;
import com.museda.favoriteheartpic.HeartFavoriteData;
import com.museda.favoriteheartpic.SetHeartRequest;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.profile.MuseProfileActivity;
import com.museda.util.ErrorDialog;
import com.museda.views.OnPhotoItemClickListener;

public class NormalFollowingViewFragment extends Fragment implements OnRefreshListener<ListView>, OnScrollListener,
		OnGetMethodProcessListener<ArrayList<PhotoData>>, OnPhotoItemClickListener, OnPostMethodProcessListener<HeartFavoriteData> {

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");

	private FollowPhotoRequest followRequest;
	private SetHeartRequest heartRequest;

	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;

	private ProgressDialog dialog;
	
	private ArrayList<PhotoData> photoList;

	private PhotoListAdapter adapter;

	private int photoIndex;
	private int lastPhotoId;
	private int responseListSize;
	private boolean lockListView;
	
//	private View listFooter;

	private AQuery aq;

	private PhotoData data;
	
	private boolean isDataChanged = false;
	
	private boolean isLastVisible;

	public static Fragment newInstance( Context context) {
		NormalFollowingViewFragment fragment = new NormalFollowingViewFragment();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.layout_normalmymusefollowview, null);
//		listFooter = getActivity().getLayoutInflater().inflate(R.layout.layout_listfooter,null);
		
		aq = new AQuery(getActivity());

		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listfragment);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setBackgroundColor(Color.WHITE);
		mListView = mPullRefreshListView.getRefreshableView();
		mListView.setOnScrollListener(this);
		
		photoList = new ArrayList<PhotoData>();
		adapter = new PhotoListAdapter(getActivity(), photoList);
		adapter.setOnPhotoItemClickListener(this);
		mListView.setAdapter(adapter);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Log.i("NormalFollowingView", "onResume()");
		
		isLastVisible = false;
		
		if ( isDataChanged ) 
			requestFollowMuseData(0);
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		int count = totalItemCount - visibleItemCount;
		
		if(firstVisibleItem >= count && totalItemCount != 0 && lockListView == false ) {
			Log.i("NormalFollowingViewFragment", "Loading next items");
			if(responseListSize >= 10) {
//				mListView.addFooterView(listFooter);
				requestFollowMuseData(lastPhotoId);
			}
//			} else
//				mListView.removeFooterView(listFooter);
			
			isLastVisible = true;
		}  
	}

	public void requestFollowMuseData(int photoId) {
		lockListView = true;
		
		if ( photoList != null) {
			Log.i("NormalFollowingView","onResume()");
			data = new PhotoData(userData.myIDNum, 10, photoId, "previous", "false");
			followRequest = new FollowPhotoRequest(new ArrayList<PhotoData>(), "picture/list/following?", data);
			followRequest.setOnGetMethodProcessListener(this);
			NetworkModel.getInstance().getNetworkData(getActivity(), followRequest);
		} else {
			isDataChanged = true;
		}
		
		if ( getActivity() != null )
			dialog = ProgressDialog.show(getActivity(), "���� ����� �ҷ����� ���Դϴ�.", "��ø� ��ٷ��ּ���.");
		
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {

		responseListSize = request.getResult().size();

		if ( responseListSize > 0)
			lastPhotoId = request.getResult().get(request.getResult().size() - 1).photoIdNum;
	
		if ( !isLastVisible && photoList.size() > 0 ) {
			photoList.clear();
//			mListView.setSelection(0);
		}
		
		photoList.addAll(request.getResult());
		
		lockListView = false;
		isDataChanged = false;
		
		adapter.notifyDataSetChanged();
		
		mPullRefreshListView.onRefreshComplete();
		
		dialog.dismiss();
		
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<HeartFavoriteData> request) {
		int heartFlag = photoList.get(photoIndex).recvHeartFlag;

		if (heartFlag == 0) {
			photoList.get(photoIndex).recvHeartFlag = 1;
			aq.id(R.id.museview_heart_btn).getImageView().setImageResource(R.drawable.heart);
		} else {
			photoList.get(photoIndex).recvHeartFlag = 0;
			aq.id(R.id.museview_heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		photoList.removeAll(photoList);
		requestFollowMuseData(0);
	}

	@Override
	public void onItemClick(View view, int position) {
		this.photoIndex = position;

		switch (view.getId()) {
		case R.id.museview_iv: {
			Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
			intent.putExtra("photoPath", photoList.get(photoIndex).photoPath);
			intent.putExtra("flag", "NormalViewFragment");
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
			NetworkModel.getInstance().sendNetworkData(getActivity(), heartRequest);
			break;
		}
		case R.id.museview_profile_iv:
		case R.id.museview_name_tv:
		case R.id.museview_id_tv:
			Intent intent = new Intent(getActivity(), MuseProfileActivity.class);
			intent.putExtra("userId", photoList.get(position).museIdNum);
			intent.putExtra("flag", "NormalViewFragment");
			startActivity(intent);
			break;
		}
	}


	@Override
	public void onPostMethodProcessError(PostNetworkRequest<HeartFavoriteData> request) {
		new ErrorDialog(getActivity(), request.getResult().errorCode).show();		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
	}

}
