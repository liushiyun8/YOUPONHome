package com.youpon.home1.ui.device;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.weigan.loopview.LoopView;
import com.youpon.home1.R;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.gsonBeas.Timer;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.view.PickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.fog.fog2sdk.MiCODevice;

public class TimeTaskAddActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.rate_tv)
    TextView rateTv;
    @BindViews({R.id.week1, R.id.week2, R.id.week3, R.id.week4, R.id.week5, R.id.week6, R.id.week7})
    List<CheckBox> checkBoxList;
    @BindView(R.id.status_check)
    RadioGroup statusCheck;
    @BindView(R.id.week1)
    CheckBox week1;
    @BindView(R.id.week2)
    CheckBox week2;
    @BindView(R.id.week3)
    CheckBox week3;
    @BindView(R.id.week4)
    CheckBox week4;
    @BindView(R.id.week5)
    CheckBox week5;
    @BindView(R.id.week6)
    CheckBox week6;
    @BindView(R.id.week7)
    CheckBox week7;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.status_tv)
    TextView statusTv;
    @BindView(R.id.open)
    RadioButton open;
    @BindView(R.id.close)
    RadioButton close;
    @BindView(R.id.didang)
    RadioButton didang;
    @BindView(R.id.gaodang)
    RadioButton gaodang;
    @BindView(R.id.huanqu)
    RadioGroup huanqu;
    @BindView(R.id.jian)
    ImageView jian;
    @BindView(R.id.seek)
    SeekBar seek;
    @BindView(R.id.add)
    ImageView add;
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
    @BindView(R.id.nuanqu)
    LinearLayout nuanqu;
    //    @BindView(R.id.hour)
//    PickerView hourw;
//    @BindView(R.id.min)
//    PickerView minw;
    @BindView(R.id.loopvH)
    LoopView loopv;
    @BindView(R.id.loopvM)
    LoopView loopvM;
    private int dst;
    private int device_id;
    private String mac;
    private int hour;
    private int minu;
    private String TAG = "_TimeTaskAddActivity";
    private MiCODevice micodev;
    private int tap;
    private int tap2;
    private SubDevice subDevice;
    private int ctrl_id;
    private boolean addTag;
    private Timer timer;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EventData eventData) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_task_add);
        ButterKnife.bind(this);
        micodev = new MiCODevice(this);
        EventBus.getDefault().register(this);
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minu = calendar.get(Calendar.MINUTE);
        init();
        initEvent();
    }

    private void initEvent() {
        statusCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.open:
                        openLayer();
                        break;
                    case R.id.close:
                        closeLayer();
                }
            }

        });
    }

    private void openLayer() {
        if (tap == 0) {
            tap = 1;
        }
        switch (subDevice.getTp()) {
            case 0:
                nuanqu.setVisibility(View.VISIBLE);
                switch (tap) {
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
                fengdang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.liang:
                                tap = 1;
                                break;
                            case R.id.nuan:
                                tap = 2;
                                break;
                            case R.id.re:
                                tap = 3;
                                break;
                        }
                    }
                });
                break;
            case 1:
                guangnuan.setVisibility(View.VISIBLE);
                seek.setProgress(tap);
                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tap = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            case 2:
                break;
            case 3:
                huanqu.setVisibility(View.VISIBLE);
                if (tap == 1) {
                    didang.setChecked(true);
                } else gaodang.setChecked(true);
                huanqu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.didang) {
                            tap = 1;
                        } else tap = 2;
                    }
                });
                break;
        }
    }

    private void closeLayer() {
        tap = 0;
        switch (subDevice.getTp()) {
            case 0:
                nuanqu.setVisibility(View.GONE);
                break;
            case 1:
                guangnuan.setVisibility(View.GONE);
                break;
            case 2:
                break;
            case 3:
                huanqu.setVisibility(View.GONE);
                break;
        }
    }

    private void init() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        ctrl_id = getIntent().getIntExtra("ctrl_id", -1);
        subDevice = (SubDevice) getIntent().getSerializableExtra("subdevice");
        dst = subDevice.getDst();
        device_id = subDevice.getGateway_id();
        mac = subDevice.getMac();
//        timePicker.setIs24HourView(true);
//        resizeTimerPicker(timePicker);
//        setNumberPickerTextSize(timePicker);
        List<String> hourlist = new ArrayList<>();
        List<String> minulist = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourlist.add(i + "");
        }
        for (int i = 0; i < 60; i++) {
            minulist.add(String.format("%02d", i));
        }
        loopv.setItems(hourlist);
        initLoopView(loopv);
        loopvM.setItems(minulist);
        initLoopView(loopvM);
