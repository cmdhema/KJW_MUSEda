package com.museda.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.museda.ParentActivity;
import com.museda.R;
import com.museda.follow.UserListActivity;
import com.museda.util.ResultDialog;

public class SearchActivity extends ParentActivity implements OnClickListener, OnKeyListener {

	private RelativeLayout blankLayout;
	private ImageView searchBtn;
	private ImageView cancelBtn;
	private EditText museIdEt;
	
	private String museId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_search);
		
		blankLayout = (RelativeLayout) findViewById(R.id.blank_layout);
		searchBtn = (ImageView) findViewById(R.id.search_btn);
		cancelBtn = (ImageView) findViewById(R.id.cancel_btn);
		museIdEt = (EditText) findViewById(R.id.museid_et);
		
		blankLayout.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		museIdEt.setOnKeyListener(this);
	
	}

	@Override
	public void onClick(View v) {
		
		if(v==blankLayout || v==cancelBtn) {
			finish();
			overridePendingTransition(R.anim.from_left_out,R.anim.from_left_in );
		} else if (v==searchBtn) 
			searchMuse();
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		if(event.getAction()==KeyEvent.ACTION_DOWN)
			if(keyCode == KeyEvent.KEYCODE_ENTER) {
				searchMuse();
				return true;
			}
		return false;
	}
	
	private void searchMuse() {
		museId = museIdEt.getText().toString();
		
		if ( museId.length() == 0 )
			new ResultDialog(this, "뮤즈 찾기", "아이디를 입력해주세요").show();
		else {
			Intent intent = new Intent(SearchActivity.this, UserListActivity.class);
			intent.putExtra("flag", "Search");
			intent.putExtra("searchId", museId);
			startActivity(intent);
		}
	}
}
