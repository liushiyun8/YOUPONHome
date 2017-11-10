package com.youpon.home1.ui.adpter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.MainBean;
import com.youpon.home1.bean.SceneDevice;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.manage.DeviceManage;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fog.callbacks.ControlDeviceCallBack;
import io.fog.fog2sdk.MiCODevice;

/**
 * Created by liuyun on 2016/12/16.
 */
public class MainGridAdapter extends BaseAdapter {
    List<MainBean> list;
    Context context;
    private ProgressDialog progressDialog;

    public MainGridAdapter(List<MainBean> list, Context context) {
        this.list = list;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("指令执行中...");
        progressDialog.setIndeterminate(true);
    }

    public void updateComplete(){
        if(progressDialog!=null&&progressDialog.isShowing())
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final MainBean mainBean = list.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_gridite, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.status.setTextColor(Color.parseColor("#333333"));
        try {switch (mainBean.getSort()){
            case 0:
                final Scenebean scenebean = App.db.selector(Scenebean.class).where("objectId", "=", mainBean.getSid()).findFirst();
                if(scenebean!=null){
                    viewHolder.name.setText(scenebean.getName());
                    viewHolder.status.setText(scenebean.getStatus()==1?"开启":"关闭");
                    if(scenebean.getStatus()==1){
                        viewHolder.icon.setImageResource(R.mipmap.equ_btn_scene_drops_on);
                    }else viewHolder.icon.setImageResource(R.mipmap.equ_btn_scene_drops_off);
                    viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<Scenebean.ActionsBean> action = scenebean.getAction();
                            if(scenebean.getStatus()!=1) {
                                scenebean.setStatus(1);
                                if (action != null && action.size() > 0) {
                                    Device device = DeviceManage.getInstance().getDevice(scenebean.getGateway_id());
                                    if (device!=null){
                                        if(scenebean.getType()==0){
                                            List<String> commands = Command.getCommands(action);
                                            for (String command : commands) {
                                                Command.sendData(device.getXDevice(), command.getBytes(), "MainGridAdapter");
                                            }

                                        }else {
                                            if("0001".equals(scenebean.getGroupId())){
                                                Command.sendData(device.getXDevice(), Command.getCallSceneStr(scenebean.getGroupId(),scenebean.getSceneId(),"FFFF",0).getBytes(), "MysceneListAdapter");
                                                try {
                                                    App.db.update(Scenebean.class, WhereBuilder.b("panel_mac","=",scenebean.getPanel_mac()).and("sceneId","!=",scenebean.getSceneId()),new KeyValue("status","0"));
                                                } catch (DbException e) {

                                                }
                                            }else
                                                Command.sendData(device.getXDevice(), Command.getCallSceneStr(scenebean.getGroupId(),scenebean.getSceneId(),scenebean.getId(),scenebean.getGateway_type()).getBytes(), "MysceneListAdapter");
                                        }
                                    }

                                }
                            }else {
                                scenebean.setStatus(0);
                                List<Scenebean.ActionsBean> action1 = new ArrayList<Scenebean.ActionsBean>();
                                for (int i = 0; i < action.size(); i++) {
                                    Scenebean.ActionsBean actionsBean = new Scenebean.ActionsBean();
                                    Scenebean.ActionsBean actionsBean1 = action.get(i);
                                    actionsBean.setMac(actionsBean1.getMac());
                                    int dstid = actionsBean1.getDstid();
                                    actionsBean.setDstid(dstid);
                                    String nclu = actionsBean1.getNclu();
                                    if("0008".equals(nclu)&&dstid==2)
                                        actionsBean.setVal(1);
                                    else actionsBean.setVal(0);
                                    if("0008".equals(nclu)&&dstid==3)
                                        actionsBean.setNclu("0006");
                                    else actionsBean.setNclu(nclu);
                                    action1.add(actionsBean);
                                }
                                if (action1 != null && action.size() > 0) {
                                    Device device = DeviceManage.getInstance().getDevice(scenebean.getGateway_id());
                                    if (device!=null){
                                        List<String> commands = Command.getCommands(action1);
                                        for (String command : commands) {
                                            Command.sendData(device.getXDevice(), command.getBytes(), "MainGridAdapter");
                                        }
                                    }

                                }
                            }
                            try {
                                App.db.saveOrUpdate(scenebean);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case 1:
                Device device=null;
                List<Device> currentdev = DeviceManage.getInstance().getCurrentdev();
                for (int j = 0; j < currentdev.size(); j++) {
                    if(currentdev.get(j).getSID().equals(mainBean.getSid())){
                        device = currentdev.get(j);
                        break;
                    }
                }
                if(device!=null){
                    viewHolder.name.setText(device.getName());
                    viewHolder.status.setText(device.isOnline()?"在线":"离线");
                    viewHolder.icon.setImageResource(device.isOnline()?R.mipmap.equ_ic_gateway:R.mipmap.equ_ic_addgateway);
                    final Device device1=device;
                    viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<SubDevice> subDevices = null;
                            try {
                                subDevices = App.db.selector(SubDevice.class).where("gateway_id", "=",device1.getXDevice().getDeviceId()).findAll();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            if(subDevices !=null){
                                for (int i = 0; i < subDevices.size(); i++) {
                                    SubDevice subDevice = subDevices.get(i);
                                    if(subDevice.getTp()==1){
                                        sendComand(subDevice,1,0);
                                    }else
                                        sendComand(subDevice,0,0);
                                }
                            }
//                            try {
//                                App.db.update(Scenebean.class, WhereBuilder.b("userName","=",Comconst.CURRENTUSER),new KeyValue("on_off",false));
//                            } catch (DbException e) {
//                                e.printStackTrace();
//                            }
                        }
                    });
                }
                break;
            case 3:
                final SubDevice subDevice = App.db.selector(SubDevice.class).where("unique", "=", mainBean.getSid()).findFirst();
                if (subDevice !=null){
                    viewHolder.name.setText(subDevice.getName());
                    if(!subDevice.isOnline()){
                        viewHolder.status.setText("离线");
                        viewHolder.status.setTextColor(Color.parseColor("#f45266"));
                        viewHolder.icon.setImageResource(Comconst.MAINTYPEOUT[subDevice.getTp()]);
                    }else {
                        if(subDevice.getTp()==1){
                            viewHolder.icon.setImageResource(subDevice.getValue2()==0?Comconst.MAINTYPEOFF[subDevice.getTp()]:Comconst.MAINTYPEON[subDevice.getTp()]);
                            viewHolder.status.setText(subDevice.getValue2()==0?"关":"开");
                        }else{
                            viewHolder.icon.setImageResource(subDevice.getValue1()==0?Comconst.MAINTYPEOFF[subDevice.getTp()]:Comconst.MAINTYPEON[subDevice.getTp()]);
                            viewHolder.status.setText(subDevice.getValue1()==0?"关":"开");
                            if(subDevice.getTp()==0){
                                switch (subDevice.getValue1()){
                                    case 1:
                                        viewHolder.status.setText("凉风");
                                        break;
                                    case 2:
                                        viewHolder.status.setText("暖风");
                                        break;
                                    case 3:
                                        viewHolder.status.setText("热风");
                                        break;
                                }
                            }else if(subDevice.getTp()==3){
                                switch (subDevice.getValue1()){
                                    case 1:
                                        viewHolder.status.setText("低档");
                                        break;
                                    case 2:
                                        viewHolder.status.setText("高档");
                                        break;
                                }
                            }
                        }
                    }
                    viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!subDevice.isOnline()){
                                Toast.makeText(context,"离线设备不可控制",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(subDevice.getTp()==1){
                               if(subDevice.getValue2()==0){
                                   sendComand(subDevice,1,1);
                               }else sendComand(subDevice,1,0);
                            }else
                            if(subDevice.getValue1()==0){
                                sendComand(subDevice,0,1);
                            }else{
                                sendComand(subDevice,0,0);
                            }
                        }
                    });
                }
                break;
            case 4:
                viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"传感器不可控制",Toast.LENGTH_LONG).show();
                    }
                });
                Sensor sensor = App.db.selector(Sensor.class).where("id", "=", mainBean.getSid()).findFirst();
                if(sensor!=null){
                    if(!sensor.isOnline()){
                        viewHolder.status.setText("离线");
                        viewHolder.status.setTextColor(Color.parseColor("#f45266"));
                    }else {
                        viewHolder.status.setText(sensor.getStatus());
                        viewHolder.status.setTextColor(Color.parseColor("#333333"));
                    }
                    viewHolder.icon.setImageResource(sensor.isOnline()?Comconst.SENSORTYPEON[sensor.getType()-1]:Comconst.SENSORTYPEOFF[sensor.getType()-1]);
                    viewHolder.name.setText(sensor.getName());
                }
                break;
        }
        } catch (DbException e) {
        e.printStackTrace();
    }
        return convertView;
    }

    private void sendComand(SubDevice subDevice,int t,int level) {
        Command.sendData1(subDevice.getGateway_id(), Command.getDeviceStr(level,subDevice,t).getBytes(), "SpaceAdapter");
    }

    static class ViewHolder {
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
