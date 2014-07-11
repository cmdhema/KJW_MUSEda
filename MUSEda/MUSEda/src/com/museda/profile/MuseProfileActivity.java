package com.museda.profile;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.museda.ParentActivity;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.adapter.GridAdapter;
import com.museda.detail.PhotoDetailActivity;
import com.museda.main.fragment.MusePhotoRequest;
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
import com.museda.util.ResultDialog;
import com.museda.views.MuseProfileView;

@SuppressWarnings("rawtypes")
public class MuseProfileActivity extends ParentActivity implements OnClickListener, OnItemClickListener, OnGetMethodProcessListener,
		OnPostMethodProcessListener {

//	private String photoDialogList[] = { "앨범에서 사진 선택", "촬영", "확대보기", "삭제", "취소" };
//	private String coverDialogList[] = { "앨범에서 사진 선택", "촬영", "삭제", "취소" };

	private String photoDialogList[] = { "앨범에서 사진 선택", "확대보기", "삭제", "취소" };
	private String coverDialogList[] = { "앨범에서 사진 선택", "삭제", "취소" };
	
	private final int PICK_FROM_CAMERA = 0;
	private final int PICK_FROM_ALBUM = 1;
	private final int CROP_FROM_CAMERA = 2;
	private final int MODIFY_OK = 3;
	
	private Uri mImageCaptureUri;

	private UserData userInfo;

	public int visible = View.VISIBLE;
	public int invisible = View.INVISIBLE;

	private AQuery aq;

	// View
	private ImageView followBtn;
	private ImageView myMuseBtn;
	private ImageView museBtn;
	private ImageView followingBtn;
	private ImageView followLongBtn;
	private ImageView modifyBtn;
	private RelativeLayout buttonsLayout;
	private ImageView coverIv;
	private ImageView profileIv;
	private GridView musePhotoGridView;
	private TitleBar header;
	private MuseProfileView profileView;

	private MuseFollowRequestData museFollowData;
	// 프로필 볼 사람의 ID
	private int userId;

	// Intent에서 넘어오는 Activity
	private String flag = "";

	private MusePhotoRequest photoRequest;
	private UserDataRequest dataRequest;
	private UserData userData;

	// Result로 얻어온 뮤즈 정보
	private UserData resultMuseData;
	private PhotoData photoData;

	// Muse or Follow로 등록하는 Request
	private MuseFollowRequest myMuseRequest;
	private MuseFollowRequest unMuseRequest;
	private MuseFollowRequest followRequest;
	private MuseFollowRequest unFollowRequest;

	private ArrayList<PhotoData> photoList;

	private String photoUpFlag;

	private static boolean isMuseUpdated;
	private static boolean isFollowUpdated;
	
	private OnMuseUpdateCallback museStatusCallback;
	
	private long tempTime;
	private Uri cropImageUri;
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_museprofile);

		aq = new AQuery(this);
		profileView = new MuseProfileView(aq);

		followBtn = (ImageView) findViewById(R.id.follow_btn);
		followingBtn = (ImageView) findViewById(R.id.following_btn);
		followLongBtn = (ImageView) findViewById(R.id.follow_long_btn);
		myMuseBtn = (ImageView) findViewById(R.id.mymuse_btn);
		museBtn = (ImageView) findViewById(R.id.muse_btn);
		modifyBtn = (ImageView) findViewById(R.id.modify_btn);
		musePhotoGridView = (GridView) findViewById(R.id.musephoto_gridview);
		coverIv = (ImageView) findViewById(R.id.museprofile_cover_iv);
		profileIv = (ImageView) findViewById(R.id.museprofile_profile_iv);
		header = (TitleBar) findViewById(R.id.titlebar);
		buttonsLayout = (RelativeLayout) findViewById(R.id.buttons_layout);

		header.setBackBtnVisible(false);
		followBtn.setOnClickListener(this);
		followingBtn.setOnClickListener(this);
		followLongBtn.setOnClickListener(this);
		myMuseBtn.setOnClickListener(this);
		museBtn.setOnClickListener(this);
		modifyBtn.setOnClickListener(this);
		profileIv.setOnClickListener(this);
		coverIv.setOnClickListener(this);
		musePhotoGridView.setOnItemClickListener(this);

		userId = getIntent().getIntExtra("userId", -1);
		flag = getIntent().getStringExtra("flag");

		
		userInfo = SingletonData.getInstance().getUserData().get("UserData");
		
		if (flag.equals("MuseMypageFragment") || userId == userInfo.myIDNum) { // 내 프로필사진이므로 수정 버튼 보여준다.
			showMyInfo();
			getMusePhotoData(userInfo.myIDNum, userId, 20, 0, "next");
		} else 
			getMuseInfo();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		userInfo = SingletonData.getInstance().getUserData().get("UserData");
		initViewStatus();
		
	}

	private void initViewStatus() {
		
		if ( UserData.profileModify ) {

			UserData.profileModify = false;
			
			Log.i("MuseProfileActivity", "ProfileModifyTrue");

			if (flag.equals("MuseMypageFragment") || userId == userInfo.myIDNum) { // 내 프로필사진이므로 수정 버튼 보여준다.
				showMyInfo();
				getMusePhotoData(userInfo.myIDNum, userId, 20, 0, "next");
			} else 
				getMuseInfo();
			
		} 
	}

	private void showMyInfo() {
		header.setTitleText(userInfo.myName);
		btnVisibility(invisible, invisible, invisible, invisible, visible, invisible);
		buttonsLayout.setVisibility(View.GONE);
		coverIv.setClickable(true);
		coverIv.setOnClickListener(this);
		profileView.setProfileView(userInfo);		
	}

	@SuppressWarnings("unchecked")
	private void getMusePhotoData(int myId, int userId, int count, int photoId, String direction) {

		dialog = ProgressDialog.show(this, "사진을 불러오는 중입니다.", "잠시만 기다려주세요.", true);
		dialog.setCancelable(true);
		
		if (myId == userId)
			photoData = new PhotoData(myId, userId, count, photoId, direction, "true");
		else
			photoData = new PhotoData(myId, userId, count, photoId, direction, "false");

		photoRequest = new MusePhotoRequest(new ArrayList<PhotoData>(), photoData);
		photoRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, photoRequest);
	}

	@SuppressWarnings("unchecked")
	private void getMuseInfo() {
		userData = new UserData(userInfo.myIDNum, userId);
		dataRequest = new UserDataRequest(new UserData(), userData);
		dataRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, dataRequest);
