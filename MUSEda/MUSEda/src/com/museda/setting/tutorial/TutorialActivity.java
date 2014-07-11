package com.museda.setting.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.museda.R;

public class TutorialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.layout_tutorial);
	}

	
}
