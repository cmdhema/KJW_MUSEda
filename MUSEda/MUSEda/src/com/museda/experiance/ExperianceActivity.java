package com.museda.experiance;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.assign.AssignTutorialActivity;
import com.museda.login.LoginActivity;
import com.museda.main.fragment.MuseSearchRequest;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.views.PopularTopView;

public class ExperianceActivity extends Activity implements OnClickListener, OnGetMethodProcessListener<ArrayList<PhotoData>> {

	private ArrayList<PhotoData> photoList;
	
	//View
	private ImageView leftBtn;
	private ImageView rightBtn;
	private ImageView photoIv;
	private ImageView heartBtn;
	private ImageView profileIv;
	private TextView nameTv;
	private TextView idTv;
	
	//접속 요청
	private MuseSearchRequest searchRequest;
	
	private AQuery aq;
	
	private PhotoData data;
	
	private PopularTopView photoView;
	
	private int photoIndex;
	
	private String dialogList[] = {"회원가입","로그인","취소"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_populartopmuse);
		
		leftBtn = (ImageView) findViewById(R.id.left_btn);
		rightBtn = (ImageView) findViewById(R.id.right_btn);
		photoIv = (ImageView) findViewById(R.id.musesearch_iv);
		heartBtn = (ImageView) findViewById(R.id.musesearch_heart_btn);
		profileIv = (ImageView) findViewById(R.id.musesearch_porfile_iv);
		nameTv = (TextView) findViewById(R.id.musesearch_name_tv);
		idTv = (TextView) findViewById(R.id.musesearch_id_tv);
		photoIv = (ImageView) findViewById(R.id.musesearch_iv);
		
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		profileIv.setOnClickListener(this);
		nameTv.setOnClickListener(this);
		idTv.setOnClickListener(this);
		photoIv.setOnClickListener(this);
		heartBtn.setOnClickListener(this);
		
		photoIv.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		aq = new AQuery(this);
		
		//View 객체 초기화
		photoView = new PopularTopView(aq);
		
		//요청 객체 설정
		data = initSearchRequestData();
		
		searchRequest = new MuseSearchRequest(new ArrayList<PhotoData>(), data, "top");
		searchRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, searchRequest);
		
		leftBtn.setVisibility(View.GONE);
		
	}

	private PhotoData initSearchRequestData() {
		data = new PhotoData();
		data.myIdNum = 0;
		data.count = 10;
		data.startPicNum = 0;
		data.direction = "next";
		
		return data;
	}
	
	@Override
	public void onClick(View v) {
		
		if ( v== leftBtn) {
			
			if(photoIndex > 1) {
				leftBtn.setVisibility(View.VISIBLE);
			} else if (photoIndex == 1) 
				leftBtn.setVisibility(View.GONE);

			photoView.setView(photoList, --photoIndex, -1);
			
		} else if ( v== rightBtn) {
			
			if (photoIndex == photoList.size() - 1) 
				photoIndex = -1;
			
			leftBtn.setVisibility(View.VISIBLE);
			
			if(photoIndex<9) {
				photoView.setView(photoList, ++photoIndex, 1);
			}
			else if (photoIndex > 8 ) {
				showAssignDialog();
			}
		} else
			showAssignDialog();
	}
	
	public void showAssignDialog() {
		new AlertDialog.Builder(this).setTitle("로그인이 필요한 메뉴입니다").setItems(dialogList, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int position) {
				if(position == 0) {
					startActivity(new Intent(ExperianceActivity.this, AssignTutorialActivity.class));
				} else if ( position == 1) {
					startActivity(new Intent(ExperianceActivity.this, LoginActivity.class));
				}
			}
		}).show();
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		photoList = request.getResult();
		
		if ( photoList.size() > 0 )
			photoView.setView(photoList, 0, 0);		
		else
			photoView.setInit();
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {
		
	}	
	
}
