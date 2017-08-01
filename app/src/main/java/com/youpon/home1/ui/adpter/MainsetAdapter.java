package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.MainBean;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.manage.DeviceManage;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/13.
 */
public class MainsetAdapter extends BaseAdapter {
    List<MainBean> list;
    Context context;

    public MainsetAdapter(List<MainBean> list, Context context) {
        this.list = list;
        this.context = context;
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
        ViewHolder viewHolder;
        final MainBean mainBean = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.room_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.linear.setVisibility(View.VISIBLE);
        switch (mainBean.getSort()) {
            case 0:
                viewHolder.icon.setImageResource(R.mipmap.equ_ic_scene);
                try {
                    Scenebean scenebean = App.db.selector(Scenebean.class).where("objectId", "=", mainBean.getSid()).findFirst();
                    if (scenebean != null) {
                        viewHolder.name.setText(scenebean.getName());
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                viewHolder.delet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            App.db.delete(mainBean);
                            App.db.update(Scenebean.class, WhereBuilder.b("name", "=", mainBean.getSid()), new KeyValue("isMain", false));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            case 1:
                viewHolder.icon.setImageResource(R.mipmap.gateway);
                final Device device = DeviceManage.getInstance().getDevice(mainBean.getSid());
                viewHolder.name.setText(device.getName());
                viewHolder.delet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            App.db.delete(mainBean);
                            device.setMain(false);
                            DeviceManage.getInstance().saveDevice(device);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            case 3:
                SubDevice subDevice = null;
                try {
                    subDevice = App.db.selector(SubDevice.class).where("unique", "=", mainBean.getSid()).findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (subDevice != null) {
                    viewHolder.name.setText(subDevice.getName());
                    viewHolder.icon.setImageResource(Comconst.IMAGETYPE[subDevice.getTp()]);
                }
                viewHolder.delet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            App.db.delete(mainBean);
                            App.db.update(SubDevice.class, WhereBuilder.b("unique", "=", mainBean.getSid()), new KeyValue[]{new KeyValue("isMain", false)});
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            case 4:
                viewHolder.icon.setImageResource(Comconst.SENSORTYPE[mainBean.getType() - 1]);
                try {
                    Sensor sensor = App.db.selector(Sensor.class).where("id", "=", mainBean.getSid()).findFirst();
                    if (sensor != null)
                        viewHolder.name.setText(sensor.getName());
                } catch (DbException e) {
                    e.printStackTrace();
                }
                viewHolder.delet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            App.db.delete(mainBean);
                            App.db.update(Sensor.class, WhereBuilder.b("id", "=", mainBean.getSid()), new KeyValue("isMain", false));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.delet)
        ImageView delet;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.drag_handle)
        ImageView dragHandle;
        @BindView(R.id.linear)
        LinearLayout linear;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
