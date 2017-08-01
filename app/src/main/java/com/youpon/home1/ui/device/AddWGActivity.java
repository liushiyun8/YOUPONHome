package com.youpon.home1.ui.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.gsonBeas.Devicebean;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Constant;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.Deviceadpter;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkCode;
import io.xlink.wifi.sdk.listener.ConnectDeviceListener;
import io.xlink.wifi.sdk.listener.GetSubscribeKeyListener;
import io.xlink.wifi.sdk.listener.ScanDeviceListener;
import io.xlink.wifi.sdk.listener.SetDeviceAccessKeyListener;
import io.xlink.wifi.sdk.listener.SubscribeDeviceListener;

public class AddWGActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.swicth)
    CheckBox swicth;
    @BindView(R.id.seachLayout)
    LinearLayout seachLayout;
    //    @BindView(R.id.stopserch)
//    Button stopserch;
//    @BindView(R.id.startserch)
//    Button startserch;
//    @BindView(R.id.saoyisao)
//    ImageView saoyisao;
//    @BindView(R.id.peiwang)
//    Button peiwang;
    private List<XDevice> dlist = new ArrayList<>();
    private Deviceadpter adapter;

    private static final int REQUEST_CODE = 200;
    private int myTime=30;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvnet(EventData eventData){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wg);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        adapter = new Deviceadpter(this, dlist);
        lv.setAdapter(adapter);
        swicth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startSerch();
                }else {
                    stopser();
                }
            }
        });
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Devicebean devicebean = dlist.get(position);
//                Intent intent = new Intent(AddWGActivity.this, DeviceInfoActivity.class);
//                intent.putExtra("info", devicebean.toString());
//                startActivity(intent);
//            }
//        });
    }

    private void test(XDevice xDevice) {
        int ret = XlinkAgent.getInstance().connectDevice(xDevice, "8888",
                connectDeviceListener);
        XlinkUtils.shortTips("ret:" + ret);
    }

    private ConnectDeviceListener connectDeviceListener = new ConnectDeviceListener() {

        @Override
        public void onConnectDevice(XDevice xDevice, int result) {
            // TODO Auto-generated method stub

            XlinkUtils.shortTips("onConnectDevice result :" + result
                    + "xDevice:" + xDevice);

        }
    };

    private ScanDeviceListener scanListener = new ScanDeviceListener() {

        @Override
        public void onGotDeviceByScan(XDevice device) {
            dlist.add(device);
            adapter.notifyDataSetChanged();
            XlinkUtils.shortTips("扫描到设备:" + device.getMacAddress());
            // if (!device.isInit()) {
//             int ret = XlinkAgent.getInstance().setDeviceAuthorizeCode(
            // device, "0000", Constant.passwrod,
            // new SetDeviceAuthorizeListener() {
            // @Override
            // public void onSetLocalDeviceAuthorizeCode(
            // XDevice device, int code,int msgId) {
            // Log("设置设备" + device.getMacAddress() + "默认密码:"
            // + code);
            // }
            // });
            // if (ret != 0) {
            // XlinkUtils.shortTips("设置" + device.getMacAddress()
            // + "密码失败,同步错误码：" + ret);
            // }
            // }
        }
    };


    private void startSerch() {
        myTime=30;
        seachLayout.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        time.setText(myTime+"s");
        time.postDelayed(new Runnable() {
            @Override
            public void run() {
                myTime--;
                time.setText(myTime+"s");
                if(myTime<=0){
                    stopser();
                    return;
                }
                time.postDelayed(this,1000);
            }
        },1000);
        dlist.clear();
        int ret = XlinkAgent.getInstance().scanDeviceByProductId(
                Constant.PRODUCTID, scanListener);
        if (ret < 0) {
            switch (ret) {
                case XlinkCode.NO_CONNECT_SERVER:
                    XlinkUtils.shortTips("未开启局域网服务");
                    if (XlinkUtils.isWifi()) {
                        XlinkAgent.getInstance().start();
                    }
                    break;
                case XlinkCode.NETWORD_UNAVAILABLE:
                    XlinkUtils.shortTips("手机无网络/wifi环境");
                    break;
                default:
                    XlinkUtils.shortTips("调用扫描失败:" + ret);
                    break;
            }
            return;
        }
//        String serviceName = "_easylink._tcp.local.";
//        miCODevice.startSearchDevices(serviceName, new SearchDeviceCallBack() {
//            @Override
//            public void onSuccess(int code, String message) {
//                Log.d("yyyy", message);
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                Log.d("YYYY", message);
//            }
//
//            @Override
//            public void onDevicesFind(int code, JSONArray deviceStatus) {
//                Log.e("YYYYY", deviceStatus.toString());
//                try {
//                    JSONArray jsonArray = new JSONArray(deviceStatus.toString());
//                    if (jsonArray.length() > 0) {
//                        dlist.clear();
//                    }
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.optJSONObject(i);
//                        String name = jsonObject.optString("Name");
//                        String ip = jsonObject.optString("IP");
//                        int port = jsonObject.optInt("Port");
//                        String mac = jsonObject.optString("MAC");
//                        String model = jsonObject.optString("Model");
//                        String fogProductId = jsonObject.optString("FogProductId");
//                        String firmware = jsonObject.optString("Firmware Rev");
//                        String mico = jsonObject.optString("MICO OS Rev");
//                        String isHaveSuperUser = jsonObject.optString("IsHaveSuperUser");
//                        String protocol = jsonObject.optString("Protocol");
//                        String isEasylinkOK = jsonObject.optString("IsEasylinkOK");
//                        String remainingUserNumber = jsonObject.optString("RemainingUserNumber");
//                        String hardware = jsonObject.optString("Hardware Rev");
//                        String manufacturer = jsonObject.optString("Manufacturer");
//                        String seed = jsonObject.optString("Seed");
//                        Devicebean devicebean = new Devicebean(name, ip, port, mac, firmware, fogProductId, isEasylinkOK, isHaveSuperUser, remainingUserNumber, hardware, mico, model, protocol, manufacturer, seed);
//                        dlist.add(devicebean);
//                    }
////                    Log.e("LLLLLLLLLLLLLLL", dlist.toString());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.notifyDataSetChanged();
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        back.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                stopser();
//            }
//        }, 10000);
    }

    private void stopser() {
        myTime=0;
        time.setVisibility(View.GONE);
        swicth.setChecked(false);
        seachLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
//            case R.id.stopserch:
//                stopser();
//                break;
//            case R.id.peiwang:
//                alertDialog.show();
//                break;
//            case R.id.saoyisao:
//                callCapture("UTF-8");
//                break;
        }
    }

//    private void stopser() {
//        miCODevice.stopSearchDevices(new SearchDeviceCallBack() {
//            @Override
//            public void onSuccess(int code, String message) {
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//            }
//        });
//    }

    private void callCapture(String characterSet) {

        Intent intent = new Intent();
        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.CHARACTER_SET, characterSet);
        intent.putExtra(Intents.Scan.WIDTH, 800);
        intent.putExtra(Intents.Scan.HEIGHT, 800);
        intent.setClass(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (null != data && requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    String json = data.getStringExtra(Intents.Scan.RESULT);
                    bindDevice(json);
//                    bindGateway(json);
                    break;
                default:
                    break;
            }
        }
    }

    private void bindDevice(String json) {
        String s = new String(Base64.decode(json, Base64.DEFAULT));
        Log.e("HHHHH", App.getApp().getAccessToken() + "");
        HttpManage.getInstance().acceptShare(s, new HttpManage.ResultCallback() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                Log.e("UUUU", error.getMsg());
            }

            @Override
            public void onSuccess(int code, Object response) {
                Log.e("UUUU", response.toString());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopser();
    }


}
