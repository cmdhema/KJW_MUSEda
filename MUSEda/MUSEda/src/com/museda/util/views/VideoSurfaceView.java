package com.museda.util.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.museda.R;


public class VideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Context mContext;
	
	private MediaPlayer mPlayer;
	
	private Uri video;
	
	private String flag;
	
	@SuppressWarnings("deprecation")
	public VideoSurfaceView(Context context, String flag) {
		super(context);

		mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		this.flag = flag;
		
		if(Build.VERSION.SDK_INT < 11) // API 11 버전 이하에서는 아래 옵션 없으면 동영상 재생이 안됨.
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		video = Uri.parse("android.resource://" + "com.museda/" + R.raw.museda_intro_resize);

		if (mPlayer == null) 
			mPlayer = new MediaPlayer();
		else
			mPlayer.reset();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		try{
			mPlayer.setDataSource(mContext, video);
			mPlayer.setDisplay(mHolder);
			mPlayer.prepare();
			if(flag.equals("MainActivity"))
				mPlayer.setLooping(true);
			else
				mPlayer.setLooping(false);
			mPlayer.start();

		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}

	public MediaPlayer getmPlayer() {
		return mPlayer;
	}
	
	
}
