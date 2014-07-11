package com.museda.detail;

import java.util.ArrayList;

import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.museda.ParentActivity;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
import com.museda.favoriteheartpic.HeartFavoriteData;
import com.museda.favoriteheartpic.SetHeartRequest;
import com.museda.main.fragment.MusePhotoRequest;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.photo.PhotoLockRequest;
import com.museda.photo.PhotoRemoveRequest;
import com.museda.profile.MuseProfileActivity;
import com.museda.profile.NormalProfileActivity;
import com.museda.util.ErrorDialog;
import com.museda.views.PhotoDetailView;

@SuppressWarnings("rawtypes")
public class PhotoDetailActivity extends ParentActivity implements OnClickListener, OnItemChangeListener, OnGetMethodProcessListener<ArrayList<PhotoData>>, OnPostMethodProcessListener {

	private UserData myInfo = SingletonData.getInstance().getUserData().get("UserData");

	private static final int[] BUTTON_ID = { R.id.photo1_iv, R.id.photo2_iv, R.id.photo3_iv, R.id.photo4_iv, R.id.photo5_iv, R.id.photo6_iv, R.id.more_iv, };
	
	private GalleryViewPager musePhotoPager;
	private AQuery aq;
	private UrlPagerAdapter pagerAdapter;
	
	private ArrayList<PhotoData> sendHeartUserList;
	private ArrayList<PhotoData> musePhotoList;
	private ArrayList<String> photoPathList;
	private ArrayList<ImageView> photoViewList;
	
	private SetHeartRequest heartRequest;
	private HeartRecvRequest heartRecvRequest;
	private MusePhotoRequest photoRequest;
	private PhotoData data;
	
	private PhotoDetailView detailView;
	
	//View
	private RelativeLayout photoInfoLayout;
	private RelativeLayout museViewLayout;
	private ImageView photoIv;
	private ImageView backBtn;
	private ImageView lockBtn;
	private ImageView removeBtn;
	private ImageView heartBtn;
	
	//Intent로 넘어오는 변수
	private int photoId;
	private int userId;
//	private int musePhotoIndex;
	private String photoPath;
	private String flag;

	private int photoIndex;
//	private int pagerIndex;
	private String userType;
	
	private boolean isLocked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_photodetail);
		
		aq = new AQuery(this);
		
		detailView = new PhotoDetailView(aq);
		photoViewList = new ArrayList<ImageView>();
		photoPathList = new ArrayList<String>();
		
		photoId = getIntent().getIntExtra("photoId", -1);
		flag = getIntent().getStringExtra("flag");
		userId = getIntent().getIntExtra("userId", -1);
		photoPath = getIntent().getStringExtra("photoPath");
