package com.museda.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorDialog extends AlertDialog.Builder {

	
	public ErrorDialog(Context context, int code) {
		super(context);
		
		setNeutralButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
			
		});
		
		setMessage("[에러코드 : " + code +"] " +  ErrorCode.getErrorMessage(code));
	}
}
