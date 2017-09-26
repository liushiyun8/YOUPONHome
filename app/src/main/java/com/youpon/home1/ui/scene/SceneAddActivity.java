package com.youpon.home1.ui.scene;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.youpon.home1.R;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SceneAddActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addShebei)
    LinearLayout addShebei;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.shebei_count)
    TextView shebeiCount;
    @BindView(R.id.addmore)
    ImageView addmore;
    private List<SubDevice> all = new ArrayList<>();

    private List<SubDevice> deviList = new ArrayList<>();
    private Scenebean scenebean;
    private String nameStr;
    private List<Roombean> rooms;
    private PopupWindow popupWindow;
    private String roomString = "";
    private CommonAdapter<SubDevice> myScenelvAdapter;
    private View view;
    private String TAG = getClass().getSimpleName();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
        if (EventData.TRANSDATA == eventData.getCode()) {
            List<SubDevice> data = (List<SubDevice>) eventData.getData();
            deviList.clear();
            deviList.addAll(data);
            updateUI();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_set);
        ButterKnife.bind(this);
        view = LayoutInflater.from(SceneAddActivity.this).inflate(R.layout.popup_view, null);
        popupWindow = new PopupWindow(view, (int) (App.mWidth * 2 / 3), (int) (App.mHeight / 3), true);
        initEvent();
        EventBus.getDefault().register(this);
    }

//    private void initData() {
//        all.clear();
//        all.addAll(DbUtil.findMyDv());
//    }

    //    private void updateData() {
//        deviList.clear();
//        for (int i = 0; i < devsorts.length; i++) {
//            List<SceneDevice> devs = new ArrayList<>();
//            if (all != null)
//                for (int j = 0; j < all.size(); j++) {
//                    SubDevice subDevice = all.get(j);
//                    if (subDevice.getType() == i) {
//                        devs.add(new SceneDevice(subDevice.getId(), subDevice.getName(), subDevice.getRoom(), subDevice.getGateway_id(), subDevice.getType(), subDevice.getValue2(), subDevice.getValue1(), subDevice.getGateway_type()));
//                    }
//                }
//        }
//
//    }
    public void updateUI() {
        myScenelvAdapter.notifyDataSetChanged();
        addShebei.setVisibility(View.GONE);
        shebeiCount.setVisibility(View.VISIBLE);
        addmore.setVisibility(View.VISIBLE);
        shebeiCount.setText(deviList.size()+"个设备");
    }

    private void initEvent() {
        back.setOnClickListener(this);
        addShebei.setOnClickListener(this);
        save.setOnClickListener(this);
        addmore.setOnClickListener(this);
        myScenelvAdapter = new CommonAdapter<SubDevice>(SceneAddActivity.this, deviList, R.layout.space_item) {

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
                        if ((int)buttonView.getTag() != position) {
                            return;
                        }
                        if (subDevice.getTp() == 1) {
                            subDevice.setValue2(isChecked ? 1 : 0);
                        } else
                            subDevice.setValue1(isChecked ? 1 : 0);
                        notifyDataSetChanged();
                    }
                });
                Panel panel = PanelManage.getInstance().getPanel(subDevice.getMac());
                helper.setText(R.id.panel_name,panel==null?"":panel.getMyName());
                switch (subDevice.getTp()) {
                    case 0:
                        Log.e(TAG, subDevice.getValue1() + "");
                        if (subDevice.getValue1() != 0) {
                            nuanqu.setVisibility(View.VISIBLE);
                        }
                        if(subDevice.getClas()==9){
                            nuan.setVisibility(View.GONE);
                            re.setVisibility(View.GONE);
                        }else {
                            nuan.setVisibility(View.VISIBLE);
                            re.setVisibility(View.VISIBLE);
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
                                if ((int)group.getTag() != position) {
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
                                if ((int)group.getTag() != position) {
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
                        if(subDevice.getValue2()==1){
                            gaodang.setVisibility(View.GONE);
                        }else gaodang.setVisibility(View.VISIBLE);
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
                                if ((int)group.getTag() != position) return;
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
        lv.setAdapter(myScenelvAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (name.getText().toString().equals("")) {
                    Toast.makeText(this, "场景名不可为空", Toast.LENGTH_LONG).show();
                    break;
                }
                scenebean = new Scenebean(name.getText().toString());
                if (deviList.size() > 0) {
                    scenebean.setGateway_id(deviList.get(0).getGateway_id());
                    String s = scenebean.sub2action(deviList);
                    scenebean.setActions(s);
                } else {
                    Toast.makeText(this, "场景设备不可为空", Toast.LENGTH_LONG).show();
                    break;
                }
                    HttpManage.getInstance().addSub(HttpManage.TYPE_SINGLE, "Scenebean", new Gson().toJson(scenebean), new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, result);
                            Scenebean scenebean = new Gson().fromJson(result, Scenebean.class);
                            try {
                                App.db.saveOrUpdate(scenebean);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }

                            EventBus.getDefault().post(new EventData(EventData.REFRESHDB, "刷新场景"));
                            finish();
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                break;
            case R.id.back:
                finish();
                break;
            case R.id.addShebei:
                Intent intent = new Intent(this, AddSceanDvActivity.class);
                startActivity(intent);
                break;
            case R.id.addmore:
                Intent intent1 = new Intent(this, AddSceanDvActivity.class);
                intent1.putExtra("data", (Serializable) deviList);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
