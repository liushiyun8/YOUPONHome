package com.youpon.home1.comm.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.youpon.home1.R;
import com.youpon.home1.ui.user.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 16-8-25.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Context mContext;
    private boolean isExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = getApplication().getApplicationContext();
        setStatusBar(false);
//        getWindow().setStatusBarColor(Color.parseColor("#192548"));
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        registerReceiver();
    }

    //修改当前 Activity 的显示模式，hideStatusBarBackground :true 全屏模式，false 着色模式
    private void setStatusBar(boolean hideStatusBarBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Log.e("html","453453");
            if (hideStatusBarBackground) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }

            ViewGroup mChildView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//            ViewGroup mChildView = (ViewGroup) window.getDecorView();
//            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                if (hideStatusBarBackground) {
                    mChildView.setPadding(
                            mChildView.getPaddingLeft(),
                            0,
                            mChildView.getPaddingRight(),
                            mChildView.getPaddingBottom()
                    );
                } else {
                    int statusHeight = getStatusBarHeight(this);
                    View view = new View(this);
                    view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,statusHeight));
                    view.setBackgroundColor(getResources().getColor(R.color.title_bar));
                    Log.e("TAGGGG","daozheli"+statusHeight);
                    ((ViewGroup)window.getDecorView()).addView(view);
//                    ViewGroup rootView =(ViewGroup)findViewById(android.R.id.content);
//                    rootView.setFitsSystemWindows(true);
//                    rootView.setClipToPadding(true);
                    mChildView.setPadding(
                            mChildView.getPaddingLeft(),
                            statusHeight,
                            mChildView.getPaddingRight(),
                            mChildView.getPaddingBottom()
                    );
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (hideStatusBarBackground) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                window.setStatusBarColor(getResources().getColor(R.color.title_bar));
            }
        }
    }

    //get StatusBar Height
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
//    public static int getStatusBarHeight(Activity activity) {
//        Rect frame = new Rect();
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        return frame.top;
//    }



    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter("exit");
        BaseActivity.this.registerReceiver(ExitReceiver, intentFilter);

    }

    protected BroadcastReceiver ExitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            unregisterReceiver(this);
            isExit = true;
            finish();
            //	App.getSp().remove(CommConst.SP_KEY_CURRENT_USER);
            //	App.getSp().remove(CommConst.SP_KEY_CURRENT_RESTYPES);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//		App.refWatcher.watch(this);
        try {
//            if(isExit){
//                Intent startMain = new Intent(this, MainActivity.class);
//                startMain.addCategory(Intent.CATEGORY_HOME);
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(startMain);//回到设备主界面，防止读书界面无法退出。
//            }

            this.unregisterReceiver(ExitReceiver);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")
                    || e.getMessage().contains("unregisterReceiver")) {
            } else {
                // throw e;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (getSystemVersion()> 18) {
//                         SharedPreferences sPreferences = getSharedPreferences("AppBrightness", 0);
//                         // 读取存在SharedPreferences里的设置
//                       boolean Fullscreen = sPreferences.getBoolean("Fullscreen",true);
//                         // 获得根视图
//                        View view = getWindow().getDecorView();
//                         // 获得根布局
//                         ViewGroup vGroup = (ViewGroup) (view.findViewById(android.R.id.content));
//                        // 用于判断应用是否已经退出沉浸模式了。
//                         int status = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                   | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_VISIBLE
//                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//                         // 判断是否要开启沉浸模式
//                         if (Fullscreen) {
//                                  // 需要开启沉浸模式则把actionbar先隐藏掉，不然在Activity跳转时会闪出来。
//                                  // 进入沉浸模式
//                                 hideSystemUI(view);
//                // 在根布局获得第一个控件，也就是最上层的layout。把内边距设为0
//
//                             getWindow().setNavigationBarColor(Color.TRANSPARENT);
//                             getWindow().setStatusBarColor(Color.TRANSPARENT);
//                             } else if (view.getSystemUiVisibility() == status){
//                                 // 如果应用已经退出沉浸模式，但是这个activity还是在沉浸模式内，则退出沉浸模式。
//                                 showSystemUI(view);
//                                 // 获得系统栏高度和actionbar高度，设置内边距。
//                                 Rect frame = new Rect();
//                                 view.getWindowVisibleDisplayFrame(frame);
//                                 vGroup.getChildAt(0).setPadding(0,frame.top, 0, 0);
//                            }
//                     }

    }
    @SuppressLint("NewApi")
         public static void hideSystemUI(View view) {
                 view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                                 );
            }

    public static void showSystemUI(View view) {
                view.setSystemUiVisibility(
                                 View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                               | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }


    public int getSystemVersion() { //这是获取系统版本的方法
                int ver = android.os.Build.VERSION.SDK_INT;
                return ver;
             }

}
