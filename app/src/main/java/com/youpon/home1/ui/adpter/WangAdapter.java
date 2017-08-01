package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.ui.device.AddDeviceActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/7.
 */
public class WangAdapter extends BaseAdapter {
    List<Device> list;
    Context context;

    public WangAdapter(List<Device> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        ViewHolder viewHolder = null;
        final Device gateway = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wang_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(gateway.getName());
        viewHolder.deviceId.setText("ID:" + gateway.getXDevice().getDeviceId());
        if (gateway.isOnline()) {
            viewHolder.onOff.setVisibility(View.GONE);
            viewHolder.next.setVisibility(View.VISIBLE);
        } else {
            viewHolder.next.setVisibility(View.GONE);
            viewHolder.onOff.setVisibility(View.VISIBLE);
            viewHolder.onOff.setText("离线");
        }
        viewHolder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddDeviceActivity.class);
                intent.putExtra("device_id", gateway.getXDevice().getDeviceId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.device_id)
        TextView deviceId;
        @BindView(R.id.next)
        ImageView next;
        @BindView(R.id.on_off)
        TextView onOff;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
