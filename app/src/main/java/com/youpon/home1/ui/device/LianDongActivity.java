package com.youpon.home1.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.gsonBeas.Liandong;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LianDongActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.nolink)
    LinearLayout nolink;
    private Sensor sensor;
    List<Liandong> list = new ArrayList<>();
    private CommonAdapter<Liandong> commonAdapter;
    private boolean editTag;
    private String TAG = getClass().getSimpleName();
    private int selectpos=-1;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvnet(EventData eventData) {
        if (eventData.code == EventData.CODE_REFRESHLINK) {
            loadData();
            commonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lian_dong);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        edit.setOnClickListener(this);
        sensor = (Sensor) getIntent().getSerializableExtra("sensor");
        loadData();
        commonAdapter = new CommonAdapter<Liandong>(this, list, R.layout.timetask_item) {
            @Override
            public void convert(final ViewHolder helper, final int position, final Liandong item) {
                helper.getView(R.id.status).setVisibility(View.GONE);
                ((TextView)helper.getView(R.id.time)).setTextSize(DeviceUtils.sp2px(15));
                if(position!= selectpos){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) helper.getView(R.id.content).getLayoutParams();
                    params.setMargins(0,0,0,0);
                    helper.getView(R.id.content).setLayoutParams(params);
                }else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) helper.getView(R.id.content).getLayoutParams();
                    params.setMargins(-helper.getView(R.id.dele).getWidth(),0,0,0);
                    helper.getView(R.id.content).setLayoutParams(params);
                }
                if (editTag) {
                    helper.getView(R.id.delete).setVisibility(View.VISIBLE);
                    helper.getView(R.id.on_off).setVisibility(View.GONE);
                    helper.getView(R.id.more).setVisibility(View.VISIBLE);
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LianDongActivity.this, LianDongSetActivity.class);
                            intent.putExtra("ctrl_id", item.getCtrl_id());
                            intent.putExtra("sensor", sensor);
                            startActivity(intent);
                        }
                    });
                }else {
                    helper.getView(R.id.delete).setVisibility(View.GONE);
                    helper.getView(R.id.on_off).setVisibility(View.VISIBLE);
                    helper.getView(R.id.more).setVisibility(View.GONE);
                    helper.getConvertView().setOnClickListener(null);
                }
                List<Liandong.EnvParasBean> env_paras = item.getEnv_paras();
                for (int i = 0; i < env_paras.size(); i++) {
                    Liandong.EnvParasBean envParasBean = env_paras.get(i);
                    if(envParasBean.getMac().equals(sensor.getMac())&&envParasBean.getSensor_type()==sensor.getType()){
                        helper.setText(R.id.time,Sensor.getStatusList(sensor.getType()).get(Sensor.getStatusLevel(sensor.getType(),envParasBean.getVal())));
                    }
                }
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        final AlertDialog alertDialog = new ProgressDialog.Builder(TimeTaskActivity.this).setMessage("正在删除中...").setCancelable(false).create();
//                        alertDialog.show();
                        selectpos=position;
                        notifyDataSetChanged();
                    }
                });
                helper.getView(R.id.dele).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Liandong.getMap().remove(item.getCtrl_id());
                        selectpos=-1;
                        String command = Command.getTimerOrLiandong(148, "[{\"ctrl_id\":" + item.getCtrl_id() + "}]");
                        Command.sendData1(sensor.getDevice_id(), command.getBytes(), TAG);
                        loadData();
                        notifyDataSetChanged();
                    }
                });
                helper.setText(R.id.week, "联动了1个设备");
                CheckBox on_off = helper.getView(R.id.on_off);
                on_off.setOnCheckedChangeListener(null);
                on_off.setChecked(item.getStatus() == 1 ? true : false);
                on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String timerOrLiandong;
                        if (isChecked) {
                            item.setStatus(1);
                            timerOrLiandong = Command.getTimerOrLiandong(149, "[{\"ctrl_id\":" + item.getCtrl_id() + ",\"status\":1" + "}]");
                        } else {
                            item.setStatus(2);
                            timerOrLiandong = Command.getTimerOrLiandong(149, "[{\"ctrl_id\":" + item.getCtrl_id() + ",\"status\":2" + "}]");
                        }
                        int count = 0;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getStatus() == 1) {
                                count++;
                            }
                        }
                        title.setText("联动（" + count + "/" + list.size() + "）");
                        Command.sendData1(sensor.getDevice_id(), timerOrLiandong.getBytes(), TAG);
                    }
                });
            }
        };
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setBackgroundColor(getResources().getColor(R.color.bgcolor));
//        linearLayout.setGravity(Gravity.CENTER);
//        linearLayout.setPadding(0,DeviceUtils.dip2px(20),0,DeviceUtils.dip2px(20));
//        ImageView v = new ImageView(this);
//        v.setImageResource(R.drawable.timingbtn_select);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(LianDongActivity.this, LianDongSetActivity.class);
//                intent1.putExtra("sensor", sensor);
//                startActivity(intent1);
//                editTag =false;
//                edit.setText("编辑");
//                selectpos=-1;
//                commonAdapter.notifyDataSetChanged();
//
//            }
//        });
//        linearLayout.addView(v);
//        lv.addFooterView(linearLayout);
        lv.setAdapter(commonAdapter);
    }

    private void loadData() {
        list.clear();
        List<Liandong> liandongs = new ArrayList<>();
        liandongs.addAll(Liandong.getMap().values());
        for (int i = 0; i < liandongs.size(); i++) {
            Liandong liandong = liandongs.get(i);
            MyLog.e(TAG, liandong.toString());
            List<Liandong.EnvParasBean> env_paras = liandong.getEnv_paras();
            for (int j = 0; j < env_paras.size(); j++) {
                Liandong.EnvParasBean envParasBean = env_paras.get(j);
                String mac = envParasBean.getMac();
                int sensor_type = envParasBean.getSensor_type();
//                   &&mac.equals(sensor.getMac())
                if (sensor.getMac().equals(mac)&&sensor.getType() == sensor_type) {
                    list.add(liandong);
                    break;
                }
            }
        }
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 1) {
                count++;
            }
        }
        title.setText("联动（" + count + "/" + list.size() + "）");
        if(list.size()>0){
            nolink.setVisibility(View.GONE);
        }else {
            nolink.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Intent intent1 = new Intent(this, LianDongSetActivity.class);
                intent1.putExtra("sensor", sensor);
                startActivity(intent1);
                editTag =false;
                edit.setText("编辑");
                selectpos=-1;
                commonAdapter.notifyDataSetChanged();
                break;
            case R.id.edit:
                if (!editTag) {
                    editTag = true;
                    edit.setText("完成");
                    commonAdapter.notifyDataSetChanged();
                } else {
                    editTag = false;
                    edit.setText("编辑");
                    selectpos=-1;
                    commonAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
