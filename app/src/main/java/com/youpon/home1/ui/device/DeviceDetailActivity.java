package com.youpon.home1.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.gsonBeas.Timer;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.DeviceImageView;
import com.youpon.home1.comm.view.MyRelativeLayout;
import com.youpon.home1.manage.DeviceManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.xlink.wifi.sdk.XDevice;

public class DeviceDetailActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "DeviceDetailActivity";
    private final int _REFRESHTEXTVIEW = 1;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.set)
    ImageView set;
    @BindView(R.id.shebei_icon)
    DeviceImageView shebeiIcon;
    @BindView(R.id.didang)
    RadioButton didang;
    @BindView(R.id.gaodang)
    RadioButton gaodang;
    @BindView(R.id.huanqi)
    RadioGroup huanqi;
    @BindView(R.id.jian)
    ImageView jian;
    @BindView(R.id.seekb)
    SeekBar seekb;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.guang)
    LinearLayout guang;
    @BindView(R.id.liang)
    RadioButton liang;
    @BindView(R.id.nuan)
    RadioButton nuan;
    @BindView(R.id.re)
    RadioButton re;
    @BindView(R.id.fengdang)
    RadioGroup fengdang;
    @BindView(R.id.jingzhi)
    RadioButton jingzhi;
    @BindView(R.id.baifeng)
    RadioButton baifeng;
    @BindView(R.id.feng_dong)
    RadioGroup fengDong;
    @BindView(R.id.feng)
    LinearLayout feng;
    @BindView(R.id.timeTask)
    Button timeTask;
    @BindView(R.id.on_off)
    RelativeLayout onOff;
    @BindView(R.id.taskTv)
    TextView taskTv;
    @BindView(R.id.baiye)
    ImageView baiye;
    @BindView(R.id.control_layout)
    MyRelativeLayout controlLayout;
    private int type;
    private String id;
    private int tap=-1;
    private int tap2=-1;
    private SpUtils sp;
    private int deviceid;
    private MyHandle myhandle = new MyHandle();
    private SubDevice subDevice;
    //    private ProgressDialog progressDialog;
    private String unique;
    private int dst;
    private XDevice xDevice;
    private String mac;
    private SubDevice device;
    private int clas;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EventData eventData) {
        if (eventData.getCode() == EventData.CODE_READ_STUTAS || eventData.getCode() == EventData.CODE_REFRESH_DEVICE) {
            updateData();
            updateComplete();
        }
        if (eventData.getCode() == EventData.CODE_REFRESH_TASK) {
            try {
                long count = App.db.selector(Timer.class).where("obj_id", "=", dst).and("mac", "=", mac).count();
                taskTv.setText("此设备关联了" + count + "个定时任务");
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        sp = App.getSp();
        init();
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("指令执行中...");
//        progressDialog.setIndeterminate(true);
    }

    public void updateComplete() {
//        if (progressDialog != null && progressDialog.isShowing())
//            progressDialog.dismiss();
    }

    private void initUI() {
        if (tap == 0) {
            onOff.setSelected(false);
        } else {
            onOff.setSelected(true);
        }
        shebeiIcon.setDeviceLevel(tap);
        if (type == 0) {
            feng.setVisibility(View.VISIBLE);
            if (clas == 9) {
                nuan.setVisibility(View.GONE);
                re.setVisibility(View.GONE);
            }
            fengdang.setOnCheckedChangeListener(null);
            fengDong.setOnCheckedChangeListener(null);
            switch (tap2) {
                case 1:
                    baiye.setVisibility(View.GONE);
                    baiye.clearAnimation();
                    jingzhi.setChecked(true);
                    break;
                case 2:
                    baiye.setVisibility(View.VISIBLE);
                    rotate(baiye);
                    baifeng.setChecked(true);
                    break;
                default:
                    baiye.setVisibility(View.GONE);
                    baiye.clearAnimation();
                    break;
            }
            switch (tap) {
                case 0:
                    fengDong.clearCheck();
                    fengdang.clearCheck();
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
            fengdang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.liang:
                            sendCommand(0, 1);
                            break;
                        case R.id.nuan:
                            sendCommand(0, 2);
                            break;
                        case R.id.re:
                            sendCommand(0, 3);
                            break;
                    }
                }
            });
            fengDong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.jingzhi:
                            sendCommand(1, 1);
                            break;
                        case R.id.baifeng:
                            if (tap == 0) {
                                fengDong.clearCheck();
                                Toast.makeText(DeviceDetailActivity.this, "请至少打开凉风、暖风、热风中的一种！", Toast.LENGTH_LONG).show();
                                break;
                            }
                            sendCommand(1, 2);
                            break;
                    }
                }
            });

        } else if (type == 1) {
            guang.setVisibility(View.VISIBLE);
            if (tap2 == 0) {
                seekb.setEnabled(false);
                jian.setEnabled(false);
                add.setEnabled(false);
                onOff.setSelected(false);
                shebeiIcon.setDeviceLevel(0);
            } else {
                seekb.setEnabled(true);
                jian.setEnabled(true);
                add.setEnabled(true);
                onOff.setSelected(true);
                shebeiIcon.setDeviceLevel(tap);
            }
            seekb.setProgress(tap);
            seekb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    if(fromUser==false){
//                        int progress1 = seekBar.getProgress();
//                        if(progress1<10)progress1=10;
//                        sendCommand(1,progress1);
//                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    if (progress < 10) {
                        progress = 10;
                        seekBar.setProgress(progress);
                    }
                    sendCommand(0, progress);
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress =seekb.getProgress();
                    progress=progress>90?100:(progress + 10);
                    seekb.setProgress(progress);
                    sendCommand(0, progress);
                }
            });
            jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress =seekb.getProgress();
                    progress=progress<20?10:(progress - 10);
                    seekb.setProgress(progress);
                    sendCommand(0, progress);
                }
            });
        } else if (type == 3) {
            huanqi.setVisibility(View.VISIBLE);
            if (tap2 == 1) {
                gaodang.setVisibility(View.GONE);
            } else gaodang.setVisibility(View.VISIBLE);
            if (tap == 0) {
                shebeiIcon.setDeviceStatus(0);
                huanqi.setOnCheckedChangeListener(null);
                huanqi.clearCheck();
            } else if (tap == 1) {
                shebeiIcon.setDeviceStatus(1);
                didang.setChecked(true);
            } else if (tap == 2) {
                shebeiIcon.setDeviceStatus(1);
                gaodang.setChecked(true);
            }
            huanqi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.didang:
                            sendCommand(0, 1);
                            break;
                        case R.id.gaodang:
                            sendCommand(0, 2);
                            break;
                    }
                }
            });
        }
    }

    private void rotate(View view) {
        RotateAnimation animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        view.startAnimation(animation);
    }

    private void sendCommand(int t, int tap) {
//        progressDialog.show();
        Log.e(TAG, "commend:" + Command.getDeviceStr(tap, subDevice, t));
        Command.sendData(xDevice, Command.getDeviceStr(tap, subDevice, t).getBytes(), TAG);
    }

    private void init() {
        device = (SubDevice) getIntent().getSerializableExtra("device");
        deviceid = device.getGateway_id();
        try {
            App.db.delete(Timer.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        Command.sendData1(deviceid, Command.getAll(Command.ALLTIMER).getBytes(), TAG);
        xDevice = DeviceManage.getInstance().getDevice(deviceid).getXDevice();
        id = device.getId();
        dst = device.getDst();
        unique = device.getUnique();
        mac = device.getMac();
        back.setOnClickListener(this);
        set.setOnClickListener(this);
        timeTask.setOnClickListener(this);
        taskTv.setOnClickListener(this);
        onOff.setOnClickListener(this);
        long count = 0;
        try {
            count = App.db.selector(Timer.class).where("obj_id", "=", dst).and("mac", "=", mac).count();
        } catch (DbException e) {
            e.printStackTrace();
        }
        taskTv.setText("此设备关联了" + count + "个定时任务");
        updateView();
    }

    private void updateView() {
        try {
            subDevice = App.db.selector(SubDevice.class).where("unique", "=", unique).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        type = subDevice.getTp();
        clas = subDevice.getClas();
        shebeiIcon.setType(type);
        deviceid = subDevice.getGateway_id();
        updateData();
    }

    private void updateData() {
        try {
            subDevice = App.db.selector(SubDevice.class).where("unique", "=", unique).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(subDevice.isOnline()){
            controlLayout.setIntercept(false);
            controlLayout.setOnClickListener(null);
            onOff.setEnabled(true);
        }else {
            controlLayout.setIntercept(true);
            controlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XlinkUtils.shortTips("设备离线，不可控制");
                }
            });
            onOff.setEnabled(false);
        }
        title.setText(subDevice.getName());
        if (tap != subDevice.getValue1() || tap2 != subDevice.getValue2()) {
            tap = subDevice.getValue1();
            tap2 = subDevice.getValue2();
            initUI();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.on_off:
                if (type == 1) {
                    if (tap2 == 0) {
                        tap2 = 1;
                    } else {
                        tap2 = 0;
                    }
                    sendCommand(1, tap2);
                } else {
                    if (tap == 0) {
                        tap = 1;
                    } else {
                        tap = 0;
                    }
                    sendCommand(0, tap);
                }
                initUI();
                break;
            case R.id.set:
                Intent intent = new Intent(this, DevEditActivity.class);
                intent.putExtra("device", device);
                startActivity(intent);
                break;
            case R.id.timeTask:
            case R.id.taskTv:
                Intent intent1 = new Intent(this, TimeTaskActivity.class);
                intent1.putExtra("subdevice", subDevice);
                startActivity(intent1);
                break;
        }
    }

    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case _REFRESHTEXTVIEW:
                    Log.e("Handler", msg.obj.toString().trim());
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
