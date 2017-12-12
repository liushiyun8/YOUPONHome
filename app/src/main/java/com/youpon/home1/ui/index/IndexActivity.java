package com.youpon.home1.ui.index;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.User;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.Constant;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.NetWorkTools;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.http.Net2db;
import com.youpon.home1.ui.home.activities.DeviceMainActivity;
import com.youpon.home1.ui.user.MainActivity;

import org.apache.http.Header;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.xlink.wifi.sdk.XlinkAgent;

public class IndexActivity extends BaseActivity {

    private SpUtils sp;
    private String loginname;
    private String pwd1;
    private boolean isRun;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        isRun=true;
        sp = App.getSp();
        timer = new Timer();
        if ((boolean)sp.get(Comconst.ISAUTO, false) == true) {
            if (NetWorkTools.getAPNType(this) == -1) {
                Toast.makeText(this, "无网络连接！", Toast.LENGTH_LONG).show();
                startActivity(new Intent(IndexActivity.this, Index2Activity.class));
            } else
                login();
        }else {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(IndexActivity.this, Index2Activity.class));
                    finish();
                }
            }, 2000);
        }

//        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1f,Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,0);
//        translateAnimation.setDuration(3000);
//        translateAnimation.setInterpolator(new OvershootInterpolator());
//        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                finish();
//                startActivity(new Intent(IndexActivity.this, MainActivity.class));
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        youpon.startAnimation(translateAnimation);
    }

    private void login() {
        loginname = (String) sp.get(Comconst.NAME, "");
        pwd1 = (String) sp.get(Comconst.PWD, "");
        HttpManage.getInstance().login(loginname, pwd1, new HttpManage.ResultCallback<Map<String, Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                //用户验证失败， 错误码提示见 </a>《错误码说明》 </a>文档
                XlinkUtils.longTips(error.getMsg());
                MyLog.e("HHHH", error.getMsg());
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(IndexActivity.this, Index2Activity.class));
                        finish();
                    }
                }, 2000);
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                MyLog.e("HHHH", response.toString());
                //验证成功， 解析返回的 JSON 获取"user_id"、"access_token"、"refresh_token"、"authorize"并保存
                String authKey = (String) response.get("authorize");
                String accessToken = (String) response.get("access_token");
                String refreshToken=(String) response.get("refresh_token");
                int appid = ((Double) response.get("user_id")).intValue();
                sp.put(Comconst.USERID, appid);
                sp.put("authKey", authKey);
                sp.put(Comconst.NAME, loginname);
                sp.put(Comconst.PWD, pwd1);
                sp.put(Comconst.ISAUTO, true);
                Comconst.CURRENTUSER = appid;
                App.getApp().setAccessToken(accessToken);
                App.getApp().setRefreshToken(refreshToken);
                App.getApp().setAppid(appid);
                App.getApp().setAuth(authKey);
                App.db=null;
                App.db = x.getDb(new DbManager.DaoConfig().setDbDir(new File("liuyun/" + appid)).setDbName("youpon"+appid).setDbVersion(1).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        try {
                            db.dropDb();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }));
                User user = new User(appid, loginname, pwd1);
                try {
                    App.db.replace(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                Net2db.getInstance().saveAll(new Net2db.Listener() {
                    @Override
                    public void complete() {
                        openDeviceMainActivity();
                    }
                });
            }
        });

    }

    private void openDeviceMainActivity() {

        if (isRun) {// 防止重复打开2个DeviceListActivity界面
            if (!XlinkAgent.getInstance().isConnectedLocal()) {
                XlinkAgent.getInstance().start();
            }
            if (!XlinkAgent.getInstance().isConnectedOuterNet()) {
                XlinkAgent.getInstance().login(App.getApp().getAppid(), App.getApp().getAuth());
            }
            isRun = false;
            // openActivity(MainActivity.class);
            Intent intent = new Intent(this, DeviceMainActivity.class);
            startActivity(intent);
            finish();
            // openActivity(DeviceListActivity.class);
        }
    }
}
