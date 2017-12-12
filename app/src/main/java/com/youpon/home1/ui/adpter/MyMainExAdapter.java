package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Devall;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.Comconst;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/13.
 */
public class MyMainExAdapter extends BaseExpandableListAdapter {
    String group[];
    Context context;
    List<List<Object>> lists;
    TextView save;

    public MyMainExAdapter(String[] group, Context context, List<List<Object>> lists, TextView save) {
        this.group = group;
        this.context = context;
        this.lists = lists;
        this.save=save;
    }
    public void getCount(){
        int count=0;
        for (int i = 0; i < 3; i++) {
            List<Object> list = lists.get(i);
            for (int j = 0; j < list.size(); j++) {
                Devall de = (Devall) list.get(j);
                if(de.isMain())
                    count++;
            }
        }
        save.setText("添加（已选"+count+"项）");
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lists.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lists.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 100 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText(group[groupPosition] + "(" + getChildrenCount(groupPosition) + ")");
        textView.setTextSize(30);
        textView.setPadding(36, 0, 0, 0);
        textView.setTextColor(Color.GRAY);
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_addex_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.check.setOnCheckedChangeListener(null);
        if (groupPosition == 0) {
            viewHolder.light.setVisibility(View.GONE);
            viewHolder.icon.setImageResource(R.mipmap.scene);
            final Scenebean scenebean = (Scenebean) lists.get(groupPosition).get(childPosition);
            viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        scenebean.setMain(true);
                    else scenebean.setMain(false);
                    getCount();
                }
            });
            viewHolder.check.setChecked(scenebean.isMain());
            viewHolder.name.setText(scenebean.getName());
        } else if (groupPosition == 1) {
            viewHolder.light.setVisibility(View.GONE);
            viewHolder.icon.setImageResource(R.mipmap.gateway);
            final Device gateway = (Device) lists.get(groupPosition).get(childPosition);
            viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        gateway.setMain(true);
                    else gateway.setMain(false);
                    getCount();
                }
            });
            viewHolder.check.setChecked(gateway.isMain());
            viewHolder.name.setText(gateway.getName());
        } else if (groupPosition == 2) {
            Object o = lists.get(groupPosition).get(childPosition);
            if(o instanceof SubDevice){
                final SubDevice subDevice = (SubDevice)o;
                viewHolder.icon.setImageResource(Comconst.IMAGETYPE[subDevice.getTp()]);
                viewHolder.name.setText(subDevice.getName());
                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                            subDevice.setMain(true);
                        else subDevice.setMain(false);
                        getCount();
                    }
                });
                viewHolder.check.setChecked(subDevice.getMain());
                viewHolder.light.setVisibility(View.GONE);
            }else if(o instanceof Sensor){
                viewHolder.light.setVisibility(View.GONE);
                final Sensor sensor= (Sensor) o;
                viewHolder.icon.setImageResource(Comconst.SENSORTYPE[sensor.getType()-1]);
                viewHolder.name.setText(sensor.getName());
                viewHolder.check.setChecked(sensor.isMain());
                MyLog.e("Sensor",sensor.isMain()+"1");
                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                            sensor.setMain(true);
                        else sensor.setMain(false);
                        getCount();
                        MyLog.e("Sensor",sensor.isMain()+"2");
                    }
                });
            }

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        @BindView(R.id.check)
        CheckBox check;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.check1)
        CheckBox check1;
        @BindView(R.id.icon1)
        ImageView icon1;
        @BindView(R.id.name1)
        TextView name1;
        @BindView(R.id.light)
        LinearLayout light;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
