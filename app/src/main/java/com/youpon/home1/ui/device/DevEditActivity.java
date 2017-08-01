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
import com.youpon.home1.bean.Panel;
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
import com.youpon.home1.manage.PanelManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevEditActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.name_edit)
    LinearLayout nameEdit;
    @BindView(R.id.room)
    TextView room;
    @BindView(R.id.room_edit)
    LinearLayout roomEdit;
    @BindView(R.id.device_type)
    TextView devidetype;
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
    @BindView(R.id.dele)
    ImageView dele;
    private SubDevice subDevice;
    private Device device;
    private String TAG = getClass().getSimpleName();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EventData eventData) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_edit);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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
        subDevice = (SubDevice) getIntent().getSerializableExtra("device");
        device = DeviceManage.getInstance().getDevice(subDevice.getGateway_id());
        name.setText(subDevice.getName());
        String type = "照明";
        switch (subDevice.getTp()) {
            case 0:
                type = "风暖设备";
                break;
            case 1:
                type = "光暖设备";
                break;
            case 2:
                type = "普通照明";
                break;
            case 3:
                type = "换气设备";
                break;
            default:
                type = "其他设备";
        }
        devidetype.setText(type);
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
        room.setText(subDevice.getRoom());
        shebeiId.setText(subDevice.getId());
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
                        String s = name.getText().toString();
                        if (s.equals("")) {
                            Toast.makeText(DevEditActivity.this, "设备名称不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        subDevice.setName(name.getText().toString());
                        try {
                            App.db.replace(subDevice);
                            Log.e(TAG, subDevice.toString());
                            HttpManage.getInstance().upDateSub("subdevice", subDevice.getObjectId(), new Gson().toJson(subDevice), new MyCallback() {
                                @Override
                                public void onSuc(String result) {
                                    MyToast.show(DevEditActivity.this, MyToast.TYPE_OK, "更新设备成功", 1);
                                }

                                @Override
                                public void onFail(int code, String msg) {
                                    MyToast.show(DevEditActivity.this, MyToast.TYPE_ERROR, msg, 1);
                                }
                            });
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_DEVICE, "刷新设备名称"));
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
                        subDevice.setRoom(s);
                        try {
                            App.db.replace(subDevice);
                            Log.e(TAG, subDevice.toString());
                            HttpManage.getInstance().upDateSub("subdevice", subDevice.getObjectId(), new Gson().toJson(subDevice), new MyCallback() {
                                @Override
                                public void onSuc(String result) {
                                    MyToast.show(DevEditActivity.this, MyToast.TYPE_OK, "更新设备成功", 1);
                                }

                                @Override
                                public void onFail(int code, String msg) {
                                    MyToast.show(DevEditActivity.this, MyToast.TYPE_ERROR, msg, 1);
                                }
                            });
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_DEVICE, "刷新设备名称"));
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
                        Command.sendData1(subDevice.getGateway_id(),Command.dele(subDevice.getMac(),subDevice.getId()).getBytes(),TAG);

                        try {
                            List<SubDevice> all = App.db.selector(SubDevice.class).where("mac", "=", subDevice.getMac()).findAll();
                            for (int i = 0; i < all.size(); i++) {
                                SubDevice subDevice = all.get(i);
                                HttpManage.getInstance().deleSub(subDevice.getObjectId(), HttpManage.SUBTABLE, new MyCallback() {
                                    @Override
                                    public void onSuc(String result) {

                                    }

                                    @Override
                                    public void onFail(int code, String msg) {

                                    }
                                });
                            }
                            App.db.delete(SubDevice.class, WhereBuilder.b("mac","=",subDevice.getMac()));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        Panel panel = PanelManage.getInstance().getPanel(subDevice.getMac());
                        if(panel!=null){
                            HttpManage.getInstance().deleSub(panel.getObjectId(), HttpManage.PANELTABLE, new MyCallback() {
                                @Override
                                public void onSuc(String result) {

                                }

                                @Override
                                public void onFail(int code, String msg) {

                                }
                            });
                        }
                        PanelManage.getInstance().removePanel(subDevice.getMac());
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
