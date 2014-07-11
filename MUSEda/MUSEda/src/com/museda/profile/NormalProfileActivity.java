package com.museda.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.museda.ParentActivity;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.detail.PhotoDetailActivity;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.photo.PhotoCheckActivity;
import com.museda.photo.PhotoCoverRemoveRequest;
import com.museda.photo.PhotoProfileRemoveRequest;
import com.museda.util.ErrorDialog;
import com.museda.util.FileUtil;
import com.museda.views.NormalProfileView;

public class NormalProfileActivity extends ParentActivity implements OnClickListener, OnGetMethodProcessListener<UserData>, OnPostMethodProcessListener<UserData> {

	private UserData userInfo = SingletonData.getInstance().getUserData().get("UserData");
	
//	private String photoDialogList[] = { "앨범에서 사진 선택", "촬영", "확대보기", "삭제", "취소" };
//	private String coverDialogList[] = { "앨범에서 사진 선택", "촬영", "삭제", "취소" };
	private String photoDialogList[] = { "앨범에서 사진 선택", "확대보기", "삭제", "취소" };
	private String coverDialogList[] = { "앨범에서 사진 선택", "삭제", "취소" };

	private final int PICK_FROM_CAMERA = 0;
	private final int PICK_FROM_ALBUM = 1;
	private final int CROP_FROM_CAMERA = 2;
	private final int MODIFY_OK = 3;

	private Uri mImageCaptureUri;
	private Uri cropImageUri;
	
	private AQuery aq;

	private String flag = "";

	private UserDataRequest dataRequest;
	private UserData data;

	// View
	private TitleBar header;
	private ImageView profileModifyIv;
	private ImageView introduceModifyIv;
	private NormalProfileView profileView;
	private ImageView coverPhotoIv;
	private ImageView profilePhotoIv;
	private ImageView photo1Iv;
	private ImageView photo2Iv;
	private ImageView photo3Iv;

	// Intent로 받을 프로필 볼 사용자의 아이디이
	private int userId;
	
	//사진 4개중 하나 Index
	private int photoIndex;

	private UserData resultNormalUserData;

	private String photoUpFlag;

