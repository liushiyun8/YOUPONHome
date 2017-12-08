package com.youpon.home1.ui.home.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.Constant;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.tools.DataParser;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.home.fragement.Device1Fragment;
import com.youpon.home1.ui.home.fragement.SceneFragment;
import com.youpon.home1.ui.home.fragement.SpaceFragement;
import com.youpon.home1.ui.home.fragement.ZhuyueFragment;
import com.youpon.home1.ui.home.service.MyService;
import com.youpon.home1.ui.home.service.MyTokenService;
import com.youpon.home1.ui.reciever.ScreenObserver;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkCode;
import io.xlink.wifi.sdk.bean.DataPoint;
import io.xlink.wifi.sdk.bean.EventNotify;
import io.xlink.wifi.sdk.listener.ConnectDeviceListener;
import io.xlink.wifi.sdk.listener.GetSubscribeKeyListener;
import io.xlink.wifi.sdk.listener.SubscribeDeviceListener;
import io.xlink.wifi.sdk.util.MyLog;

public class DeviceMainActivity extends BaseActivity {
    String TAG="deviceMainactivity";
    private final int _REFRESHTEXTVIEW = 1;
    private final int BEGINADD=5;
    @BindView(R.id.shouyue)
    RadioButton shouyue;
    @BindView(R.id.kongjian)
    RadioButton kongjian;
    @BindView(R.id.shebei)
    RadioButton shebei;
    @BindView(R.id.changjing)
    RadioButton changjing;
    @BindView(R.id.rg)
    public RadioGroup rg;
    @BindView(R.id.container)
    FrameLayout container;
    private FragmentManager sFm;
    private ZhuyueFragment zhuyueFragment;
    private RadioButton oldView;
    private SpUtils sp;
    boolean ListenFlag=false;
    private MyHandle myhandle = new MyHandle();
    private Intent service;
    private PowerManager.WakeLock mWakeLock;
    private ScreenObserver screenObserver;
    private ProgressDialog dialog;
    public List<DataPoint> dataPoints = new ArrayList<>();
    private boolean isRegisterBroadcast;
    private Intent service1;
    private long old;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvnet(EventData eventData){
        if(EventData.TAG_REFRESH.equals(eventData.getTag())){
            loadData();
        }else if(eventData.getCode()==EventData.CODE_RECONNECT){
//            Log.e(TAG,"已经传递到了："+(XDevice) eventData.getData());
            connectDevice(DeviceManage.getInstance().getDevice((XDevice) eventData.getData()));
        }
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if(now-old<2000){
            super.onBackPressed();
        }else {
            XlinkUtils.shortTips("再按一次退出");
            old=now;
        }

    }

    private void showConnectedDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = ProgressDialog.show(DeviceMainActivity.this, "连接设备", "正在连接设备...",
                true, true);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void hideBar(){
        rg.setVisibility(View.GONE);
    }

