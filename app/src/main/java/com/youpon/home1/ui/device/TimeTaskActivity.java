package com.youpon.home1.ui.device;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.gsonBeas.Timer;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeTaskActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "_TimeTaskActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.notime)
    LinearLayout notime;
    private int device_id;
    private String mac;
    private int dst;
    private List<Timer> tasks = new ArrayList<>();
    private CommonAdapter<Timer> commonAdapter;
    private boolean onff;
    boolean editTag;
    private SubDevice subdevice;
    private int selectpos=-1;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EventData eventData) {
        if (eventData.getCode() == EventData.CODE_REFRESH_TASK) {
            initData();
            commonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_task);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        subdevice = (SubDevice) getIntent().getSerializableExtra("subdevice");
        device_id = subdevice.getGateway_id();
        mac = subdevice.getMac();
        dst = subdevice.getDst();
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        edit.setOnClickListener(this);
        initData();
        commonAdapter = new CommonAdapter<Timer>(this, tasks, R.layout.timetask_item) {
            @Override
            public void convert(final ViewHolder helper, final int position, final Timer item) {
                if(position!=selectpos){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) helper.getView(R.id.content).getLayoutParams();
                    params.setMargins(0,0,0,0);
                    helper.getView(R.id.content).setLayoutParams(params);
                }else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) helper.getView(R.id.content).getLayoutParams();
                    params.setMargins(-helper.getView(R.id.dele).getWidth(),0,0,0);
                    helper.getView(R.id.content).setLayoutParams(params);
                }
                if (editTag) {
                    helper.getView(R.id.delete).setVisibility(View.VISIBLE);
                    helper.getView(R.id.on_off).setVisibility(View.GONE);
                    helper.getView(R.id.more).setVisibility(View.VISIBLE);
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimeTaskActivity.this, TimeTaskAddActivity.class);
                            intent.putExtra("ctrl_id", item.getCtrl_id());
                            intent.putExtra("subdevice", subdevice);
                            startActivity(intent);
                        }
                    });
                }else {
                    helper.getView(R.id.delete).setVisibility(View.GONE);
                    helper.getView(R.id.on_off).setVisibility(View.VISIBLE);
                    helper.getView(R.id.more).setVisibility(View.GONE);
                    helper.getConvertView().setOnClickListener(null);
                }
                int w = item.getTimer_week();
                String s = Integer.toBinaryString(w);
                s = new StringBuilder(s).reverse().toString();
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    String s1 = s.substring(i, i + 1);
                    if ("1".equals(s1)) {
                        switch (i) {
                            case 0:
                                name.append("周一、");
                                break;
                            case 1:
                                name.append("周二、");
                                break;
                            case 2:
                                name.append("周三、");
                                break;
                            case 3:
                                name.append("周四、");
                                break;
                            case 4:
                                name.append("周五、");
                                break;
                            case 5:
                                name.append("周六、");
                                break;
                            case 6:
                                name.append("周日");
                                break;
                        }
                    }
                }

                String minu = (item.getTimer_time_exe() % 3600 / 60) < 10 ? "0" + (item.getTimer_time_exe() % 3600 / 60) : (item.getTimer_time_exe() % 3600 / 60) + "";
                helper.setText(R.id.time, item.getTimer_time_exe() / 3600 + ":" + minu);
//                if (week.equals("5,6")) {
//                    name.append("周末");
//                } else if (week.equals("0,1,2,3,4")) {
//                    name.append("工作日");
//                } else if (week.equals("*")) {
//                    name.append("每天");
//                } else if (week.equals("")||week.equals(null)) {
//                    name.append("单次");
//                } else {
//                    String[] split = week.split(",");
//                    for (int i = 0; i < split.length; i++) {
//                        switch (split[i]) {
//                            case "1":
//                                name.append("一、");
//                                break;
//                            case "2":
//                                name.append("二、");
//                                break;
//                            case "3":
//                                name.append("三、");
//                                break;
//                            case "4":
//                                name.append("四、");
//                                break;
//                            case "5":
//                                name.append("五、");
//                                break;
//                            case "6":
//                                name.append("六、");
//                                break;
//                            case "0":
//                                name.append("日");
//                                break;
//                        }
//                    }
//                }
                String name1 = name.toString();
                if (name1.length() == 0) {
                    name1 = "单次";
                }
                helper.setText(R.id.week, name1.endsWith("、") ? name1.substring(0, name1.length() - 1) : name1);
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectpos=position;
                        notifyDataSetChanged();
