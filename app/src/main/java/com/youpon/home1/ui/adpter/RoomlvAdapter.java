package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Roombean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/12.
 */
public class RoomlvAdapter extends BaseAdapter {
    Context context;
    List<Roombean> list;


    public RoomlvAdapter(Context context, List<Roombean> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder viewHolder;
        final Roombean roombean = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.room_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else
           viewHolder= (ViewHolder) convertView.getTag();
        if(position==0){
            viewHolder.delet.setVisibility(View.GONE);
            viewHolder.edit.setVisibility(View.GONE);
            viewHolder.name.setPadding(10,30,0,30);
        }else {
            viewHolder.delet.setVisibility(View.VISIBLE);
            viewHolder.edit.setVisibility(View.VISIBLE);
        }
        viewHolder.name.setText(roombean.getName());
        viewHolder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(roombean);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.delet)
        ImageView delet;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.drag_handle)
        ImageView edit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
