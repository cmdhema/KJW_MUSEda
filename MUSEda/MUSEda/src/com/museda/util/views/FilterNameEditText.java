package com.museda.util.views;

import java.util.regex.Pattern;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

public class FilterNameEditText extends EditText {

	public FilterNameEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public FilterNameEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FilterNameEditText(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

				Pattern pattern = Pattern.compile("^[a-z0-9_A-Z]+$");
				if (!pattern.matcher(source).matches())
					return "";

				return null;

			}

		};
		setFilters(new InputFilter[] { filter });
	}

}
