package com.museda.detail;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.ParentActivity;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.favoriteheartpic.HeartFavoriteData;
import com.museda.favoriteheartpic.SetFavoriteRequest;
import com.museda.favoriteheartpic.SetHeartRequest;
import com.museda.main.fragment.MusePhotoRequest;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.profile.MuseProfileActivity;
import com.museda.util.ErrorDialog;
import com.museda.views.DetailView;

/*
 * Favorite 경우 수정이 필요하다.
 * 현재는 다른 사진을 누르면 Favorite 버튼이 안보이도록 해놨다.
 * MusePhotoRequest에서 Favorite 변수들도 달라고 말하자.
 */

public class MuseDetailActivity extends ParentActivity implements OnClickListener, OnPostMethodProcessListener<HeartFavoriteData>, OnGetMethodProcessListener<ArrayList<PhotoData>> {

	private UserData userInfo = SingletonData.getInstance().getUserData().get("UserData");

	private MusePhotoRequest photoRequest;
	private SetHeartRequest heartRequest;
	private SetFavoriteRequest favoriteRequest;
	
	private PhotoData photoData;

	private AQuery aq;
	
	private ProgressDialog dialog;

	private TitleBar header;
	private ImageView heartBtn;
	private ImageView profilePhotoIv;
	private ImageView photoIv;
	private TextView nameTv;
	private TextView idTv;
	private ImageView favoriteBtn;
	private ImageView photo1;
	private ImageView photo2;
	private ImageView photo3;
	private ImageView photo4;
	private ImageView photo5;
	private ImageView moreBtn;
	private DetailView detailView;
	

	private ArrayList<ImageView> photoViewList;
	private ArrayList<PhotoData> photoList;

	// Itent 변수들
	private int userId;
	private String flag = " ";
	private String favoriteFlag = " ";
	private String photoPath = " ";
	private int heartFlag;
	private int photoHeight;
	private int photoWidth;
	private int photoId;
	
	//하트 or 즐겨찾기 요청을 구분할 변수
	private String requestFlag;
	
	//하트 or 즐겨찾기 전송 후 상태 변경을 위한 변수
	private int photoIndex = -1;
	
	private boolean isClickPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_musedetail);

		aq = new AQuery(this);
		detailView = new DetailView(aq);

		photoIv = (ImageView) findViewById(R.id.museview_iv);
		heartBtn = (ImageView) findViewById(R.id.heart_btn);
		profilePhotoIv = (ImageView) findViewById(R.id.museview_porfile_iv);
		nameTv = (TextView) findViewById(R.id.museview_name_tv);
		idTv = (TextView) findViewById(R.id.museview_id_tv);
		favoriteBtn = (ImageView) findViewById(R.id.favorite_iv);
		moreBtn = (ImageView) findViewById(R.id.morebtn);
		header = (TitleBar) findViewById(R.id.titlebar);
		photo1 = (ImageView) findViewById(R.id.photo1);
		photo2 = (ImageView) findViewById(R.id.photo2);
		photo3 = (ImageView) findViewById(R.id.photo3);
		photo4 = (ImageView) findViewById(R.id.photo4);
		photo5 = (ImageView) findViewById(R.id.photo5);

		header.setSettingBtnVisible(false);
		header.setBackBtnVisible(false);
		photoIv.setOnClickListener(this);
		photo1.setOnClickListener(this);
		photo2.setOnClickListener(this);
		photo3.setOnClickListener(this);
		photo4.setOnClickListener(this);
		photo5.setOnClickListener(this);
		moreBtn.setOnClickListener(this);
		favoriteBtn.setOnClickListener(this);
		heartBtn.setOnClickListener(this);
		profilePhotoIv.setOnClickListener(this);
		nameTv.setOnClickListener(this);
		idTv.setOnClickListener(this);

		photoViewList = new ArrayList<ImageView>();
		photoViewList.add(photo1);
		photoViewList.add(photo2);
		photoViewList.add(photo3);
		photoViewList.add(photo4);
		photoViewList.add(photo5);
		photoViewList.add(moreBtn);

		photoId = getIntent().getIntExtra("photoId", -1);
		photoWidth = getIntent().getIntExtra("width", -1);
		photoHeight = getIntent().getIntExtra("height", -1);
