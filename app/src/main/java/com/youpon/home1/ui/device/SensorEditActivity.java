package com.youpon.home1.ui.device;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.comm.view.RoomDialog;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;

import org.greenrobot.eventbus.EventBus;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SensorEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.dele)
    ImageView dele;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.name_edit)
    LinearLayout nameEdit;
    @BindView(R.id.room)
    TextView room;
    @BindView(R.id.room_edit)
    LinearLayout roomEdit;
    @BindView(R.id.device_type)
    TextView deviceType;
    @BindView(R.id.shebei_id)
    TextView shebeiId;
    @BindView(R.id.hardware)
    TextView hardware;
    @BindView(R.id.hard_edit)
    ImageView hardEdit;
    @BindView(R.id.device_name)
    TextView deviceName;
    @BindView(R.id.device_id)
    TextView deviceId;
//    @BindView(R.id.save)
//    TextView save;
    private Sensor sensor;
    private Device device;
    private String TAG=getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_edit);
        ButterKnife.bind(this);
        initEvents();
        initDatas();
    }

    private void initEvents() {
        back.setOnClickListener(this);
        nameEdit.setOnClickListener(this);
        roomEdit.setOnClickListener(this);
        dele.setOnClickListener(this);
    }

    private void initDatas() {
        sensor = (Sensor) getIntent().getSerializableExtra("sensor");
        device = DeviceManage.getInstance().getDevice(sensor.getDevice_id());
        name.setText(sensor.getName());
        String type = "照明";
        switch (sensor.getType()) {
            case 1:
                type = "红外";
                break;
            case 2:
                type = "光感" ;
                break;
            case 3:
                type = "温度传感器";
                break;
            case 4:
                type= "湿度传感器";
                break;
            case 5:
                type = "二氧化碳传感器";
                break;
            case 6:
                type= "TVOC传感器";
                break;
            case 7:
                type = "可燃气";
                break;
            case 8:
                type = "烟感";
                break;
            default:
                type="其他";
                break;
        }
        deviceType.setText(type);
//        name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.equals("")) {
//                    Toast.makeText(DevEditActivity.this, "设备名称不能为空", Toast.LENGTH_SHORT).show();
//                } else {
//                    List<SubDevice> de = null;
//                    try {
//                        de = App.db.selector(SubDevice.class).where("name", "=", s.toString()).findAll();
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                    if (de != null && de.size() > 0 && !subDevice.getName().equals(s)) {
//                        Log.e("Dev", s.toString() + ":::" + de.toString());
//                        Toast.makeText(DevEditActivity.this, "设备名称不能重复，请重新命名！", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        room.setText(sensor.getRoom());
        shebeiId.setText(sensor.getId());
        String mac = device.getMac();
        StringBuffer sB= new StringBuffer();
        for (int i = 0; i < mac.length();i=i+2) {
            String sub = mac.substring(i, i + 2);
            sB.append(sub+"-");
        }
        deviceId.setText(sB.substring(0,sB.length()-1));
        deviceName.setText(device.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                String s = name.getText().toString();
                if (s.equals("")) {
                    Toast.makeText(SensorEditActivity.this, "设备名称不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                sensor.setName(name.getText().toString());
                sensor.setRoom(room.getText().toString());
                try {
                    App.db.replace(sensor);
                    Log.e(TAG, sensor.toString());
                    HttpManage.getInstance().upDateSub(HttpManage.SENSORTABLE, sensor.getObjectId(), new Gson().toJson(sensor), new MyCallback() {
                        @Override
                        public void onSuc(String result) {
                            MyToast.show(SensorEditActivity.this, MyToast.TYPE_OK, "更新设备成功", 1);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            MyToast.show(SensorEditActivity.this, MyToast.TYPE_ERROR, msg, 1);
                        }
                    });
                } catch (DbException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new EventData(EventData.REFRESHDB, "刷新设备名称"));
                EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_SENSOR, "刷新传感器"));
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
                        String s =myDialog.getEditText();
                        if (s.equals("")) {
                            Toast.makeText(SensorEditActivity.this, "设备名称不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sensor.setName(name.getText().toString());
                        try {
                            App.db.replace(sensor);
                            Log.e(TAG, sensor.toString());
                            HttpManage.getInstance().upDateSub(HttpManage.SENSORTABLE, sensor.getObjectId(), new Gson().toJson(sensor), new MyCallback() {
                                @Override
                                public void onSuc(String result) {
                                    MyToast.show(SensorEditActivity.this, MyToast.TYPE_OK, "更新设备成功", 1);
                                }

                                @Override
                                public void onFail(int code, String msg) {
                                    MyToast.show(SensorEditActivity.this, MyToast.TYPE_ERROR, msg, 1);
                                }
                            });
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new EventData(EventData.REFRESHDB, "刷新设备名称"));
                        EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_SENSOR, "刷新传感器"));
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
            case R.id.room_edit:
                new RoomDialog(this).show(new RoomDialog.Listener() {
                    @Override
                    public void yes(String s) {
                        room.setText(s);
                        sensor.setRoom(s);
                        try {
                            App.db.replace(sensor);
                            Log.e(TAG, sensor.toString());
                            HttpManage.getInstance().upDateSub(HttpManage.SENSORTABLE, sensor.getObjectId(), new Gson().toJson(sensor), new MyCallback() {
                                @Override
                                public void onSuc(String result) {
                                    MyToast.show(SensorEditActivity.this, MyToast.TYPE_OK, "更新设备成功", 1);
                                }

                                @Override
                                public void onFail(int code, String msg) {
                                    MyToast.show(SensorEditActivity.this, MyToast.TYPE_ERROR, msg, 1);
                                }
                            });
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new EventData(EventData.REFRESHDB, "刷新设备名称"));
                        EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_SENSOR, "刷新传感器"));
                    }
                });
                break;
            case R.id.dele:
                final MyDialog dialog = new MyDialog(this);
                dialog.setType(MyDialog.MESSAGETYPE);
                dialog.setMessage("确定要删除此设备吗？删除后此设备所在面板上的全部设备将删除");
                dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Command.sendData1(sensor.getDevice_id(),Command.dele(sensor.getMac(),sensor.getDevisort_id()).getBytes(),TAG);

                        try {
                            List<Sensor> all = App.db.selector(Sensor.class).where("mac", "=", sensor.getMac()).findAll();
                            for (int i = 0; i < all.size(); i++) {
                                Sensor sensor = all.get(i);
                                HttpManage.getInstance().deleSub(sensor.getObjectId(), HttpManage.SENSORTABLE, new MyCallback() {
                                    @Override
                                    public void onSuc(String result) {

                                    }

                                    @Override
                                    public void onFail(int code, String msg) {

                                    }
                                });
                            }
                            App.db.delete(Sensor.class, WhereBuilder.b("mac","=",sensor.getMac()));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        EventBus.getDefault().post(new EventData(EventData.CODE_GETDEVICE,""));
                        finish();
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
