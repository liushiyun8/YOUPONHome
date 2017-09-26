package com.youpon.home1.ui.adpter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.device.DeviceDetailActivity;
import com.youpon.home1.ui.device.GatewaySetActivity;
import com.youpon.home1.ui.device.SensorDetailActivity;

import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/12.
 */
public class SpaceAdapter extends BaseAdapter {
    private ProgressDialog progressDialog;
    Context context;
    List<SpaceBean> list;

    public SpaceAdapter(List<SpaceBean> list, Context context) {
        this.list = list;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("指令执行中...");
        progressDialog.setIndeterminate(true);
    }

    public void updateComplete() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        SpaceBean spaceBean = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.space_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.onOff.setVisibility(View.VISIBLE);
        viewHolder.onOff.setOnCheckedChangeListener(null);
        viewHolder.sensorLayout.setVisibility(View.GONE);
        viewHolder.status.setVisibility(View.GONE);

        viewHolder.huanqu.setVisibility(View.GONE);
        viewHolder.nuanqu.setVisibility(View.GONE);
        viewHolder.guangnuan.setVisibility(View.GONE);
        viewHolder.panel_name.setVisibility(View.VISIBLE);
        try {
            switch (spaceBean.getSort()) {
                case 1:
                    viewHolder.panel_name.setVisibility(View.GONE);
                    viewHolder.icon.setImageResource(R.mipmap.equ_ic_gateway);
                    Device device =null;
                    List<Device> currentdev = DeviceManage.getInstance().getCurrentdev();
                    for (int i = 0; i < currentdev.size(); i++) {
                        if (currentdev.get(i).getXDevice().getMacAddress().equals(spaceBean.getSid())) {
                            device = currentdev.get(i);
                        }
                    }
                    if(device==null){
                        list.remove(spaceBean);
                        App.db.delete(spaceBean);
                        notifyDataSetChanged();
                    }
                    if (device != null) {
                        final int deviceid=device.getXDevice().getDeviceId();
                        viewHolder.name.setText(device.getName());
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, GatewaySetActivity.class);
                                intent.putExtra("device_id",deviceid);
                                context.startActivity(intent);
                            }
                        });
                        viewHolder.onOff.setOnCheckedChangeListener(null);
                        if (device.isOnline()) {
                            viewHolder.onOff.setChecked(true);
                        } else {
                            viewHolder.onOff.setVisibility(View.GONE);
                            viewHolder.status.setVisibility(View.VISIBLE);
                        }
