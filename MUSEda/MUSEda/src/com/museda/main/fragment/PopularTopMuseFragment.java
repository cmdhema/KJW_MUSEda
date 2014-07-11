package com.museda.main.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
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
import com.museda.views.PopularTopView;

public class PopularTopMuseFragment extends Fragment implements OnClickListener, OnPostMethodProcessListener<HeartFavoriteData>, OnGetMethodProcessListener<ArrayList<PhotoData>> {

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");
	
	private ArrayList<PhotoData> photoList;
	
	//View
	private ImageView leftBtn;
	private ImageView rightBtn;
	private ImageView heartBtn;
	private ImageView profileIv;
	private ImageView photoIv;
	private TextView nameTv;
	private TextView idTv;

	//접속 요청
	private MuseSearchRequest searchRequest;
	private SetHeartRequest heartRequest;
	
	private PhotoData data;
	
	private PopularTopView photoView;
	
	private int photoIndex;
	
	private AQuery aq;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.layout_populartopmuse, container, false);
		
		leftBtn = (ImageView) view.findViewById(R.id.left_btn);
		rightBtn = (ImageView) view.findViewById(R.id.right_btn);
		heartBtn = (ImageView) view.findViewById(R.id.musesearch_heart_btn);
		profileIv = (ImageView) view.findViewById(R.id.musesearch_porfile_iv);
		nameTv = (TextView) view.findViewById(R.id.musesearch_name_tv);
		idTv = (TextView) view.findViewById(R.id.musesearch_id_tv);
		photoIv = (ImageView) view.findViewById(R.id.musesearch_iv);
		
		profileIv.setOnClickListener(this);
		nameTv.setOnClickListener(this);
		idTv.setOnClickListener(this);
		photoIv.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		heartBtn.setOnClickListener(this);
		
//		photoIv.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		aq = new AQuery(getActivity());
		
		//View 객체 초기화
		photoView = new PopularTopView(aq);
		
		//Popular, Top 구분
		String flag = getArguments().getString("flag");
		
		//요청 객체 설정
		data = initSearchRequestData();
		
		searchRequest = new MuseSearchRequest(new ArrayList<PhotoData>(), data, flag);
		searchRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(getActivity(), searchRequest);
		
		return view;
	}

	private PhotoData initSearchRequestData() {
		data = new PhotoData();
		data.myIdNum = userData.myIDNum;
		data.count = 100;
		data.startPicNum = 0;
		data.direction = "next";
		
		return data;
	}


	@Override
	public void onClick(View v) {
		if ( v== photoIv && photoList.size() != 0 ) {
			
			Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
			intent.putExtra("photoPath", photoList.get(photoIndex).photoPath);
			intent.putExtra("flag", "PopularTopMuseFragment");
			intent.putExtra("userId", photoList.get(photoIndex).museIdNum);
			startActivity(intent);

		} else if ( v == idTv || v == nameTv || v == profileIv && photoList.size() != 0 ) {
			
			Intent intent = new Intent(getActivity(), MuseProfileActivity.class);
			intent.putExtra("userId", photoList.get(photoIndex).museIdNum);
			intent.putExtra("flag", "PopularTopMuseFragment");
			startActivity(intent);	

		} else if ( v== leftBtn && photoList.size() != 0 ) {
			
			if(photoIndex == 0)
				photoIndex = photoList.size();
			
			photoView.setView(photoList, --photoIndex, -1);
			
		} else if ( v== rightBtn && photoList.size() != 0 ) {
			
			if (photoIndex == photoList.size() - 1) 
				photoIndex = -1;
			
			photoView.setView(photoList, ++photoIndex, 1);
			
		} else if ( v == heartBtn && photoList.size() != 0 ) {
			HeartFavoriteData data = null;
			int heartFlag = photoList.get(photoIndex).recvHeartFlag;
			int myId = userData.myIDNum;
			int museId = photoList.get(photoIndex).museIdNum;
			int photoId = photoList.get(photoIndex).photoIdNum;
			String flag = "";
			
			if(heartFlag == 0) 
				flag = "true";
			else 
				flag = "false";
			
			data = new HeartFavoriteData(myId, museId, photoId, flag);
			
			heartRequest = new SetHeartRequest(data, "heart");
			heartRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(getActivity(), heartRequest);
			
		}
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		Log.i("PopularTopMuseFragment","Get Search Success");
		
		photoList = request.getResult();
		
		if ( photoList.size() > 0 ) {
			//List를 랜덤으로 섞음
			Collections.shuffle(photoList);
			
			photoView.setView(photoList, 0, 0);
		} else {
			photoView.setInit();
		}
	}


	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<HeartFavoriteData> request) {
		Log.i("PopularTopMuseFragment","Heart Success");
		
		photoView.changeHeartStatus(photoList, photoIndex);		
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<HeartFavoriteData> request) {
		new ErrorDialog(getActivity(), request.getResult().errorCode);
	}
}