//        hourw.setData(hourlist);
//        minw.setData(minulist);
//        hourw.setmMinTextSize(dp2px(15));
//        hourw.setmMaxTextSize(dp2px(20));
//        minw.setmMinTextSize(dp2px(15));
//        minw.setmMaxTextSize(dp2px(20));
//        hourw.setOnSelectListener(new PickerView.onSelectListener() {
//            @Override
//            public void onSelect(int position, String text) {
//                hour = Integer.parseInt(text);
//            }
//        });
//        minw.setOnSelectListener(new PickerView.onSelectListener() {
//            @Override
//            public void onSelect(int position, String text) {
//                minu = Integer.parseInt(text);
//            }
//        });
//        hourw.setAdapter(new NumericWheelAdapter(0,23));
//        minw.setAdapter(new NumericWheelAdapter(0,59,"%02d"));
//        hourw.TEXT_SIZE=dp2px(30);
//        minw.TEXT_SIZE=dp2px(30);
        if (ctrl_id == -1) {
            Calendar calendar = Calendar.getInstance();
            loopv.setInitPosition(calendar.get(Calendar.HOUR_OF_DAY));
            loopvM.setInitPosition(calendar.get(Calendar.MINUTE));
//            loopv.setSelected(calendar.get(Calendar.HOUR_OF_DAY));
//            minw.setSelected(calendar.get(Calendar.MINUTE));
            addTag = true;
            title.setText("添加定时");
            edit.setText("完成");
        } else {
            try {
                timer = App.db.selector(Timer.class).where("ctrl_id", "=", this.ctrl_id).findFirst();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (timer != null) {
                loopv.setInitPosition(timer.getTimer_time_exe() / 3600);
                loopvM.setInitPosition((timer.getTimer_time_exe() % 3600) / 60);
//                hourw.setSelected(timer.getTimer_time_exe() / 3600);
//                minw.setSelected((timer.getTimer_time_exe() % 3600) / 60);
                hour = timer.getTimer_time_exe() / 3600;
                minu = (timer.getTimer_time_exe() % 3600) / 60;
//                timePicker.setCurrentHour(timer.getTimer_time_exe() / 3600);
//                timePicker.setCurrentMinute((timer.getTimer_time_exe() % 3600) / 60);
                tap = timer.getVal();
                if (tap > 0) {
                    open.setChecked(true);
                    openLayer();
                }
                int w = timer.getTimer_week();
                String s = Integer.toBinaryString(w);
                s = new StringBuilder(s).reverse().toString();
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    String s1 = s.substring(i, i + 1);
                    if ("1".equals(s1)) {
                        checkBoxList.get(i).setChecked(true);
                        switch (i) {
                            case 0:
                                name.append("一、");
                                break;
                            case 1:
                                name.append("二、");
                                break;
                            case 2:
                                name.append("三、");
                                break;
                            case 3:
                                name.append("四、");
                                break;
                            case 4:
                                name.append("五、");
                                break;
                            case 5:
                                name.append("六、");
                                break;
                            case 6:
                                name.append("日");
                                break;
                        }
                    }
                }
                String name1 = name.toString();
                rateTv.setText(name1.endsWith("、") ? name1.substring(0, name1.length() - 1) : name1);
            }
        }
//        final String[] rateArray = getResources().getStringArray(R.array.rate_stringarray);
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                hour = hourOfDay;
//                minu = minute;
//            }
//        });
        for (int i = 0; i < checkBoxList.size(); i++) {
            checkBoxList.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    StringBuilder name = new StringBuilder();
                    for (int j = 0; j < checkBoxList.size(); j++) {
                        if (checkBoxList.get(j).isChecked()) {
                            switch (j) {
                                case 0:
                                    name.append("一、");
                                    break;
                                case 1:
                                    name.append("二、");
                                    break;
                                case 2:
                                    name.append("三、");
                                    break;
                                case 3:
                                    name.append("四、");
                                    break;
                                case 4:
                                    name.append("五、");
                                    break;
                                case 5:
                                    name.append("六、");
                                    break;
                                case 6:
                                    name.append("日");
                                    break;
                            }
                        }
                    }
                    String name1 = name.toString();
                    if (name1.length() == 0) {
                        name1 = "单次";
                    }
                    rateTv.setText(name1.endsWith("、") ? name1.substring(0, name1.length() - 1) : name1);
                }
            });
        }
