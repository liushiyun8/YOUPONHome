/**
 * copyright @ iwit,. android team. 2014-2015
 */
package com.youpon.home1.comm;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import io.xlink.wifi.sdk.util.MyLog;


import com.se7en.utils.DeviceUtils;
import com.se7en.utils.SystemUtil;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.home.activities.NotifyEventInfoActivity;

import org.xutils.DbManager;
import org.xutils.x;


import java.io.Serializable;
import java.util.List;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkCode;
import io.xlink.wifi.sdk.bean.DataPoint;
import io.xlink.wifi.sdk.bean.EventNotify;
import io.xlink.wifi.sdk.listener.XlinkNetListener;

/**
 * @author yun
 *
 */
public class App extends Application implements XlinkNetListener{
	public static Context ctx;
	 private static SpUtils spUtil;
	 private static Typeface mFace;
	 
	public static Bitmap mBitmap;
	public static Drawable mDrawable;

	public static int mWidth = 0; 
    public static int mHeight = 0; 
    public static float mDensity = 0; 
    public static int mDensityDpi = 0;
	public static DbManager db;

	private static App application;

	private static final String TAG = "MyApp";


	/**
	 * 首选项设置
	 */
	public static SharedPreferences sharedPreferences;
	// 判断程序是否正常启动
	public boolean auth;
	//	public static RefWatcher refWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
//		MyLog.a=Boolean.FALSE;
		application=this;
		ctx = this.getApplicationContext();
		SystemUtil.setContext(this);
		DeviceUtils.setContext(this);
//		refWatcher = LeakCanary.install(this);
		XlinkAgent.init(this);
		XlinkAgent.getInstance().setPreInnerServiceMode(true);
		XlinkAgent.setCMServer("cm2.xlink.cn", 23778);
		XlinkAgent.getInstance().addXlinkListener(this);
//		CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
//		MiCO.init("https://v2.fogcloud.io");

		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		mWidth = dm.widthPixels;  // 屏幕宽度（像素）
		mHeight = dm.heightPixels; // 屏幕高度（像素）
		if(mWidth<mHeight){
			int i=mWidth;
			mWidth=mHeight;
			mHeight=i;
		}
		mDensity = dm.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
		mDensityDpi = dm.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
		MyLog.e("qh", "mWidth:"+mWidth);
		MyLog.e("qh", "mHeight:"+mHeight);
		MyLog.e("qh", "mDensity:"+mDensity);
		MyLog.e("qh", "mDensityDpi:"+mDensityDpi);
		x.Ext.init(this);
		sharedPreferences = getSharedPreferences("XlinkOfficiaDemo", Context.MODE_PRIVATE);
		appid = (int) getSp().get(Constant.SAVE_appId,0);
		authKey = (String) getSp().get(Constant.SAVE_authKey, "");
		String prodctid = (String) getSp().get(Constant.SAVE_PRODUCTID,Constant.PRODUCTID);
		String compayId = (String) getSp().get(Constant.SAVE_COMPANY_ID,Constant.COMPAYID);

		if (!TextUtils.isEmpty(prodctid)) {
			Constant.PRODUCTID = prodctid.replace(" ", "");
		}
		if (!TextUtils.isEmpty(compayId)) {
			HttpManage.COMPANY_ID = compayId.replace(" ", "");
		}
		// if (prodctid.equals("")) {
		// SharedPreferencesUtil.keepShared("pid", Constant.PRODUCTID);
		// } else if (prodctid.length() > 30) {
		// Constant.PRODUCTID = prodctid;
		// }
		// Constant.PRODUCTID= Constant.PRODUCTID.trim();
		// Constant.PRODUCTID=Constant.PRODUCTID.replace(" ", "");
		initHandler();
		for (Device device : DeviceManage.getInstance().getDevices()) {// 向sdk初始化设备
			MyLog.e(TAG, "init device:" + device.getMacAddress());
			XlinkAgent.getInstance().initDevice(device.getXDevice());
		}

