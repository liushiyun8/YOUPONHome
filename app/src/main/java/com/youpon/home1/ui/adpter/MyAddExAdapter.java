package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.tools.Command;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fog.callbacks.ControlDeviceCallBack;
import io.fog.fog2sdk.MiCODevice;

/**
 * Created by liuyun on 2016/12/8.
 */
public class MyAddExAdapter extends BaseExpandableListAdapter {
    Context context;
    List<List<SubDevice>> lists;
    String[] group;
    private MiCODevice miCODevice;
    String device_id;
    String device_pw;

    public MyAddExAdapter(Context context, List<List<SubDevice>> lists, String[] group, String device_id, String device_pw) {
        this.context = context;
        this.lists = lists;
        this.group = group;
        this.device_id=device_id;
        this.device_pw=device_pw;
        miCODevice = new MiCODevice(context);
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
        convertView = LayoutInflater.from(context).inflate(R.layout.wang_group_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.name);
        Button add = (Button) convertView.findViewById(R.id.add);
        textView.setText(group[groupPosition]);
        textView.setPadding(36, 0, 0, 0);
        if(lists.get(groupPosition).size()==0){
            add.setVisibility(View.VISIBLE);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   miCODevice.sendCommand(device_id, device_pw, Command.getOtherStr(Command.ALLOWNET), "json", new ControlDeviceCallBack() {
                       @Override
                       public void onSuccess(String message) {
                           Toast.makeText(context,"等待设备接入中....",Toast.LENGTH_LONG).show();
                       }
                   }, (String) App.getSp().get(Comconst.TOKEN,""));
                }
            });
        }else add.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        SubDevice subDevice = lists.get(groupPosition).get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wang_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        if(subDevice.getType()==2){
            viewHolder.linear.setVisibility(View.VISIBLE);
            viewHolder.name1.setText(subDevice.getName()+"-2");
            viewHolder.icon1.setImageResource(Comconst.IMAGETYPE[subDevice.getType()]);
        }else {
            viewHolder.linear.setVisibility(View.GONE) ;
        }
        viewHolder.icon.setImageResource(Comconst.IMAGETYPE[subDevice.getType()]);
        viewHolder.next.setVisibility(View.GONE);
        viewHolder.add.setVisibility(View.VISIBLE);
        viewHolder.name.setText(subDevice.getName());
        viewHolder.deviceId.setText("网络ID：" + subDevice.getId());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.device_id)
        TextView deviceId;
        @BindView(R.id.on_off)
        TextView onOff;
        @BindView(R.id.next)
        ImageView next;
        @BindView(R.id.add)
        TextView add;
        @BindView(R.id.name1)
        TextView name1;
        @BindView(R.id.linear)
        LinearLayout linear;
        @BindView(R.id.icon1)
                ImageView icon1;
        @BindView(R.id.icon)
                ImageView icon;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
