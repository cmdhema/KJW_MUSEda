package com.museda.setting;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.museda.R;
import com.museda.util.views.VideoSurfaceView;

public class IntroMovieActivity extends Activity {

	//배경화면 재생을 위한 SurfaceView
	private VideoSurfaceView  videoView;
	
	//Layout View
	private LinearLayout videoLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_intromovie);
		videoLayout = (LinearLayout) findViewById(R.id.movie_layout);
			
		videoView = new VideoSurfaceView(getApplicationContext(),"IntroMovieActivity");
		videoLayout.addView(videoView);
		
		videoView.getmPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				IntroMovieActivity.this.finish();
			}
		});

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		videoView.getmPlayer().stop();
		
	}
}
