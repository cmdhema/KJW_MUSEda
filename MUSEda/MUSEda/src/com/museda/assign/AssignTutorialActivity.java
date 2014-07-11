package com.museda.assign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.museda.R;
import com.museda.util.ResultDialog;

public class AssignTutorialActivity extends Activity implements OnClickListener {

	private ImageView museAssignBtn;
	private ImageView normalAssignBtn;
	private ImageView checkBtn;
	private ImageView backBtn;
	private ImageView cancelBtn;
	private ImageView nextBtn1;
	private ImageView nextBtn2;
	private TextView clauseBtn;

	private LinearLayout tuto1Layout;
	private LinearLayout tuto2Layout;
	private LinearLayout tuto3Layout;

	private boolean checkFlag;
	
	private AQuery aq;
	
	private Animation fromRight;
	private Animation fromLeft;
	
	private int viewIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_assigninfo);

		museAssignBtn = (ImageView) findViewById(R.id.assign_muse_btn);
		normalAssignBtn = (ImageView) findViewById(R.id.assign_normal_btn);
		checkBtn = (ImageView) findViewById(R.id.check_iv);
		backBtn = (ImageView) findViewById(R.id.back_btn);
		cancelBtn = (ImageView) findViewById(R.id.cancel_btn);
		tuto1Layout = (LinearLayout) findViewById(R.id.assign_tuto1_layout);
		tuto2Layout = (LinearLayout) findViewById(R.id.assign_tuto2_layout);
		tuto3Layout = (LinearLayout) findViewById(R.id.assign_tuto3_layout);
		clauseBtn = (TextView) findViewById(R.id.clause_tv);
		nextBtn1 = (ImageView) findViewById(R.id.assign_next_btn);
		nextBtn2 = (ImageView) findViewById(R.id.assign_next_btn2);

		nextBtn1.setOnClickListener(this);
		nextBtn2.setOnClickListener(this);
		clauseBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		checkBtn.setOnClickListener(this);
		museAssignBtn.setOnClickListener(this);
		normalAssignBtn.setOnClickListener(this);

		aq = new AQuery(this);
		fromRight = AnimationUtils.loadAnimation(aq.getContext(), R.anim.from_right_out);
		fromLeft = AnimationUtils.loadAnimation(aq.getContext(), R.anim.from_left_out);
	}

	@Override
	public void onClick(View v) {

		if (v == museAssignBtn) {

			if (checkFlag) {
				Intent intent = new Intent(this, AssignActivity.class);
				intent.putExtra("flag", "MuseAssign");
				startActivity(intent);
				finish();
			} else {
				showAgreeDialog(v);
			}
		} else if (v == normalAssignBtn) {
			if (checkFlag) {
				Intent intent = new Intent(this, AssignActivity.class);
				intent.putExtra("flag", "NormalAssign");
				startActivity(intent);
				finish();
			} else {
				showAgreeDialog(v);
			}
		} else if (v == checkBtn) {
			if (!checkFlag) {
				checkBtn.setImageResource(R.drawable.intro_checkbox2);
				checkFlag = true;
			} else {
				checkBtn.setImageResource(R.drawable.intro_checkbox1);
				checkFlag = false;
			}
		} else if (v == backBtn) {
			viewIndex--;
			if ( viewIndex == 0 ) {
				backBtn.setVisibility(View.INVISIBLE);
				aq.id(tuto1Layout).getView().startAnimation(fromLeft);
				tuto1Layout.setVisibility(View.VISIBLE);
				tuto2Layout.setVisibility(View.GONE);
			} else if ( viewIndex == 1 ) {
				aq.id(tuto2Layout).getView().startAnimation(fromLeft);
				tuto2Layout.setVisibility(View.VISIBLE);
				tuto3Layout.setVisibility(View.GONE);
			}
		} else if (v == cancelBtn) {
			viewIndex = 0;
			finish();
		} else if (v == clauseBtn) {
			new ResultDialog(this, "이용 약관", getResources().getString(R.string.clause)).show();
		} else if ( v == nextBtn1 ) {
			viewIndex++;
			backBtn.setVisibility(View.VISIBLE);
			aq.id(tuto2Layout).getView().startAnimation(fromRight);
			tuto1Layout.setVisibility(View.GONE);
			tuto2Layout.setVisibility(View.VISIBLE);
		} else if ( v == nextBtn2 ) {
			viewIndex++;
			aq.id(tuto3Layout).getView().startAnimation(fromRight);
			tuto2Layout.setVisibility(View.GONE);
			tuto3Layout.setVisibility(View.VISIBLE);
		}
	}

	private void showAgreeDialog(final View v) {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
		alert_confirm.setMessage("약관 동의가 필용합니다.").setCancelable(false).setPositiveButton("동의하고 회원가입", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (v == museAssignBtn) {
					Intent intent = new Intent(AssignTutorialActivity.this, AssignActivity.class);
					intent.putExtra("flag", "MuseAssign");
					startActivity(intent);
				} else {
					Intent intent = new Intent(AssignTutorialActivity.this, AssignActivity.class);
					intent.putExtra("flag", "NormalAssign");
					startActivity(intent);
				}

				AssignTutorialActivity.this.finish();
			}
		}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		AlertDialog alert = alert_confirm.create();
		alert.show();
	}

	@Override
	public void onBackPressed() {
		
		viewIndex--;
		if ( viewIndex == 0 ) {
			backBtn.setVisibility(View.INVISIBLE);
			aq.id(tuto1Layout).getView().startAnimation(fromLeft);
			tuto1Layout.setVisibility(View.VISIBLE);
			tuto2Layout.setVisibility(View.GONE);
		} else if ( viewIndex == 1 ) {
			aq.id(tuto2Layout).getView().startAnimation(fromLeft);
			tuto2Layout.setVisibility(View.VISIBLE);
			tuto3Layout.setVisibility(View.GONE);
		} if ( viewIndex == -1 )
			finish();
		
	}

	
}