//                        List<SubDevice> subDevices = null;
//                        try {
//                            subDevices = App.db.selector(SubDevice.class).where("gateway_id", "=", device.getXDevice().getDeviceId()).findAll();
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
//                        final List<SubDevice> finalSubDevices = subDevices;
                        viewHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                if (finalSubDevices != null) {
//                                    for (int i = 0; i < finalSubDevices.size(); i++) {
//                                        SubDevice subDevice = finalSubDevices.get(i);
//                                        if (subDevice.getValue1() != 0) {
//                                            if(subDevice.getTp()==1){
//                                                sendComand(subDevice,1, 0);
//                                            }else
//                                            sendComand(subDevice,0, 0);
//                                        }
//                                    }
//                                }
                        Command.sendData1(deviceid, Command.closeAll().getBytes(), "SpaceAdapter");
                            }
                        });
                    }
                    break;
                case 3:
                    final SubDevice subDevice = App.db.selector(SubDevice.class).where("unique", "=", spaceBean.getSid()).findFirst();
                    if (subDevice == null)
                        break;
                    viewHolder.icon.setImageResource(Comconst.IMAGETYPE[subDevice.getTp()]);
                    viewHolder.name.setText(subDevice.getName());
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DeviceDetailActivity.class);
                            intent.putExtra("device",subDevice);
                            context.startActivity(intent);
                        }
                    });
                    final Panel panel = PanelManage.getInstance().getPanel(subDevice.getMac());
                    if(panel!=null){
                        viewHolder.panel_name.setText(panel.getMyName());
                        viewHolder.panel_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final MyDialog myDialog = new MyDialog(context);
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

                    if (!subDevice.isOnline()) {
                        viewHolder.status.setVisibility(View.VISIBLE);
                        viewHolder.onOff.setVisibility(View.GONE);
                    } else {
                        viewHolder.onOff.setTag(position);
                        if (subDevice.getTp() == 1) {
                            viewHolder.onOff.setChecked(subDevice.getValue2() == 0?false:true);
                        } else{
                            viewHolder.onOff.setChecked(subDevice.getValue1() == 0 ? false : true);
                        }
                        viewHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if ((int)buttonView.getTag() != position) {
                                    return;
                                }
                                if (subDevice.getTp() == 1) {
                                    sendComand(subDevice,1, isChecked ? 1 : 0);
                                } else
                                    sendComand(subDevice,0, isChecked ? 1 : 0);
                            }
                        });
                        switch (subDevice.getTp()) {
                            case 0:
                                if (subDevice.getValue1()!=0){
                                    viewHolder.nuanqu.setVisibility(View.VISIBLE);
                                }
                                viewHolder.fengdang.setOnCheckedChangeListener(null);
                                viewHolder.fengDong.setOnCheckedChangeListener(null);
                                if(subDevice.getClas()==9){
                                    viewHolder.nuan.setVisibility(View.GONE);
                                    viewHolder.re.setVisibility(View.GONE);
                                }else {
                                    viewHolder.nuan.setVisibility(View.VISIBLE);
                                    viewHolder.re.setVisibility(View.VISIBLE);
                                }
                                viewHolder.fengDong.check(subDevice.getValue2() == 2 ?R.id.baifeng: R.id.jingzhi);
                                switch (subDevice.getValue1()) {
                                    case 0:
                                        viewHolder.fengdang.clearCheck();
                                        viewHolder.fengDong.clearCheck();
                                        break;
                                    case 1:
                                        viewHolder.liang.setChecked(true);
                                        break;
                                    case 2:
                                        viewHolder.nuan.setChecked(true);
                                        break;
                                    case 3:
                                        viewHolder.re.setChecked(true);
                                        break;
                                }
                                viewHolder.fengDong.setTag(position);
                                viewHolder.fengdang.setTag(position);
                                viewHolder.fengDong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        if ((int)group.getTag() != position) {
                                            return;
                                        }
                                        switch (checkedId) {
                                            case R.id.jingzhi:
                                                sendComand(subDevice, 1, 1);
                                                break;
                                            case R.id.baifeng:
                                                sendComand(subDevice, 1, 2);
                                                break;
                                        }
                                    }
                                });
                                viewHolder.fengdang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        if ((int)group.getTag() != position) {
                                            return;
                                        }
                                        switch (checkedId) {
                                            case R.id.liang:
                                                sendComand(subDevice, 0, 1);
                                                break;
                                            case R.id.nuan:
                                                sendComand(subDevice, 0, 2);
                                                break;
                                            case R.id.re:
                                                sendComand(subDevice, 0, 3);
                                                break;
                                        }
                                    }
                                });
                                break;
                            case 1:
                                if (subDevice.getValue2() == 0) {
                                    viewHolder.guangnuan.setVisibility(View.GONE);
                                } else {
                                    viewHolder.guangnuan.setVisibility(View.VISIBLE);
                                }
                                viewHolder.seek.setOnSeekBarChangeListener(null);
                                viewHolder.seek.setProgress(subDevice.getValue1());
                                viewHolder.jian.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (viewHolder.seek.getProgress() >= 10)
                                            viewHolder.seek.setProgress(viewHolder.seek.getProgress() - 10);
                                    }
                                });
                                viewHolder.add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (viewHolder.seek.getProgress() <= 90)
                                            viewHolder.seek.setProgress(viewHolder.seek.getProgress() + 10);
                                    }
                                });
                                viewHolder.seek.setTag(position);
                                viewHolder.seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        if (!fromUser) {
                                            sendComand(subDevice, 0, progress);
                                        }
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                        sendComand(subDevice,0, seekBar.getProgress());
                                    }
                                });
                                break;
                            case 2:
                                break;
                            case 3:
                                if(subDevice.getValue1()!=0&&subDevice.getClas()==299){
                                    viewHolder.huanqu.setVisibility(View.VISIBLE);
                                }
                                viewHolder.huanqu.setTag(position);
                                viewHolder.huanqu.setOnCheckedChangeListener(null);
                                switch (subDevice.getValue1()) {
                                    case 1:
                                        viewHolder.didang.setChecked(true);
                                        break;
                                    case 2:
                                        viewHolder.gaodang.setChecked(true);
                                }
                                viewHolder.huanqu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        if ((int)group.getTag() != position) return;
                                        switch (checkedId) {
                                            case R.id.didang:
                                                sendComand(subDevice,0, 1);
                                                break;
                                            case R.id.gaodang:
                                                sendComand(subDevice,0, 2);
                                                break;
                                        }
                                    }
                                });
                                break;

                        }
                    }
                    break;
                case 4:
                    viewHolder.onOff.setVisibility(View.GONE);
                    final Sensor sensor = App.db.selector(Sensor.class).where("id", "=", spaceBean.getSid()).findFirst();
                    if (sensor == null)
                        break;
                    final Panel panel1 = PanelManage.getInstance().getPanel(sensor.getMac());
                    if(panel1!=null){
                        viewHolder.panel_name.setText(panel1.getMyName());
                        viewHolder.panel_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final MyDialog myDialog = new MyDialog(context);
                                myDialog.setTitle("更改面板名称");
                                myDialog.setType(MyDialog.EDITTYPE);
                                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                                    @Override
                                    public void onYesClick() {
                                        panel1.setName(myDialog.getEditText());
                                        PanelManage.getInstance().updatePanel(panel1);
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
                    }else {
                        Device device1 = DeviceManage.getInstance().getDevice(sensor.getDevice_id());
                        viewHolder.panel_name.setText(device1==null?"":device1.getName());
                        viewHolder.panel_name.setOnClickListener(null);
                    }
                    if (!sensor.isOnline()){
                        viewHolder.status.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.sensorLayout.setVisibility(View.VISIBLE);
                        viewHolder.info.setText(sensor.getStatus());
                    }
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, SensorDetailActivity.class);
                            intent.putExtra("sensor_id",sensor.getId());
                            context.startActivity(intent);
                        }
                    });
                    viewHolder.name.setText(sensor.getName());
                    viewHolder.icon.setImageResource(Comconst.SENSORTYPE[(sensor.getType()>0&&sensor.getType()<=8)?(sensor.getType() - 1):1]);
//                    viewHolder.status.setVisibility(View.GONE);
//                    if (sensor.isOnline()) {
//                        viewHolder.status.setText("联动");
//                    } else viewHolder.status.setText("离线");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void sendComand(SubDevice subDevice, int t, int level) {
        Command.sendData1(subDevice.getGateway_id(), Command.getDeviceStr(level, subDevice, t).getBytes(), "SpaceAdapter");
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.on_off)
        CheckBox onOff;
        @BindView(R.id.info)
        TextView info;
        @BindView(R.id.sensor_more)
        ImageView sensorMore;
        @BindView(R.id.sensor_layout)
        LinearLayout sensorLayout;
        @BindView(R.id.didang)
        RadioButton didang;
        @BindView(R.id.gaodang)
        RadioButton gaodang;
        @BindView(R.id.huanqu)
        RadioGroup huanqu;
        @BindView(R.id.jian)
        ImageView jian;
        @BindView(R.id.seek)
        SeekBar seek;
        @BindView(R.id.add)
        ImageView add;
        @BindView(R.id.guangnuan)
        LinearLayout guangnuan;
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
        @BindView(R.id.nuanqu)
        LinearLayout nuanqu;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.panel_name)
        TextView panel_name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
