package com.museda.views;

import android.net.Uri;
import android.util.Log;

import com.androidquery.AQuery;
import com.museda.R;
import com.museda.UserData;
import com.museda.util.CountryUtil;
import com.museda.util.DateUtil;

public class NormalProfileView {

	private AQuery aq;
	
	public NormalProfileView(AQuery aq) {
		this.aq = aq;
	}

	public void setProfileView(UserData userInfo) {
		
		aq.id(R.id.name).text(userInfo.myName);
		aq.id(R.id.city).text(CountryUtil.getCountryFullName(userInfo.nationalCode));
		aq.id(R.id.id).text(userInfo.myAccount);
		aq.id(R.id.introduce).text(userInfo.introduce);
		aq.id(R.id.normalprofilejob_tv).text(userInfo.job);
		aq.id(R.id.normalprofileschool_tv).text(userInfo.school);
		aq.id(R.id.normalprofilehobby_tv).text(userInfo.hobby);
		aq.id(R.id.normalprofilelike_tv).text(userInfo.enjoy);
		aq.id(R.id.normalprofileboase_tv).text(userInfo.boast);
		aq.id(R.id.normalprofile_cover_iv).image(userInfo.coverPhotoPath);

		if (userInfo.birth.startsWith("9999"))
			aq.id(R.id.birth).text("비공개입니다.");
		else
			aq.id(R.id.birth).text(DateUtil.getDisplayBirthDate(userInfo.birth));
		
		
		if(userInfo.profileThumbPhotoPath.length()>0)
			aq.id(R.id.normalprofile_profile_iv).image(userInfo.profileThumbPhotoPath,false,false);
		else
			aq.id(R.id.normalprofile_profile_iv).image(R.drawable.signin_man_btn);
		
		
		if(userInfo.myPhoto1ThumbPhotoPath.length()>0) 
			aq.id(R.id.photo1_iv).image(userInfo.myPhoto1ThumbPhotoPath);
		else
			aq.id(R.id.photo1_iv).image(R.drawable.signin_man_btn);
		if(userInfo.myPhoto2ThumbPhotoPath.length()>0) 
			aq.id(R.id.photo2_iv).image(userInfo.myPhoto2ThumbPhotoPath);
		else
			aq.id(R.id.photo2_iv).image(R.drawable.signin_man_btn);
		if(userInfo.myPhoto3ThumbPhotoPath.length()>0) 
			aq.id(R.id.photo3_iv).image(userInfo.myPhoto3ThumbPhotoPath);
		else
			aq.id(R.id.photo3_iv).image(R.drawable.signin_man_btn);
		
	}
	
	public void setProfilePhoto(int index, Uri photoUri) {
		
		Log.i("NormalProfileView", photoUri.toString());
		if ( index == 0) {
			if(photoUri.toString().length()>0) 
				aq.id(R.id.normalprofile_profile_iv).getImageView().setImageURI(photoUri);
			else
				aq.id(R.id.normalprofile_profile_iv).image(R.drawable.signin_man_btn);		
		} else if ( index == 1 ) {
			if(photoUri.toString().length()>0) 
				aq.id(R.id.photo1_iv).getImageView().setImageURI(photoUri);
			else
				aq.id(R.id.photo1_iv).image(R.drawable.signin_man_btn);		
		} else if ( index == 2 ) {
			if(photoUri.toString().length()>0) 
				aq.id(R.id.photo2_iv).getImageView().setImageURI(photoUri);
			else
				aq.id(R.id.photo2_iv).image(R.drawable.signin_man_btn);		
		} else if ( index == 3 ) {
			if(photoUri.toString().length()>0) 
				aq.id(R.id.photo3_iv).getImageView().setImageURI(photoUri);
			else
				aq.id(R.id.photo3_iv).image(R.drawable.signin_man_btn);		
		} 
	}
	
	public void setCoverPhoto(Uri photoUri) {
		
		if ( photoUri.toString().length() > 0)
			aq.id(R.id.normalprofile_cover_iv).getImageView().setImageURI(photoUri);
		else
			aq.id(R.id.normalprofile_cover_iv).image(R.drawable.defaultcover);
		
	}
	
}
