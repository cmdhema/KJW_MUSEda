package com.museda.setting.account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.util.ErrorDialog;
import com.museda.util.ResultDialog;

public class AccountPwEditFragment extends Fragment implements OnClickListener, OnPostMethodProcessListener<UserData> {

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");
	
	private EditText newPwEt;
	private EditText pwAgainEt;
	private ImageView saveBtn;
	
	private String pw;
	private String againPw;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_accountpwedit, container, false);
	
		newPwEt = (EditText) view.findViewById(R.id.account_newpw_et);
		pwAgainEt = (EditText) view.findViewById(R.id.account_newpwagain_et);
		saveBtn = (ImageView) view.findViewById(R.id.account_pwsave_btn);
		
		saveBtn.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		pw = newPwEt.getText().toString();
		againPw = pwAgainEt.getText().toString();
		
		if( ! pw.equals(againPw) ) {
			new ResultDialog(getActivity(), "��й�ȣ ����", "��й�ȣ�� ��ġ���� �ʽ��ϴ�.").show();
		} 
		if (!(pw.length() > 7 && pw.length() < 13)) {
			new ResultDialog(getActivity(), "��й�ȣ ����", "��й�ȣ�� 8~12�ڷ� �Է����ּ���").show();
		} else {
			UserData data = new UserData(userData.myAccount, userData.password, pw);
			PasswordEditRequest request = new PasswordEditRequest(data);
			request.setOnPostMethodProcessListener(this);
			NetworkModel.getInstance().sendNetworkData(getActivity(), request);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		new ResultDialog(getActivity(), "��й�ȣ ����", "��й�ȣ�� �����Ͽ����ϴ�.").show();
		userData.password = newPwEt.getText().toString();
		SharedPreferences sp = getActivity().getSharedPreferences("info", getActivity().MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("login", false);
		editor.commit();
		
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<UserData> request) {
		new ErrorDialog(getActivity(), request.getResult().errorCode).show();
	}
	
	
}