//		musePhotoIndex = getIntent().getIntExtra("photoIndex", -1);
		
		heartBtn = (ImageView) findViewById(R.id.heart_btn);
		backBtn = (ImageView) findViewById(R.id.back_btn);
		removeBtn = (ImageView) findViewById(R.id.remove_btn);
		lockBtn = (ImageView) findViewById(R.id.lock_btn);
		photoInfoLayout = (RelativeLayout) findViewById(R.id.userinfo_layout);
		museViewLayout = (RelativeLayout) findViewById(R.id.museview_layout);
		photoIv = (ImageView) findViewById(R.id.musephoto_iv);
		musePhotoPager = (GalleryViewPager) findViewById(R.id.musephoto_pager);
		musePhotoPager.setPageMargin(20);
		musePhotoPager.setOffscreenPageLimit(3);

		heartBtn.setOnClickListener(this);
		photoIv.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		removeBtn.setOnClickListener(this);
		removeBtn.setOnClickListener(this);
		lockBtn.setOnClickListener(this);
		
		for (int id : BUTTON_ID) {
			ImageView imageView = (ImageView) findViewById(id);
			imageView.setOnClickListener(this);
			photoViewList.add(imageView);
		}
		
		if(myInfo.myIDNum == userId) {
			if (flag.equals("MuseAlbumFragment"))
				setViewVisibility(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
			else
				setViewVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.VISIBLE);
		} else
			setViewVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
		
		if(photoPath!=null) {
			setViewVisibility(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
			aq.id(photoIv).progress(R.id.progress).image(photoPath);
			photoIv.setVisibility(View.VISIBLE);
			musePhotoPager.setVisibility(View.GONE);	
			
		} else {
			photoIv.setVisibility(View.GONE);
			musePhotoPager.setVisibility(View.VISIBLE);
			data = new PhotoData(myInfo.myIDNum, userId, 100, 0, "next", "true");
			photoRequest = new MusePhotoRequest(new ArrayList<PhotoData>(), data);
			photoRequest.setOnGetMethodProcessListener(this);
			NetworkModel.getInstance().getNetworkData(this, photoRequest);
		}
	}

	public void setViewVisibility(int lock, int remove, int museview, int photoInfo, int backbtn) {
		lockBtn.setVisibility(lock);
		removeBtn.setVisibility(remove);
		museViewLayout.setVisibility(museview);
		photoInfoLayout.setVisibility(photoInfo);
		backBtn.setVisibility(backbtn);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.musephoto_iv:
			finish();
			break;
		case R.id.photo1_iv:
			showSendHeartUserProfile(0);
			break;
		case R.id.photo2_iv:
			showSendHeartUserProfile(1);
			break;
		case R.id.photo3_iv:
			showSendHeartUserProfile(2);
			break;
		case R.id.photo4_iv:
			showSendHeartUserProfile(3);
			break;
		case R.id.photo5_iv:
			showSendHeartUserProfile(4);
			break;
		case R.id.photo6_iv:
			showSendHeartUserProfile(5);
			break;
		case R.id.more_iv:
			Intent intent = new Intent(this, HeartMoreActivity.class);
			intent.putExtra("photoId", photoId);
			startActivity(intent);
			break;
		case R.id.heart_btn:
			int heartFlag = musePhotoList.get(photoIndex).recvHeartFlag;
			HeartFavoriteData data = null;
			if (heartFlag == 0)
				data = new HeartFavoriteData(myInfo.myIDNum, musePhotoList.get(photoIndex).museIdNum, musePhotoList.get(photoIndex).photoIdNum, "true");
			else
				data = new HeartFavoriteData(myInfo.myIDNum, musePhotoList.get(photoIndex).museIdNum, musePhotoList.get(photoIndex).photoIdNum, "false");
				
			heartRequest = new SetHeartRequest(data, "heart");
			heartRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, heartRequest);
			break;
		case R.id.lock_btn :
			if(musePhotoList.get(photoIndex).secretFlag == 0) {
				showLockDialog("사진을 비공개로 바꾸시겠습니까?", "true");
			}
			else {
				showLockDialog("사진을 공개로 바꾸시겠습니까?", "false");
			}
			break;
		case R.id.remove_btn :
			showRemoveDialog();
			break;
		case R.id.back_btn:
			finish();
			break;
		}
	}

	private void showRemoveDialog() {
		new AlertDialog.Builder(this).setTitle("사진 삭제").setMessage("사진에 포함된 모든 정보가 삭제됩니다. 정말 사진을 삭제하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PhotoData removeData = new PhotoData(myInfo.myIDNum, photoId);
				PhotoRemoveRequest removeRequest = new PhotoRemoveRequest(removeData);
				removeRequest.setOnPostMethodProcessListener(PhotoDetailActivity.this);
				NetworkModel.getInstance().sendNetworkData(PhotoDetailActivity.this, removeRequest);
			}
		}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).show();
	}

	private void showLockDialog(String msg, final String flag) {
		new AlertDialog.Builder(this).setTitle("사진 삭제").setMessage(msg).setPositiveButton("네", new DialogInterface.OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PhotoData lockRequestData = new PhotoData(myInfo.myIDNum, photoId, flag);
				PhotoLockRequest lockRequest = new PhotoLockRequest(lockRequestData);
				lockRequest.setOnPostMethodProcessListener(PhotoDetailActivity.this);
				NetworkModel.getInstance().sendNetworkData(PhotoDetailActivity.this, lockRequest);
			}
		}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).show();
	}

	private void showSendHeartUserProfile(int index) {
		
		userType = sendHeartUserList.get(index).userType;
		
		if(userType.equals("0")) {
			Intent intent = new Intent(this, NormalProfileActivity.class);
			intent.putExtra("flag", "PhotoDetailActivity");
			intent.putExtra("userId", sendHeartUserList.get(index).senderId);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, MuseProfileActivity.class);
			intent.putExtra("flag", "PhotoDetailActivity");
			intent.putExtra("userId", sendHeartUserList.get(index).senderId);
			startActivity(intent);
		}		
	}

	@Override
	public void onItemChange(int position) {
//		this.pagerIndex = position;
		photoIndex = position;
				
		if(flag.equals("MuseAlbumFragment")) {
			data = new PhotoData(myInfo.myIDNum, 7, musePhotoList.get(position).photoIdNum, 0);
			heartRecvRequest = new HeartRecvRequest(new ArrayList<PhotoData>(), data);
			heartRecvRequest.setOnGetMethodProcessListener(this);
			NetworkModel.getInstance().getNetworkData(this, heartRecvRequest);
			
		} else
			detailView.setPhotoView(musePhotoList.get(position));
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		
		ArrayList<PhotoData> result = request.getResult();
		
		if( request instanceof MusePhotoRequest) {
			musePhotoList = result ;
			for(int i=0;i<result.size();i++)
				photoPathList.add(result.get(i).photoPath);
			
			for(photoIndex=0;photoIndex<result.size();photoIndex++) {
				if(photoId == result.get(photoIndex).photoIdNum) 
					break;
			}
			
			pagerAdapter = new UrlPagerAdapter(this, photoPathList, aq);
			musePhotoPager.setAdapter(pagerAdapter);
			musePhotoPager.setCurrentItem(photoIndex);
			pagerAdapter.setOnItemChangeListener(this);

			//맨 처음 사진을 눌렀을 경우엔 ViewPager의 OnItemChange가 호출이 안돼서 사진 정보가 안불러 와지므로 호출함
			if ( flag.equals("MuseAlbumFragment")) {
				if(photoIndex == 0) {
					data = new PhotoData(myInfo.myIDNum, 7, photoId, 0);
					heartRecvRequest = new HeartRecvRequest(new ArrayList<PhotoData>(), data);
					heartRecvRequest.setOnGetMethodProcessListener(this);
					NetworkModel.getInstance().getNetworkData(this, heartRecvRequest);
				}
			}
			
		} else if (request instanceof HeartRecvRequest) {
			sendHeartUserList = result;
			detailView.setHeartRecvPhoto(result, photoViewList, musePhotoList.get(photoIndex).heartCount);
		}
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest request) {
		int heartFlag = musePhotoList.get(photoIndex).recvHeartFlag;
		
		if ( request instanceof SetHeartRequest ) {
			if(heartFlag == 0) {
				musePhotoList.get(photoIndex).recvHeartFlag = 1;
				musePhotoList.get(photoIndex).heartCount++;
				aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.heart);
			} else {
				musePhotoList.get(photoIndex).recvHeartFlag = 0;
				musePhotoList.get(photoIndex).heartCount--;
				aq.id(R.id.heart_btn).getImageView().setImageResource(R.drawable.btn_whiteheart_selector);
			}	
			detailView.setPhotoView(musePhotoList.get(photoIndex));
			UserData.lastSetHeartedTime = System.currentTimeMillis();
			
		} else if ( request instanceof PhotoRemoveRequest ) {
			UserData.photoModify = true;
			finish();
		} else if ( request instanceof PhotoLockRequest ) {
			if ( isLocked ) {
				musePhotoList.get(photoIndex).secretFlag = 0;
			} else {
				musePhotoList.get(photoIndex).secretFlag = 1;				
			}
			UserData.photoModify = true;
			isLocked = !isLocked;
		}
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest request) {
		new ErrorDialog(this,( ( HeartFavoriteData ) request.getResult() ).errorCode).show();
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
	}
}
