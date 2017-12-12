package com.youpon.home1.ui.space.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.bean.Devall;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.comm.view.RoomDialog;
import com.youpon.home1.comm.view.XCFlowLayout;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpaceDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tishi)
    TextView tishi;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.move)
    TextView move;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.delet)
    TextView delet;
    @BindView(R.id.shebeiLayout)
    LinearLayout shebeiLayout;
    @BindView(R.id.check_all)
    CheckBox checkAll;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.move_to)
    TextView moveTo;
    @BindView(R.id.cancle)
    TextView cancle;
    @BindView(R.id.checkLayout)
    LinearLayout checkLayout;
    private String room;
    private List<Devall> room1 = new ArrayList<>();
    private CommonAdapter<Devall> commonAdapter;
    private boolean AllCheckMode;
    private boolean moveMode;
    private List<Devall> selectList=new ArrayList<>();
    private RoomDialog dialog;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        DeviceUtils.setContext(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        move.setOnClickListener(this);
        delet.setOnClickListener(this);
        moveTo.setOnClickListener(this);
        cancle.setOnClickListener(this);
        dialog = new RoomDialog(this);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AllCheckMode =isChecked;
                if(AllCheckMode){
                    selectList.clear();
                    selectList.addAll(room1);
                }else {
                    selectList.clear();
                }
                commonAdapter.notifyDataSetChanged();
            }
        });
        room = getIntent().getStringExtra("room");
        name.setText(room);
        if ("客厅".equals(room)) {
            tishi.setVisibility(View.VISIBLE);
            delet.setVisibility(View.GONE);
        } else tishi.setVisibility(View.GONE);
        getData();
        commonAdapter = new CommonAdapter<Devall>(this, room1, R.layout.myroom_item) {
            @Override
            public void convert(ViewHolder helper, int position, final Devall item) {
                helper.getView(R.id.more).setVisibility(View.GONE);
                helper.getView(R.id.count).setVisibility(View.GONE);
                CheckBox box = helper.getView(R.id.check);
                if(moveMode){
                    box.setVisibility(View.VISIBLE);
                }else box.setVisibility(View.GONE);
                box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            if(!selectList.contains(item))
                            selectList.add(item);
                            MyLog.e("adpter_add",item.getSID());
                        }else {
                            if (selectList.contains(item))
                                selectList.remove(item);
                            MyLog.e("adpter_remove", item.getSID());
                        }
                        count.setText(selectList.size()+"");
                    }
                });
                if(selectList.contains(item)){
                    box.setChecked(true);
                }else box.setChecked(false);
                ImageView icon = helper.getView(R.id.icon);
                icon.setVisibility(View.VISIBLE);
                TextView name = helper.getView(R.id.name);
                switch (item.getSort()) {
                    case 1:
                        Device device = DeviceManage.getInstance().getDevice(item.getSID());
                        icon.setImageResource(R.mipmap.equ_ic_gateway);
                        helper.getView(R.id.panel_name).setVisibility(View.GONE);
                        name.setText(device.getName());
                        break;
                    case 3:
                        try {
                            SubDevice sub = App.db.selector(SubDevice.class).where("unique", "=", item.getSID()).findFirst();
                            if (sub != null) {
                                icon.setImageResource(Comconst.IMAGETYPE[sub.getTp()]);
                                name.setText(sub.getName());
                                final Panel panel = PanelManage.getInstance().getPanel(sub.getMac());
                                if(panel!=null){
                                    helper.setText(R.id.panel_name,panel.getMyName());
                                    helper.getView(R.id.panel_name).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final MyDialog myDialog = new MyDialog(SpaceDetailActivity.this);
                                            myDialog.setTitle("更改面板名称");
                                            myDialog.setType(MyDialog.EDITTYPE);
                                            myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                                                @Override
                                                public void onYesClick() {
                                                    panel.setName(myDialog.getEditText());
                                                    PanelManage.getInstance().updatePanel(panel);
                                                    myDialog.dismiss();
                                                    notifyDataSetChanged();
                                                }
                                            });
                                            myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                                                @Override
                                                public void onNoClick() {
                                                    myDialog.dismiss();
                                                }
                                            });
                                            myDialog.show();
                                        }
                                    });
                                }
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        try {
                            Sensor sensor = App.db.selector(Sensor.class).where("id", "=",item.getSID()).findFirst();
                            helper.getView(R.id.panel_name).setOnClickListener(null);
                            if (sensor != null) {
                                icon.setImageResource(Comconst.SENSORTYPE[sensor.getType() - 1]);
                                name.setText(sensor.getName());
                                final Device device1 =DeviceManage.getInstance().getDevice(sensor.getDevice_id());
                                if(device1!=null){
                                    helper.setText(R.id.panel_name,device1.getName());
                                }
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
        lv.setAdapter(commonAdapter);
    }

    public List<Devall> getMyData(String s){
        List<Devall> list=new ArrayList<>();
        try {
            List<Device> gates = DeviceManage.getInstance().getCurrentdev();
            if(gates!=null){
                if("所有空间".equals(s)){
                    list.addAll(gates);
                }else
                    for (int i = 0; i < gates.size(); i++) {
                        Device gateway = gates.get(i);
                        if(gateway.getRoom().equals(s)){
                            list.add(gateway);
                        }
                    }
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<SubDevice> ds=null;
                    if("所有空间".equals(s)){
                        ds= App.db.selector(SubDevice.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).and("type","!=",0).findAll();
                    }else
                        ds= App.db.selector(SubDevice.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).and("type","!=",0).and("room","=",s).findAll();
                    if(ds!=null){
                        list.addAll(ds);
                    }
                }
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<Sensor> ds=null;
                    if("所有空间".equals(s)) {
                        ds= App.db.selector(Sensor.class).where("device_id", "=", gateway.getXDevice().getDeviceId()).findAll();
                    }else
                        ds=App.db.selector(Sensor.class).where("device_id", "=", gateway.getXDevice().getDeviceId()).and("room", "=", s).findAll();
                    if(ds!=null){
                        list.addAll(ds);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void getData() {
        room1.clear();
        room1.addAll(getMyData(room));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.move:
                shebeiLayout.setVisibility(View.GONE);
                checkLayout.setVisibility(View.VISIBLE);
                moveMode =true;
                commonAdapter.notifyDataSetChanged();
                break;
            case R.id.delet:
                final MyDialog myDialog = new MyDialog(this);
                myDialog.setMessage("删除空间后，该空间的设备全部移入默认空间（客厅）");
                myDialog.setType(MyDialog.MESSAGETYPE);
                myDialog.setYesOnclickListener("删除", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        try {
                            final Roombean roombean = App.db.selector(Roombean.class).where("name", "=", room).findFirst();
                            if(roombean!=null){
                                HttpManage.getInstance().deleSub(roombean.getObjectId(), "Roombean", new MyCallback() {
                                    @Override
                                    public void onSuc(String result) {
                                        MyLog.e("delet",result);
                                        try {
                                            App.db.delete(roombean);
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                        MyToast.show(SpaceDetailActivity.this,MyToast.TYPE_OK,"删除成功",1);
                                        EventBus.getDefault().post(new EventData(EventData.REFRESHDB,""));
                                        myDialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onFail(int code, String msg) {
                                        MyToast.show(SpaceDetailActivity.this,MyToast.TYPE_ERROR,msg,1);
                                    }
                                });
                            }
                            for (int i = 0; i < room1.size(); i++) {
                                Devall spaceBean = room1.get(i);
                                spaceBean.setRoom("客厅");
                                switch (spaceBean.getSort()){
                                    case 1:
                                        Device device = DeviceManage.getInstance().getDevice(spaceBean.getSID());
                                        DeviceManage.getInstance().updateDevice(device);
                                        break;
                                    case 3:
                                        App.db.update(SubDevice.class, WhereBuilder.b("unique","=",spaceBean.getSID()),new KeyValue("room","客厅"));
                                        break;
                                    case 4:
                                        App.db.update(Sensor.class, WhereBuilder.b("id","=",spaceBean.getSID()),new KeyValue("room","客厅"));
                                        break;
                                }
                            }
//                            App.db.replace(room1);
                        } catch (DbException e) {
                            e.printStackTrace();
                            MyToast.show(SpaceDetailActivity.this,MyToast.TYPE_ERROR,"设备移动空间失败",1);
                        }
                    }
                });
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
            case R.id.move_to:
                dialog.show(new RoomDialog.Listener() {
                    @Override
                    public void yes(String s) {
                        if(s==null){
                            Toast.makeText(SpaceDetailActivity.this,"请选择要移动的空间",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            for (int i = 0; i < selectList.size(); i++) {
                                Devall spaceBean = selectList.get(i);
                                spaceBean.setRoom(s);
                                switch (spaceBean.getSort()){
                                    case 1:
                                        Device device = DeviceManage.getInstance().getDevice(spaceBean.getSID());
                                        device.setRoom(s);
                                        DeviceManage.getInstance().updateDevice(device);
                                        break;
                                    case 3:
                                        App.db.update(SubDevice.class, WhereBuilder.b("unique","=",spaceBean.getSID()),new KeyValue("room",s));
                                        final SubDevice sub = App.db.selector(SubDevice.class).where("unique", "=", spaceBean.getSID()).findFirst();
                                        if(sub!=null){
                                            sub.setRoom(s);
                                            HttpManage.getInstance().upDateSub(HttpManage.SUBTABLE, sub.getObjectId(), new Gson().toJson(sub), new MyCallback() {
                                                @Override
                                                public void onSuc(String result) {
                                                    try {
                                                        App.db.saveOrUpdate(sub);
                                                    } catch (DbException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFail(int code, String msg) {

                                                }
                                            });
                                        }

                                        break;
                                    case 4:
                                        App.db.update(Sensor.class, WhereBuilder.b("id","=",spaceBean.getSID()),new KeyValue("room",s));
                                        final Sensor sen= App.db.selector(Sensor.class).where("id","=",spaceBean.getSID()).findFirst();
                                        if(sen!=null){
                                            sen.setRoom(s);
                                            HttpManage.getInstance().upDateSub(HttpManage.SUBTABLE, sen.getObjectId(), new Gson().toJson(sen), new MyCallback() {
                                                @Override
                                                public void onSuc(String result) {
                                                    try {
                                                        App.db.saveOrUpdate(sen);
                                                    } catch (DbException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFail(int code, String msg) {

                                                }
                                            });
                                        };
                                }
                            }
                            getData();
                            commonAdapter.notifyDataSetChanged();
                            if(selectList.size()>0){
                                MyToast.show(SpaceDetailActivity.this,MyToast.TYPE_OK,"设备移动成功",1);
                            }
                            else MyToast.show(SpaceDetailActivity.this,MyToast.TYPE_OK,"没有设备被移动",1);
                            selectList.clear();
                            EventBus.getDefault().post(new EventData(EventData.REFRESHDB,""));
                        } catch (DbException e) {
                            e.printStackTrace();
                            MyToast.show(SpaceDetailActivity.this,MyToast.TYPE_ERROR,"设备移动失败",1);
                        }
                    }
                });
                break;
            case R.id.cancle:
                shebeiLayout.setVisibility(View.VISIBLE);
                checkLayout.setVisibility(View.GONE);
                moveMode =false;
                commonAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
