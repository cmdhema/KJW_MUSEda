package com.museda.setting.noti;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.museda.ParentActivity;
import com.museda.R;
import com.museda.TitleBar;
import com.museda.adapter.NotiAdapter;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;

public class NotiListActivity extends ParentActivity implements OnGetMethodProcessListener<ArrayList<NotiData>> {

	private ListView notiListView;

	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_notilist);
		
		TitleBar header = (TitleBar) findViewById(R.id.titlebar);
		header.setTitleText("Notification");
		notiListView = (ListView) findViewById(R.id.notilist_listview);
		
		NotiRequest request = new NotiRequest(new ArrayList<NotiData>());
		request.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(this, request);
		
		dialog = ProgressDialog.show(this, "공지사항 목록을 불러오는 중입니다.", "잠시만 기다려주세요.");
		
	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<NotiData>> request) {
		NotiAdapter adapter = new NotiAdapter(this, request.getResult());
		notiListView.setAdapter(adapter);
		
		dialog.dismiss();
	}

	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<NotiData>> request) {
		dialog.dismiss();
	}

}
