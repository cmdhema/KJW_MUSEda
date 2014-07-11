package com.museda.assign;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.androidquery.AQuery;
import com.museda.MainActivity;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.UserData;
import com.museda.encryption.Encrypt;
import com.museda.network.MultiPartNetworkRequest;
import com.museda.network.MultiPartNetworkRequest.OnMultiPartProcessListener;
import com.museda.network.NetworkModel;
import com.museda.network.PostNetworkRequest;
import com.museda.network.PostNetworkRequest.OnPostMethodProcessListener;
import com.museda.util.CountryUtil;
import com.museda.util.ErrorDialog;
import com.museda.util.FileUtil;
import com.museda.util.ResultDialog;
import com.museda.util.views.FilterIdEditText;
import com.museda.util.views.FilterNameEditText;

public class AssignActivity extends Activity implements OnClickListener, OnMultiPartProcessListener<UserData>, OnPostMethodProcessListener<UserData> {

	private final int PICK_FROM_CAMERA = 0;
	private final int PICK_FROM_ALBUM = 1;
	private final int CROP_FROM_CAMERA = 2;

	private Uri mImageCaptureUri;

	private String flag;

	private ImageView backBtn;
	private ImageView cancelBtn;
	
	private FilterIdEditText idEt;
	private FilterNameEditText nameEt;
	private EditText emailEt;
	private EditText passwordEt;
	private EditText passwordReEt;
	private Spinner countrySpinner;

	private Button doneBtn;
	private ImageView nextBtn;
	private ImageView profileBtn;

	private LinearLayout assignLayout;
	private LinearLayout photoupLayout;

	private ArrayAdapter<String> adspin;

	private int spinnerPosition;

	private String id;
	private String pw;
	private String pwRe;
	private long tempTime;
	private Uri cropImageUri;

	private String uploadImagePath;
	private ProgressDialog dialog;
	private String croppedImagePath;
	
	private ScrollView backGroundLayout;
	
