package com.youpon.home1.comm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.youpon.home1.comm.App;


/**
 * Created by Administrator on 16-9-28.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setTypeface(App.getTypeFace());
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }
}
