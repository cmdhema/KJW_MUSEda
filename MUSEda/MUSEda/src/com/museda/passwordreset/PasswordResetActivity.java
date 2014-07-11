package com.museda.passwordreset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.museda.R;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.util.ErrorDialog;
import com.museda.util.ResultDialog;

public class PasswordResetActivity extends Activity implements OnClickListener, OnPostMethodProcessListener<UserData> {

	private TitleBar header;
	private Button doneBtn;
	private EditText emailEt;
	
	private String email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.layout_passwordreset);
		
		header = (TitleBar) findViewById(R.id.titlebar);
		doneBtn = (Button) findViewById(R.id.passwordreset_done_btn);
		emailEt = (EditText) findViewById(R.id.passwordreset_email_et);
		
		header.setTitleText("비밀번호 초기화");
		
		doneBtn.setOnClickListener(this);;
	}

	@Override
	public void onClick(View v) {
	
		email = emailEt.getText().toString();
		
		UserData requestData = new UserData();
		requestData.email = email;
		
		PasswordResetRequest request = new PasswordResetRequest(requestData);
		request.setOnPostMethodProcessListener(this);
		NetworkModel.getInstance().sendNetworkData(this, request);
		
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		new ResultDialog(this, "비밀번호 변경", "입력하신 메일로 초기화 메일을 발송하였습니다.").show();
		
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();
	}
	
}
