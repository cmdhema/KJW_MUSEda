package com.museda.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ResultDialog extends AlertDialog.Builder {

	public ResultDialog(Context context, String title, String msg) {
		super(context);
	
		setNeutralButton("»Æ¿Œ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
			
		});
		setTitle(title);
		setMessage(msg);
	}

}
