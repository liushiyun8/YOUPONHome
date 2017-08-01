package com.youpon.home1.ui.scene;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetSceneActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit)
    ImageView edit;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.rename)
    ImageView rename;
    @BindView(R.id.shebei_count)
    TextView shebeiCount;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.save)
    TextView save;
    private Scenebean sceneBean;
    private String TAG = getClass().getSimpleName();
    private CommonAdapter<SubDevice> myScenelvAdapter;
    private List<SubDevice> deviList = new ArrayList<>();
    private PopupWindow popupWindow;
    private MyDialog dialog;
    private Map<String, List<Scenebean.ActionsBean>> map = new HashMap<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
        if (EventData.TRANSDATA == eventData.getCode()) {
            List<SubDevice> data = (List<SubDevice>) eventData.getData();
            deviList.clear();
            deviList.addAll(data);
            initUI();
            myScenelvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_scene);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        DeviceUtils.setContext(this);
        init();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void init() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        rename.setOnClickListener(this);
        save.setOnClickListener(this);
        View view = LayoutInflater.from(this).inflate(R.layout.scene_popup, null);
        View ad = view.findViewById(R.id.adddevice);
        View dele = view.findViewById(R.id.deletscene);
        View divider = view.findViewById(R.id.divider);
        ad.setOnClickListener(this);
        dele.setOnClickListener(this);
        popupWindow = new PopupWindow(view, DeviceUtils.dip2px(154), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        sceneBean = (Scenebean) getIntent().getSerializableExtra("sceneBean");
        if (sceneBean != null) {
            List<SubDevice> subdev = sceneBean.getSubdev();
            deviList.clear();
            deviList.addAll(subdev);
            if (sceneBean.getType() == 1 || sceneBean.getType() == 2) {
                ad.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                popupWindow.setHeight(DeviceUtils.dip2px(60));
            }
            name.setText(sceneBean.getName());
        }
        myScenelvAdapter = new CommonAdapter<SubDevice>(SetSceneActivity.this, deviList, R.layout.space_item) {

            @Override
            public void convert(ViewHolder helper, final int position, final SubDevice item) {
                final ImageView icon = helper.getView(R.id.icon);
                TextView name = helper.getView(R.id.name);
                CheckBox onOff = helper.getView(R.id.on_off);
                LinearLayout sensorLayout = helper.getView(R.id.sensor_layout);
                RadioButton didang = helper.getView(R.id.didang);
                RadioButton gaodang = helper.getView(R.id.gaodang);
                RadioGroup huanqu = helper.getView(R.id.huanqu);
                ImageView jian = helper.getView(R.id.jian);
                final SeekBar seek = helper.getView(R.id.seek);
                ImageView add = helper.getView(R.id.add);
                LinearLayout guangnuan = helper.getView(R.id.guangnuan);
                RadioButton liang = helper.getView(R.id.liang);
                RadioButton nuan = helper.getView(R.id.nuan);
                RadioButton re = helper.getView(R.id.re);
                RadioGroup fengdang = helper.getView(R.id.fengdang);
                RadioGroup fengDong = helper.getView(R.id.feng_dong);
                LinearLayout nuanqu = helper.getView(R.id.nuanqu);
                TextView status = helper.getView(R.id.status);
                onOff.setVisibility(View.VISIBLE);
                onOff.setOnCheckedChangeListener(null);
                sensorLayout.setVisibility(View.GONE);
                status.setVisibility(View.GONE);
                huanqu.setVisibility(View.GONE);
                nuanqu.setVisibility(View.GONE);
                guangnuan.setVisibility(View.GONE);
                final SubDevice subDevice = item;
                Panel panel = PanelManage.getInstance().getPanel(subDevice.getMac());
                helper.setText(R.id.panel_name, panel == null ? "" : panel.getName());
                icon.setImageResource(Comconst.IMAGETYPE[subDevice.getTp()]);
                name.setText(subDevice.getName());
                onOff.setTag(position);
                if (subDevice.getTp() == 1) {
                    onOff.setChecked(subDevice.getValue2() == 0 ? false : true);
                } else {
                    onOff.setChecked(subDevice.getValue1() == 0 ? false : true);
                }
                onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.getTag() != position) {
                            return;
                        }
                        if (subDevice.getTp() == 1) {
                            subDevice.setValue2(isChecked ? 1 : 0);
                        } else
                            subDevice.setValue1(isChecked ? 1 : 0);
                        notifyDataSetChanged();
                    }
                });
                switch (subDevice.getTp()) {
                    case 0:
                        Log.e(TAG, subDevice.getValue1() + "");
                        if (subDevice.getValue1() != 0) {
                            nuanqu.setVisibility(View.VISIBLE);
                        }
                        fengdang.setOnCheckedChangeListener(null);
                        fengDong.setOnCheckedChangeListener(null);
                        fengDong.check(subDevice.getValue2() == 2 ? R.id.baifeng : R.id.jingzhi);
                        switch (subDevice.getValue1()) {
                            case 0:
                                fengdang.clearCheck();
                                fengDong.clearCheck();
                                break;
                            case 1:
                                liang.setChecked(true);
                                break;
                            case 2:
                                nuan.setChecked(true);
                                break;
                            case 3:
                                re.setChecked(true);
                                break;
                        }
                        fengDong.setTag(position);
                        fengdang.setTag(position);
                        fengDong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (group.getTag() != position) {
                                    return;
                                }
                                switch (checkedId) {
                                    case R.id.jingzhi:
                                        item.setValue2(1);
                                        break;
                                    case R.id.baifeng:
                                        item.setValue2(2);
                                        break;
                                }
                            }
                        });
                        fengdang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (group.getTag() != position) {
                                    return;
                                }
                                switch (checkedId) {
                                    case R.id.liang:
                                        item.setValue1(1);
                                        break;
                                    case R.id.nuan:
                                        item.setValue1(2);
                                        break;
                                    case R.id.re:
                                        item.setValue1(3);
                                        break;
                                }
                            }
                        });
                        break;
                    case 1:
                        if (subDevice.getValue2() == 0) {
                            guangnuan.setVisibility(View.GONE);
                        } else {
                            guangnuan.setVisibility(View.VISIBLE);
                        }
                        seek.setProgress(subDevice.getValue1());
                        jian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (seek.getProgress() >= 10)
                                    seek.setProgress(seek.getProgress() - 10);
                            }
                        });
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (seek.getProgress() <= 90)
                                    seek.setProgress(seek.getProgress() + 10);
                            }
                        });
                        seek.setTag(position);
                        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (!fromUser) {
                                    item.setValue1(progress);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                item.setValue1(seekBar.getProgress());
                            }
                        });
                        break;
                    case 2:
                        break;
                    case 3:
                        if (subDevice.getValue1() != 0) {
                            huanqu.setVisibility(View.VISIBLE);
                        }
                        huanqu.setTag(position);
                        huanqu.setOnCheckedChangeListener(null);
                        switch (subDevice.getValue1()) {
                            case 1:
                                didang.setChecked(true);
                                break;
                            case 2:
                                gaodang.setChecked(true);
                        }
                        huanqu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (group.getTag() != position) return;
                                switch (checkedId) {
                                    case R.id.didang:
                                        item.setValue1(1);
                                        break;
                                    case R.id.gaodang:
                                        item.setValue1(2);
                                        break;
                                }
                            }
                        });
                        break;
                }
            }

        };
        initUI();
        lv.setAdapter(myScenelvAdapter);
    }

    private void initUI() {
        shebeiCount.setText(deviList.size() + "个设备");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                String s = "";
                if (deviList.size() > 0) {
                    sceneBean.setGateway_id(deviList.get(0).getGateway_id());
                    s = sceneBean.sub2action(deviList);
                } else {
                    Toast.makeText(this, "场景设备不可为空！", Toast.LENGTH_LONG).show();
                    break;
                }
                if (s.equals(sceneBean.getActions())) {
                    Toast.makeText(this, "场景未做任何修改！", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    sceneBean.setActions(s);
                }
                HttpManage.getInstance().upDateSub("Scenebean", sceneBean.getObjectId(), new Gson().toJson(sceneBean), new MyCallback() {
                    @Override
                    public void onSuc(String result) {
                        Log.e(TAG, result);
                        Scenebean scenebean = new Gson().fromJson(result, Scenebean.class);
                        try {
                            App.db.update(scenebean);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new EventData(EventData.REFRESHDB, "刷新场景"));
                        finish();
                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
                try {
                    App.db.update(sceneBean);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (sceneBean.getType() == 1||sceneBean.getType()==2) {
//                    String panel_mac = sceneBean.getPanel_mac();
//                    Panel panel = PanelManage.getInstance().getPanel(panel_mac);
                    List<Scenebean.ActionsBean> action = sceneBean.getAction();
                    for (int i = 0; i < action.size(); i++) {
                        Scenebean.ActionsBean actionsBean = action.get(i);
                        String mac = actionsBean.getMac();
                        List<Scenebean.ActionsBean> list = map.get(mac);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(actionsBean);
                        map.put(mac, list);
                    }
                    Set<Map.Entry<String, List<Scenebean.ActionsBean>>> entries = map.entrySet();
                    for (Map.Entry<String, List<Scenebean.ActionsBean>> enty : entries) {
                        String panel_mac = enty.getKey();
                        Panel panel = PanelManage.getInstance().getPanel(panel_mac);
                        List<Scenebean.ActionsBean> action1 = enty.getValue();
                        String s1 = Integer.toHexString(action1.size());
                        if (s1.length() < 2) {
                            s1 = "0" + s1;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append(s1);
                        for (int j = 0; j < action1.size(); j++) {
                            Scenebean.ActionsBean actionsBean = action1.get(j);
                            int dstid = actionsBean.getDstid();
                            int val = actionsBean.getVal();
                            String va = Integer.toHexString(val);
                            if (va.length() < 2) {
                                va = "0" + va;
                            }
                            String nclu = actionsBean.getNclu();
                            boolean eq = "0008".equals(nclu);
                            sb.append("0" + dstid + (eq ? "0800" : "0600") + "0000" + (eq ? "20" : "10") + va);
                        }
                        if (panel != null)
                            if (panel.getClas() == 299) {
                                Command.sendData1(panel.getGateway_id(), Command.getWriteSceneStr(sceneBean.getGroupId(), sceneBean.getSceneId(), panel.getId(), 0, sb.toString()).getBytes(), TAG);
                            } else {
                                Command.sendData1(panel.getGateway_id(), Command.getWriteSceneStr(sceneBean.getGroupId(), sceneBean.getSceneId(), panel.getId(), 1, sb.toString()).getBytes(), TAG);
                            }
                    }
                }
//                    String s = Integer.toHexString(action.size());
//                    if (s.length() < 2) {
//                        s = "0" + s;
//                    }
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(s);
//                    for (int j = 0; j < action.size(); j++) {
//                        Scenebean.ActionsBean actionsBean = action.get(j);
//                        int dstid = actionsBean.getDstid();
//                        int val = actionsBean.getVal();
//                        String va = Integer.toHexString(val);
//                        if (va.length() < 2) {
//                            va = "0" + va;
//                        }
//                        String nclu = actionsBean.getNclu();
//                        boolean eq = "0008".equals(nclu);
//                        sb.append("0" + dstid + (eq ? "0800" : "0600") + "0000" + (eq ? "20" : "10") + va);
//                    }
//                    if (panel != null)
//                        if (panel.getClas() == 299) {
//                            Command.sendData1(panel.getGateway_id(), Command.getWriteSceneStr(sceneBean.getGroupId(), sceneBean.getSceneId(), panel.getId(), 0, sb.toString()).getBytes(), TAG);
//                        } else {
//                            Command.sendData1(panel.getGateway_id(), Command.getWriteSceneStr(sceneBean.getGroupId(), sceneBean.getSceneId(), panel.getId(), 1, sb.toString()).getBytes(), TAG);
//                        }
//                }
                EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                finish();
                break;
            case R.id.edit:
                popupWindow.showAsDropDown(v, -DeviceUtils.dip2px(125), 2);
                backgroundAlpha(0.5f);
                break;
            case R.id.rename:
                dialog = new MyDialog(this);
                dialog.setTitle("请输入新的场景名称");
                dialog.setType(MyDialog.EDITTYPE);
                dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        String nameStr = dialog.getEditText();
                        name.setText(nameStr);
                        try {
                            Scenebean sc = App.db.selector(Scenebean.class).where("name", "=", nameStr).findFirst();
                            if (sc != null) {
                                XlinkUtils.shortTips("名称重复，请重新输入");
                            } else {
                                sceneBean.setName(nameStr);
                                App.db.update(sceneBean);
                                dialog.dismiss();
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }


                    }
                });
                dialog.show();
                break;
            case R.id.adddevice:
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                Intent intent = new Intent(this, AddSceanDvActivity.class);
                intent.putExtra("data", (Serializable) deviList);
                startActivity(intent);
                break;
            case R.id.deletscene:
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                dialog = new MyDialog(this);
                dialog.setMessage("确定删除该场景吗？");
                dialog.setType(MyDialog.MESSAGETYPE);
                dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        if(sceneBean.getType()!=0){
                            try {if("0001".equals(sceneBean.getGroupId())){
                                App.db.delete(SubDevice.class, WhereBuilder.b("mac","=",sceneBean.getPanel_mac()).and("dst","=",Integer.parseInt(sceneBean.getSceneId())+1));
                            }else {
                                App.db.delete(SubDevice.class,WhereBuilder.b("mac","=",sceneBean.getPanel_mac()).and("dst","=",Integer.parseInt(sceneBean.getSceneId())+1));
                            }
                            } catch (DbException e) {
                            e.printStackTrace();
                        }
                        }
                        HttpManage.getInstance().deleSub(sceneBean.getObjectId(), "Scenebean", new MyCallback() {
                            @Override
                            public void onSuc(String result) {
                                try {
                                    App.db.delete(sceneBean);
                                    MyToast.show(SetSceneActivity.this, MyToast.TYPE_OK, "删除成功", 1);
                                    EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                                    dialog.dismiss();
                                    finish();
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFail(int code, String msg) {
                                try {
                                    App.db.delete(sceneBean);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                MyToast.show(SetSceneActivity.this, MyToast.TYPE_OK, "删除失败", 1);
                                dialog.dismiss();
                                EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                                finish();
                            }
                        });

                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
