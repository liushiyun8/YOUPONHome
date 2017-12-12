package com.youpon.home1.ui.device;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SoftVersionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.currentV)
    TextView currentV;
    @BindView(R.id.describe)
    TextView describe;
    @BindView(R.id.update)
    Button update;
    private int type;
    private int device_id;
    private ProgressDialog progressDialog;
    private String TAG=getClass().getSimpleName();
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_version);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(SoftVersionActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        type = getIntent().getIntExtra("type", 0);
        device_id = getIntent().getIntExtra("device_id", 0);
        device = DeviceManage.getInstance().getDevice(device_id);
        HttpManage.getInstance().getDeviceUpdateTask(device_id, new MyCallback() {
            @Override
            public void onSuc(String result) {
                MyLog.e("UpdateTask",result);
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
        if(type==1){
            title.setText("固件版本");
            HttpManage.getInstance().getDeviceUpdate(device_id, new MyCallback() {
                @Override
                public void onSuc(String result) {
                    MyLog.e(TAG,result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if(jsonObject!=null){
                            int newest = jsonObject.optInt("newest");
                            String description = jsonObject.optString("description");
                            describe.setText(description);
                            currentV.setText("V"+newest);
                            if(newest>device.getXDevice().getMcuSoftVersion()){
                                update.setEnabled(true);
                            }else update.setEnabled(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(int code, String msg) {
                    MyLog.e(TAG,msg);
                }
            });
        }else {
            update.setEnabled(false);
            describe.setText("已是最新");
            currentV.setText(device==null?"1":device.getXDevice().getMcuHardVersion()+"");
        }
        back.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.update:
                HttpManage.getInstance().getFirmware(device_id, new MyCallback() {
                    @Override
                    public void onSuc(String result) {
                        MyLog.e("TAG", result+"设备升级");
                        Toast.makeText(SoftVersionActivity.this, "设备开始升级..." + result, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        Toast.makeText(SoftVersionActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }
}
