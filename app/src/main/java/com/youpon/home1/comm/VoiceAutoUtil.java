package com.youpon.home1.comm;


import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class VoiceAutoUtil {
	/**
	 * 在此界面不锁屏
	 */
	public static void acquireWakeLock(WakeLock mWakeLock,Context ctx) {
		if (mWakeLock == null) {
			PowerManager mPowerManager = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
			mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,ctx.getClass().getCanonicalName());
					
			mWakeLock.acquire();
		}
	}

	/**
	 * 退出界面后 恢复锁屏
	 */
	public static void releaseWakeLock(WakeLock mWakeLock) {
		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}
	
	public static boolean WaitForThreadStop(Thread mThread) {
		int WaitCount = 0;
		do {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!mThread.isAlive())
				return true;
		} while (WaitCount++ < 20);
		mThread.stop();
		return false;
	}
}
