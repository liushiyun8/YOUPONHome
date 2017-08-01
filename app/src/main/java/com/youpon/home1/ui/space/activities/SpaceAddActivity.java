package com.youpon.home1.ui.space.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.ui.adpter.MySpaceExAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpaceAddActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.exlv)
    ExpandableListView exlv;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.title)
    TextView title;
    List<List<Object>> lists=new ArrayList<>();
    private String name;
    private MySpaceExAdapter mySpaceExAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void init() {
        name = getIntent().getStringExtra("name");
        title.setText("添加至"+name);
        List<Gateway> gates=null;
        try {for (int i = 0; i < 3; i++) {
            List<Object> list=new ArrayList<>();
            if(i==0){
                List<Scenebean> all = App.db.selector(Scenebean.class).where("userName","=",(String) App.getSp().get(Comconst.NAME,"")).findAll();
                if(all!=null)
                    list.addAll(all);
            }else if(i==1){
                gates = App.db.selector(Gateway.class).where("user","=",Comconst.CURRENTUSER).findAll();
                if(gates!=null)
                    list.addAll(gates);
            }else if(i==2){
                if(gates!=null){
                    for (int j = 0; j < gates.size(); j++) {
                        List<SubDevice> all = App.db.selector(SubDevice.class).where("gatewang_id","=",gates.get(j).getDevice_id()).findAll();
                        if(all!=null)
                            list.addAll(all);
                    }
                    for (int z = 0; z < gates.size(); z++) {
                        List<Sensor> sensors = App.db.selector(Sensor.class).where("device_id", "=", gates.get(z).getDevice_id()).findAll();
                        if(sensors!=null)
                            list.addAll(sensors);
                    }
                }
            }
            lists.add(list);
        }} catch (DbException e) {
            e.printStackTrace();
        }
        String group[]={"情景","网关","设备"};
        mySpaceExAdapter = new MySpaceExAdapter(group, this, lists, save, name);
        exlv.setAdapter(mySpaceExAdapter);
        exlv.setGroupIndicator(null);
        for (int i = 0; i < group.length; i++) {
            exlv.expandGroup(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:
                for (int i = 0; i < 3; i++) {
                    try {
                        if(i==0||i==1)
                        App.db.replace(lists.get(i));
                        else if(i==2){
                            for (int j = 0; j < lists.get(2).size(); j++) {
                                App.db.replace(lists.get(2).get(j));
                            }
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                EventBus.getDefault().post(new EventData("spacedata",""));
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
