package com.museda.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;
import com.museda.R;
import com.museda.adapter.MainViewPageAdapter;
import com.museda.main.fragment.MuseAlbumFragment;
import com.museda.main.fragment.MuseMypageFragment;
import com.museda.main.fragment.MuseSearchFragment;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.setting.version.AppData;
import com.museda.setting.version.VersionRequest;
import com.museda.util.ErrorDialog;

public class MuseMainActivity extends FragmentActivity implements OnClickListener {

	// ViewPager 변수
	private JazzyViewPager mPager;
	private MainViewPageAdapter adapter;

	// 뒤로가기 두번 눌러서 종료에 쓰이는 변수
	private long m_startTime;
	private long m_endTime;
	private boolean m_isPressedBackButton;

	// 튜토피얼 화면 변수
	private RelativeLayout tutoPage1;
	private RelativeLayout tutoPage2;

	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_main);

		tutoPage1 = (RelativeLayout) findViewById(R.id.movetutorial_layout);
		tutoPage2 = (RelativeLayout) findViewById(R.id.photouptutorial_layout);
		mPager = (JazzyViewPager) findViewById(R.id.pager);
		
		adapter = new MainViewPageAdapter(getApplicationContext(), getSupportFragmentManager(), mPager);

		adapter.addFragment(MuseMypageFragment.newInstance(this));
		adapter.addFragment(MuseAlbumFragment.newInstance(this));
		adapter.addFragment(MuseSearchFragment.newInstance(this));

		mPager.setTransitionEffect(TransitionEffect.Tablet);
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(1);
		mPager.setOffscreenPageLimit(3);
		mPager.setPageMargin(20);

		tutoPage1.setOnClickListener(this);
		tutoPage2.setOnClickListener(this);

		setTutoPage();
		checkUpdate();
	}

	private void setTutoPage() {
		sp = getSharedPreferences("info", MODE_PRIVATE);
		boolean tutoFlag = sp.getBoolean("tuto", false);
		
		if (!tutoFlag) {
			tutoPage1.setVisibility(View.VISIBLE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("tuto", true);
			editor.commit();
		} else {
			tutoPage1.setVisibility(View.GONE);
			tutoPage2.setVisibility(View.GONE);
		}
	}

	private void checkUpdate() {

		VersionRequest request = new VersionRequest(new AppData());
		request.setOnGetMethodProcessListener(new OnGetMethodProcessListener<AppData>() {

			@SuppressWarnings("unused")
			@Override
			public void onGetMethodProcessSuccess(GetNetworkRequest<AppData> request) {
				String serverVersionCode = request.getResult().versionCode;
				String currentVersionName = null;
				int currentVersionCode;
				
				try {
					PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
					currentVersionCode = info.versionCode;
					currentVersionName = info.versionName;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}

				if ( !serverVersionCode.equals(currentVersionName) ) {
					new AlertDialog.Builder(MuseMainActivity.this).setTitle("업데이트").setMessage("업데이트가 있습니다. 업데이트를 받아주세요").setPositiveButton("업데이트 하러가기", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Uri uri = Uri.parse("market://details?id=" + getPackageName());
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							MuseMainActivity.this.startActivity(intent);
						}
					}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).show();
				}
			}

			@Override
			public void onGetMethodProcessError(GetNetworkRequest<AppData> request) {
				new ErrorDialog(MuseMainActivity.this, request.getResult().errorCode).show();
			}
		});
		NetworkModel.getInstance().getNetworkData(this, request);
	}
	
	@Override
	public void onClick(View v) {
		if (v == tutoPage1) {
			tutoPage1.setVisibility(View.GONE);
			tutoPage2.setVisibility(View.VISIBLE);
		} else if (v == tutoPage2) {
			tutoPage2.setVisibility(View.GONE);
			SharedPreferences.Editor edit = sp.edit();
			edit.putBoolean("tuto", true);
			edit.commit();
		}
	}

	@Override
	public void onBackPressed() {
		m_endTime = System.currentTimeMillis();

		if (m_endTime - m_startTime > 2000)
			m_isPressedBackButton = false;
		if (m_isPressedBackButton == false) {
			m_isPressedBackButton = true;
			m_startTime = System.currentTimeMillis();
			Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
		} else {
			finish();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
