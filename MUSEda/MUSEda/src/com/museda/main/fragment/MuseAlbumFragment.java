package com.museda.main.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.museda.PhotoData;
import com.museda.R;
import com.museda.SingletonData;
import com.museda.TitleBar;
import com.museda.UserData;
import com.museda.adapter.MuseAlbumListAdapter;
import com.museda.detail.PhotoDetailActivity;
import com.museda.network.GetNetworkRequest;
import com.museda.network.GetNetworkRequest.OnGetMethodProcessListener;
import com.museda.network.NetworkModel;
import com.museda.photo.PhotoCheckActivity;
import com.museda.util.TimeUtil;
import com.museda.util.views.RoundedImageView;
import com.museda.views.OnPhotoItemClickListener;

public class MuseAlbumFragment extends Fragment implements OnRefreshListener<ListView>, OnPhotoItemClickListener, OnClickListener, OnGetMethodProcessListener<ArrayList<PhotoData>>, OnScrollListener {

//	private String photoUpMenuItems[] = { "갤러리", "카메라" };
	private String photoUpMenuItems[] = { "갤러리" };

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private final int UPDATE_OK = 2;

	private ProgressDialog dialog;
	
	private Uri fileUri;

	private UserData userData = SingletonData.getInstance().getUserData().get("UserData");
	private TitleBar header;

	private PullToRefreshListView mPullRefreshListView;
	private ListView albumListView;

	private MusePhotoRequest albumRequest;
	private PhotoData data;

	private AQuery aq;
	
	private ImageView photoUpBtn;
	private RelativeLayout noPictureLayout;
	private RoundedImageView profileIv;
	private RoundedImageView noPictureProfileIv;
	private TextView noPicturePhotoCountTv;
	private TextView photoCountTv;
	private ImageView noPictureImage;
	
	private ArrayList<PhotoData> photoList;
	
	private int lastPhotoId;
	private int responseListSize;
	private boolean lockListView;
	
	private MuseAlbumListAdapter adapter;
	
	private View listHeader;
//	private View listFooter;


	public static Fragment newInstance(Context context) {
		MuseAlbumFragment layout = new MuseAlbumFragment();
		return layout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_musealbum, container, false);
		
		listHeader = getActivity().getLayoutInflater().inflate(R.layout.layout_musealbumlist_header, null);
//		listFooter = getActivity().getLayoutInflater().inflate(R.layout.layout_listfooter,null);
		
		aq = new AQuery(getActivity());

