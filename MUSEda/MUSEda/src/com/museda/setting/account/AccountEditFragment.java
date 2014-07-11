package com.museda.setting.account;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.museda.R;
import com.museda.SingletonData;
import com.museda.UserData;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.profile.ModifyUserInfoRequest;
import com.museda.util.ErrorDialog;
import com.museda.util.ResultDialog;

public class AccountEditFragment extends Fragment implements OnClickListener, OnPostMethodProcessListener<UserData> {

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");
	
	private TextView editTv;
	private EditText editEt;
	private ImageView saveBtn;
	
	private String flag;
	
	private UserData data;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.layout_accountedit, container, false);
		
		editTv = (TextView) view.findViewById(R.id.account_edit_tv);
		editEt = (EditText) view.findViewById(R.id.account_edit_et);
		saveBtn = (ImageView) view.findViewById(R.id.account_edit_btn);
	
		flag = getArguments().getString("flag");
		
		editTv.setText(flag);
		
		if(flag.equals("이름")) {
			editEt.setText(userData.myName);
			setEditTextFilter();
		}
		else
			editEt.setText(userData.email);
		
		saveBtn.setOnClickListener(this);
		
		return view;
	}

	private void setEditTextFilter() {
		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

				Pattern pattern = Pattern.compile("^[a-z0-9_]+$");
				if (!pattern.matcher(source).matches())
					return "";

				return null;

			}

		};
		editEt.setFilters(new InputFilter[] { filter });
	}

	@Override
	public void onClick(View v) {
		
		data = new UserData();
		data.userIdNum = userData.myIDNum;
		data.birth = userData.birth;
		data.nationalCode = userData.nationalCode;
		data.introduce = userData.introduce;
		
		if ( flag.equals("이름")) {
			
			if (!(editEt.getText().toString().length() > 1 && editEt.getText().toString().length() < 13)) {
				new ResultDialog(getActivity(), "정보수정", "이름은 2~12자로 입력해주세요").show();
				return;
			} else {
				data.myName = editEt.getText().toString();
				data.email = userData.email;
			}
			 
		} else {
			
			if (!isAvailableEmailType(editEt.getText().toString())) {
				new ResultDialog(getActivity(), "정보수정", "잘못된 이메일 형식입니다").show();
				return;
			} else {
				data.myName = userData.myName;
				data.email = editEt.getText().toString();
			}
		}
		
		ModifyUserInfoRequest request = new ModifyUserInfoRequest(data);
		request.setOnPostMethodProcessListener(this);
		NetworkModel.getInstance().sendNetworkData(getActivity(), request);
	}

	private boolean isAvailableEmailType(String string) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		new ResultDialog(getActivity(), "정보 수정", "정보 수정 성공").show();
		
		if ( flag.equals("이름") )  
			userData.myName = editEt.getText().toString();
		else 
			userData.email = editEt.getText().toString();
		
		
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<UserData> request) {
		new ErrorDialog(getActivity(), request.getResult().errorCode).show();
	}

	
}
