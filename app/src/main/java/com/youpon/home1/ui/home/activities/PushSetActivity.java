package com.youpon.home1.ui.home.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.youpon.home1.R;
import com.youpon.home1.bean.NoticeInfo;
import com.youpon.home1.bean.User;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.http.HttpManage;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PushSetActivity extends BaseActivity{

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.liandong_info)
    CheckBox liandongInfo;
    @BindView(R.id.sensor_info)
    CheckBox sensorInfo;
    @BindView(R.id.device_info)
    CheckBox deviceInfo;
    private String setting;
    private List<NoticeInfo> noticeInfoList=new ArrayList<>();
    private List<NoticeInfo> sheziList=new ArrayList<>();
    private boolean liandongChek;
    private boolean sensorChek;
    private boolean deviceChek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_set);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheziList.add(new NoticeInfo(1,deviceChek));
                sheziList.add(new NoticeInfo(2,sensorChek));
                sheziList.add(new NoticeInfo(3,liandongChek));
                String s = new Gson().toJson(sheziList);
                Log.e("TTGGGG",s);
                HttpManage.getInstance().userSet("[{\"type\": 1,\"enable\": true}]", s, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("CCCC",result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("XXXX",ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        Log.e("XXXX","finish");
                    }
                });
                finish();
            }
        });
        try {
            User user = App.db.selector(User.class).where("id", "=", App.getApp().appid).findFirst();
            setting = user.getSetting();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(setting!=null){
            try {
                JSONArray jsonArray = new JSONArray(setting);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    int type = jsonObject.optInt("type");
                    boolean receive = jsonObject.optBoolean("receive");
                    NoticeInfo noticeInfo = new NoticeInfo(type, receive);
                    noticeInfoList.add(noticeInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < noticeInfoList.size(); i++) {
            NoticeInfo noticeInfo = noticeInfoList.get(i);
            switch (noticeInfo.getType()){
                case 1:
                    sensorInfo.setChecked(noticeInfo.isReceive());
                    sensorChek =noticeInfo.isReceive();
                    break;
                case 2:
                    liandongInfo.setChecked(noticeInfo.isReceive());
                    liandongChek =noticeInfo.isReceive();
                    break;
                case 3:
                    deviceInfo.setChecked(noticeInfo.isReceive());
                    deviceChek =noticeInfo.isReceive();
                    break;
            }
        }
        liandongInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    liandongChek =isChecked;
            }
        });
        sensorInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sensorChek=isChecked;
            }
        });
        deviceInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deviceChek=isChecked;
            }
        });

    }

}
