package com.youpon.home1.comm.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

import com.youpon.home1.comm.App;



/**
 * Created by Administrator on 16-9-28.
 */
public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
        init();
    }

//    setTypeface(App.getTypeFace());

    private void init() {

    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public boolean post(Runnable action) {
        // FIXME: 2017/5/25 android 7.0以上post方法发生改变，导致点击事件无效
        if(Build.VERSION.SDK_INT >= 24){
            mHandler.post(action);
            return true;
        }
        return super.post(action);
    }

//    @Override
//    public boolean removeCallbacks(Runnable action) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && action != null && mHandler != null) {
//            mHandler.removeCallbacks(action);
//            return true;
//        }
//        return super.removeCallbacks(action);
//    }

}