    public void showBar(){
        rg.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_main);
        ButterKnife.bind(this);
        screenObserver = new ScreenObserver(this);
        sp=App.getSp();
        service1 = new Intent(this,MyTokenService.class);
        startService(service1);
        initEvent();
        loadData();
        sFm = getSupportFragmentManager();
        zhuyueFragment = new ZhuyueFragment();
        sFm.beginTransaction().add(R.id.container,zhuyueFragment).commit();
        oldView=shouyue;
//        startAnim(shouyue);
        EventBus.getDefault().register(this);
        service = new Intent(DeviceMainActivity.this, MyService.class);
        service.putExtra("music",R.raw.hongwai_warn);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constant.BROADCAST_RECVPIPE);
        myIntentFilter.addAction(Constant.BROADCAST_DEVICE_CHANGED);
        myIntentFilter.addAction(Constant.BROADCAST_DEVICE_SYNC);
        myIntentFilter.addAction(Constant.BROADCAST_RECVPIPE_SYNC);
        myIntentFilter.addAction(Constant.BROADCAST_SEND_DATA);
        myIntentFilter.addAction(Constant.BROADCAST_DATAPOINT_RECV);
        myIntentFilter.addAction(Constant.BROADCAST_EVENT_NOTIFY);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        isRegisterBroadcast=true;
        screenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
            }

            @Override
            public void onScreenOff() {

            }

            @Override
            public void onUserPresent() {
//                loadData();
            }
        });
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(Constant.BROADCAST_EVENT_NOTIFY)) {
                EventNotify notify = (EventNotify) intent.getSerializableExtra(Constant.NOTIDATA);
                if (notify.messageType == 5) {
                    String msg = new String(notify.notifyData).trim();
                    String dat = msg.substring(msg.indexOf("{"));
                    try {
                        JSONObject jsonObject = new JSONObject(dat);
                        String type = jsonObject.optString("type");
                        int deviceid=jsonObject.optInt("device_id");
                        if("online".equals(type)){
                                Device device = DeviceManage.getInstance().getDevice(deviceid);
                                if(device!=null)
                                device.setOnline(true);
                                DeviceManage.getInstance().addDevice(device);
                        }else {
                            Device device = DeviceManage.getInstance().getDevice(deviceid);
                            if(device!=null){
                                device.setOnline(false);
                                DeviceManage.getInstance().addDevice(device);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loadData();
                    Log.e(TAG, "Notify:" + msg);
                }
            } else {
                String mac = intent.getStringExtra(Constant.DEVICE_MAC);
                if (mac == null) {
                    return;
                }
                Device device = DeviceManage.getInstance().getDevice(mac);
                if(device==null){
                    return;
                }
                XDevice xDevice = device.getXDevice();
                if (action.equals(Constant.BROADCAST_SEND_DATA)) {
                    byte[] data = intent.getByteArrayExtra(Constant.DATA);
                    Command.sendData(xDevice, data, TAG + "Broadcast");
                    return;
                }
                // 收到pipe包
                if (action.equals(Constant.BROADCAST_RECVPIPE)) {
                    byte[] data = intent.getByteArrayExtra(Constant.DATA);
                    Log("收到数据我的字符串：" + new String(data).trim());
                    try {
                        DataParser.getInstance().parse(data, xDevice, myhandle);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (action.equals(Constant.BROADCAST_RECVPIPE_SYNC)) {
                    byte[] data = intent.getByteArrayExtra(Constant.DATA);
                    Log("收到数据我的SYNC字符串：" + new String(data).trim());
                    try {
                        DataParser.getInstance().parse(data, xDevice, myhandle);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
//                pipeData = data;
                } else if (action.equals(Constant.BROADCAST_DEVICE_CHANGED)) {
                    int status = intent.getIntExtra(Constant.STATUS, -1);
                    if (status == XlinkCode.DEVICE_CHANGED_CONNECTING) {
                        Log("正在重连设备...");
                        // if (dialog != null && !dialog.isShowing()) {
                        // showConnectedDialog();
                        // }
                    } else if (status == XlinkCode.DEVICE_CHANGED_CONNECT_SUCCEED) {
                        Log("连接设备成功");
                        XlinkUtils.shortTips("连接设备成功");
                        dialog.dismiss();
                    } else if (status == XlinkCode.DEVICE_CHANGED_OFFLINE) {
                        dialog.dismiss();
                        XlinkUtils.shortTips("连接设备失败");
                        Log("连接设备失败");
                    }

                } // sync
                else if (action.equals(Constant.BROADCAST_DEVICE_SYNC)) {

                } else if (action.equals(Constant.BROADCAST_EXIT)) {
                    finish();
                } else if (action.equals(Constant.BROADCAST_DATAPOINT_RECV)) {
                    List<DataPoint> dps = (List<DataPoint>) intent.getSerializableExtra(Constant.DATA);
                    for (DataPoint dp : dps) {
                        if (dataPoints.contains(dp)) {
                            int index = dataPoints.indexOf(dp);
                            DataPoint dataPoint = dataPoints.get(dataPoints.indexOf(dp));
                            if (dataPoint != null) {
                                dataPoint.setValue(dp.getValue());
                                dataPoints.remove(index);
                                dataPoints.add(index, dataPoint);
                            }
                        }
                    }
                    intent = new Intent(Constant.BROADCAST_DEVICE_DATAPOINT_RECV);
                    intent.putExtra(Constant.DATA, (Serializable) dataPoints);
                    sendBroadcast(intent);
                }
            }
        }
    };

    public void connectDevice(final Device device) {

//        showConnectedDialog();

        //V3版本获取SUBKEY
        if (device.getXDevice().getVersion() >= 3 && device.getXDevice().getSubKey() <= 0) {
            Log("get subkey:" + device.getXDevice().getMacAddress() + " " + device.getXDevice().getSubKey());
            XlinkAgent.getInstance().getInstance().getDeviceSubscribeKey(device.getXDevice(), device.getXDevice().getAccessKey(), new GetSubscribeKeyListener() {
                @Override
                public void onGetSubscribekey(XDevice xdevice, int code, int subKey) {
                    device.getXDevice().setSubKey(subKey);
                    DeviceManage.getInstance().updateDevice(device);
                }
            });
        }

        //订阅设备,V3版本设备开始使用subKey订阅设备。
        if (!device.isSubscribe()) {
            XlinkAgent.getInstance().subscribeDevice(device.getXDevice(), device.getXDevice().getSubKey(), new SubscribeDeviceListener() {
                @Override
                public void onSubscribeDevice(XDevice xdevice, int code) {
                    Log.e("Subscribe",code+"");
                    if (code == XlinkCode.SUCCEED) {
                        device.setSubscribe(true);
                    }
                }
            });
        }

//        int ret = XlinkAgent.getInstance().connectDevice(device.getXDevice(), device.getXDevice().getAccessKey(), connectDeviceListener);
        int ret =XlinkAgent.getInstance().connectDevice(device.getXDevice(),connectDeviceListener);
        Log.e(TAG,"连接设备的状态："+ret);
        if (ret < 0) {// 调用设备失败
            if (dialog != null) {
                dialog.dismiss();
            }
            switch (ret) {
                case XlinkCode.INVALID_DEVICE_ID:
                    XlinkUtils.shortTips("无效的设备ID，请先联网激活设备");
                    break;
                case XlinkCode.NO_CONNECT_SERVER:
                    XlinkUtils.shortTips("连接设备失败，手机未连接服务器");
                    if (XlinkUtils.isConnected()) {
                        int appid =App.getApp().appid;
                        String authKey =App.getApp().authKey;
                        XlinkAgent.getInstance().start();
                        XlinkAgent.getInstance().login(appid, authKey);
                    }
                    break;
                case XlinkCode.NETWORD_UNAVAILABLE:
                    XlinkUtils.shortTips("当前网络不可用,无法连接设备");
                    break;
                case XlinkCode.NO_DEVICE:
                    XlinkUtils.shortTips("未找到设备");
                    XlinkAgent.getInstance().initDevice(device.getXDevice());
                    break;
                // 重复调用了连接设备接口
                case XlinkCode.ALREADY_EXIST:
                    XlinkUtils.shortTips("重复调用");
                    break;
                default:
                    XlinkUtils.shortTips("连接设备" + device.getName() + "失败:" + ret);
                    break;
            }

        }
    }

    private ConnectDeviceListener connectDeviceListener = new ConnectDeviceListener() {

        @Override
        public void onConnectDevice(XDevice xDevice, int result) {
//            dialog.dismiss();
            // TODO: handle exception
            byte[] bs=Command.getAll(Command.ALLDEVICE).getBytes();
            byte[] bs1=Command.getAll(Command.ALLSENSOR).getBytes();
//            byte[] bs2=Command.getRead485("FFFF").getBytes();
            byte[] bs3=Command.getOtherStr(Command.CUSDEVICE).getBytes();
            String tips;
            Log.e(TAG,"连接设备的listener:"+result);
            switch (result) {
                // 连接设备成功 设备处于内网
                case XlinkCode.DEVICE_STATE_LOCAL_LINK:
                    // 连接设备成功，成功后
//                    DeviceManage.getInstance().updateDevice(xDevice);
                    tips = "正在局域网控制设备(" + xDevice.getMacAddress() + ")";
                    XlinkUtils.shortTips(tips);
                    Log(tips);
                    Command.sendData(xDevice,bs,TAG);
                    Command.sendData(xDevice,bs1,TAG);
//                    Command.sendData(xDevice,bs2,TAG);
//                    Command.sendData(xDevice,bs3,TAG);
                    XlinkAgent.getInstance().sendProbe(xDevice);
                    break;
                // 连接设备成功 设备处于云端
                case XlinkCode.DEVICE_STATE_OUTER_LINK:
//                    DeviceManage.getInstance().updateDevice(xDevice);
                    tips = "正在通过云端控制设备(" + xDevice.getDeviceName() + ")";
                    Command.sendData(xDevice,bs,TAG);
                    Command.sendData(xDevice,bs1,TAG);

//                    Command.sendData(xDevice,bs3,TAG);
//                    Command.sendData(xDevice,bs4,TAG);
//                    XlinkUtils.shortTips(tips);
//                    DeviceManage.getInstance().addDevice(xDevice);
                    Log(tips);
                    break;
                // 设备授权码错误
                case XlinkCode.CONNECT_DEVICE_INVALID_KEY:
                    Log.e(TAG, "Device:" + xDevice.getDeviceName() + "设备认证失败");
                    XlinkUtils.shortTips("设备认证失败");
                    break;
                // 设备不在线
                case XlinkCode.CONNECT_DEVICE_OFFLINE:
                    // Log.e(TAG, "Device:" + xDevice.getMacAddress() + "设备不在线");
                    XlinkUtils.shortTips("设备不在线");
                    Log("设备不在线");
                    break;

                // 连接设备超时了，（设备未应答，或者服务器未应答）
                case XlinkCode.CONNECT_DEVICE_TIMEOUT:
                    // Log.e(TAG, "Device:" + xDevice.getMacAddress() + "连接设备超时");
                    XlinkUtils.shortTips("连接设备超时");
//                    connectDevice(DeviceManage.getInstance().getDevice(xDevice));
                    break;
                case XlinkCode.CONNECT_DEVICE_SERVER_ERROR:
                    XlinkUtils.shortTips("连接设备失败，服务器内部错误");
                    break;
                case XlinkCode.CONNECT_DEVICE_OFFLINE_NO_LOGIN:
                    XlinkUtils.shortTips("连接设备失败，设备未在局域网内，且当前手机只有局域网环境");
                    break;
                default:
                    XlinkUtils.shortTips("连接设备失败，其他错误码:" + result);
                    break;
            }

        }
    };

    private void initEvent() {
//        try {
////            App.db.replace(new Roombean("客厅"));
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.shouyue:
//                        startAnim(shouyue);
                        ZhuyueFragment zhuyueFragment=new ZhuyueFragment();
                        sFm.beginTransaction().replace(R.id.container,zhuyueFragment).commit();
//                        stoptAnim(oldView);
//                        oldView =shouyue;
                        break;
                    case R.id.kongjian:
//                        startAnim(kongjian);
                        SpaceFragement spaceFragement = new SpaceFragement();
                        sFm.beginTransaction().replace(R.id.container,spaceFragement).commit();
//                        stoptAnim(oldView);
//                        oldView =kongjian;
                        break;
                    case R.id.shebei:
//                        startAnim(shebei);
                        Device1Fragment deviceFragment = new Device1Fragment();
                        sFm.beginTransaction().replace(R.id.container,deviceFragment).commit();
//                        stoptAnim(oldView);
//                        oldView =shebei;
                        break;
                    case R.id.changjing:
//                        startAnim(changjing);
                        SceneFragment sceneFragment = new SceneFragment();
                        sFm.beginTransaction().replace(R.id.container,sceneFragment).commit();
//                        stoptAnim(oldView);
//                        oldView =changjing;
                        break;
                }
            }
        });
    }
    public void loadData() {
        HttpManage.getInstance().getSubscribeList(Comconst.CURRENTUSER, 1, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                DeviceManage.getInstance().clearCurrentdev();
                try {
                    JSONArray datas = new JSONArray(result);
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject jsonObject = datas.optJSONObject(i);
                        Log.e("JSONGateway", jsonObject.toString());
                        DeviceManage.getInstance().parseXDevice(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new EventData(EventData.CODE_GETDEVICE, ""));
                for (int j = 0; j < DeviceManage.getInstance().getCurrentdev().size(); j++) {
//                    Log.e("JSONCurrent", DeviceManage.getInstance().getCurrentdev().toString());
                    connectDevice(DeviceManage.getInstance().getCurrentdev().get(j));
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

            }
        });
    }

    private void Log(String msg) {
        Intent intent = new Intent(Constant.BROADCAST_LOG_ACTION);
        intent.putExtra(Constant.DATA, msg);
        sendBroadcast(intent);
        MyLog.e(TAG, msg);
    }


    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
//                case _REFRESHTEXTVIEW:
//                    String s = msg.obj.toString().trim();
//                    Log.e("Handle", s);
//                    break;
                case 2:
                    Toast.makeText(DeviceMainActivity.this,msg.obj!=null?msg.obj.toString():"",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
//    private void startAnim(View view) {
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        scaleAnimation.setFillAfter(true);
//        view.startAnimation(scaleAnimation);
//    }
//    private void stoptAnim(View view) {
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1, 1.5f, 1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        view.startAnimation(scaleAnimation);
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenObserver.stopScreenStateUpdate();
        if (isRegisterBroadcast) {
            unregisterReceiver(mBroadcastReceiver);
        }
        EventBus.getDefault().unregister(this);
        stopService(service1);
        XlinkAgent.getInstance().removeAllDevice();
        XlinkAgent.getInstance().stop();
    }
}
