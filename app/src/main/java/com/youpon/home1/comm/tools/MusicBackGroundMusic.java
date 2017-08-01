package com.youpon.home1.comm.tools;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicBackGroundMusic extends Service{
	
	private static MediaPlayer mediaPlayer;//媒体的播放器
	private static boolean mIsPaused = false;//是否在暂停状态
	private int resouceId;//传入的资源id
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	/**
	 * 开始启动service
	 */
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		if(intent != null){
			resouceId=intent.getIntExtra("music", 0);
			if(resouceId != 0 && mediaPlayer==null){
				mediaPlayer = MediaPlayer.create(this, resouceId);
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
			}
		}
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
		if(mediaPlayer != null ){
		    mediaPlayer.stop();
		    mediaPlayer=null;
		}
	}
}
