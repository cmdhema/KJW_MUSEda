package com.museda;

import android.app.Activity;

import com.museda.network.NetworkModel;

public class ParentActivity extends Activity{

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.from_top_out,R.anim.from_top_in );
	}

	
	@Override
	protected void onDestroy() {
		NetworkModel.getInstance().cleanUpRequest(this);
		super.onDestroy();
	}
}