		// 获取当前软件包版本号和版本名称
		try {
			PackageInfo pinfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionCode = pinfo.versionCode;
			versionName = pinfo.versionName;
			packageName = pinfo.packageName;

		} catch (PackageManager.NameNotFoundException e) {
		}

	}

	public String versionName;
	public int versionCode;
	public String packageName;
	private static Handler mainHandler = null;
	private String accessToken;

	public static void initHandler() {
		mainHandler = new Handler();
	}


	/**
	 * 执行在主线程任务
	 *
	 * @param runnable
	 */
	public static void postToMainThread(Runnable runnable) {
		mainHandler.post(runnable);
	}


	// 全局登录的 appId 和auth
	public int appid;
	public String authKey;
	public String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public static App getApp() {
		return application;
	}

	public void setAppid(int id) {
		appid = id;
	}

	public void setAuth(String auth) {
		this.authKey = auth;
	}

	public int getAppid() {
		return appid;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}


	public String getAuth() {
		return authKey;
	}

	// 当前的activity
	private Activity currentActivity;

	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}
	
	/**
	 * 统一获取Sp操作权
	 * @return
	 */
	public static SpUtils getSp(){
		if (spUtil == null) {
			spUtil = new SpUtils(ctx, Comconst.SP_FILE);
		}
		return spUtil;
	}

	/**
	 * 获得地区代码，比如CN/US/HK等
	 * 
	 * @return 系统语言
	 * @see
	 */
	public static String getCountryCode() {
		String able = ctx.getResources().getConfiguration().locale
				.getCountry();
		MyLog.v("qh", "able"+able);
		return able;
	}
	
	public static Typeface getTypeFace() {
		if(mFace == null){
			 mFace = Typeface.createFromAsset(ctx.getAssets(), "fonts/buffer.ttf");
		}
		return mFace;
	}
	/**
	 * 一些退出清理系统的事情都放这里做
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	@Override
	public void onStart(int code) {
// TODO Auto-generated method stub
		MyLog.e(TAG, "onStart code" + code);
		sendBroad(Constant.BROADCAST_ON_START, code);
	}

	@Override
	public void onLogin(int code) {
// TODO Auto-generated method stub
		MyLog.e(TAG, "login code" + code);
		sendBroad(Constant.BROADCAST_ON_LOGIN, code);
		if (code == XlinkCode.SUCCEED) {
			XlinkUtils.shortTips("云端网络已可用");
		} else if (code == XlinkCode.CLOUD_CONNECT_NO_NETWORK
				|| XlinkUtils.isConnected()) {
			// XlinkUtils.shortTips("网络不可用，请检查网络连接");

		} else {
			XlinkUtils.shortTips("连接到服务器失败，请检查网络连接");
		}
	}

	@Override
	public void onLocalDisconnect(int code) {
		if (code == XlinkCode.LOCAL_SERVICE_KILL) {
			// 这里是xlink服务被异常终结了（第三方清理软件，或者进入应用管理被强制停止应用/服务）
			// 永不结束的service
			// 除非调用 XlinkAgent.getInstance().stop（）;
			XlinkAgent.getInstance().start();
		}
		XlinkUtils.shortTips("本地网络已经断开");
	}

	@Override
	public void onDisconnect(int code) {
		if (code == XlinkCode.CLOUD_SERVICE_KILL) {
			// 这里是服务被异常终结了（第三方清理软件，或者进入应用管理被强制停止服务）
			if (appid != 0 && !TextUtils.isEmpty(authKey)) {
				XlinkAgent.getInstance().login(appid, authKey);
			}
		}
		XlinkUtils.shortTips("正在修复云端连接");
	}

	@Override
	public void onRecvPipeData(short i, XDevice xdevice, byte[] data) {
// TODO Auto-generated method stub
//		MyLog.e(TAG, "onRecvPipeData::device:" + xdevice.toString() + "data:"
//				+ new String(data).trim());
		Device device = DeviceManage.getInstance().getDevice(
				xdevice.getMacAddress());
		if (device != null) {
			// 发送广播，那个activity需要该数据可以监听广播，并获取数据，然后进行响应的处理
			sendPipeBroad(Constant.BROADCAST_RECVPIPE, device, data);
			// TimerManage.getInstance().parseByte(device,data);
		}
	}

	@Override
	public void onRecvPipeSyncData(short messageId, XDevice xdevice, byte[] data) {
		// TODO Auto-generated method stub
//		MyLog.e(TAG, "onRecvPipeSyncData::device:" + xdevice.toString() + "data:"
//				+new String(data).trim());
		Device device = DeviceManage.getInstance().getDevice(
				xdevice.getMacAddress());
		if (device != null) {
			// 发送广播，那个activity需要该数据可以监听广播，并获取数据，然后进行响应的处理
			// TimerManage.getInstance().parseByte(device,data);
			sendPipeBroad(Constant.BROADCAST_RECVPIPE_SYNC, device, data);
		}
	}

	public void sendBroad(String action, int code) {
		Intent intent = new Intent(action);
		intent.putExtra(Constant.STATUS, code);
		sendBroadcast(intent);
	}

	/**
	 */
	public void sendPipeBroad(String action, Device device, byte[] data) {
		Intent intent = new Intent(action);
		intent.putExtra(Constant.DEVICE_MAC, device.getMacAddress());
		if (data != null) {
			intent.putExtra(Constant.DATA, data);
		}
		sendBroadcast(intent);
	}

	@Override
	public void onDeviceStateChanged(XDevice xdevice, int state) {
		MyLog.e(TAG, "onDeviceStateChanged::" + xdevice.getMacAddress()
				+ " state:" + state);
		Device device = DeviceManage.getInstance().getDevice(
				xdevice.getMacAddress());
		if (device != null) {
			device.setxDevice(xdevice);
			Intent intent = new Intent(Constant.BROADCAST_DEVICE_CHANGED);
			intent.putExtra(Constant.DEVICE_MAC, device.getMacAddress());
			intent.putExtra(Constant.STATUS, state);
			sendBroadcast(intent);
		}
	}

	@Override
	public void onDataPointUpdate(XDevice xDevice, List<DataPoint> dataPionts, int i) {
		MyLog.e(TAG,"onDataPointUpdate:"+dataPionts.toString());

		Device device = DeviceManage.getInstance().getDevice(xDevice.getMacAddress());
		if (device != null) {
			Intent intent = new Intent(Constant.BROADCAST_DATAPOINT_RECV);
			intent.putExtra(Constant.DEVICE_MAC, device.getMacAddress());
			if (dataPionts != null) {
				intent.putExtra(Constant.DATA, (Serializable) dataPionts);
			}
			sendBroadcast(intent);
		}
	}

	@Override
	public void onEventNotify(EventNotify eventNotify) {
		String str = "EventNotify{" +
				"notyfyFlags=" + eventNotify.notyfyFlags +
				", formId=" + eventNotify.formId +
				", messageId=" + eventNotify.messageId +
				", messageType=" + eventNotify.messageType +
				", notifyData=" +new String(eventNotify.notifyData).trim() +
				'}';
		MyLog.e("APP",str);
		Intent intent1=new Intent(Constant.BROADCAST_EVENT_NOTIFY);
		intent1.putExtra(Constant.NOTIDATA,eventNotify);
		sendBroadcast(intent1);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				.setDefaults(Notification.DEFAULT_SOUND)
				.setOngoing(false)//不是正在进行的   true为正在进行  效果和.flag一样
				.setSmallIcon(R.mipmap.default_icon)
				.setContentTitle("友邦智能家居")
				.setContentText(new String(eventNotify.notifyData).trim());
		Notification notify = mBuilder.build();
		Intent intent=new Intent(this, NotifyEventInfoActivity.class);
		intent.putExtra(NotifyEventInfoActivity.NOTIFY_BUNDLE,eventNotify);
		PendingIntent pendingIntent=PendingIntent.getActivities(getApplicationContext(),0,new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);
		notify.contentIntent=pendingIntent;

		notify.flags = Notification.FLAG_AUTO_CANCEL;
		NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.notify(new Random().nextInt(), notify);
		mNotificationManager.notify(1, notify);
		XlinkUtils.longTips(new String(eventNotify.notifyData).trim());
	}
}
