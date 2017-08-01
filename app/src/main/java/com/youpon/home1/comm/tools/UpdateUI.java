package com.youpon.home1.comm.tools;


import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuyun on 2017/5/19.
 */
public abstract class UpdateUI {
    private Runnable runnable;
    long oldtime;
    Handler handler=new Handler();

    public UpdateUI() {
        runnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

    }

    public  void updade(){
        long now=System.currentTimeMillis();
        if(now-oldtime>=1000){
            handler.removeCallbacks(runnable);
            getData();
            oldtime=now;
            return;
       }
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,300);
    };
    public abstract void getData();
}
