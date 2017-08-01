package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.SceneDevice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/9.
 */
public class MySceneExAdapter extends BaseExpandableListAdapter {
    Context context;
    List<List<SceneDevice>> lists;
    String[] group;

    public MySceneExAdapter(Context context, List<List<SceneDevice>> lists, String[] group) {
        this.context = context;
        this.lists = lists;
        this.group = group;
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
        return true;
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
        ViewHolder viewHolder = null;
        final SceneDevice sceneDevice = lists.get(groupPosition).get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.scene_item, null,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.onOff.setVisibility(View.GONE);
        viewHolder.quchu.setChecked(sceneDevice.isControl()?true:false);
        viewHolder.quchu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sceneDevice.setControl(true);
                }else sceneDevice.setControl(false);
            }
        });
        if (sceneDevice.getType() == 2) {
            viewHolder.fengnuan.setVisibility(View.GONE);
            viewHolder.guangnuan.setVisibility(View.GONE);
            viewHolder.huanqu.setVisibility(View.GONE);
            viewHolder.light.setVisibility(View.VISIBLE);
            viewHolder.onOff.setVisibility(View.VISIBLE);
            viewHolder.onOff.setChecked(sceneDevice.getValue1()==0?false:true);
            viewHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        sceneDevice.setValue1(1);
                    }else sceneDevice.setValue1(0);
                }
            });
            viewHolder.onOff1.setChecked(sceneDevice.getValue2()==0?false:true);
            viewHolder.onOff1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        sceneDevice.setValue2(1);
                    }else sceneDevice.setValue2(0);
                }
            });
        } else if(sceneDevice.getType()==0){
            viewHolder.light.setVisibility(View.GONE);
            viewHolder.fengnuan.setVisibility(View.VISIBLE);
            viewHolder.onOff.setVisibility(View.VISIBLE);
            viewHolder.guangnuan.setVisibility(View.GONE);
            viewHolder.huanqu.setVisibility(View.GONE);
            viewHolder.fengDong.check(sceneDevice.getValue2()==0?R.id.jingzhi:R.id.baifeng);
            switch (sceneDevice.getValue1()){
                case 0:
                    viewHolder.onOff.setChecked(false);
                    viewHolder.fengDong.setOnCheckedChangeListener(null);
                    viewHolder.fengdang.setOnCheckedChangeListener(null);
                    viewHolder.fengdang.clearCheck();
                    viewHolder.fengDong.clearCheck();
                    break;
                case 1:
                    viewHolder.liang.setChecked(true);
                    viewHolder.onOff.setChecked(true);
                    break;
                case 2:
                    viewHolder.nuan.setChecked(true);
                    viewHolder.onOff.setChecked(true);
                    break;
                case 3:
                    viewHolder.re.setChecked(true);
                    viewHolder.onOff.setChecked(true);
                    break;
            }
            viewHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        sceneDevice.setValue1(1);
                        Log.e("onff","开");
                    }else{
                        sceneDevice.setValue1(0);
                        sceneDevice.setValue2(0);
                        Log.e("onff","关");
                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.fengdang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.liang:
                            sceneDevice.setValue1(1);
                            break;
                        case R.id.nuan:
                            sceneDevice.setValue1(2);
                            break;
                        case R.id.re:
                            sceneDevice.setValue1(3);
                            break;
                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.fengDong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.jingzhi:
                            sceneDevice.setValue2(0);
                            break;
                        case R.id.baifeng:
                            sceneDevice.setValue2(1);
                            break;
                    }
                }
            });
        }else if(sceneDevice.getType()==1){
            viewHolder.light.setVisibility(View.GONE);
            viewHolder.fengnuan.setVisibility(View.GONE);
            viewHolder.guangnuan.setVisibility(View.VISIBLE);
            viewHolder.huanqu.setVisibility(View.GONE);
            viewHolder.nuanduzhi.setText(sceneDevice.getValue1()+"");
            viewHolder.nuanduoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        sceneDevice.setValue2(1);
                    }else sceneDevice.setValue2(0);
                }
            });
            viewHolder.nuanduoff.setChecked(sceneDevice.getValue2()==0?false:true);
            viewHolder.nuanduzhi.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                        if(s.toString().trim()!=null&&!s.toString().trim().equals("")){
                            Log.e("我的光",s.toString().trim());
                            sceneDevice.setValue1(Integer.parseInt(s.toString().trim()));
                        }
                }
            });
        }else if(sceneDevice.getType()==3){
            viewHolder.light.setVisibility(View.GONE);
            viewHolder.fengnuan.setVisibility(View.GONE);
            viewHolder.guangnuan.setVisibility(View.GONE);
            viewHolder.huanqu.setVisibility(View.VISIBLE);
            switch (sceneDevice.getValue1()){
                case 1:
                    viewHolder.huanqudang.setChecked(false);
                    viewHolder.huanqudang.setText("低档");
                    viewHolder.huanquonoff.setChecked(true);
                    break;
                case 2:
                    viewHolder.huanqudang.setChecked(true);
                    viewHolder.huanqudang.setText("高档");
                    viewHolder.huanquonoff.setChecked(true);
                    break;
                case 0:
                    viewHolder.huanquonoff.setChecked(false);
                    break;
            }
           final CheckBox huangdang =viewHolder.huanqudang;
            final CheckBox huanquonoff=viewHolder.huanquonoff;
           viewHolder.huanqudang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if(isChecked){
                      buttonView.setText("高档");
                      if(huanquonoff.isChecked()){
                          sceneDevice.setValue1(2);
                      }else sceneDevice.setValue1(0);
                  }else {
                      buttonView.setText("低档");
                      if(huanquonoff.isChecked()){
                          sceneDevice.setValue1(1);
                      }else sceneDevice.setValue1(0);
                  }
               }
           });
            viewHolder.huanquonoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(huangdang.isChecked()){
                            sceneDevice.setValue1(2);
                        }else sceneDevice.setValue1(1);
                    }else sceneDevice.setValue1(0);
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        @BindView(R.id.quchu)
        CheckBox quchu;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.on_off)
        CheckBox onOff;
        @BindView(R.id.name1)
        TextView name1;
        @BindView(R.id.on_off1)
        CheckBox onOff1;
        @BindView(R.id.light)
        LinearLayout light;
        @BindView(R.id.huanqu)
        LinearLayout huanqu;
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
        @BindView(R.id.fengnuan)
        LinearLayout fengnuan;
        @BindView(R.id.nuanduzhi)
        EditText nuanduzhi;
        @BindView(R.id.nuanduoff)
        CheckBox nuanduoff;
        @BindView(R.id.huanqudang)
        CheckBox huanqudang;
        @BindView(R.id.huanquonoff)
        CheckBox huanquonoff;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
