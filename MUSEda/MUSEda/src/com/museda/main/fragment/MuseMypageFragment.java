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
import android.widget.ScrollView;

import com.androidquery.AQuery;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.favoriteheartpic.HeartFavoriteListActivity;
import com.museda.feed.MuseFeedActivity;
import com.museda.follow.UserListActivity;
import com.museda.heart.MuseHeartActivity;
import com.museda.profile.MuseProfileActivity;
import com.museda.setting.SettingActivity;
import com.museda.util.ResultDialog;

public class MuseMypageFragment extends Fragment implements OnRefreshListener<ScrollView>, OnClickListener{
	
	private TitleBar header;
	private RelativeLayout profileButton;
	private RelativeLayout museFollowerButton;
	private RelativeLayout followingButton;
	private RelativeLayout followerButton;
	private RelativeLayout heartButton;
	private ImageView feedButton;
	private ImageView museMapButton;
	private ImageView heartPicButton;
	private ImageView settingButton;
	
	private PullToRefreshScrollView pullToRefreshScrollView;
	private UserData userData;
	
	private AQuery aq;
	
	public static Fragment newInstance(Context context) {
		MuseMypageFragment layout = new MuseMypageFragment();
		return layout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_musemypage, container, false);
		
		aq = new AQuery(view);
		
		header = (TitleBar) view.findViewById(R.id.titlebar);
		profileButton = (RelativeLayout) view.findViewById(R.id.profile_btn);
		museFollowerButton = (RelativeLayout) view.findViewById(R.id.musefollower_btn);
		followingButton = (RelativeLayout) view.findViewById(R.id.following_btn);
		followerButton = (RelativeLayout) view.findViewById(R.id.follower_btn);
		heartButton = (RelativeLayout) view.findViewById(R.id.heart_btn);
		heartPicButton = (ImageView) view.findViewById(R.id.heartpic_btn);
		feedButton = (ImageView) view.findViewById(R.id.feed_btn);
		museMapButton = (ImageView) view.findViewById(R.id.map_btn);
		settingButton = (ImageView) view.findViewById(R.id.btn_setting);
		pullToRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.scrollview);
		pullToRefreshScrollView.setMode(Mode.DISABLED);
		
		pullToRefreshScrollView.setOnRefreshListener(this);
		profileButton.setOnClickListener(this);
		museFollowerButton.setOnClickListener(this);
		followingButton.setOnClickListener(this);
		heartButton.setOnClickListener(this);
		heartPicButton.setOnClickListener(this);
		feedButton.setOnClickListener(this);
		followerButton.setOnClickListener(this);
		museMapButton.setOnClickListener(this);
		settingButton.setOnClickListener(this);
		
		header.setTitleImage(R.drawable.home_topbar);
		header.setTitleText("Home");
		header.setDoneBtnVisible(false);
		header.setSettingBtnVisible(true);
		header.setBackBtnVisible(false);
		 
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		userData = SingletonData.getInstance().getUserData().get("UserData");
		showViewInfo(userData);
	}

	private void showViewInfo(UserData userInfo) {
		aq.id(R.id.musefollower_tv).text(userInfo.recvMUSECount+"");
		aq.id(R.id.follower_tv).text(userInfo.followerCount+"");
		aq.id(R.id.musemypageprofile_iv).image(userInfo.profileThumbPhotoPath);
		aq.id(R.id.following_tv).text(userInfo.followingCount +"");
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
	}

	@Override
	public void onClick(View v) {
		if (v == profileButton){
			Intent intent = new Intent(getActivity(), MuseProfileActivity.class);
			intent.putExtra("flag", "MuseMypageFragment");
			intent.putExtra("userId", userData.myIDNum);
			intent.putExtra("museId", userData.myIDNum);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		}
		else if (v == museFollowerButton) {	//MUSE FOLLOWER
			Intent intent = new Intent(getActivity(), UserListActivity.class);
			intent.putExtra("flag", "Muse Follower");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		} else if (v == followerButton) {	//FOLLOWER
			Intent intent = new Intent(getActivity(), UserListActivity.class);
			intent.putExtra("flag", "Follower");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		} else if (v== followingButton) {	//FOLLOWING
			Intent intent = new Intent(getActivity(), UserListActivity.class);
			intent.putExtra("flag", "Following");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		} else if (v == heartButton){	//HEART
			startActivity(new Intent(getActivity(), MuseHeartActivity.class));
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		} else if (v == heartPicButton){	//HEART PIC
			Intent intent = new Intent(getActivity(), HeartFavoriteListActivity.class);
			intent.putExtra("flag", "Heart Pic");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		} else if (v== feedButton) {	//FEED
			Intent intent = new Intent(getActivity(), MuseFeedActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		} else if (v== museMapButton) {	//MUSE MAP
			new ResultDialog(getActivity(), "마이 페이지", "준비중인 기능입니다.").show();
//			Intent intent = new Intent(getActivity(), MuseMapActivity.class);
//			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		} else if (v== settingButton) {
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.from_bottom_out, R.anim.from_bottom_in);
		}
	}
	
	
}
