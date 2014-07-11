package com.museda.photo;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
import com.museda.network.MultiPartNetworkRequest;
import com.museda.network.MultiPartNetworkRequest.OnMultiPartProcessListener;
import com.museda.network.NetworkModel;
import com.museda.util.ErrorDialog;
import com.museda.util.FileUtil;

public class PhotoCheckActivity extends Activity implements OnClickListener, OnMultiPartProcessListener<UserData> {

	private UserData userInfo = SingletonData.getInstance().getUserData().get("UserData");

	private ProgressDialog dialog;
		
	private Button photoUpBtn;
	private Button rotateBtn;
	private ImageView photoIv;
	
	private AQuery aq;
	
	private String flag;
	private String photoPath;

	private String uploadImagePath;
	
	private int index;
	
	private UserData data;
	
	private PhotoUpRequest photoRequest;
	private PhotoCoverUpRequest coverRequest;
	private PhotoProfileUpRequest profileRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_photocheck);
		
		rotateBtn = (Button) findViewById(R.id.photocheck_rotatebtn);
		photoUpBtn = (Button) findViewById(R.id.photockeck_uploadbtn);
		photoIv = (ImageView) findViewById(R.id.photockeck_iv);
		
		rotateBtn.setOnClickListener(this);
		photoUpBtn.setOnClickListener(this);
		aq = new AQuery(this);
		
		flag = getIntent().getStringExtra("flag");
		index = getIntent().getIntExtra("index", -1);
		if ( getIntent().getStringExtra("path") != null )
			photoPath = getIntent().getStringExtra("path");
		
		if ( getIntent().getStringExtra("uri") != null )
			photoPath = getImagePath(Uri.parse(getIntent().getStringExtra("uri")));

		aq.id(photoIv).getImageView().setImageURI(Uri.fromFile(new File(photoPath)));

		String fileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		uploadImagePath = PhotoData.imageDir + "/" + fileName;
		
		FileUtil.getInstance().modifyImageSizeTask(uploadImagePath, photoPath);
	}
	
	/**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     * @param uri
     * @return
     */
    private String getImagePath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
 
        Cursor mCursor = getContentResolver().query(uri, projection, null, null, 
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if(mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();
 
        String path = mCursor.getString(column_index);
 
        if (mCursor !=null ) {
            mCursor.close();
            mCursor = null;
        }
 
        return path;
    }
    
	@Override
	public void onClick(View v) {
		if ( v == photoUpBtn) {
			
			if(flag.equals("cover"))  {
				data = new UserData(userInfo.myIDNum, 0, "add", uploadImagePath);
				coverRequest = new PhotoCoverUpRequest(data, new UserData());
				coverRequest.setOnMultiPartProcessListener(this);
				NetworkModel.getInstance().sendPhotoData(this, coverRequest);
			}
			else if ( flag.equals("profile")) {
				data = new UserData(userInfo.myIDNum, index, "add", uploadImagePath);
				profileRequest = new PhotoProfileUpRequest(data, new UserData());
				profileRequest.setOnMultiPartProcessListener(this);
				NetworkModel.getInstance().sendPhotoData(this, profileRequest);
			}
			else {
				data = new UserData(userInfo.myIDNum, userInfo.myAccount, uploadImagePath );
				data.myIDNum = userInfo.myIDNum;
				data.myAccount = userInfo.myAccount;
				photoRequest = new PhotoUpRequest(data);
				photoRequest.setOnMultiPartProcessListener(this);
				NetworkModel.getInstance().sendPhotoData(this, photoRequest);
			}
			
			dialog = ProgressDialog.show(this, "사진 업로드 중입니다.", "잠시만 기다려주세요",true);
		} else if ( v == rotateBtn ) {
			aq.id(photoIv).image(FileUtil.rotateImage(uploadImagePath));
		}
			
	}
	

	@Override
	public void onMultiPartProcessSuccess(MultiPartNetworkRequest<UserData> request) {
		
		if(request instanceof PhotoCoverUpRequest) {
			UserData data = (UserData) request.getResult();
			userInfo.coverPhotoPath = data.resultPhotoPath;
		} else if (request instanceof PhotoProfileUpRequest) {
			UserData data = (UserData) request.getResult();
			if( index == 0 ){
				userInfo.profilePhotoPath = data.resultPhotoPath;
				userInfo.profileThumbPhotoPath = data.resultPhotoPath;
			} else if ( index == 1) {
				userInfo.myPhoto1Path = data.resultPhotoPath;
				userInfo.myPhoto1ThumbPhotoPath = data.resultPhotoThumbPath;
			} else if ( index == 2) {
				userInfo.myPhoto2Path = data.resultPhotoPath;
				userInfo.myPhoto2ThumbPhotoPath = data.resultPhotoThumbPath;
			} else if ( index == 3) {
				userInfo.myPhoto3Path = data.resultPhotoPath;
				userInfo.myPhoto3ThumbPhotoPath = data.resultPhotoThumbPath;
			}
		}
		
		removeImages();
		UserData.photoModify = true;
		finish();
		dialog.dismiss();
	}

	@Override
	public void onMultiPartProcessError(MultiPartNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();
		dialog.dismiss();
	}
	
	public void removeImages() {
		if ( !flag.equals("Album") ) 
			FileUtil.removeImage(photoPath);

		FileUtil.removeImage(uploadImagePath);
	}
	
}
