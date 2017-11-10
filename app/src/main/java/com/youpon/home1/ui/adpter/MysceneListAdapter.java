package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.scene.SceneAddActivity;
import com.youpon.home1.ui.scene.SetSceneActivity;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuyun on 2016/12/9.
 */
public class  MysceneListAdapter extends BaseAdapter {
    Context context;
    List<Scenebean> list;

    public MysceneListAdapter(Context context, List<Scenebean> list) {
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
        final ViewHolder viewHolder;
        final Scenebean scenebean = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.scenefragment_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        if(scenebean.getType()==0){
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }else convertView.setBackgroundColor(Color.parseColor("#F6F7FA"));

        final List<Scenebean.ActionsBean> action = scenebean.getAction();
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0; i < (action.size()>4?4:action.size()); i++) {
            Scenebean.ActionsBean actionsBean1 = action.get(i);
            try {
                SubDevice subDevice = App.db.selector(SubDevice.class).where("mac", "=", actionsBean1.getMac()).and("dst", "=", actionsBean1.getDstid()).findFirst();
                if(subDevice!=null){
                    sbStr.append(subDevice.getName()+" "+(actionsBean1.getVal()==0?"关\t\t":"开\t\t"));
                }
                if(i==1){
                    sbStr.append("\n");
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        if(scenebean.getType()==2){
            viewHolder.detail.setText("默认场景");
        }else {
            if(action.size()>4)sbStr.append("...");
            viewHolder.detail.setText(sbStr.toString());
        }

        viewHolder.name.setText(scenebean.getName());
        viewHolder.onOff.setOnCheckedChangeListener(null);
        viewHolder.onOff.setChecked(scenebean.getStatus() == 1 ? true : false);
        viewHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scenebean.setStatus(1);
                    if (action != null && action.size() > 0) {
                        Device device = DeviceManage.getInstance().getDevice(scenebean.getGateway_id());
                        if (device!=null){
                            if(scenebean.getType()==0){
                                List<String> commands = Command.getCommands(action);
                                for (String command : commands) {
                                    Command.sendData(device.getXDevice(), command.getBytes(), "MysceneListAdapter");
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
                } else {
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
                                Command.sendData(device.getXDevice(), command.getBytes(), "MysceneListAdapter");
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
        viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scenebean.getType() == 2) {
                    final MyDialog dialog = new MyDialog(context);
                    dialog.setYesOnclickListener("修改", new MyDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            dialog.dismiss();
                            Intent intent = new Intent(context, SetSceneActivity.class);
                            intent.putExtra("sceneBean", scenebean);
                            context.startActivity(intent);
                        }
                    });
                    dialog.setNoOnclickListener("确定", new MyDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setTitle("场景详情");
                    dialog.setMessage(Comconst.SCENETEXT[Integer.parseInt(scenebean.getSceneId())]);
                    dialog.show();
                }else {
                    Intent intent = new Intent(context, SetSceneActivity.class);
                    intent.putExtra("sceneBean", scenebean);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.delet)
        ImageView delet;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.detail)
        TextView detail;
        @BindView(R.id.click)
        LinearLayout click;
        @BindView(R.id.on_off)
        CheckBox onOff;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
