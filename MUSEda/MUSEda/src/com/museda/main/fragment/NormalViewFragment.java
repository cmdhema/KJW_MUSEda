package com.museda.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.museda.R;
import com.museda.profile.MuseProfileActivity;

public class NormalViewFragment extends Fragment implements OnClickListener {

	private LinearLayout buttonListLayout;
	private ImageView myMuseBtn;
	private ImageView followBtn;

	private NormalMyMuseViewFragment myMuseFragment;
	private NormalFollowingViewFragment followingFragment;

	public static Fragment newInstance(Context context) {
		NormalViewFragment layout = new NormalViewFragment();
		return layout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_normalview, null);

		followBtn = (ImageView) view.findViewById(R.id.following_btn);
		myMuseBtn = (ImageView) view.findViewById(R.id.mymuse_btn);
		buttonListLayout = (LinearLayout) view.findViewById(R.id.museviewtitlebar);

		followBtn.setOnClickListener(this);
		myMuseBtn.setOnClickListener(this);
		
		if(followingFragment == null) 
			followingFragment = new NormalFollowingViewFragment();
		if(myMuseFragment == null)
			myMuseFragment = new NormalMyMuseViewFragment();
		
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.normalview_container, myMuseFragment).commit();
		myMuseFragment.requestMyMuseData(0);
		
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		final MuseProfileActivity museActivity = new MuseProfileActivity();
		MuseProfileActivity.OnMuseUpdateCallback museCallback = new MuseProfileActivity.OnMuseUpdateCallback() {
			
			@Override
			public void onMuseUpdated(boolean updated) {
				if ( updated ) {
					myMuseFragment.requestMyMuseData(0);
					museActivity.setMyMuseUpdated(false);
				}
			}

			@Override
			public void onFollowUpdated(boolean updated) {
				if ( updated ) {
					followingFragment.requestFollowMuseData(0);
					museActivity.setFollowingUpdated(false);
				}
			}
		};
		museActivity.setOnMuseUpdateCallback(museCallback);
		museActivity.isMuseUpdated();
		
		
	}


	@Override
	public void onClick(View v) {

		if (v == myMuseBtn) {
			
			buttonListLayout.setBackgroundResource(R.drawable.musefollowing2_topbar);
			followBtn.setClickable(true);
			myMuseBtn.setClickable(false);
			
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.normalview_container, myMuseFragment).commit();
			myMuseFragment.requestMyMuseData(0);
		} else if (v == followBtn) {
			
			buttonListLayout.setBackgroundResource(R.drawable.musefollowing1_topbar);
			followBtn.setClickable(false);
			myMuseBtn.setClickable(true);

			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.normalview_container, followingFragment).commit();
			followingFragment.requestFollowMuseData(0);
		}
	}
}
