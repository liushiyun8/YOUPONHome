package com.youpon.home1.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.Constant;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkCode;

public class AddDeviceActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.swicth)
    CheckBox swicth;
    @BindView(R.id.seachLayout)
    LinearLayout seachLayout;
    @BindView(R.id.lv)
    ListView lv;

    private int device_id;
    List<SubDevice> deviList = new ArrayList<>();
    private CommonAdapter<SubDevice> adapter;
    private int myTime;
    private String TAG=getClass().getSimpleName();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
        if (eventData.getCode() == EventData.CODE_GETDEVICE) {
            updateData();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initEvent();
    }

    private void initEvent() {
        final Intent intent = getIntent();
        device_id = intent.getIntExtra("device_id", 0);
        updateData();
        adapter = new CommonAdapter<SubDevice>(this, deviList, R.layout.device_item) {
            @Override
            public void convert(ViewHolder helper, int position, SubDevice item) {
                TextView bind = helper.getView(R.id.bind);
                bind.setEnabled(false);
                bind.setText("已添加");
                helper.setImageResource(R.id.icon, Comconst.IMAGETYPE[item.getTp()]);
                helper.setText(R.id.mdnsname,item.getName());
                helper.setText(R.id.mdnsmac,"网络ID:"+item.getId());
            }
        };
        lv.setAdapter(adapter);
        back.setOnClickListener(this);
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
    }

    private void updateData() {
        deviList.clear();
        try {
            List<SubDevice> all = App.db.selector(SubDevice.class).where("gateway_id", "=",device_id).and("type","!=",0).findAll();
            deviList.addAll(all);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void startSerch() {
        myTime = 30;
        seachLayout.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        time.setText(myTime + "s");
        time.postDelayed(new Runnable() {
            @Override
            public void run() {
                myTime--;
                time.setText(myTime + "s");
                if (myTime <= 0) {
                    Command.sendData1(device_id,Command.getAll(Command.ALLDEVICE).getBytes(),TAG);
                    Command.sendData1(device_id,Command.getAll(Command.ALLSENSOR).getBytes(),TAG);
                    stopser();
                    return;
                }
                if(myTime==20){
                    Command.sendData1(device_id,Command.getAll(Command.ALLDEVICE).getBytes(),TAG);
                    Command.sendData1(device_id,Command.getAll(Command.ALLSENSOR).getBytes(),TAG);
                }
                time.postDelayed(this, 1000);
            }
        }, 1000);
        Command.sendData1(device_id,Command.getOtherStr(Command.ALLOWNET).getBytes(),TAG);
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

        }
    }
}
