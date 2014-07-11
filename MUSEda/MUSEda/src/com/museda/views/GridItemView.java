package com.museda.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.museda.PhotoData;
import com.museda.R;

public class GridItemView extends FrameLayout {

	AQuery aq;
	
	ImageView gridImageView;
	
	public GridItemView(Context context) {
		super(context);
		aq = new AQuery(context);
		LayoutInflater.from(context).inflate(R.layout.photogridview_row, this);
		
		gridImageView = (ImageView) findViewById(R.id.grid_row);
	}

	public void setData(PhotoData photoData) {
		aq.id(gridImageView).image(photoData.photoThumbPath, true, true, 0, 0, null, AQuery.FADE_IN);
	}
}
