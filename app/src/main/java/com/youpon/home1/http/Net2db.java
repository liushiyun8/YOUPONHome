package com.youpon.home1.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;

import com.google.gson.Gson;
import com.youpon.home1.bean.MainBean;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.bean.SceneDevice;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.manage.PanelManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuyun on 2017/4/7.
 */
public class Net2db {
    private static Net2db instance;
    private static String[] tables={"subdevice","Roombean","Scenebean","Dbsensor","MainBean","panel"};
    private static Class[]  dbtabs={SubDevice.class, Roombean.class, Scenebean.class, Sensor.class, MainBean.class,Panel.class};
    int i;
    String queryString;
    private static String updateTime;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                i++;
                if(i==tables.length){
                    App.getSp().put(Comconst.CURRENTUSER+Comconst.LASTTIME,updateTime);
                    listener.complete();
                    i=0;
                }

            }
        }
    };
    private String lastTime;
    private Listener listener;

    public static Net2db getInstance() {
        if (instance == null) {
            instance = new Net2db();
        }
        return instance;
    }
    public void saveAll(Listener listener){
        this.listener =listener;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        updateTime=dateFormat.format(date);
        MyLog.e("YYYY",updateTime);
        lastTime = (String) App.getSp().get(Comconst.CURRENTUSER+Comconst.LASTTIME,"");
        for (int i = 0; i < tables.length; i++) {
            queryNet(i);
        }
    }

    private void queryNet(final int j) {
        if (lastTime != null && lastTime != "") {
            queryString="{\"limit\":\"10000\",\"query\":{\"updateAt\":{\"$gte\":" + lastTime + "}}}";
        }else queryString="{\"limit\":\"10000\"}";
        HttpManage.getInstance().querySub(tables[j],queryString , new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.e("WDEWDEWDE",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int count = jsonObject.optInt("count");
                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        Object o = new Gson().fromJson(jsonObject1.toString(), dbtabs[j]);
                        if(j==dbtabs.length-1){
                            PanelManage.getInstance().addPanel((Panel) o);
                        }
                        App.db.saveOrUpdate(o);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.e("FFFFFF","finish");
                Message msg = Message.obtain();
                msg.what=1;
                handler.sendMessage(msg);
            }
        });
    }

    public interface Listener{
        public void complete();
    }
}
