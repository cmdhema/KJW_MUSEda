package com.museda.map;

import android.os.Bundle;
import android.view.Window;

import com.museda.ParentActivity;
import com.museda.R;

public class MuseMapActivity extends ParentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_map);
		
		
		
	}
}