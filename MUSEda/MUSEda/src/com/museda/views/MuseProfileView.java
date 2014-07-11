package com.museda.views;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.museda.R;
import com.museda.UserData;
import com.museda.util.CountryUtil;
import com.museda.util.DateUtil;
import com.museda.util.TimeUtil;

public class MuseProfileView {

	private AQuery aq;

	public MuseProfileView(AQuery aq) {
		this.aq = aq;
	}

	public void setProfileView(UserData data) {
		
		aq.id(R.id.museprofile_cover_iv).image(data.coverPhotoPath);
		aq.id(R.id.name).text(data.myName);
		aq.id(R.id.id).text(data.myAccount);
		aq.id(R.id.city).text(CountryUtil.getCountryFullName(data.nationalCode));
		aq.id(R.id.museprofile_mymusenum_tv).text(data.recvMUSECount + "");
		aq.id(R.id.museprofile_followingnum_tv).text(data.followingCount + "");
		aq.id(R.id.museprofile_followernum_tv).text(data.followerCount + "");
		aq.id(R.id.museprofile_heartnum_tv).text(data.recvHeartCount + "");
		
		if ( data.profilePhotoPath.length() > 0)
			aq.id(R.id.museprofile_profile_iv).image(data.profilePhotoPath);
		else
			aq.id(R.id.museprofile_profile_iv).image(R.drawable.signin_man_btn);
		
		if (TimeUtil.getTimeZone().equals("+09:00"))
			aq.id(R.id.today_tv).text(" " + (data.todayCount) + "");
		else
			aq.id(R.id.today_layout).visibility(View.INVISIBLE);

		if (data.birth.startsWith("9999"))
			aq.id(R.id.birth).text("비공개입니다.");
		else
			aq.id(R.id.birth).text(DateUtil.getDisplayBirthDate(data.birth));

		if (data.introduce.length() > 0)
			aq.id(R.id.introduce).text("  " + data.introduce);
		else
			aq.id(R.id.introduce).text("  " + "안녕하세요~!");
	}
	
	public void setProfilePhoto(Uri photoUri) {
		

		Log.i("MuseProfileView", photoUri.toString());
		
		if ( photoUri.toString().length() > 0)
			aq.id(R.id.museprofile_profile_iv).getImageView().setImageURI(photoUri);
		else
			aq.id(R.id.museprofile_profile_iv).image(R.drawable.signin_man_btn);
	}
	
	public void setCoverPhoto(Uri photoUri) {
		
		if ( photoUri.toString().length() > 0)
			aq.id(R.id.museprofile_cover_iv).getImageView().setImageURI(photoUri);
		else
			aq.id(R.id.museprofile_cover_iv).image(R.drawable.defaultcover);
	}
	

}
