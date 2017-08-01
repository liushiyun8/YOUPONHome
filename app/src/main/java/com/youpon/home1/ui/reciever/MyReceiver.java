package com.youpon.home1.ui.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.youpon.home1.comm.base.EventData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onDataEvnet(EventData eventData){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())){
//            EventBus.getDefault().register(this);
//            EventBus.getDefault().post(new EventData(EventData.TAG_REFRESH,""));
        }

    }
}
