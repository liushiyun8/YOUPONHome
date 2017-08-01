package com.youpon.home1.comm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

public class CrashHandler implements UncaughtExceptionHandler {
	/** CrashHandler实例 */

	private static CrashHandler instance;
	private Context mContext;
	private UncaughtExceptionHandler mDefaultHandler;

	/** 获取CrashHandler实例 ,单例模式 */

	public static CrashHandler getInstance() {
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	public void init(Context context) {
		mContext = context;
		// 记录下默认的UncaughtExceptionHandler
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		String logdir;
		if (Environment.getExternalStorageDirectory() != null) {
			logdir = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "error"
					+ File.separator + "log";

			File file = new File(logdir);
			boolean mkSuccess;
			if (!file.isDirectory()) {
				mkSuccess = file.mkdirs();
				if (!mkSuccess) {
					mkSuccess = file.mkdirs();
				}
			}
			try {
				FileWriter fw = new FileWriter(logdir + File.separator
						+ "error.log", true);
				fw.write(new Date() + "\n");
				StackTraceElement[] stackTrace = ex.getStackTrace();
				fw.write(thread + ", Cause By:" + ex + "\r\n\r\n");
				for (int i = 0; i < stackTrace.length; i++) {
					fw.write(stackTrace[i].toString() + "\r\n");
				}
				fw.write("\n");
				fw.close();
			} catch (IOException e) {
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