	private long tempTime;

	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_normalprofile);

		aq = new AQuery(this);
		profileView = new NormalProfileView(aq);

		header = (TitleBar) findViewById(R.id.titlebar);
		profileModifyIv = (ImageView) findViewById(R.id.profile_modify_btn);
		introduceModifyIv = (ImageView) findViewById(R.id.normalprofile_introducemodify_btn);
		coverPhotoIv = (ImageView) findViewById(R.id.normalprofile_cover_iv);
		profilePhotoIv = (ImageView) findViewById(R.id.normalprofile_profile_iv);
		photo1Iv = (ImageView) findViewById(R.id.photo1_iv);
		photo2Iv = (ImageView) findViewById(R.id.photo2_iv);
		photo3Iv = (ImageView) findViewById(R.id.photo3_iv);

		coverPhotoIv.setOnClickListener(this);
		profileModifyIv.setOnClickListener(this);
		introduceModifyIv.setOnClickListener(this);
		profilePhotoIv.setOnClickListener(this);
		photo1Iv.setOnClickListener(this);
		photo2Iv.setOnClickListener(this);
		photo3Iv.setOnClickListener(this);
		
		userId = getIntent().getIntExtra("userId", -1);
		flag = getIntent().getStringExtra("flag");
		
		if ( ! flag.equals("NormalMypageFragment")) {
			setBtnSetting(false, View.INVISIBLE, View.INVISIBLE);
			normalUserInfoRequest();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		userInfo = SingletonData.getInstance().getUserData().get("UserData");

		if ( flag.equals("NormalMypageFragment") || UserData.profileModify ) {
			UserData.profileModify = false;
			header.setTitleText(userInfo.myName);
			setBtnSetting(true, View.VISIBLE, View.VISIBLE);
			profileView.setProfileView(userInfo);
		}
	}
	
	private void normalUserInfoRequest() {
		
		data = new UserData(userInfo.myIDNum, userId);
		dataRequest = new UserDataRequest(new UserData(), data);
		dataRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, dataRequest);		

		dialog = ProgressDialog.show(this, "사진을 불러오는 중입니다.", "잠시만 기다려주세요.", true);
	}

	@Override
	public void onClick(View v) {
		if (v == coverPhotoIv) {
			photoUpFlag = "cover";
			if (flag.equals("NormalMypageFragment"))
				showCoverPhotoDialog(userInfo.coverPhotoPath);

		} else if (v == profilePhotoIv) {

			photoUpFlag = "profile";
			if (flag.equals("NormalMypageFragment")) {
				showPhotoDiaglog(userInfo.profilePhotoPath, 0);
			} else {
				Intent intent = new Intent(NormalProfileActivity.this, PhotoDetailActivity.class);
				intent.putExtra("photoPath", resultNormalUserData.profilePhotoPath);
				intent.putExtra("flag", "NormalProfileActivity");
				startActivity(intent);
			}

		} else if (v == photo1Iv) {
			photoUpFlag = "profile";
			if (flag.equals("NormalMypageFragment"))
				showPhotoDiaglog(userInfo.myPhoto1Path, 1);
			else {
				Intent intent = new Intent(NormalProfileActivity.this, PhotoDetailActivity.class);
				intent.putExtra("photoPath", resultNormalUserData.myPhoto1Path);
				intent.putExtra("flag", "NormalProfileActivity");
				startActivity(intent);
			}

		} else if (v == photo2Iv) {
			photoUpFlag = "profile";
			if (flag.equals("NormalMypageFragment")) 
				showPhotoDiaglog(userInfo.myPhoto2Path, 2);
			else {
				Intent intent = new Intent(NormalProfileActivity.this, PhotoDetailActivity.class);
				intent.putExtra("photoPath", resultNormalUserData.myPhoto2Path);
				intent.putExtra("flag", "NormalProfileActivity");
				startActivity(intent);
			}

		} else if (v == photo3Iv) {
			photoUpFlag = "profile";
			if (flag.equals("NormalMypageFragment")) {
				showPhotoDiaglog(userInfo.myPhoto3Path, 3);
			} else {
				Intent intent = new Intent(NormalProfileActivity.this, PhotoDetailActivity.class);
				intent.putExtra("photoPath", resultNormalUserData.myPhoto3Path);
				intent.putExtra("flag", "NormalProfileActivity");
				startActivity(intent);
			}

		} else if (v == profileModifyIv) {
			Intent intent = new Intent(this, ModifyUserInfoActivity.class);
			intent.putExtra("flag","normal");
			startActivityForResult(intent, MODIFY_OK);
		} else if (v == introduceModifyIv) {
			Intent intent = new Intent(this, ModifyNormalInfoActivity.class);
			intent.putExtra("flag","normal");
			startActivityForResult(intent, MODIFY_OK);
		}
	}

	private void showPhotoDiaglog(final String url, final int index) {
		new AlertDialog.Builder(this).setTitle("프로필 이미지 편집").setItems(photoDialogList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int position) {
				
				photoIndex = index;
				
//				if (position == 0)
//					doTakeAlbumAction();
//				else if (position == 1)
//					doTakePhotoAction();
//				else if (position == 2) {
//					Intent intent = new Intent(NormalProfileActivity.this, PhotoDetailActivity.class);
//					intent.putExtra("photoPath", url);
//					startActivity(intent);
//				} else if (position == 3)
//					deletePhoto(index);
				if (position == 0)
					doTakeAlbumAction();
				else if (position == 1) {
					Intent intent = new Intent(NormalProfileActivity.this, PhotoDetailActivity.class);
					intent.putExtra("photoPath", url);
					startActivity(intent);
				} else if (position == 2)
					deletePhoto(index);

			}

		}).show();
	}

	private void showCoverPhotoDialog(final String url) {
		new AlertDialog.Builder(this).setTitle("커머 이미지 편집").setItems(coverDialogList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int position) {
//				if (position == 0)
//					doTakeAlbumAction();
//				else if (position == 1)
//					doTakePhotoAction();
//				else if (position == 2)
//					deleteCoverPhoto();

				if (position == 0)
					doTakeAlbumAction();
				else if (position == 1)
					deleteCoverPhoto();

			}
		}).show();
	}

	public void setBtnSetting(boolean cover, int profileModify, int introduceModify) {
		coverPhotoIv.setClickable(cover);
		profileModifyIv.setVisibility(profileModify);
		introduceModifyIv.setVisibility(introduceModify);
	}


	private void deletePhoto(int index) {
		UserData data = new UserData(userInfo.myIDNum, index, "remove");
		PhotoProfileRemoveRequest request = new PhotoProfileRemoveRequest(data);
		request.setOnPostMethodProcessListener(this);
		NetworkModel.getInstance().sendNetworkData(this, request);
	}

	protected void deleteCoverPhoto() {
		UserData data = new UserData(userInfo.myIDNum, "remove");
		PhotoCoverRemoveRequest request = new PhotoCoverRemoveRequest(data);
		request.setOnPostMethodProcessListener(this);
		NetworkModel.getInstance().sendNetworkData(this, request);
	}

	  /**
     * 앨범 호출 하기
     */
    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


	/**
     * Result Code
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

    	if(resultCode != RESULT_OK)
            return;

    	Log.i("NormalProfileActivity", "RESULT_OK");
    	
        switch(requestCode) {

        case PICK_FROM_ALBUM : {
             
            mImageCaptureUri = data.getData();
        }
        case PICK_FROM_CAMERA : {
 
            // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
            // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
 
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(mImageCaptureUri, "image/*"); 
            tempTime = System.currentTimeMillis();
            cropImageUri = FileUtil.createSaveCropFile(tempTime);
            // Crop한 이미지를 저장할 Path
            intent.putExtra("output", cropImageUri);
            
            if ( photoUpFlag.equals("profile")) {
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
            } else {
                intent.putExtra("aspectX", 3);
                intent.putExtra("aspectY", 2);	
            }
            startActivityForResult(intent, CROP_FROM_CAMERA);
 
            break;
        }
        
        case CROP_FROM_CAMERA: {
			Intent intent = new Intent(this, PhotoCheckActivity.class);
			intent.putExtra("path", FileUtil.getFilePath(tempTime));
			intent.putExtra("flag", photoUpFlag);
			intent.putExtra("index", photoIndex);
			startActivityForResult(intent, MODIFY_OK);
            break;
        }
        case MODIFY_OK :
        	Log.i("NormalProfileActivity", "MODEFY_OK");
        	normalUserInfoRequest();
        	break;
        }
    }


	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<UserData> request) {
		resultNormalUserData = request.getResult();
		header.setTitleText(resultNormalUserData.myName);
		profileView.setProfileView(resultNormalUserData);
		
		dialog.dismiss();
	}


	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		
		if( request instanceof PhotoProfileRemoveRequest) {
			if( photoIndex == 0 ) {
				userInfo.profilePhotoPath = "";
				userInfo.profileThumbPhotoPath = "";
			} else if ( photoIndex == 1) {
				userInfo.myPhoto1Path = "";
				userInfo.myPhoto1ThumbPhotoPath = "";
			} else if ( photoIndex == 2) {
				userInfo.myPhoto2Path = "";
				userInfo.myPhoto2ThumbPhotoPath = "";
			} else if ( photoIndex == 3) {
				userInfo.myPhoto3Path = "";
				userInfo.myPhoto3ThumbPhotoPath = "";
			}
			profileView.setProfilePhoto(photoIndex, Uri.parse(""));
			
		} else if ( request instanceof PhotoCoverRemoveRequest) { 
			userInfo.coverPhotoPath = "";
			profileView.setCoverPhoto(Uri.parse(""));
		}
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();
	}
	
	@Override
	public void onGetMethodProcessError(GetNetworkRequest<UserData> request) {
	}

}
