package com.museda.util.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

	public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareImageView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	    int w = MeasureSpec.getSize(widthMeasureSpec);
	    int h = MeasureSpec.getSize(heightMeasureSpec);

		if ( w == 0 )
			w = h;
		if ( h == 0 )
			h = w;

	    setMeasuredDimension(w, h);

	}
}