//                        final AlertDialog alertDialog = new ProgressDialog.Builder(TimeTaskActivity.this).setMessage("正在删除中...").setCancelable(false).create();
//                        alertDialog.show();
                    }
                });
                helper.getView(R.id.dele).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            App.db.delete(item);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        selectpos=-1;
                        String command = Command.getTimerOrLiandong(143, "[{\"ctrl_id\":" + item.getCtrl_id() + "}]");
                        Command.sendData1(device_id, command.getBytes(), TAG);
                        initData();
                        notifyDataSetChanged();
                    }
                });
                CheckBox on_off = helper.getView(R.id.on_off);
                TextView status = helper.getView(R.id.status);
                TextView statusV= helper.getView(R.id.statusV);
                if (item.getVal() == 0) {
                    status.setText("关闭");
                    statusV.setVisibility(View.GONE);
                } else {
                    statusV.setVisibility(View.VISIBLE);
                    switch (subdevice.getTp()){
                        case 0:
                            if(item.getVal()==1){
                                statusV.setText("凉风");
                            }else if(item.getVal()==2){
                                statusV.setText("暖风");
                            }else if(item.getVal()==3){
                                statusV.setText("热风");
                            }
                            break;
                        case 1:
                            statusV.setText(item.getVal()+"%");
                            break;
                        case 3:
                            statusV.setText(item.getVal()==1?"低档":"高档");
                            break;
                        default:
                            statusV.setVisibility(View.GONE);
                            break;
                    }
                    status.setText("开启");
                }
                on_off.setOnCheckedChangeListener(null);
                on_off.setChecked(item.getStatus() == 1 ? true : false);
                on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String timerOrLiandong;
                        if (isChecked) {
                            onff = true;
                            item.setStatus(1);
                            timerOrLiandong = Command.getTimerOrLiandong(144, "[{\"ctrl_id\":" + item.getCtrl_id() + ",\"status\":1" + "}]");
                        } else {
                            item.setStatus(2);
                            timerOrLiandong = Command.getTimerOrLiandong(144, "[{\"ctrl_id\":" + item.getCtrl_id() + ",\"status\":2" + "}]");
                            onff = false;
                        }
                        int count = 0;
                        for (int i = 0; i < tasks.size(); i++) {
                            if (tasks.get(i).getStatus() == 1) {
                                count++;
                            }
                        }
                        title.setText("定时（" + count + "/" + tasks.size() + "）");
                        Command.sendData1(device_id, timerOrLiandong.getBytes(), TAG);
                        try {
                            App.db.replace(item);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
//                helper.getView(R.id.more).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
            }
        };
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setBackgroundColor(getResources().getColor(R.color.bgcolor));
//        linearLayout.setGravity(Gravity.CENTER);
//        linearLayout.setPadding(0,DeviceUtils.dip2px(20),0,DeviceUtils.dip2px(20));
//        ImageView v = new ImageView(this);
//        v.setImageResource(R.drawable.timingbtn_select);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(TimeTaskActivity.this, TimeTaskAddActivity.class);
//                intent1.putExtra("subdevice", subdevice);
//                startActivity(intent1);
//                editTag =false;
//                edit.setText("编辑");
//                selectpos=-1;
//                commonAdapter.notifyDataSetChanged();
//
//            }
//        });
//        linearLayout.addView(v);
//        lv.addFooterView(linearLayout);
        lv.setAdapter(commonAdapter);
    }

    private void initData() {
        tasks.clear();
        List<Timer> dbtasks = null;
        try {
            dbtasks = App.db.selector(Timer.class).where("mac", "=", this.mac).and("obj_id", "=", dst).orderBy("timer_time_exe").findAll();
//            dbtasks = App.db.findAll(Timer.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dbtasks != null)
            tasks.addAll(dbtasks);
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getStatus() == 1) {
                count++;
            }
        }
        title.setText("定时（" + count + "/" + tasks.size() + "）");
        if (tasks.size() > 0) {
            notime.setVisibility(View.GONE);
        }else {
            notime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_TASK,""));
                finish();
                break;
            case R.id.add:
                Intent intent1 = new Intent(this, TimeTaskAddActivity.class);
                intent1.putExtra("subdevice", subdevice);
                startActivity(intent1);
                editTag =false;
                edit.setText("编辑");
                selectpos=-1;
                commonAdapter.notifyDataSetChanged();
                break;
            case R.id.edit:
                if (!editTag) {
                    editTag = true;
                    edit.setText("完成");
                    commonAdapter.notifyDataSetChanged();
                } else {
                    editTag =false;
                    edit.setText("编辑");
                    selectpos=-1;
                    commonAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
