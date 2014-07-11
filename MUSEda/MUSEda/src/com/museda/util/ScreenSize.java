package com.museda.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenSize {

	public static int dpToPx(int dp, Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static float getScreenWidth(Context context) {

		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();

		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		return outMetrics.widthPixels;
	}

}
