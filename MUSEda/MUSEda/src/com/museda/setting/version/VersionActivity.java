package com.museda.setting.version;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.museda.R;
import com.museda.TitleBar;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.util.ErrorDialog;
import com.museda.util.ResultDialog;

public class VersionActivity extends Activity{

	private TitleBar titleBar;
	
	private Button updateBtn;
	private TextView currentVersionTextView;
	private TextView recentVersionTextView;
	
	private String currentVersion;
	private String recentVersion;
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_version);
		
		updateBtn = (Button) findViewById(R.id.update_btn);
		currentVersionTextView = (TextView) findViewById(R.id.currentversion_tv);
		recentVersionTextView = (TextView) findViewById(R.id.recentversion_tv);
		titleBar = (TitleBar) findViewById(R.id.titlebar);
		
		titleBar.setTitleText("���� ����");
		
		checkUpdate();
		
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if ( !currentVersion.equals(recentVersion) ) {
					new AlertDialog.Builder(VersionActivity.this).setTitle("������Ʈ").setMessage("������Ʈ�� �ֽ��ϴ�. ������Ʈ�� �޾��ּ���").setPositiveButton("������Ʈ �Ϸ�����", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse("market://details?id=" + getPackageName()));
							startActivity(intent);
						}
					}).setNegativeButton("���", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).show();
				} else 
					new ResultDialog(VersionActivity.this, "������Ʈ", "�ֽŹ����Դϴ�.").show();
			}
		});
	
	}
	
	private void checkUpdate() {

		VersionRequest request = new VersionRequest(new AppData());
		request.setOnGetMethodProcessListener(new OnGetMethodProcessListener<AppData>() {

			@Override
			public void onGetMethodProcessSuccess(GetNetworkRequest<AppData> request) {
				recentVersion = request.getResult().versionCode;
				currentVersion = null;
				
				try {
					PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
					currentVersion = info.versionName;
					
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
				currentVersionTextView.setText(currentVersion);
				recentVersionTextView.setText(recentVersion);
				
				dialog.dismiss();
			}

			@Override
			public void onGetMethodProcessError(GetNetworkRequest<AppData> request) {
				new ErrorDialog(VersionActivity.this, request.getResult().errorCode).show();
				dialog.dismiss();
			}
		});
		NetworkModel.getInstance().getNetworkData(this, request);
		
		dialog = ProgressDialog.show(this, "�������� ����� �ҷ����� ���Դϴ�.", "��ø� ��ٷ��ּ���.");
		
	}
	
}
