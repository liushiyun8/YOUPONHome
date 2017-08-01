package com.youpon.home1.comm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by liuyun on 2017/6/7.
 */
public class MyListView extends ListView {
    private float maxHeight;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxheight(float maxheight){
        this.maxHeight=maxheight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if(size>maxHeight&&maxHeight>0){
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(Float.valueOf(maxHeight).intValue(),
                    MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
