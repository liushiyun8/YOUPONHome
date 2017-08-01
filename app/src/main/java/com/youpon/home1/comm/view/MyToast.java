package com.youpon.home1.comm.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;

/**
 * Created by liuyun on 2017/4/5.
 */
public class MyToast {
    public static final int TYPE_OK=1;
    public static final int TYPE_ERROR=2;
    public static void show(Context context,int type, String message, int time){
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
        View view = LayoutInflater.from(context).inflate(R.layout.toastview, null);
        TextView tv= (TextView) view.findViewById(R.id.toast_info);
        ImageView iv = (ImageView) view.findViewById(R.id.toast_icon);
        if(type==TYPE_ERROR){
            iv.setImageResource(R.mipmap.msg_ic_fail);
        }else iv.setImageResource(R.mipmap.msg_ic_succe);
        tv.setText(message);
        toast.setView(view);
        toast.setDuration(time);
        toast.show();
    }
}
