package com.youpon.home1.comm.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * Description  : 文本框滚动控制类
 * @author      : 
 * @date        : 2014-6-26
 * @version     :
 * @see         :[class/class#method]
 * @since       :[product/model]
 */
public class ScrollForeverTextView extends TextView {

	public ScrollForeverTextView(Context con) {
		super(con);
	}

	public ScrollForeverTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollForeverTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
	}
}
