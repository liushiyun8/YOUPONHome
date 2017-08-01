package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SpaceBean;
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
 * Created by liuyun on 2016/12/15.
 */
public class SpacesetAdapter extends BaseAdapter {
    List<SpaceBean> list;
    Context context;

    public SpacesetAdapter(List<SpaceBean> list, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final SpaceBean spaceBean = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.room_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.icon.setVisibility(View.VISIBLE);
        try {switch (spaceBean.getSort()){
            case 0:
                Scenebean scenebean = App.db.selector(Scenebean.class).where("name", "=", spaceBean.getSid()).findFirst();
                viewHolder.name.setText(scenebean.getName());
                viewHolder.icon.setImageResource(R.mipmap.scene);
                break;
            case 1:
                for (int i = 0; i < DeviceManage.getInstance().getCurrentdev().size(); i++) {
                    Device device = DeviceManage.getInstance().getCurrentdev().get(i);
                    if(device.getSID().equals(spaceBean.getSid())){
                        viewHolder.name.setText(device.getName());
                    }
                }
                viewHolder.icon.setImageResource(R.mipmap.gateway);
                break;
            case 3:
                SubDevice subDevice = App.db.selector(SubDevice.class).where("unique", "=", spaceBean.getSid()).findFirst();
                viewHolder.name.setText(subDevice.getName());
                viewHolder.icon.setImageResource(Comconst.IMAGETYPE[spaceBean.getType()]);
                break;
            case 4:
                Sensor sensor = App.db.selector(Sensor.class).where("id", "=", spaceBean.getSid()).findFirst();
                viewHolder.name.setText(sensor.getName());
                viewHolder.icon.setImageResource(Comconst.SENSORTYPE[spaceBean.getType()]);
                break;
        }
        } catch (DbException e) {
            e.printStackTrace();
        }

        viewHolder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {switch (spaceBean.getSort()){
                    case 0:
                        App.db.update(Scenebean.class, WhereBuilder.b("uid","=",spaceBean.getSid()),new KeyValue("room",""));
                        break;
                    case 1:
                        List<Device> currentdev = DeviceManage.getInstance().getCurrentdev();
                        for (int i = 0; i < currentdev.size(); i++) {
                            Device device = currentdev.get(i);
                            if(device.getMac().equals(spaceBean.getSid())){
                                device.setRoom("客厅");
                                DeviceManage.getInstance().saveDevice(device);
                            }
                        }
                        break;
                    case 2:
//                        App.db.update(SubDevice.class,WhereBuilder.b("unique","=",spaceBean.getSid()),new KeyValue("roomNum",""));
                        break;
                    case 3:
                        App.db.update(SubDevice.class,WhereBuilder.b("unique","=",spaceBean.getSid()),new KeyValue("room","客厅"));
                        break;
                    case 4:
                        App.db.update(Sensor.class,WhereBuilder.b("id","=",spaceBean.getSid()),new KeyValue("room","客厅"));
                        break;
                }
                } catch (DbException e) {
                e.printStackTrace();
            }
                list.remove(spaceBean);
                notifyDataSetChanged();
            }
        });
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
        ImageView edit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
