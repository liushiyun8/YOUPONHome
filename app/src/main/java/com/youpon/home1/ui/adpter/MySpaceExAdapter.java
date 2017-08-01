package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.graphics.Color;
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
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.Comconst;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/15.
 */
public class MySpaceExAdapter extends BaseExpandableListAdapter {
    String group[];
    Context context;
    List<List<Object>> lists;
    TextView save;
    String name;

    public MySpaceExAdapter(String[] group, Context context, List<List<Object>> lists, TextView save,String name) {
        this.group = group;
        this.context = context;
        this.lists = lists;
        this.save=save;
        this.name=name;
    }
    public void getCount(){
        int count=0;
        for (int i = 0; i < 3; i++) {
            List<Object> list = lists.get(i);
            for (int j = 0; j < list.size(); j++) {
                Devall de = (Devall) list.get(j);
                if(de.getRoom()!=null&&de.getRoom().equals(name))
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_addex_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        if (groupPosition == 0) {
            viewHolder.light.setVisibility(View.GONE);
            viewHolder.icon.setImageResource(R.mipmap.scene);
            final Scenebean scenebean = (Scenebean) lists.get(groupPosition).get(childPosition);
            final String oldname = scenebean.getRoom();
            viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getCount();
                }
            });
            viewHolder.check.setChecked(name.equals(scenebean.getRoom()));
            viewHolder.name.setText(scenebean.getName());
        } else if (groupPosition == 1) {
            viewHolder.icon.setImageResource(R.mipmap.gateway);
            viewHolder.light.setVisibility(View.GONE);
            final Gateway gateway = (Gateway) lists.get(groupPosition).get(childPosition);
            final String room = gateway.getRoom();
            viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        gateway.setRoom(name);
                    else {
                        if(name.equals(room)){
                            gateway.setRoom(null);
                        }else
                        gateway.setRoom(room);
                    }
                    getCount();
                }
            });
            viewHolder.check.setChecked(name.equals(gateway.getRoom()));
            viewHolder.name.setText(gateway.getDevice_name());
        } else if (groupPosition == 2) {
            Object o = lists.get(groupPosition).get(childPosition);
            if(o instanceof SubDevice){
                final SubDevice subDevice = (SubDevice) o;
                viewHolder.name.setText(subDevice.getName());
                final String roomNum = subDevice.getRoom();
                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                        {
                            subDevice.setRoom(name);
                            viewHolder.check1.setChecked(true);
                        }
                        else {
                            if(name.equals(roomNum)){
                                subDevice.setRoom(null);
                            }else
                                subDevice.setRoom(roomNum);
                            viewHolder.check1.setChecked(false);
                        }
                        getCount();
                    }
                });
                viewHolder.check.setChecked(name.equals(subDevice.getRoom()));
                switch (subDevice.getType()){
                    case 0:
                        viewHolder.icon.setImageResource(R.mipmap.fengnuan_item);
                        break;
                    case 1:
                        viewHolder.icon.setImageResource(R.mipmap.guangnuan_item);
                        break;
                    case 2:
                        viewHolder.icon.setImageResource(R.mipmap.light_item);
                        break;
                    case 3:
                        viewHolder.icon.setImageResource(R.mipmap.huanqu_item);
                        break;
                }
                if (subDevice.getType() == 2) {
                    viewHolder.light.setVisibility(View.VISIBLE);
                    viewHolder.name1.setText(subDevice.getName() + "-2");
                    viewHolder.icon1.setImageResource(R.mipmap.light_item);
                    viewHolder.check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked)
                            {
                                subDevice.setRoom(name);
                                viewHolder.check.setChecked(true);
                            }
                            else {
                                if(name.equals(roomNum)){
                                    subDevice.setRoom(null);
                                }else
                                    subDevice.setRoom(roomNum);
                                viewHolder.check.setChecked(false);
                            }
                            getCount();
                        }
                    });
                    viewHolder.check1.setChecked(name.equals(subDevice.getRoom()));
                } else viewHolder.light.setVisibility(View.GONE);
            }
            else if(o instanceof Sensor){
                final Sensor sensor= (Sensor) o;
                viewHolder.name.setText(sensor.getName());
                final String roomNum =sensor.getRoom();
                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                            sensor.setRoom(name);
                        else {
                            if(name.equals(roomNum)){
                                sensor.setRoom(null);
                            }else
                                sensor.setRoom(roomNum);
                        }
                        getCount();
                    }
                });
                viewHolder.check.setChecked(name.equals(sensor.getRoom()));
                viewHolder.icon.setImageResource(Comconst.SENSORTYPE[sensor.getType()]);
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