//        weekPick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String s = rateArray[position];
//                rateTv.setText(s);
//                switch (position) {
//                    case 0:
//                        for (int i = 0; i < checkBoxList.size(); i++) {
//                            checkBoxList.get(i).setChecked(false);
//                        }
//                        break;
//                    case 1:
//                        for (int i = 0; i < checkBoxList.size(); i++) {
//                            if (i >= 0 && i < 5)
//                                checkBoxList.get(i).setChecked(true);
//                            else checkBoxList.get(i).setChecked(false);
//                        }
//                        break;
//                    case 2:
//                        for (int i = 0; i < checkBoxList.size(); i++) {
//                            checkBoxList.get(i).setChecked(true);
//                        }
//                        break;
//                    case 3:
//                        for (int i = 0; i < checkBoxList.size(); i++) {
//                            if (i >= 0 && i < 5)
//                                checkBoxList.get(i).setChecked(false);
//                            else checkBoxList.get(i).setChecked(true);
//                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void initLoopView(LoopView loopv) {
        loopv.setCenterTextColor(Color.WHITE);
        loopv.setItemsVisibleCount(7);
        loopv.setTextSize(23);
        loopv.setScaleX(0.8f);
        loopv.setDividerColor(Color.parseColor("#88ffffff"));
        loopv.setOuterTextColor(Color.parseColor("#88ffffff"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                hour=loopv.getSelectedItem();
                minu=loopvM.getSelectedItem();
                final StringBuffer week = new StringBuffer();
                for (int i = 0; i < checkBoxList.size(); i++) {
                    if (checkBoxList.get(i).isChecked()) {
                        week.append(1);
                    } else week.append(0);
                }
                String s = week.reverse().toString();
                if (addTag) {
                    Timer timer = new Timer();
                    timer.setMac(mac);
                    timer.setObj_id(dst);
                    timer.setTimer_week(Integer.parseInt(s, 2));
                    timer.setTimer_time_exe(hour * 3600 + minu * 60);
                    timer.setVal(tap);
                    List<Timer> list = new ArrayList<>();
                    list.add(timer);
                    String command = Command.getTimerOrLiandong(141, new Gson().toJson(list));
                    Command.sendData1(device_id, command.getBytes(), TAG);
                } else {
                    timer.setVal(tap);
                    timer.setTimer_week(Integer.parseInt(s, 2));
                    timer.setTimer_time_exe(hour * 3600 + minu * 60);
                    List<Timer> list = new ArrayList<>();
                    list.add(timer);
                    String command = Command.getTimerOrLiandong(142, new Gson().toJson(list));
                    Command.sendData1(device_id, command.getBytes(), TAG);
                }
                Command.sendData1(device_id, Command.getAll(Command.ALLTIMER).getBytes(), TAG);
                finish();
                break;
        }
    }

//    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
//        List<NumberPicker> npList = new ArrayList<NumberPicker>();
//        View child = null;
//
//        if (null != viewGroup) {
//            for (int i = 0; i < viewGroup.getChildCount(); i++) {
//                child = viewGroup.getChildAt(i);
//                if (child instanceof NumberPicker) {
//                    npList.add((NumberPicker) child);
//                } else if (child instanceof LinearLayout) {
//                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
//                    if (result.size() > 0) {
//                        return result;
//                    }
//                }
//            }
//        }
//
//        return npList;
//    }

//    private void resizeTimerPicker(TimePicker tp) {
//        List<NumberPicker> npList = findNumberPicker(tp);
//
//        for (NumberPicker np : npList) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(150), LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(dp2px(10), 0, dp2px(10), 0);
//            np.setGravity(Gravity.CENTER);
//            np.setLayoutParams(params);
//        }
//    }

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

//    private void set_numberpicker_text_colour(NumberPicker number_picker) {
//        final int count = number_picker.getChildCount();
//        //这里就是要设置的颜色，修改一下作为参数传入会更好
//        final int color = Color.WHITE;
//
//        for (int i = 0; i < count; i++) {
//            View child = number_picker.getChildAt(i);
//
//            try {
//                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
//                wheelpaint_field.setAccessible(true);
//                Paint paint = (Paint) wheelpaint_field.get(number_picker);
//                paint.setColor(color);
//                paint.setTextSize(dp2px(20));
//                ((EditText) child).setTextColor(Color.parseColor("#c2c4cc"));
//                ((EditText) child).setTextSize(dp2px(10));
//                number_picker.invalidate();
//            } catch (NoSuchFieldException e) {
//                Log.w("setColor", e);
//            } catch (IllegalAccessException e) {
//                Log.w("setColor", e);
//            } catch (IllegalArgumentException e) {
//                Log.w("setColor", e);
//            }
//        }
//    }

//    private void setNumberPickerTextSize(ViewGroup viewGroup) {
//        List<NumberPicker> npList = findNumberPicker(viewGroup);
//        if (null != npList) {
//            for (NumberPicker np : npList) {
//                set_numberpicker_text_colour(np);
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
