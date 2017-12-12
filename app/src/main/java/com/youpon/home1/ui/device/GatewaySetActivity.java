package com.youpon.home1.ui.device;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.comm.view.RoomDialog;
import com.youpon.home1.comm.view.XCFlowLayout;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GatewaySetActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.name_edit)
    LinearLayout nameEdit;
    @BindView(R.id.room)
    TextView room;
    @BindView(R.id.room_edit)
    LinearLayout roomEdit;
    @BindView(R.id.shebei_id)
    TextView shebeiId;
    @BindView(R.id.software)
    TextView software;
    @BindView(R.id.soft_edit)
    LinearLayout softEdit;
    @BindView(R.id.hardware)
    TextView hardware;
    @BindView(R.id.hard_edit)
    LinearLayout hardEdit;
    @BindView(R.id.sub_device)
    TextView subDevice;
    @BindView(R.id.binb_more)
    LinearLayout binbMore;
    @BindView(R.id.save)
    TextView save;
    private int device_id;
    private Device gateway;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_set);
        ButterKnife.bind(this);
        DeviceUtils.setContext(this);
        initEvents();
        initDatas();
    }

    private void initEvents() {
        back.setOnClickListener(this);
        nameEdit.setOnClickListener(this);
        roomEdit.setOnClickListener(this);
        softEdit.setOnClickListener(this);
        hardEdit.setOnClickListener(this);
        hardware.setOnClickListener(this);
        binbMore.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void initDatas() {
        Intent intent = getIntent();
        device_id = intent.getIntExtra("device_id", 0);
        gateway = DeviceManage.getInstance().getDevice(device_id);
        if (gateway == null) {
            return;
        }
        name.setText(gateway.getName());
        room.setText(gateway.getRoom());
        hardware.setText("V"+gateway.getXDevice().getMcuSoftVersion());
        software.setText("V"+gateway.getXDevice().getMcuHardVersion());
        HttpManage.getInstance().getDeviceUpdate(device_id, new MyCallback() {
            @Override
            public void onSuc(String result) {
                MyLog.e(TAG,result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject!=null){
                        int newest = jsonObject.optInt("newest");
                        if(newest>gateway.getXDevice().getMcuSoftVersion()){
                            hardware.setEnabled(true);
                            hardware.setText("可升级");
                        }
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
        String mac =gateway.getMac();
        StringBuffer sB= new StringBuffer();
        for (int i = 0; i < mac.length();i=i+2) {
            String sub = mac.substring(i, i + 2);
            sB.append(sub+"-");
        }
        shebeiId.setText(sB.substring(0,sB.length()-1));
        try {
            List<SubDevice> subs = App.db.selector(SubDevice.class).where("gateway_id", "=", device_id).and("type","!=",0).findAll();
            if(subs!=null){
                subDevice.setText(subs.size()+"个");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.name_edit:
                final MyDialog myDialog = new MyDialog(this);
                myDialog.setType(MyDialog.EDITTYPE);
                myDialog.setTitle("重命名");
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });
                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        name.setText(myDialog.getEditText());
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name",myDialog.getEditText());
                        HttpManage.getInstance().updateDevice(device_id, map, new MyCallback() {
                            @Override
                            public void onSuc(String result) {
                                MyLog.e(TAG, result);
                                gateway.setName(myDialog.getEditText());
                                DeviceManage.getInstance().updateDevice(gateway);
                                EventBus.getDefault().post(new EventData(EventData.CODE_GETDEVICE, ""));
                            }

                            @Override
                            public void onFail(int code, String msg) {

                            }
                        });
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
            case R.id.save:
                final MyDialog dialog1 = new MyDialog(this);
                dialog1.setMessage("删除网关后会解除该网关与其子设备与账号的绑定关系，确定要删除吗？");
                dialog1.setType(MyDialog.MESSAGETYPE);
                dialog1.setYesOnclickListener("删除", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        HttpManage.getInstance().unsubscribe(device_id, new HttpManage.ResultCallback<Map<String,Object>>() {
                            @Override
                            public void onError(Header[] headers, HttpManage.Error error) {
                                MyLog.e("fail",error.getCode()+"  info:"+error.getMsg());
                                dialog1.dismiss();
                                MyToast.show(GatewaySetActivity.this,MyToast.TYPE_ERROR,error.getMsg(),1);
                            }

                            @Override
                            public void onSuccess(int code, Map<String, Object> response) {
                                deleData();
                                EventBus.getDefault().post(new EventData(EventData.CODE_GETDEVICE,""));
                                EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_DEVICE,""));
                                dialog1.dismiss();
                                finish();
                            }
                        });
                    }
                });
                dialog1.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
            case R.id.soft_edit:
                Intent intent = new Intent(this, SoftVersionActivity.class);
                intent.putExtra("type",0);
                intent.putExtra("device_id",device_id);
                startActivity(intent);
                break;
            case R.id.hard_edit:
            case R.id.hardware:
                Intent intent1 = new Intent(this, SoftVersionActivity.class);
                intent1.putExtra("type",1);
                intent1.putExtra("device_id",device_id);
                startActivity(intent1);
                break;
            case R.id.binb_more:
                Intent intent2 = new Intent(this, SubDevicelistActivity.class);
                intent2.putExtra("device_id",device_id);
                startActivity(intent2);
                break;
            case R.id.room_edit:
                new RoomDialog(this).show(new RoomDialog.Listener() {
                    @Override
                    public void yes(String s) {
                        Device device = DeviceManage.getInstance().getDevice(device_id);
                        device.setRoom(s);
                        DeviceManage.getInstance().updateDevice(device);
                        try {
                            App.db.update(SpaceBean.class, WhereBuilder.b("sid","=",device.getSID()),new KeyValue("room",s));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        room.setText(s);
                        EventBus.getDefault().post(new EventData(EventData.CODE_GETDEVICE,""));
                        MyToast.show(GatewaySetActivity.this,MyToast.TYPE_OK,"空间移动成功",1);
                    }
                });
                break;
//            case R.id.getuser:
//                Intent intent1 = new Intent(this, UserListActivity.class);
//                intent1.putExtra("device_id", device_id);
//                startActivity(intent1);
//                break;
        }


    }

    private void deleData() {
        DeviceManage.getInstance().removeDevice(gateway.getMac());
        DeviceManage.getInstance().removeCurrentdev(gateway);
        try {
            List<SubDevice> subs = App.db.selector(SubDevice.class).where("gateway_id","=",device_id).findAll();
            List<Sensor> sensors=App.db.selector(Sensor.class).where("device_id","=",device_id).findAll();
            List<Panel> panels=App.db.selector(Panel.class).where("gateway_id","=",device_id).findAll();
            List<Scenebean> scenebeen=App.db.selector(Scenebean.class).where("gateway_id","=",device_id).findAll();
            if(subs!=null){
                App.db.delete(subs);
                for (int i = 0; i < subs.size(); i++) {
                    String objectId = subs.get(i).getObjectId();
                    HttpManage.getInstance().deleSub(objectId,HttpManage.SUBTABLE, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {

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

            }
            if(sensors!=null){
                App.db.delete(sensors);
                for (int i = 0; i < sensors.size(); i++) {
                    String objectId =sensors.get(i).getObjectId();
                    HttpManage.getInstance().deleSub(objectId,HttpManage.SENSORTABLE, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {

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
            }
            if(panels!=null){
                for (int i = 0; i < panels.size(); i++) {
                    PanelManage.getInstance().removePanel(panels.get(i).getMac());
                    String objectId =panels.get(i).getObjectId();
                    HttpManage.getInstance().deleSub(objectId,"panel", new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {

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
            }
            if(scenebeen!=null){
                App.db.delete(scenebeen);
                for (int i = 0; i < scenebeen.size(); i++) {
                    Scenebean entity = scenebeen.get(i);
                    HttpManage.getInstance().deleSub(entity.getObjectId(), HttpManage.SCENETABLE, new MyCallback() {
                        @Override
                        public void onSuc(String result) {

                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
