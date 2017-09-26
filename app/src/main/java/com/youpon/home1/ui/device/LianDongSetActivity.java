package com.youpon.home1.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.youpon.home1.R;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.gsonBeas.Liandong;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.PickerView;
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

public class LianDongSetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.pickerView)
    PickerView pickerView;
    @BindView(R.id.value)
    EditText value;
    @BindView(R.id.shebei_count)
    TextView shebeiCount;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.addshebei)
    LinearLayout addshebei;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.name)
    EditText name;
    private int ctrl_id;
    private Sensor sensor;
    private boolean addTag;
    private Liandong liandong;
    private String pos="";
    private List<SubDevice> list = new ArrayList<>();
    private CommonAdapter<SubDevice> myAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
        if (EventData.TRANSDATA == eventData.getCode()) {
            List<SubDevice> data = (List<SubDevice>) eventData.getData();
            list.clear();
            list.addAll(data);
            upDateUI();
        }
    }

    private void upDateUI() {
        shebeiCount.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        addshebei.setVisibility(View.GONE);
        myAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lian_dong_set);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        add.setOnClickListener(this);
        addshebei.setOnClickListener(this);
        ctrl_id = getIntent().getIntExtra("ctrl_id", -1);
        sensor = (Sensor) getIntent().getSerializableExtra("sensor");
        final List<String> statusList = Sensor.getStatusList(sensor.getType());
        myAdapter = new CommonAdapter<SubDevice>(this, list, R.layout.space_item) {

            @Override
            public void convert(ViewHolder helper, final int position, final SubDevice item) {
                final ImageView icon = helper.getView(R.id.icon);
                TextView name = helper.getView(R.id.name);
                CheckBox onOff = helper.getView(R.id.on_off);
                LinearLayout sensorLayout = helper.getView(R.id.sensor_layout);
                RadioButton didang = helper.getView(R.id.didang);
                RadioButton gaodang = helper.getView(R.id.gaodang);
                RadioGroup huanqu = helper.getView(R.id.huanqu);
                ImageView jian = helper.getView(R.id.jian);
                final SeekBar seek = helper.getView(R.id.seek);
                ImageView add = helper.getView(R.id.add);
                LinearLayout guangnuan = helper.getView(R.id.guangnuan);
                RadioButton liang = helper.getView(R.id.liang);
                RadioButton nuan = helper.getView(R.id.nuan);
                RadioButton re = helper.getView(R.id.re);
                RadioGroup fengdang = helper.getView(R.id.fengdang);
                RadioGroup fengDong = helper.getView(R.id.feng_dong);
                LinearLayout nuanqu = helper.getView(R.id.nuanqu);
                TextView status = helper.getView(R.id.status);
                onOff.setVisibility(View.VISIBLE);
                onOff.setOnCheckedChangeListener(null);
                sensorLayout.setVisibility(View.GONE);
                status.setVisibility(View.GONE);
                huanqu.setVisibility(View.GONE);
                nuanqu.setVisibility(View.GONE);
                guangnuan.setVisibility(View.GONE);
                final SubDevice subDevice = item;
                icon.setImageResource(Comconst.IMAGETYPE[subDevice.getTp()]);
                name.setText(subDevice.getName());
                onOff.setTag(position);
                if (subDevice.getTp() == 1) {
                    onOff.setChecked(subDevice.getValue2() == 0 ? false : true);
                } else {
                    onOff.setChecked(subDevice.getValue1() == 0 ? false : true);
                }
                onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if ((int)buttonView.getTag() != position) {
                            return;
                        }
                        if (subDevice.getTp() == 1) {
                            subDevice.setValue2(isChecked ? 1 : 0);
                        } else
                            subDevice.setValue1(isChecked ? 1 : 0);
                        notifyDataSetChanged();
                    }
                });
                switch (subDevice.getTp()) {
                    case 0:
                        if (subDevice.getValue1() != 0) {
                            nuanqu.setVisibility(View.VISIBLE);
                        }
                        fengdang.setOnCheckedChangeListener(null);
                        fengDong.setOnCheckedChangeListener(null);
                        fengDong.check(subDevice.getValue2() == 2 ? R.id.baifeng : R.id.jingzhi);
                        switch (subDevice.getValue1()) {
                            case 0:
                                fengdang.clearCheck();
                                fengDong.clearCheck();
                                break;
                            case 1:
                                liang.setChecked(true);
                                break;
                            case 2:
                                nuan.setChecked(true);
                                break;
                            case 3:
                                re.setChecked(true);
                                break;
                        }
                        fengDong.setTag(position);
                        fengdang.setTag(position);
                        fengDong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if ((int)group.getTag() != position) {
                                    return;
                                }
                                switch (checkedId) {
                                    case R.id.jingzhi:
                                        item.setValue2(1);
                                        break;
                                    case R.id.baifeng:
                                        item.setValue2(2);
                                        break;
                                }
                            }
                        });
                        fengdang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if ((int)group.getTag() != position) {
                                    return;
                                }
                                switch (checkedId) {
                                    case R.id.liang:
                                        item.setValue1(1);
                                        break;
                                    case R.id.nuan:
                                        item.setValue1(2);
                                        break;
                                    case R.id.re:
                                        item.setValue1(3);
                                        break;
                                }
                            }
                        });
                        break;
                    case 1:
                        if (subDevice.getValue2() == 0) {
                            guangnuan.setVisibility(View.GONE);
                        } else {
                            guangnuan.setVisibility(View.VISIBLE);
                        }
                        seek.setProgress(subDevice.getValue1());
                        jian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (seek.getProgress() >= 10)
                                    seek.setProgress(seek.getProgress() - 10);
                            }
                        });
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (seek.getProgress() <= 90)
                                    seek.setProgress(seek.getProgress() + 10);
                            }
                        });
                        seek.setTag(position);
                        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (!fromUser) {
                                    item.setValue1(progress);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                item.setValue1(seekBar.getProgress());
                            }
                        });
                        break;
                    case 2:
                        break;
                    case 3:
                        if (subDevice.getValue1() != 0) {
                            huanqu.setVisibility(View.VISIBLE);
                        }
                        gaodang.setVisibility(View.GONE);
                        huanqu.setTag(position);
                        huanqu.setOnCheckedChangeListener(null);
                        switch (subDevice.getValue1()) {
                            case 1:
                                didang.setChecked(true);
                                break;
                            case 2:
                                gaodang.setChecked(true);
                        }
                        huanqu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if ((int)group.getTag() != position) return;
                                switch (checkedId) {
                                    case R.id.didang:
                                        item.setValue1(1);
                                        break;
                                    case R.id.gaodang:
                                        item.setValue1(2);
                                        break;
                                }
                            }
                        });
                        break;
                }
            }

        };
        lv.setAdapter(myAdapter);
        pickerView.setData(statusList);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                pos =text;
            }
        });
        if (ctrl_id == -1) {
            pos=statusList.get(0);
            addTag = true;
            title.setText("添加联动");
            edit.setText("完成");
            shebeiCount.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            addshebei.setVisibility(View.VISIBLE);
            pickerView.setSelected(0);
        } else {
            liandong = Liandong.getMap().get(ctrl_id);
            if (liandong != null) {
                List<Liandong.EnvParasBean> env_paras = liandong.getEnv_paras();
                for (int i = 0; i < env_paras.size(); i++) {
                    Liandong.EnvParasBean envParasBean = env_paras.get(i);
                    long val = envParasBean.getVal();
                    int statusLevel = Sensor.getStatusLevel(envParasBean.getSensor_type(), val);
                    pos=statusList.get(statusLevel);
                    pickerView.setSelected(statusLevel);
                }
                String mac = liandong.getMac();
                name.setText(liandong.getCtrl_n());
                int obj_id = liandong.getObj_id();
                Log.e("MAC:", mac + " id:" + obj_id);
                try {
                    SubDevice subDevice = App.db.selector(SubDevice.class).where("mac", "like", mac + "%").and("dst", "=", obj_id).findFirst();
                    if (subDevice != null) {
                        subDevice.setValue1(liandong.getValue());
                        list.add(subDevice);
                        myAdapter.notifyDataSetChanged();
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                if (list.size() == 0) {
                    XlinkUtils.shortTips("请添加执行设备");
                    break;
                }
                SubDevice subDevice = list.get(0);
                Log.e("sensor",sensor.toString());
                Log.e("pos",pos);
                Log.e("val:",Sensor.getStatusValue(sensor.getType(), pos)+"");
                if (addTag) {
                    Liandong liandong = new Liandong();
                    liandong.setMac(subDevice.getMac());
                    if(subDevice.getType()==0){
                        liandong.setObj_id(subDevice.getDst()>=8?(subDevice.getDst()-8):(subDevice.getDst()-1));
                    }else {
                        liandong.setObj_id(subDevice.getDst());
                    }
                    liandong.setCtrl_type(subDevice.getType() == 0 ? 2 : 1);
                    liandong.setCtrl_n(name.getText().toString());
                    liandong.setEnv_cond("&&");
                    liandong.setStatus(1);
                    liandong.setValue(subDevice.getValue1());
                    Liandong.EnvParasBean envParasBean = new Liandong.EnvParasBean();
                    envParasBean.setCond(">=");
                    envParasBean.setMac(sensor.getMac());
                    envParasBean.setSensor_type(sensor.getType());
                    if(sensor.getType()==1){
                        envParasBean.setCond("==");
                    }
                    envParasBean.setVal(Sensor.getStatusValue(sensor.getType(), pos));
                    List<Liandong.EnvParasBean> li = new ArrayList<>();
                    li.add(envParasBean);
                    liandong.setEnv_paras(li);
                    List<Liandong> list = new ArrayList<>();
                    list.add(liandong);
                    String command = Command.getTimerOrLiandong(146, new Gson().toJson(list));
                    Command.sendData1(sensor.getDevice_id(), command.getBytes(), "LiandongSetActivity");
                } else {
                    liandong.setMac(subDevice.getMac());
                    liandong.setObj_id(subDevice.getDst());
                    liandong.setCtrl_type(subDevice.getType() == 0 ? 2 : 1);
                    liandong.setStatus(1);
                    liandong.setValue(subDevice.getValue1());
                    List<Liandong.EnvParasBean> li = new ArrayList<>();
                    List<Liandong.EnvParasBean> env_paras = liandong.getEnv_paras();
                    for (int j = 0; j < env_paras.size(); j++) {
                        Liandong.EnvParasBean envParasBean = env_paras.get(j);
                        String mac = envParasBean.getMac();
                        int sensor_type = envParasBean.getSensor_type();
                        if (sensor.getType() == sensor_type && mac.equals(sensor.getMac())) {
                            long statusValue = Sensor.getStatusValue(sensor.getType(), pos);
                            Log.e("value", statusValue + "");
                            envParasBean.setVal(statusValue);
                        }
                        li.add(envParasBean);
                    }
                    List<Liandong> list = new ArrayList<>();
                    list.add(liandong);
                    String command = Command.getTimerOrLiandong(147, new Gson().toJson(list));
                    Command.sendData1(sensor.getDevice_id(), command.getBytes(), "LiandongSetActivity");
                }
                finish();
                break;
            case R.id.add:
            case R.id.addshebei:
                Intent intent = new Intent(this, LianDongDevActivity.class);
                intent.putExtra("device_id", sensor.getDevice_id());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
