package com.museda.profile;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.util.CountryUtil;
import com.museda.util.DateUtil;
import com.museda.util.ErrorDialog;
import com.museda.util.ResultDialog;

public class ModifyUserInfoActivity extends Activity implements OnClickListener, OnPostMethodProcessListener<UserData> {

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");
	
	private TitleBar header;
	private Spinner countrySpinner;
	private TextView birthdayTv;
	private TextView introduceTextLengthTv;
	private EditText introduceEt;
	private ImageView saveBtn;

	private ArrayAdapter<String> adspin;
	private int spinnerPosition;

	private String birth;

	private String national;

	private String introduce;
	
	private int birthMonth;
	private int birthDay;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_modifyinfo);

		header = (TitleBar) findViewById(R.id.titlebar);
		countrySpinner = (Spinner) findViewById(R.id.modify_country_spinner);
		birthdayTv = (TextView) findViewById(R.id.modify_birthday_tv);
		introduceEt = (EditText) findViewById(R.id.modify_introduce_et);
		introduceTextLengthTv = (TextView) findViewById(R.id.modify_textlength_tv);
		saveBtn = (ImageView) findViewById(R.id.modify_save_btn);

		header.setTitleText("My Profile");
		adspin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CountryUtil.getCountryList());
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countrySpinner.setAdapter(adspin);
		countrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinnerPosition = position;
			}

			public void onNothingSelected(AdapterView<?> parent) {}

		});

		introduceEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				int textLength = introduceEt.getText().toString().length();
				if(textLength > 40) {
					new ResultDialog(ModifyUserInfoActivity.this, "정보 수정", "40자를 넘길 수 없습니다.").show();
				} else 
					introduceTextLengthTv.setText(textLength + "");
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}

		});
		
		birthdayTv.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		
		initInfo();
	}

	private void initInfo() {
		
		spinnerPosition = CountryUtil.getCountryList().indexOf(CountryUtil.getCountryFullName(userData.nationalCode));
		national = userData.nationalCode;
		countrySpinner.setSelection(spinnerPosition);
		birthdayTv.setText(DateUtil.getDisplayBirthDate(userData.birth));
		introduceEt.setText(userData.introduce);
		birthMonth = Integer.parseInt(userData.birth.substring(5, 7));
		birthDay = Integer.parseInt(userData.birth.substring(8, 10));
		
	}

	@Override
	public void onClick(View v) {
		if (v == birthdayTv)
			showDatePickerDialog();
		else if (v == saveBtn)
			modifyRequest();
	}

	private void modifyRequest() {
		
		national = CountryUtil.getISO2CountryCode((String) countrySpinner.getItemAtPosition(spinnerPosition));
		introduce = introduceEt.getText().toString();
		birth = DateUtil.getBirthDate(birthMonth, birthDay);
		Log.e("ModifyUserInfo modifyRequest", birth);
		
		if(introduce.length() < 2)
			introduce = "비공개 입니다.";
		
		UserData modifyData = new UserData(userData.myIDNum, userData.myName, birth, national, introduce);
		modifyData.email = userData.email;
		ModifyUserInfoRequest request = new ModifyUserInfoRequest(modifyData);	
		request.setOnPostMethodProcessListener(this);
		NetworkModel.getInstance().sendNetworkData(this, request);
		
	}

	private void showDatePickerDialog() {

		final DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, 1990, 0, 1);

		try{
		    Field[] datePickerDialogFields = datePickerDialog.getClass().getDeclaredFields();
		    for (Field datePickerDialogField : datePickerDialogFields) { 
		        if (datePickerDialogField.getName().equals("mDatePicker")) {
		            datePickerDialogField.setAccessible(true);
		            DatePicker datePicker = (DatePicker) datePickerDialogField.get(datePickerDialog);
		            datePicker.init(1990, 0, 1, new OnDateChangedListener() {
						
						@Override
						public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							datePickerDialog.setTitle("날짜 선택");
						}
					});
		            Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
		            for (Field datePickerField : datePickerFields) {
		               if ("mYearPicker".equals(datePickerField.getName()) || "mYearSpinner".equals(datePickerField.getName())) {
		                  datePickerField.setAccessible(true);
		                  Object dayPicker = new Object();
		                  dayPicker = datePickerField.get(datePicker);
		                  ((View) dayPicker).setVisibility(View.GONE);
		               }
		            }
		         }

		      }
		    }catch(Exception ex){
		    }
		datePickerDialog.setTitle("날짜 선택");
		datePickerDialog.show();

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			birthMonth = monthOfYear + 1;
			birthDay = dayOfMonth;
			birthdayTv.setText(DateUtil.getMonth(birthMonth) + " " + birthDay);
		}
	};

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		
		new ResultDialog(this, "정보 수정", "정보 수정 성공").show();
		userData.nationalCode = national;
		userData.birth = birth;
		userData.introduce = introduce;

		UserData.profileModify = true;
	}


	@Override
	public void onPostMethodProcessError(PostNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();		
	}

}