//		dialog = ProgressDialog.show(this, "사진을 불러오는 중입니다.", "잠시만 기다려주세요.", true);
	}

	public void btnVisibility(int follow, int following, int muse, int mymuse, int modify, int followLong) {
		followBtn.setVisibility(follow);
		followingBtn.setVisibility(following);
		museBtn.setVisibility(muse);
		myMuseBtn.setVisibility(mymuse);
		followLongBtn.setVisibility(followLong);
		modifyBtn.setVisibility(modify);
	}

	@Override
	public void onClick(View v) {
		if (v == profileIv) {
			photoUpFlag = "profile";
			if (flag.equals("MuseMypageFragment")) {
				showPhotoDiaglog(userInfo.profilePhotoPath, 0);
			} else {
				Intent intent = new Intent(MuseProfileActivity.this, PhotoDetailActivity.class);
				intent.putExtra("photoPath", resultMuseData.profilePhotoPath);
				startActivity(intent);
			}

		} else if (v == coverIv) {
			photoUpFlag = "cover";
			if (flag.equals("MuseMypageFragment"))
				showCoverPhotoDialog(userInfo.coverPhotoPath);
		} else if (v == modifyBtn) {
			Intent intent = new Intent(this, ModifyUserInfoActivity.class);
			intent.putExtra("flag","muse");
			startActivityForResult(intent, MODIFY_OK);
		} else if (v == followBtn || v == followLongBtn) {
			setMuseFollow("Follow");
		} else if (v == followingBtn) {
			showSetUnFollowDialog();
		} else if (v == myMuseBtn) {
			showSetUnMuseDialog();
		} else if (v == museBtn) {
			showSetMuseDialog();
		}
	}

	private void showSetUnMuseDialog() {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
		alert_confirm.setMessage("언팔로우 하시겠습니까?").setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setMuseFollow("UnMuse");
			}
		}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		AlertDialog alert = alert_confirm.create();
		alert.show();
	}

	private void showSetUnFollowDialog() {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
		alert_confirm.setMessage("언팔로우 하시겠습니까?").setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setMuseFollow("UnFollow");
			}
		}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		AlertDialog alert = alert_confirm.create();
		alert.show();
	}

	private void showSetMuseDialog() {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
		alert_confirm.setMessage("뮤즈는 단 한명만 선택 가능합니다.\n당신의 뮤즈를 변경하시겠습니까?").setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setMuseFollow("Muse");
			}
		}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		AlertDialog alert = alert_confirm.create();
		alert.show();
	}

	@SuppressWarnings("unchecked")
	private void setMuseFollow(String flag) {

		if (flag.equals("Muse")) {
			museFollowData = new MuseFollowRequestData(userInfo.myIDNum, userId, "add");
			myMuseRequest = new MuseFollowRequest(new MuseFollowRequestData(), museFollowData, "user/mymuse");
			myMuseRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, myMuseRequest);
		} else if (flag.equals("UnMuse")) {
			museFollowData = new MuseFollowRequestData(userInfo.myIDNum, userId, "remove");
			unMuseRequest = new MuseFollowRequest(new MuseFollowRequestData(), museFollowData, "user/mymuse");
			unMuseRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, unMuseRequest);
		} else if (flag.equals("Follow")) {
			museFollowData = new MuseFollowRequestData(userData.myIDNum, userId, "add");
			followRequest = new MuseFollowRequest(new MuseFollowRequestData(), museFollowData, "user/following");
			followRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, followRequest);

		} else if (flag.equals("UnFollow")) {
			museFollowData = new MuseFollowRequestData(userData.myIDNum, userId, "remove");
			unFollowRequest = new MuseFollowRequest(new MuseFollowRequestData(), museFollowData, "user/following");
			unFollowRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, unFollowRequest);
		}

	}

	private void showCoverPhotoDialog(String coverPhotoPath) {
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

	private void showPhotoDiaglog(final String profilePhotoPath, final int index) {
		new AlertDialog.Builder(this).setTitle("프로플 사진 편집").setItems(photoDialogList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int position) {
//				if (position == 0)
//					doTakeAlbumAction();
//				else if (position == 1)
//					doTakePhotoAction();
//				else if (position == 2) {
//					Intent intent = new Intent(MuseProfileActivity.this, PhotoDetailActivity.class);
//					intent.putExtra("photoPath", profilePhotoPath);
//					startActivity(intent);
//				} else if (position == 3)
//					deletePhoto(index);
				
				if (position == 0)
					doTakeAlbumAction();
				else if (position == 1) {
					Intent intent = new Intent(MuseProfileActivity.this, PhotoDetailActivity.class);
					intent.putExtra("photoPath", profilePhotoPath);
					startActivity(intent);
				} else if (position == 2)
					deletePhoto(index);
				
			}
		}).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(this, PhotoDetailActivity.class);
		intent.putExtra("flag", "MuseDetailActivity");
		intent.putExtra("photoId", photoList.get(position).photoIdNum);
		intent.putExtra("userId", photoList.get(position).museIdNum);
		startActivity(intent);
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
			intent.putExtra("index", 0);
			startActivityForResult(intent, MODIFY_OK);
            break;
        }
        case MODIFY_OK : 
        	initViewStatus();
        	break;
        }
    }
    
	@SuppressWarnings("unchecked")
	private void deletePhoto(int index) {
		UserData data = new UserData(userInfo.myIDNum, index, "remove");
		PhotoProfileRemoveRequest request = new PhotoProfileRemoveRequest(data);
		request.setOnPostMethodProcessListener(this);
		NetworkModel.getInstance().sendNetworkData(this, request);
	}

	@SuppressWarnings("unchecked")
	protected void deleteCoverPhoto() {
		UserData data = new UserData(userInfo.myIDNum, "remove");
		PhotoCoverRemoveRequest request = new PhotoCoverRemoveRequest(data);
		request.setOnPostMethodProcessListener(this);
		NetworkModel.getInstance().sendNetworkData(this, request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest request) {
		
		if (request instanceof MusePhotoRequest) {
			Log.i("MuseProfileActivity", "PhotoData");
			photoList = (ArrayList<PhotoData>) request.getResult();
			GridAdapter adapter = new GridAdapter(this, photoList);
			musePhotoGridView.setAdapter(adapter);
			dialog.dismiss();
		} else if (request instanceof UserDataRequest) {
			Log.i("MuseProfileActivity", "UserData");
			resultMuseData = (UserData) request.getResult();
			profileView.setProfileView(resultMuseData);
			header.setTitleText(((UserData) request.getResult()).myName);
			getMusePhotoData(userInfo.myIDNum, userId, 100, 0, "next");

			if (resultMuseData.followFlag.equals("true"))
				btnVisibility(invisible, visible, invisible, invisible, invisible, invisible);
			else {
				if (flag.equals("NormalMypageFragment") || flag.equals("NormalViewFragment_MyMuse") || userId == userInfo.myMUSEIDNum)
					btnVisibility(invisible, invisible, invisible, visible, invisible, invisible);
				else {
					if (userInfo.userType.equals("0"))
						btnVisibility(visible, invisible, visible, invisible, invisible, invisible);
					else
						btnVisibility(invisible, invisible, invisible, invisible, invisible, visible);
				}
			}
		} else {
			Log.i("MuseProfileActivity", "WTF");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest request) {

		if (request == myMuseRequest) {
			isMuseUpdated = true;
			userInfo.museProfilePhotoPath = resultMuseData.profilePhotoPath;
			userInfo.museProfileThumbPhotoPath = resultMuseData.profileThumbPhotoPath;
			userInfo.myMUSEIDNum = userId;
			btnVisibility(invisible, invisible, invisible, visible, invisible, invisible);
		} else if (request == unMuseRequest) {
			isMuseUpdated = true;
			museFollowData = new MuseFollowRequestData(userData.myIDNum, userId, "add");
			followRequest = new MuseFollowRequest(new MuseFollowRequestData(), museFollowData, "user/following");
			followRequest.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(this, followRequest);

			userInfo.museProfilePhotoPath = "";
			userInfo.museProfileThumbPhotoPath = "";
			userInfo.myMUSEIDNum = 0;
			btnVisibility(invisible, visible, invisible, invisible, invisible, invisible);
		} else if (request == followRequest) {
			isFollowUpdated = true;
			userInfo.followingCount += 1;
			btnVisibility(invisible, visible, invisible, invisible, invisible, invisible);
		} else if (request == unFollowRequest) {
			isFollowUpdated = true;
			userInfo.followingCount -= 1;
			if (userInfo.userType.equals("0"))
				btnVisibility(visible, invisible, visible, invisible, invisible, invisible);
			else
				btnVisibility(invisible, invisible, invisible, invisible, invisible, visible);
		} else if ( request instanceof PhotoCoverRemoveRequest) {
			new ResultDialog(this, "사진 삭제", "사진 삭제 성공").show();
			userInfo.coverPhotoPath = "";
			profileView.setCoverPhoto(Uri.parse(""));
		} else if ( request instanceof PhotoProfileRemoveRequest) {
			new ResultDialog(this, "사진 삭제", "사진 삭제 성공").show();
			userInfo.profilePhotoPath = "";
			userInfo.profileThumbPhotoPath = "";
			profileView.setProfilePhoto(Uri.parse(""));
		}
		
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest request) {
		new ErrorDialog(this, ( (MuseFollowRequest) request).getResult().errorCode ).show();
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest request) {
		dialog.dismiss();
	}

	public interface OnMuseUpdateCallback {
		void onMuseUpdated(boolean updated);
		void onFollowUpdated(boolean updated);	
	}

	public void setOnMuseUpdateCallback(OnMuseUpdateCallback cb) {
		this.museStatusCallback = cb;
	}
	
	public void isMuseUpdated() {
		
		if ( museStatusCallback != null) {
			museStatusCallback.onMuseUpdated(isMuseUpdated);
			museStatusCallback.onFollowUpdated(isFollowUpdated);
		}
	}

	public void setMyMuseUpdated(boolean updated) {
		isMuseUpdated = updated;
	}

	public void setFollowingUpdated(boolean updated) {
		isFollowUpdated = updated;
	}
	
}
