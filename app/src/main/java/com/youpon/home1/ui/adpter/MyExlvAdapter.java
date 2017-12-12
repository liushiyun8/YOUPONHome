package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.Devall;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.Constant;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.ui.device.DeviceDetailActivity;
import com.youpon.home1.ui.device.GatewaySetActivity;
import com.youpon.home1.ui.device.PanelDetailActivity;
import com.youpon.home1.ui.device.SensorDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fog.callbacks.ControlDeviceCallBack;
import io.fog.callbacks.ManageDeviceCallBack;
import io.fog.fog2sdk.MiCODevice;

/**
 * Created by liuyun on 2016/12/2.
 */
public class MyExlvAdapter extends BaseExpandableListAdapter {
    Context context;
    List<Object> lists;
    List<String> group;

    public MyExlvAdapter(Context context, List<Object> lists, List<String> group) {
        this.context = context;
        this.lists = lists;
        this.group=group;
    }
//    public void setGateList(List<Device> list){
//        this.list=list;
//    }
//    public void setSensorList(List<List<Sensor>> list){
//        this.sensorLists=list;
//    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Devall> list= (List<Devall>) lists.get(groupPosition);
        return list==null?0:list.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Devall> list= (List<Devall>) lists.get(groupPosition);
        return list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        int childrenCount = getChildrenCount(groupPosition);
        textView.setText(group.get(groupPosition)+"("+ childrenCount +")");
        textView.setTextSize(30);
        textView.setPadding(36, 0, 0, 0);
        textView.setTextColor(Color.DKGRAY);
        return textView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.exlv_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Devall child = (Devall) getChild(groupPosition, childPosition);
        viewHolder.l1.setVisibility(View.GONE);
        if(child.isOnline()){
            convertView.setEnabled(true);
                viewHolder.detail.setVisibility(View.VISIBLE);
                convertView.setBackgroundColor(Color.WHITE);
        }else {
            convertView.setEnabled(false);
                viewHolder.detail.setVisibility(View.GONE);
                convertView.setBackgroundColor(Color.GRAY);
        }
        viewHolder.name.setText(child.getName());
        if(child instanceof Device){
            viewHolder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,GatewaySetActivity.class);
                    intent.putExtra("device_id",((Device)child).getXDevice().getDeviceId());
                    context.startActivity(intent);
                }
            });
        }else if(child instanceof Panel){
            viewHolder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,PanelDetailActivity.class);
                    intent.putExtra("panel",(Panel)child);
                    context.startActivity(intent);
                }
            });

        }else if(child instanceof SubDevice){
            viewHolder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DeviceDetailActivity.class);
                    intent.putExtra("device",(SubDevice)child);
                    context.startActivity(intent);
                }
            });
        }else if (child instanceof Sensor){
            viewHolder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SensorDetailActivity.class);
                    intent.putExtra("sensor_id",((Sensor)child).getId());
                    context.startActivity(intent);
                }
            });
        }

//        if(child instanceof Device){
//            final Device gateway = (Device) child;
//            if(gateway.isOnline()){
//                convertView.setEnabled(true);
//                viewHolder.detail.setVisibility(View.VISIBLE);
//                convertView.setBackgroundColor(Color.WHITE);
//            }else{
//                convertView.setEnabled(false);
//                viewHolder.detail.setVisibility(View.GONE);
//                convertView.setBackgroundColor(Color.GRAY);
//            }
//            viewHolder.detail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context,GatewaySetActivity.class);
//                    intent.putExtra("device_id",gateway.getXDevice().getDeviceId());
//                    context.startActivity(intent);
//                }
//            });
//            viewHolder.delet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(context).setMessage("确定解绑网关吗？解绑后上面的子设备也会随之删除！").setTitle("警告！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
//
//                }
//            });
//            MyLog.e("ExLvAdapter",gateway.getName());
//            viewHolder.name.setText(gateway.getName());
//        }else if(child instanceof SubDevice){
//            final SubDevice subDevice = (SubDevice) child;
//            if(subDevice.isOnline()){
//                convertView.setEnabled(true);
//                viewHolder.detail.setVisibility(View.VISIBLE);
//                viewHolder.detail1.setVisibility(View.VISIBLE);
//                convertView.setBackgroundColor(Color.WHITE);
//            }else {
//                convertView.setEnabled(false);
//                viewHolder.detail.setVisibility(View.GONE);
//                viewHolder.detail1.setVisibility(View.GONE);
//                convertView.setBackgroundColor(Color.GRAY);
//            }
//            viewHolder.name.setText(subDevice.getName());
//            viewHolder.detail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, DeviceDetailActivity.class);
//                    intent.putExtra("device", subDevice);
//                    context.startActivity(intent);
//                }
//            });
//            viewHolder.delet.setImageResource(Comconst.IMAGETYPE[subDevice.getTp()]);
//        }else if(child instanceof Sensor){
//            final Sensor sensor= (Sensor) child;
//            viewHolder.l1.setVisibility(View.GONE);
//            viewHolder.name.setText(sensor.getName());
//            if(sensor.isOnline()){
//                convertView.setEnabled(true);
//                viewHolder.detail.setVisibility(View.VISIBLE);
//                convertView.setBackgroundColor(Color.WHITE);
//            }else {
//                convertView.setEnabled(false);
//                viewHolder.detail.setVisibility(View.GONE);
//                convertView.setBackgroundColor(Color.GRAY);
//            }
//            viewHolder.detail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SensorDetailActivity.class);
//                    intent.putExtra("sensor_id",sensor.getId());
//                    context.startActivity(intent);
//                }
//            });
//            viewHolder.delet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,"传感器无法删除！",Toast.LENGTH_LONG).show();
//                }
//            });
//        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    static class ViewHolder {
        @BindView(R.id.delet)
        ImageView delet;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.detail)
        ImageView detail;
        @BindView(R.id.delet1)
        ImageView delet1;
        @BindView(R.id.name1)
        TextView name1;
        @BindView(R.id.detail1)
        ImageView detail1;
        @BindView(R.id.linesr1)
        LinearLayout l1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
