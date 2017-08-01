package com.youpon.home1.ui.home.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

public class MyService extends Service {

    private static MediaPlayer mediaPlayer;//媒体的播放器
    private static boolean mIsPaused = false;//是否在暂停状态
    private int resouceId;//传入的资源id
    private Vibrator vibrator;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        super.onCreate();
    }

    /**
     * 开始启动service
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        vibrator.vibrate(new long[]{1000,1000,1000,1000,1000,1000},-1);
        if(intent != null){
            resouceId=intent.getIntExtra("music", 0);
            if(resouceId != 0 && mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(this, resouceId);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * 恢复播放
     */
    public  static void onResume(){
        if(mediaPlayer != null && mIsPaused){
            mediaPlayer.start();
            mIsPaused=false;
        }
    }
    /**
     * 暂停播放
     */
    public  static void onPause(){
        if(mediaPlayer != null
                && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mIsPaused = true;
        }
    }
    /**
     * 停止播放 销毁service 并释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
        if(mediaPlayer != null ){
            mediaPlayer.stop();
            mediaPlayer=null;
        }
    }
}