//		heartFlag = getIntent().getIntExtra("heartFlag", -1);
		userId = getIntent().getIntExtra("userId", -1);
		flag = getIntent().getStringExtra("flag");
		photoPath = getIntent().getStringExtra("photoPath");
		favoriteFlag = getIntent().getStringExtra("favoriteFlag");

		if (flag.equals("HeartFavoriteListActivity")) {

			if (userInfo.userType.equals("0")) {
				favoriteBtn.setVisibility(View.VISIBLE);
				
				if (favoriteFlag.equals("true"))
					favoriteBtn.setImageResource(R.drawable.page_userid_fav2);
				else
					favoriteBtn.setImageResource(R.drawable.page_userid_fav1);
			} else
				favoriteBtn.setVisibility(View.GONE);

			heartBtn.setImageResource(R.drawable.heart);

		} else {

			favoriteBtn.setVisibility(View.GONE);

//			if (heartFlag == 1)
//				heartBtn.setImageResource(R.drawable.heart);
//			else
//				heartBtn.setImageResource(R.drawable.lineheart_selector);
			
		}

		//큰 이미지를 보여줌
		detailView.setImage( (float)photoHeight, (float) photoWidth, photoPath );

		photoData = new PhotoData(userInfo.myIDNum, userId, 6, 0, "next", "false");
		photoRequest = new MusePhotoRequest(new ArrayList<PhotoData>(), photoData);
		photoRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, photoRequest);

		dialog = ProgressDialog.show(this, "사진을 불러오는 중입니다.", "잠시만 기다려주세요.", true);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.morebtn:
		case R.id.museview_id_tv:
		case R.id.museview_porfile_iv:
		case R.id.museview_name_tv: {
			Intent intent = new Intent(MuseDetailActivity.this, MuseProfileActivity.class);
			intent.putExtra("flag", "DetailMuseActivity");
			intent.putExtra("userId", userId);
			startActivity(intent);
			overridePendingTransition(R.anim.from_left_out, R.anim.from_left_in);
			break;
		}
		case R.id.heart_btn: {
			requestFlag = "Heart";
			sendHeartFavorite("heart");
			break;
		}
		case R.id.favorite_iv:{
			requestFlag = "Favorite";
			sendHeartFavorite("favorites");
			break;
		}
		case R.id.museview_iv: {
			
			Intent intent = new Intent(this, PhotoDetailActivity.class);
			intent.putExtra("flag", "MuseDetailActivity");
			intent.putExtra("photoId", photoId);
			intent.putExtra("userId", photoList.get(0).museIdNum);
			startActivity(intent);
			break;
		}
		case R.id.photo1:
			setClickPhoto(0);
			break;
		case R.id.photo2:
			setClickPhoto(1);
			break;
		case R.id.photo3:
			setClickPhoto(2);
			break;
		case R.id.photo4:
			setClickPhoto(3);
			break;
		case R.id.photo5:
			setClickPhoto(4);
			break;
		}
	}

	private void setClickPhoto(int index) {
		favoriteBtn.setVisibility(View.GONE);
		isClickPhoto = true;
		favoriteFlag = photoList.get(index).favoriteFlag;
		photoId = photoList.get(index).photoIdNum;
		photoIndex = index;
		heartFlag = photoList.get(index).recvHeartFlag;
		detailView.setPhotoIndexInfo(photoList, index);
	}
	
	private void sendHeartFavorite(String request) {
		
		HeartFavoriteData data = null;
		String flag = "";
		
		if(request.equals("heart")) {
			if(heartFlag == 0) 
				flag = "true";
			else 
				flag = "false";
			
			data = new HeartFavoriteData(userInfo.myIDNum, userId, photoId, flag);
			heartRequest = new SetHeartRequest(data, request);
			heartRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, heartRequest);		
			
		} else {
			if(favoriteFlag.equals("false")) 
				flag = "true";
			else 
				flag = "false";
			data = new HeartFavoriteData(userInfo.myIDNum, photoId, flag);
			favoriteRequest = new SetFavoriteRequest(data, request);
			favoriteRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, favoriteRequest);					
		}

	}
	

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<HeartFavoriteData> request) {
		if(requestFlag.equals("Heart")) {
			if (heartFlag == 0) {
				heartBtn.setImageResource(R.drawable.heart);
				heartFlag = 1;
				
				for(int i=0;i<photoList.size();i++) {
					if(photoId == photoList.get(i).photoIdNum) {
						photoList.get(i).recvHeartFlag = 1;
						break;
					}
				}
					
				if(isClickPhoto)
					photoList.get(photoIndex).recvHeartFlag = 1;
			} else {
				heartFlag = 0;
				heartBtn.setImageResource(R.drawable.btn_whiteheart_selector);
				
				for(int i=0;i<photoList.size();i++) {
					if(photoId == photoList.get(i).photoIdNum) {
						photoList.get(i).recvHeartFlag = 0;
						break;
					}
				}
				
				if(isClickPhoto)
					photoList.get(photoIndex).recvHeartFlag = 0;
			}
			
			UserData.lastSetHeartedTime = System.currentTimeMillis();
			
		} else {
			if (favoriteFlag.equals("true")) {
				favoriteFlag = "false";
				favoriteBtn.setImageResource(R.drawable.page_userid_fav1);
//				if(isClickPhoto)
//					photoList.get(photoIndex).favoriteFlag="false";
			} else {
				
				favoriteFlag = "true";
				favoriteBtn.setImageResource(R.drawable.page_userid_fav2);
//				if(isClickPhoto)
//					photoList.get(photoIndex).favoriteFlag="true";
			}
		}
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<HeartFavoriteData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
	
		photoList = request.getResult();
		header.setTitleText(photoList.get(0).museName);
		detailView.setDetailView(photoList, photoViewList);
		
		dialog.dismiss();
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
	}

}
