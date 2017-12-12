package com.youpon.home1.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.User;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.Constant;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.comm.tools.StringUtils;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.http.Net2db;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.home.activities.DeviceMainActivity;

import org.apache.http.Header;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.xlink.wifi.sdk.XlinkAgent;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.acount)
    EditText acount;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.sure)
    Button sure;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.dele)
    ImageView dele;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.dele1)
    ImageView dele1;
    @BindView(R.id.show)
    ImageView show;
    private String pwd1;
    private String loginname;
    private SpUtils sp;
    private List<User> all;
    private List<Map<String, String>> list = new ArrayList<>();
    private boolean Flag;
    private boolean isRun;
    private String TAG=getClass().getSimpleName();
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRun = true;
        ButterKnife.bind(this);
        sp = App.getSp();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initEvent();
        initData();
    }

    private void initData() {
        if (sp.get(Comconst.NAME, "") != "") {
            acount.setText(sp.get(Comconst.NAME, "").toString());
        }
    }

    private void initEvent() {
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
//        sure.setFocusable(true);
//        sure.setFocusableInTouchMode(true);
//        sure.requestFocus();
        forget.setOnClickListener(this);
        register.setOnClickListener(this);
        if (all != null && all.size() > 0) {
            for (int i = 0; i < all.size(); i++) {
                User user = all.get(i);
                String name = user.getName();
                String pwd = user.getPwd();
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("pwd", pwd);
                list.add(map);
            }
        }
        acount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(s.toString())) {
                    dele.setVisibility(View.VISIBLE);
                    if(!"".equals(pwd.getText().toString())&&pwd.getText().length()>=6){
                        sure.setEnabled(true);
                    }
                    if(StringUtils.isPhoneNum(s.toString())){
                        MyLog.e(TAG,"被调用了");
                        acount.clearFocus();
                        pwd.requestFocus();
                        pwd.performClick();
                    }
                    if(s.length()>11){
                        s.delete(11,s.length());
                    }
                }else {
                    sure.setEnabled(false);
                    dele.setVisibility(View.GONE);
                }
            }
        });
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(s.toString())) {
                    dele1.setVisibility(View.VISIBLE);
                    if(s.length()>=6)
                    sure.setEnabled(true);
                }else {
                    sure.setEnabled(false);
                    dele1.setVisibility(View.GONE);
                }
            }
        });
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, android.R.layout.test_list_item, new String[]{"name"}, new int[]{android.R.id.text1});
        lv.setAdapter(simpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acount.setText(list.get(position).get("name"));
                pwd.setText(list.get(position).get("pwd"));
                lv.setVisibility(View.GONE);
            }
        });
        dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acount.setText("");
                v.setVisibility(View.GONE);
            }
        });
        dele1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd.setText("");
                v.setVisibility(View.GONE);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Flag){
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_invis_nor);
                }else {
                    pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_visible_nor);
                }
                Flag=!Flag;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                 break;
            case R.id.sure:
                MyLog.e(TAG,"sure被点击");
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                login();
                break;
            case R.id.forget:
                startActivity(new Intent(this, FindpwdActivity.class));
                break;
            case R.id.register:
                regis();
                break;
        }
    }

    private void regis() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void login() {
        loginname = acount.getText().toString().trim();
        pwd1 = pwd.getText().toString().trim();
        HttpManage.getInstance().login(loginname, pwd1, new HttpManage.ResultCallback<Map<String, Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                //用户验证失败， 错误码提示见 </a>《错误码说明》 </a>文档
                Toast.makeText(MainActivity.this,"登录失败", Toast.LENGTH_SHORT).show();
                XlinkUtils.shortTips(error.getMsg()+"");
                MyLog.e("HHHHE", error.getMsg()+"");
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                MyLog.e("HHHHS", response.toString());
                //验证成功， 解析返回的 JSON 获取"user_id"、"access_token"、"refresh_token"、"authorize"并保存
                String authKey = (String) response.get("authorize");
                String accessToken = (String) response.get("access_token");
                String refreshToken=(String) response.get("refresh_token");
                int appid = ((Double) response.get("user_id")).intValue();
                sp.put(Comconst.USERID, appid);
                sp.put("authKey", authKey);
                sp.put(Comconst.NAME, loginname);
                sp.put(Comconst.PWD, pwd1);
                sp.put(Comconst.ISAUTO, true);
                Comconst.CURRENTUSER = appid;
                App.getApp().setAccessToken(accessToken);
                App.getApp().setRefreshToken(refreshToken);
                App.getApp().setAppid(appid);
                App.getApp().setAuth(authKey);
                App.db=null;
                App.db = x.getDb(new DbManager.DaoConfig().setDbDir(new File("liuyun/" + appid)).setDbName("youpon"+appid).setDbVersion(1).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        try {
                            db.dropDb();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }));
                User user = new User(appid, loginname, pwd1);
                try {
                    App.db.replace(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                Net2db.getInstance().saveAll(new Net2db.Listener() {
                    @Override
                    public void complete() {
                        openDeviceMainActivity();
                    }
                });
            }
        });
//        micoUser.login(loginname, pwd1, appid, new MiCOCallBack() {
//            @Override
//            public void onSuccess(String message) {
//                MyLog.e("TTTAG", message);
//                if (message.contains("token")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(message);
//                        String token = jsonObject.optString(Comconst.TOKEN);
//                        String clinetid = jsonObject.optString(Comconst.CLIENTID);
//                        sp.put(Comconst.TOKEN, token);
//                        sp.put(Comconst.CLIENTID, clinetid);
//                        sp.put(Comconst.NAME,loginname);
//                        sp.put(Comconst.PWD, pwd1);
//                        sp.put(Comconst.ISREMBER, remember.isChecked());
//                        sp.put(Comconst.ISAUTO, auto.isChecked());
//                        Comconst.CURRENTUSER=loginname;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.loginsuccess), Toast.LENGTH_LONG).show();
//                    User user = new User(loginname, pwd1, remember.isChecked(), auto.isChecked());
//                    try {
//                        App.db.replace(user);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                    startActivity(new Intent(MainActivity.this, DeviceMainActivity.class));
//                    finish();
//                } else if (message.contains("meta")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(message);
//                        JSONObject meta = jsonObject.optJSONObject("meta");
//                        String message1 = meta.optString("message");
//                        Toast.makeText(MainActivity.this, message1, Toast.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                MyLog.e("TTTG", message);
//                Toast.makeText(MainActivity.this, code + " " + message, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void openDeviceMainActivity() {

        if (isRun) {// 防止重复打开2个DeviceListActivity界面
            if (!XlinkAgent.getInstance().isConnectedLocal()) {
                XlinkAgent.getInstance().start();
            }
            if (!XlinkAgent.getInstance().isConnectedOuterNet()) {
                XlinkAgent.getInstance().login(App.getApp().getAppid(), App.getApp().getAuth());
            }
            isRun = false;
            // openActivity(MainActivity.class);
            Intent intent = new Intent(this, DeviceMainActivity.class);
            startActivity(intent);
            PanelManage.getInstance().reload();
            finish();
            // openActivity(DeviceListActivity.class);
        }
    }
}
