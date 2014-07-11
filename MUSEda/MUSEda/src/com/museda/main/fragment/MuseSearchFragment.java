package com.museda.main.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.museda.R;
import com.museda.search.SearchActivity;

public class MuseSearchFragment extends Fragment {

	// Drop Down 메뉴
	private ArrayList<Integer> menuIds;
	private ArrayList<ImageView> menuBtns;
	private LinearLayout mDropdownFoldOutMenu;
	private ImageView header;
	private ImageView alt0;
	private ImageView alt1;
	private ImageView tempImageView;
	private ImageView searchBtn;
	
	private int headerId;
	private int alt0Id;
	private int alt1Id;

	// Fragment 변수
	private Fragment newFragment;
	private Fragment popularFragment;
	private Fragment topFragment;

	// Fragment로 전달할 Bundle
	private Bundle flagBundle;

	public static Fragment newInstance(Context context) {
		MuseSearchFragment layout = new MuseSearchFragment();
		return layout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_musemain, container, false);

		// DropDown 메뉴
		header = (ImageView) view.findViewById(R.id.menu_title_iv);
		alt0 = (ImageView) view.findViewById(R.id.dropdown_alt0);
		alt1 = (ImageView) view.findViewById(R.id.dropdown_alt1);
		searchBtn = (ImageView) view.findViewById(R.id.search_btn);
		mDropdownFoldOutMenu = ((LinearLayout) view.findViewById(R.id.dropdown_foldout_menu));

		menuIds = new ArrayList<Integer>();
		menuBtns = new ArrayList<ImageView>();
		menuBtns.add(header);
		menuBtns.add(alt0);
		menuBtns.add(alt1);

		headerId = header.getId();
		alt0Id = alt0.getId();
		alt1Id = alt1.getId();

		header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDropdownFoldOutMenu.getVisibility() == View.GONE) {
					if (header.getId() == headerId)
						menuBtns.get(0).setBackgroundResource(R.drawable.page_popular_b_topbar2);
					else if (header.getId() == alt0Id)
						menuBtns.get(0).setBackgroundResource(R.drawable.page_topmuse_b_topbar1);
					else if (header.getId() == alt1Id)
						menuBtns.get(0).setBackgroundResource(R.drawable.page_new_b_topbar2);
					openDropdown();
				} else {
					if (header.getId() == headerId)
						menuBtns.get(0).setBackgroundResource(R.drawable.page_popular_topbar);
					else if (header.getId() == alt0Id)
						menuBtns.get(0).setBackgroundResource(R.drawable.page_topmuse_topbar);
					else if (header.getId() == alt1Id)
						menuBtns.get(0).setBackgroundResource(R.drawable.page_new_topbar);
					closeDropdown();
				}
			}
		});

		alt0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (menuIds.size() > 0)
					menuIds.removeAll(menuIds);

				if (alt0.getId() == alt0Id) {
					showTopMuse();
				} else if (alt0.getId() == alt1Id) {
					showNewMuse();
				} else if (alt0.getId() == headerId) {
					showPopularMuse();
				}

				tempImageView = alt0;
				alt0 = header;
				header = tempImageView;

				menuIds.add(header.getId());
				menuIds.add(alt0.getId());
				menuIds.add(alt1.getId());

				setMenuItems();

				closeDropdown();
			}

		});
		alt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (menuIds.size() > 0)
					menuIds.removeAll(menuIds);

				if (alt1.getId() == alt0Id) {
					showTopMuse();
				} else if (alt1.getId() == alt1Id) {
					showNewMuse();
				} else if (alt1.getId() == headerId) {
					showPopularMuse();
				}

				tempImageView = alt1;
				alt1 = header;
				header = tempImageView;

				menuIds.add(header.getId());
				menuIds.add(alt0.getId());
				menuIds.add(alt1.getId());

				setMenuItems();

				closeDropdown();
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.from_right_out, R.anim.from_right_in);
			}
		});

		flagBundle = new Bundle();

		if (null == topFragment)
			topFragment = new PopularTopMuseFragment();
		if (null == newFragment)
			newFragment = new NewMuseFragment();
		if (null == popularFragment)
			popularFragment = new PopularTopMuseFragment();

		showPopularMuse();

		return view;
	}

	private void setMenuItems() {
		if (menuIds.get(0) == headerId) {
			menuBtns.get(0).setBackgroundResource(R.drawable.page_popular_topbar);
			if (menuIds.get(1) == alt0Id) {
				menuBtns.get(1).setBackgroundResource(R.drawable.page_topmuse_b_btn);
				menuBtns.get(2).setBackgroundResource(R.drawable.page_new_b_btn);
			} else if (menuIds.get(1) == alt1Id) {
				menuBtns.get(2).setBackgroundResource(R.drawable.page_topmuse_b_btn);
				menuBtns.get(1).setBackgroundResource(R.drawable.page_new_b_btn);
			}
		} else if (menuIds.get(0) == alt0Id) {
			menuBtns.get(0).setBackgroundResource(R.drawable.page_topmuse_topbar);
			if (menuIds.get(1) == headerId) {
				menuBtns.get(1).setBackgroundResource(R.drawable.page_popular_b_btn);
				menuBtns.get(2).setBackgroundResource(R.drawable.page_new_b_btn);
			} else if (menuIds.get(1) == alt1Id) {
				menuBtns.get(2).setBackgroundResource(R.drawable.page_popular_b_btn);
				menuBtns.get(1).setBackgroundResource(R.drawable.page_new_b_btn);
			}
		} else if (menuIds.get(0) == alt1Id) {
			menuBtns.get(0).setBackgroundResource(R.drawable.page_new_topbar);
			if (menuIds.get(1) == headerId) {
				menuBtns.get(1).setBackgroundResource(R.drawable.page_popular_b_btn);
				menuBtns.get(2).setBackgroundResource(R.drawable.page_topmuse_b_btn);
			} else if (menuIds.get(1) == alt0Id) {
				menuBtns.get(2).setBackgroundResource(R.drawable.page_popular_b_btn);
				menuBtns.get(1).setBackgroundResource(R.drawable.page_topmuse_b_btn);
			}
		} else {
		}
	}

	private void openDropdown() {
		if (mDropdownFoldOutMenu.getVisibility() != View.VISIBLE) {
			ScaleAnimation anim = new ScaleAnimation(1, 1, 0, 1);
			anim.setDuration(getResources().getInteger(R.integer.dropdown_amination_time));
			mDropdownFoldOutMenu.startAnimation(anim);
			mDropdownFoldOutMenu.setVisibility(View.VISIBLE);
		}
	}

	private void closeDropdown() {
		if (mDropdownFoldOutMenu.getVisibility() == View.VISIBLE) {
			ScaleAnimation anim = new ScaleAnimation(1, 1, 1, 0);
			anim.setDuration(getResources().getInteger(R.integer.dropdown_amination_time));
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mDropdownFoldOutMenu.setVisibility(View.GONE);
				}
			});
			mDropdownFoldOutMenu.startAnimation(anim);
		}
	}

	private void showPopularMuse() {
		flagBundle.putString("flag", "popular");
		popularFragment.setArguments(flagBundle);
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, popularFragment).commit();
	}

	private void showTopMuse() {
		flagBundle.putString("flag", "top");
		topFragment.setArguments(flagBundle);
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, topFragment).commit();
	}

	private void showNewMuse() {
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).commit();
	}

}
