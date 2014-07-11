package com.museda.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.favoriteheartpic.HeartFavoriteListActivity;
import com.museda.follow.UserListActivity;
import com.museda.profile.MuseProfileActivity;
import com.museda.profile.NormalProfileActivity;
import com.museda.setting.SettingActivity;

public class NormalMypageFragment extends Fragment implements OnClickListener {

	private UserData userInfo;
	
	private TitleBar header;
	private ImageView settingButton;
	private ImageView heartPicButton;
	private ImageView favoriteButton;
	private RelativeLayout followingButton;
	private RelativeLayout museProfileButton;
	private RelativeLayout profileButton;
	
	private AQuery aq;
	
	
	public static Fragment newInstance(Context context) {
		NormalMypageFragment layout = new NormalMypageFragment();
		return layout;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_normalmypage, container, false);
		
		aq = new AQuery(view);
		museProfileButton = (RelativeLayout) view.findViewById(R.id.museprofile_btn);
		profileButton = (RelativeLayout) view.findViewById(R.id.profile_btn);
		heartPicButton = (ImageView) view.findViewById(R.id.heartpic_btn);
		favoriteButton = (ImageView)view.findViewById(R.id.favorite_btn);
		followingButton = (RelativeLayout)view.findViewById(R.id.following_btn);
		settingButton = (ImageView) view.findViewById(R.id.btn_setting);
		header = (TitleBar) view.findViewById(R.id.titlebar);
		header.setTitleImage(R.drawable.home_topbar);
		header.setTitleText("Home");
		header.setDoneBtnVisible(false);
		header.setSettingBtnVisible(true);
		
		museProfileButton.setOnClickListener(this);
		profileButton.setOnClickListener(this);
		heartPicButton.setOnClickListener(this);
		favoriteButton.setOnClickListener(this);
		followingButton.setOnClickListener(this);
		settingButton.setOnClickListener(this);
		
		return view;
	}

	public void showProfileInfo() {
		aq.id(R.id.heart_tv).text(userInfo.recvHeartCount+"");
		aq.id(R.id.following_tv).text(userInfo.followingCount+"");
		aq.id(R.id.profile_iv).image(userInfo.profilePhotoPath);
		aq.id(R.id.muse_iv).image(userInfo.museProfilePhotoPath);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profile_btn:{
			Intent intent = new Intent(getActivity(), NormalProfileActivity.class);
			intent.putExtra("flag", "NormalMypageFragment");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
			break;
		}
		case R.id.favorite_btn:{
			Intent intent = new Intent(getActivity(), HeartFavoriteListActivity.class);	
			intent.putExtra("flag", "Favorite");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
			break;
		}
		case R.id.heartpic_btn:{
			Intent intent = new Intent(getActivity(), HeartFavoriteListActivity.class);	
			intent.putExtra("flag", "Heart Pic");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
			break;
		}
		case R.id.museprofile_btn:{
			Intent intent = new Intent(getActivity(), MuseProfileActivity.class);
			intent.putExtra("flag", "NormalMypageFragment");
//			intent.putExtra("userId",userInfo.myIDNum);			//Muse Profile에서 아이디 처리를 편하게 해주도록 
			intent.putExtra("userId", userInfo.myMUSEIDNum);	//id를 포함해서 인텐트를 넘겨주자
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
			break;
		}
		case R.id.following_btn:{
			Intent intent = new Intent(getActivity(), UserListActivity.class);
			intent.putExtra("flag", "Following");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
			break;
		}
		case R.id.btn_setting :{
			startActivity(new Intent(getActivity(), SettingActivity.class));
		}
		}
	}
	
	public void setBtnVisibility(boolean backBtn, boolean doneBtn, boolean settingBtn) {
		header.setBackBtnVisible(backBtn);
		header.setDoneBtnVisible(doneBtn);
		header.setSettingBtnVisible(settingBtn);
	}

	@Override
	public void onResume() {
		super.onResume();

		userInfo = SingletonData.getInstance().getUserData().get("UserData");
		
		setBtnVisibility(false, false, true);
		
		showProfileInfo();
	}
	
	
}
