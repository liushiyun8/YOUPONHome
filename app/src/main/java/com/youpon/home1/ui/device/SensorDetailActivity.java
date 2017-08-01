package com.youpon.home1.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.view.DeviceImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SensorDetailActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.set)
    ImageView set;
    @BindView(R.id.shebei_icon)
    DeviceImageView shebeiIcon;
    @BindView(R.id.sensor_hongwai)
    ImageView sensorHongwai;
    @BindView(R.id.info1)
    TextView info1;
    @BindView(R.id.info2)
    TextView info2;
    @BindView(R.id.liandongLienar)
    LinearLayout liandongLienar;
    @BindView(R.id.liandong)
    Button liandong;
    @BindView(R.id.tempiv)
    DeviceImageView tempiv;
    @BindView(R.id.humitureiv)
    DeviceImageView humitureiv;
    @BindView(R.id.tempandhumitrue)
    LinearLayout tempandhumitrue;
    private String sensor_id;
    private Sensor sensor;
    private int level;
    int hongwai[] = {R.mipmap.equ_img_infrared_noone2, R.mipmap.equ_img_infrared_soone2};
    private double level2;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EventData eventData) {
        if (eventData.getCode() == EventData.CODE_REFRESH_SENSOR) {
            updateData();
        } else if (EventData.REFRESHDB.equals(eventData.getTag())) {
            updateView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        shebeiIcon.setSort(1);
        tempiv.setSort(1);
        humitureiv.setSort(1);
        back.setOnClickListener(this);
        set.setOnClickListener(this);
        liandong.setOnClickListener(this);
        sensor_id = getIntent().getStringExtra("sensor_id");
        updateView();
    }

    private void updateView() {
        if (sensor_id != null || sensor_id != "") {
            try {
                sensor = App.db.selector(Sensor.class).where("id", "=", sensor_id).findFirst();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        if (sensor != null) {
            title.setText(sensor.getName());
            shebeiIcon.setType(sensor.getType());
            level = sensor.getValue1();
            Command.sendData1(sensor.getDevice_id(), Command.getAll(Command.ALLLIANDONG).getBytes(), "SensorDetailActivity");
            if (sensor.getType() == 1) {
                sensorHongwai.setVisibility(View.VISIBLE);
                RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setRepeatCount(-1);
                rotate.setDuration(5000);
                LinearInterpolator linearInterpolator = new LinearInterpolator();
                rotate.setInterpolator(linearInterpolator);
                sensorHongwai.startAnimation(rotate);
            }else if(sensor.getType()==3){
                shebeiIcon.setVisibility(View.GONE);
                tempandhumitrue.setVisibility(View.VISIBLE);
                int level2=sensor.getValue2();
                tempiv.setType(sensor.getType());
                humitureiv.setType(sensor.getType()+1);
                tempiv.setDeviceLevel(level);
                humitureiv.setDeviceLevel(level2);
            }
            shebeiIcon.setDeviceLevel(level);
            if (sensor.getType() == 1) {
                sensorHongwai.setImageResource(hongwai[level]);
            }
        }
    }

    private void updateData() {
        if (sensor_id != null || sensor_id != "") {
            try {
                sensor = App.db.selector(Sensor.class).where("id", "=", sensor_id).findFirst();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        if (sensor != null) {
            int value1 = sensor.getValue1();
            int value2 = sensor.getValue2();
            if(sensor.getType()==3){
                if (level != value1) {
                    level=value1;
                    tempiv.setDeviceLevel(sensor.getValue1());
                }
                if(level2!=value2){
                    level2=value2;
                    humitureiv.setDeviceLevel(sensor.getValue2());
                }
            }else {
                if (level != value1) {
                    level = value1;
                    shebeiIcon.setDeviceLevel(level);
                    if (sensor.getType() == 1) {
                        sensorHongwai.setImageResource(hongwai[level]);
                    }
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
            case R.id.set:
                Intent intent1 = new Intent(this, SensorEditActivity.class);
                intent1.putExtra("sensor", sensor);
                startActivity(intent1);
                break;
            case R.id.liandong:
                Intent intent = new Intent(this, LianDongActivity.class);
                intent.putExtra("sensor", sensor);
                startActivity(intent);
                break;
        }
    }
}