	private int viewIndex;
	private AQuery aq;
	private Animation fromRight;
	private Animation fromLeft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_assign);

		flag = getIntent().getStringExtra("flag");

		backBtn = (ImageView) findViewById(R.id.back_btn);
		cancelBtn = (ImageView) findViewById(R.id.cancel_btn);
		doneBtn = (Button) findViewById(R.id.assign_done_btn);
		idEt = (FilterIdEditText) findViewById(R.id.id_et);
		nameEt = (FilterNameEditText) findViewById(R.id.name_et);
		passwordEt = (EditText) findViewById(R.id.password_et);
		passwordReEt = (EditText) findViewById(R.id.password_re_et);
		countrySpinner = (Spinner) findViewById(R.id.country_et);
		emailEt = (EditText) findViewById(R.id.email_et);
		assignLayout = (LinearLayout) findViewById(R.id.assign_layout);
		photoupLayout = (LinearLayout) findViewById(R.id.musephotoup_layout);
		nextBtn = (ImageView) findViewById(R.id.assign_next_btn);
		profileBtn = (ImageView) findViewById(R.id.photoup_iv);
		backGroundLayout = (ScrollView) findViewById(R.id.assign_background);

		idEt.setPrivateImeOptions("defaultInputmode=english;");
		nameEt.setPrivateImeOptions("defaultInputmode=english;");
		passwordEt.setPrivateImeOptions("defaultInputmode=english;");
		passwordReEt.setPrivateImeOptions("defaultInputmode=english;");
		emailEt.setPrivateImeOptions("defaultInputmode=english;");

		adspin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CountryUtil.getCountryList());
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		countrySpinner.setAdapter(adspin);
		countrySpinner.setSelection(CountryUtil.getCountryList().indexOf(CountryUtil.getCountryFullName(CountryUtil.getISO2CountryCode())));
		countrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinnerPosition = position;
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		if (flag.equals("MuseAssign")) {
			photoupLayout.setVisibility(View.VISIBLE);
			assignLayout.setVisibility(View.GONE);
		} else if (flag.equals("NormalAssign")) {
			photoupLayout.setVisibility(View.GONE);
			assignLayout.setVisibility(View.VISIBLE);
			backGroundLayout.setBackgroundResource(R.color.white);
		}

		doneBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

		aq = new AQuery(this);
		fromRight = AnimationUtils.loadAnimation(aq.getContext(), R.anim.from_right_out);
		fromLeft = AnimationUtils.loadAnimation(aq.getContext(), R.anim.from_left_out);

	}


	@Override
	public void onClick(View v) {
		if (v == profileBtn) {
			doTakeAlbumAction();
		} else if (v == nextBtn) {
			aq.id(assignLayout).getView().startAnimation(fromRight);
			assignLayout.setVisibility(View.VISIBLE);
			photoupLayout.setVisibility(View.GONE);
//			backGroundLayout.setBackgroundResource(R.color.white);
			backBtn.setVisibility(View.VISIBLE);
			cancelBtn.setVisibility(View.INVISIBLE);
			viewIndex++;
		} else if (v == doneBtn) {
			requestAssign();
		} else if ( v == backBtn ) {
			viewIndex--;
			if ( viewIndex == 0 ) {
				backBtn.setVisibility(View.INVISIBLE);
				aq.id(photoupLayout).getView().startAnimation(fromLeft);
				photoupLayout.setVisibility(View.VISIBLE);
				assignLayout.setVisibility(View.GONE);
				cancelBtn.setVisibility(View.VISIBLE);
			}
		} else if (v == cancelBtn ) {
			finish();
			viewIndex = 0;
		}
	}

	private void requestAssign() {
		id = idEt.getText().toString();
		pw = passwordEt.getText().toString();
		pwRe = passwordReEt.getText().toString();
		
		if ( !pw.equals(pwRe)) {
			new ResultDialog(this, "회원가입", "입력하신 비밀번호가 일치하지 않습니다. 다시 입력해주세요").show();
			return;
		}
		
		String name = nameEt.getText().toString();
		String country = CountryUtil.getISO2CountryCode((String) countrySpinner.getItemAtPosition(spinnerPosition));
		String email = emailEt.getText().toString();
		String userType;
		String photoUri;
		if (flag.equals("MuseAssign")) {
			userType = "1";
//			photoUri = mImageCaptureUri.getPath();
			photoUri = uploadImagePath;
			
		} else {
			userType = "0";
			photoUri = "";
		}

		UserData assignData = new UserData(id, pw, name, email, country, photoUri, userType);
		if (isAvailable(id, pw, name, email)) {
			if (flag.equals("MuseAssign")) {

				MuseAssignRequest request = new MuseAssignRequest(assignData);
				request.setOnMultiPartProcessListener(this);
				NetworkModel.getInstance().sendPhotoData(this, request);
			} else {
				NormalAssignRequest normalAssignRequest = new NormalAssignRequest(assignData);
				normalAssignRequest.setOnPostMethodProcessListener(this);
				NetworkModel.getInstance().sendNetworkData(this, normalAssignRequest);
			}
		}
		
		dialog = ProgressDialog.show(this, "회원가입 중입니다.", "잠시만 기다려주세요",true);
	}

	private boolean isAvailable(String id, String pw, String name, String email) {

		if (!(id.length() > 3 && id.length() < 13)) {
			new ResultDialog(this, "회원가입", "아이디는 4~12자로 입력해주세요").show();
			return false;
		} else if (!(pw.length() > 7 && pw.length() < 13)) {
			new ResultDialog(this, "회원가입", "비밀번호는 8~12자로 입력해주세요").show();
			return false;
		} else if (!(name.length() > 1 && name.length() < 13)) {
			new ResultDialog(this, "회원가입", "이름은 2~12자로 입력해주세요").show();
			return false;
		} else if (!isAvailableEmailType(email)) {
			new ResultDialog(this, "회원가입", "잘못된 이메일 형식입니다").show();
			return false;
		}

		return true;
	}

	private boolean isAvailableEmailType(String email) {

		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

//    private void doTakePhotoAction() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Crop된 이미지를 저장할 파일의 경로를 생성
//        mImageCaptureUri = createSaveCropFile();
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//        startActivityForResult(intent, PICK_FROM_CAMERA);
//    }

    /**
     * 앨범 호출 하기
     */
    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

	/**
     * Result Code
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }
 
        switch(requestCode) {
        case PICK_FROM_ALBUM : {
             
            mImageCaptureUri = data.getData();
        }
        case PICK_FROM_CAMERA : {
 
            // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
            // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
 
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(mImageCaptureUri, "image/*"); 
            tempTime = System.currentTimeMillis();
            cropImageUri = FileUtil.createSaveCropFile(tempTime);
            
    		// Crop한 이미지를 저장할 Path
            intent.putExtra("output", cropImageUri);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            
            startActivityForResult(intent, CROP_FROM_CAMERA);
 
            break;
        }
         
        case CROP_FROM_CAMERA: {
        	
        	String fileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
    		
    		uploadImagePath = PhotoData.imageDir + "/" + fileName;
    		croppedImagePath = FileUtil.getFilePath(tempTime);
            FileUtil.getInstance().modifyImageSizeTask(uploadImagePath, croppedImagePath );
            
            profileBtn.setImageURI(mImageCaptureUri);
			nextBtn.setVisibility(View.VISIBLE);
        	break;
        }
	    }
    }

	@Override
	public void onMultiPartProcessSuccess(MultiPartNetworkRequest<UserData> request) {

		dialog.dismiss();
		
		//이 코드가 있는 이유는...... 이 변수가 static 이기 때문에 만약 로그인 후에 로그아웃 하고 다시 가입했을 경우 MuseAlbumFragment에서 onResume에 있는 request 함수를 안타기 때문에 넣어놈... 나중에 봐도 이해안될걸?? 
		UserData.photoModify = true;
		
		new AlertDialog.Builder(this).setTitle("회원가입").setMessage("회원가입에 성공했습니다.").setNeutralButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();

				try {
					editor.putString("pw", Encrypt.encrypt(pw));
					editor.putString("id", id);
					editor.putBoolean("login", false);
					editor.commit();

					Intent intent = new Intent(AssignActivity.this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).show();
		
		FileUtil.removeImage(croppedImagePath);
		FileUtil.removeImage(uploadImagePath);
		
	}

	@Override
	public void onMultiPartProcessError(MultiPartNetworkRequest<UserData> request) {

		new ErrorDialog(this, request.getResult().errorCode).show();
		dialog.dismiss();
	}

	@Override
	public void onPostMethodProcessSuccess(PostNetworkRequest<UserData> request) {
		dialog.dismiss();
		
		new AlertDialog.Builder(this).setTitle("회원가입").setMessage("회원가입에 성공했습니다.").setNeutralButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();

				try {
					editor.putString("pw", Encrypt.encrypt(pw));
					editor.putString("id", id);
					editor.putBoolean("login", false);
					editor.commit();

					Intent intent = new Intent(AssignActivity.this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).show();
	}

	@Override
	public void onPostMethodProcessError(PostNetworkRequest<UserData> request) {
		new ErrorDialog(this, request.getResult().errorCode).show();
		dialog.dismiss();
	}

}