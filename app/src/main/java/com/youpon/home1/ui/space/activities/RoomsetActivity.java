package com.youpon.home1.ui.space.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortListView;
import com.youpon.home1.R;
import com.youpon.home1.bean.Devall;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.RoomlvAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomsetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.lv)
    ListView lv;
    private List<Roombean> all=new ArrayList<>();
    private CommonAdapter<Roombean> roomlvAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){
        if(eventData.getTag()==EventData.REFRESHDB){
            initData();
            roomlvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomset);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        initData();
        roomlvAdapter = new CommonAdapter<Roombean>(this,all,R.layout.myroom_item) {
                @Override
                public void convert(ViewHolder helper, int position, Roombean item) {
                    helper.getView(R.id.panel_name).setVisibility(View.GONE);
                    helper.getView(R.id.diver).setVisibility(View.GONE);
                    final String name = item.getName();
                    helper.setText(R.id.name, name);
                    helper.setText(R.id.count,(getMyData(name).size())+"个");
                }
            };
        lv.setAdapter(roomlvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.e("tag","点击到我了");
                Intent intent = new Intent(RoomsetActivity.this, SpaceDetailActivity.class);
                intent.putExtra("room",all.get(position).getName());
                startActivity(intent);
            }
        });
//        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        back.setOnClickListener(this);
        add.setOnClickListener(this);
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
                        if(s.equals(gateway.getRoom())){
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

    private void initData() {
        all.clear();
        try {
           List<Roombean> all1 = App.db.selector(Roombean.class).findAll();
            MyLog.e("all",App.db.getDaoConfig()+all1.toString());
            if(all1!=null){
                all.addAll(all1);
            }
        } catch (DbException e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                startActivity(new Intent(this,AddRoomActivity.class));
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