		photoCountTv = (TextView) listHeader.findViewById(R.id.photonum_tv);
		profileIv = (RoundedImageView) listHeader.findViewById(R.id.myphoto_profile_iv);
		noPictureProfileIv = (RoundedImageView) view.findViewById(R.id.first_myphoto_profile_iv);
		header = (TitleBar) view.findViewById(R.id.titlebar);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.myphoto_list);
		photoUpBtn = (ImageView) view.findViewById(R.id.photoup_btn);
		noPictureLayout = (RelativeLayout) view.findViewById(R.id.firstalbum_layout);
		noPicturePhotoCountTv = (TextView) view.findViewById(R.id.first_photonum_tv);		
		noPictureImage = (ImageView) view.findViewById(R.id.first_picture);
		
		photoUpBtn.setOnClickListener(this);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setOnScrollListener(this);
		mPullRefreshListView.setVerticalScrollBarEnabled(false);
		albumListView = mPullRefreshListView.getRefreshableView();
		albumListView.addHeaderView(listHeader);
		albumListView.setDivider(null);

		header.setTitleImage(R.drawable.albumbar);
		setBtnVisibility(false, false);

		photoList = new ArrayList<PhotoData>();
		adapter = new MuseAlbumListAdapter(getActivity(), this.photoList);
		adapter.setOnPhotoItemClickListener(this);
		albumListView.setAdapter(adapter);
		
		return view;

	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		adapter.notifyDataSetChanged();
		
		if ( UserData.photoModify ) {
			if ( photoList.size() > 0 )
				photoList.removeAll(photoList);
			UserData.photoModify = false;
			requestMuseData(0);
		}
	}

	private void requestMuseData(int startPhotoId) {
		
		lockListView = true;
		
		data = new PhotoData(userData.myIDNum, userData.myIDNum, 10, startPhotoId, "previous", "true");
		albumRequest = new MusePhotoRequest(new ArrayList<PhotoData>(), data);
		albumRequest.setOnGetMethodProcessListener(this);
		NetworkModel.getInstance().getNetworkData(getActivity(), albumRequest);
		
		dialog = ProgressDialog.show(getActivity(), "사진을 불러오는 중입니다.", "잠시만 기다려주세요",true);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		photoList.removeAll(photoList);
		requestMuseData(0);
	}

	public void setBtnVisibility(boolean backBtn, boolean doneBtn) {
		header.setBackBtnVisible(backBtn);
		header.setDoneBtnVisible(doneBtn);
	}

	@Override
	public void onItemClick(View view, int position) {
		switch (view.getId()) {
		case R.id.myphoto_iv:
			Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
			intent.putExtra("flag", "MuseAlbumFragment");
			intent.putExtra("userId", photoList.get(position).museIdNum);
			intent.putExtra("photoId", photoList.get(position).photoIdNum);
			intent.putExtra("photoIndex", position);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == photoUpBtn) {
			showPhotoUpDialog();
		}
	}

	private void showPhotoUpDialog() {
		new AlertDialog.Builder(getActivity()).setTitle("사진 업로드").setItems(photoUpMenuItems, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int position) {
				if (position == 0)
					doTakeAlbumAction();
//				else
//					doTakePhotoAction();
			}
		}).show();
	}


	private void doTakeAlbumAction() {
		// 앨범 호출
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);

	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != getActivity().RESULT_OK)
			return;

		switch (requestCode) {
		case PICK_FROM_ALBUM:
			fileUri = data.getData();
		case PICK_FROM_CAMERA:
			if (fileUri != null) {
				Intent intent = new Intent(getActivity(), PhotoCheckActivity.class);
				intent.putExtra("uri", fileUri.toString());
				intent.putExtra("flag", "Album");
				startActivityForResult(intent, UPDATE_OK);
				break;
			}
		case UPDATE_OK :
			requestMuseData(0);
			
			mPullRefreshListView.setVisibility(View.VISIBLE);
			noPictureLayout.setVisibility(View.GONE);
			noPictureImage.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		int count = totalItemCount - visibleItemCount;
		
		if(firstVisibleItem >= count && totalItemCount != 0 && lockListView == false ) {
			Log.i("MuseAlbumFragment", "Loading next items");
			if(responseListSize >= 10) {
//				albumListView.addFooterView(listFooter);
				requestMuseData(lastPhotoId);
			}
//			} else
//				albumListView.removeFooterView(listFooter);
		}  

	}

	@Override
	public void onGetMethodProcessSuccess(GetNetworkRequest<ArrayList<PhotoData>> request) {
		this.photoList.addAll(request.getResult());

		lockListView = false;
		mPullRefreshListView.onRefreshComplete();
		adapter.notifyDataSetChanged();
		aq.id(profileIv).image(userData.profileThumbPhotoPath);
		responseListSize = request.getResult().size();
		
		if (this.photoList.size() == 0) {
			noPictureImage.setVisibility(View.VISIBLE);
			noPictureLayout.setVisibility(View.VISIBLE);
			mPullRefreshListView.setVisibility(View.GONE);
			aq.id(noPicturePhotoCountTv).text("일상의 아름다움을 간직하세요");
			aq.id(noPictureProfileIv).image(userData.profileThumbPhotoPath);
			aq.id(R.id.assigntime_tv).text(TimeUtil.getConvertDay(userData.assignDate) + " " + TimeUtil.getMuseMyPhotoDayInfo(userData.assignDate));
		} else { 
			noPictureImage.setVisibility(View.GONE);
			noPictureLayout.setVisibility(View.GONE);
			mPullRefreshListView.setVisibility(View.VISIBLE);
			lastPhotoId = this.photoList.get(photoList.size()-1).photoIdNum;
			aq.id(photoCountTv).text("  " + adapter.getCount() + "장의 아름다움  ");
		}
		
		dialog.dismiss();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
	@Override
	public void onGetMethodProcessError(GetNetworkRequest<ArrayList<PhotoData>> request) {}
}
