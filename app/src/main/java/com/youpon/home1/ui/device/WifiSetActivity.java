package com.youpon.home1.ui.device;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.comm.view.CircleProgress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fog.fog2sdk.MiCODevice;
import io.fogcloud.easylink.helper.EasyLinkCallBack;

public class WifiSetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.acount)
    TextView acount;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.dele1)
    ImageView dele1;
    @BindView(R.id.show)
    ImageView show;
    @BindView(R.id.set)
    TextView set;
    @BindView(R.id.cirPro)
    CircleProgress cirPro;
    @BindView(R.id.settinglayout)
    LinearLayout settinglayout;
    @BindView(R.id.wifiSetlayout)
    LinearLayout wifiSetlayout;
    @BindView(R.id.setlay)
    LinearLayout setlay;

    private MiCODevice miCODevice;
    private boolean Flag;
    private int value;
    private boolean SetFlag;
    private InputMethodManager imm;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_set);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setListenerToRootView();
        init();
    }

    private void init() {
        miCODevice = new MiCODevice(this);
        cirPro.setCircleBackgroud(Color.WHITE).setProdressWidth(cirPro.dp2px(10)).setPreProgress(Color.GRAY).setProgress(Color.parseColor("#f6ab00")).setStartAngle(-90).setTextColor(Color.BLACK).setPaddingscale(1).setTextSize(cirPro.dp2px(16));
        back.setOnClickListener(this);
        set.setOnClickListener(this);
        dele1.setOnClickListener(this);
        show.setOnClickListener(this);
        show.setVisibility(View.VISIBLE);
        acount.setText(miCODevice.getSSID());
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(s)) {
                    dele1.setVisibility(View.VISIBLE);
                    if (s.length() >= 8)
                        set.setEnabled(true);
                    else {
                        set.setEnabled(false);
                    }
                } else {
                    dele1.setVisibility(View.GONE);
                    set.setEnabled(false);
                }
            }
        });

    }

    private void setListenerToRootView() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, "[onGlobalLayout] .. in ..");
                boolean mKeyboardUp = isKeyboardShown(rootView);
                if (mKeyboardUp) {
                    Log.e(TAG, "键盘弹出..");
                    setlay.setPadding(0,0,0,DeviceUtils.dip2px(10));
                } else {
                    Log.e(TAG, "键盘收起..");
                    setlay.setPadding(0,0,0, DeviceUtils.dip2px(60));
                }
            }
        });
    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight =100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff =dm.heightPixels-r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (SetFlag) {
                    stopEasyLink();
                }
                finish();
                break;
            case R.id.dele1:
                pwd.setText("");
                v.setVisibility(View.GONE);
                break;
            case R.id.show:
                if (Flag) {
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_invis_nor);
                } else {
                    pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_visible_nor);
                }
                Flag = !Flag;
                break;
            case R.id.set:
                SetFlag = !SetFlag;
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (SetFlag) {
                    startEasyLink();
                } else {
                    stopEasyLink();
                }
                break;
        }
    }

    private void stopEasyLink() {
        value = 100;
        miCODevice.stopEasyLink(new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                Log.e("success", message);
                Toast.makeText(WifiSetActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e("fail", message);
                Toast.makeText(WifiSetActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
        settinglayout.setVisibility(View.GONE);
        wifiSetlayout.setVisibility(View.VISIBLE);
        set.setText("重新配置");
    }

    private void startEasyLink() {
        value = 0;
        miCODevice.startEasyLink(miCODevice.getSSID(), pwd.getText().toString().trim(), 40000, 20, "", "", new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                Log.e("success", message);
//                Toast.makeText(WifiSetActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e("fail", message);
//                Toast.makeText(WifiSetActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
        settinglayout.setVisibility(View.VISIBLE);
        wifiSetlayout.setVisibility(View.GONE);
        set.setText("停止配置");
        set.postDelayed(new Runnable() {
            @Override
            public void run() {
                cirPro.setValue(value);
                value++;
                if (value >= 100) {
                    settinglayout.setVisibility(View.GONE);
                    wifiSetlayout.setVisibility(View.VISIBLE);
                    SetFlag = false;
                    set.setText("重新配置");
                    return;
                }
                set.postDelayed(this, 300);
            }
        }, 300);
        recieveData();
    }

    private void recieveData() {
        try {
        final DatagramSocket udpSocket = new DatagramSocket();
        udpSocket.setBroadcast(true);
        final byte[] buffer=new byte[1024];
        new Thread(){

            @Override
            public void run() {
                while (true){
                    try {
                        Log.e("TAG","到这来");
                        udpSocket.receive(new DatagramPacket(buffer,1024));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("TAG",new String(buffer).trim().toString());
                    // Log.d("UDP_SEND", "--------" + Integer.toHexString(length) +"   "
                    // + port + "--------");

                }
            }
        }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
