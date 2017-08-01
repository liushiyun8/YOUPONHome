package com.youpon.home1.ui.home.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.youpon.home1.comm.App;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.http.HttpManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MyTokenService extends Service {

    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
    }

    /**
     * 开始启动service
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("tag","start");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                HttpManage.getInstance().refreshToken(new MyCallback() {
                    @Override
                    public void onSuc(String result) {
                        Log.e("tag",result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            App.getApp().setAccessToken(jsonObject.optString("access_token"));
                            App.getApp().setRefreshToken(jsonObject.optString("refresh_token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        Log.e("tag1",msg);
                    }
                });
            }
        };
        timer.schedule(timerTask,60*1000,1800*1000);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
