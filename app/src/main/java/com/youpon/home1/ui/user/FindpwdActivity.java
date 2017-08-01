package com.youpon.home1.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.Utils;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.comm.tools.StringUtils;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FindpwdActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.pwdlayout)
    LinearLayout pwdlayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.acount)
    EditText account;
    @BindView(R.id.dele)
    ImageView dele;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.yanzheng)
    LinearLayout yanzheng;
    @BindView(R.id.dele1)
    ImageView dele1;
    @BindView(R.id.show)
    ImageView show;
    private String number;
    private int flag;
    private SpUtils sp;
    private int accounttype =1;  //0代表邮箱账号 1代表手机账号 -1代表无效的账户
    private int time;
    private String TAG = getClass().getSimpleName();
    private boolean Flag;
    private String verifycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);
        ButterKnife.bind(this);
        iniEvnet();
        sp = App.getSp();
    }

    private void iniEvnet() {
        send.setOnClickListener(this);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        account.addTextChangedListener(new TextWatcher() {
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
                    if(StringUtils.isPhoneNum(s.toString())){
                        send.setEnabled(true);
                    }else send.setEnabled(false);
                    if(s.length()>11){
                        s.delete(11,s.length());
                    }
                } else {
                    send.setEnabled(false);
                    dele.setVisibility(View.GONE);
                }
            }
        });
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!"".equals(s.toString())&&s.length()==6){
                    next.setEnabled(true);
                }else next.setEnabled(false);
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
                    show.setVisibility(View.VISIBLE);
                    if(s.length()>=6){
                        next.setEnabled(true);
                    }else {
                        next.setEnabled(false);
                    }
                }else {
                    next.setEnabled(false);
                    show.setVisibility(View.GONE);
                    dele1.setVisibility(View.GONE);
                }
            }
        });
        dele.setOnClickListener(this);
        dele1.setOnClickListener(this);
        show.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.send:
                sendcode();
                break;
            case R.id.dele:
                account.setText("");
                break;
            case R.id.dele1:
                pwd.setText("");
                break;
            case R.id.show:
                if(Flag){
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_invis_nor);
                }else {
                    pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_visible_nor);
                }
                Flag =!Flag;
                break;
            case R.id.next:
                if (flag == 0) {
                    String s = account.getText().toString();
                    if (StringUtils.isPhoneNum(s)) {
                        accounttype = 1;
                    } else {
                        accounttype = -1;
                        XlinkUtils.shortTips("请输入正确的手机号");
                    }
                    checkcode();
                } else {
                   setPWD();
                }
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    private void setPWD() {
        String password = pwd.getText().toString().trim();
        HttpManage.getInstance().findPasswd(account.getText().toString().trim(),verifycode, password, new HttpManage.ResultCallback<Map<String, Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                XlinkUtils.longTips("重置失败");
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                XlinkUtils.longTips("重置成功");
                finish();
                startActivity(new Intent(FindpwdActivity.this, MainActivity.class));
            }
        });

//        miCOUser.resetPassword(password, new MiCOCallBack() {
//            @Override
//            public void onSuccess(String message) {
//                Toast.makeText(FindpwdActivity.this, message, Toast.LENGTH_LONG).show();
//                startActivity(new Intent(FindpwdActivity.this, MainActivity.class));
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                Toast.makeText(FindpwdActivity.this, message, Toast.LENGTH_LONG).show();
//            }
//        }, token);
    }

    private void checkcode() {
        String codenumber = code.getText().toString().trim();
        String phone = account.getText().toString().trim();
        HttpManage.getInstance().verifyCode(phone, codenumber, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    verifycode = jsonObject.optString("verifycode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                XlinkUtils.longTips("验证成功");
                updateUI();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                XlinkUtils.longTips("验证失败，请重新发送");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void updateUI() {
        title.setText("重置密码");
        next.setText("完成");
        next.setEnabled(false);
        sp.put(Comconst.NAME, account.getText().toString());
        sp.put(Comconst.PWD, "");
        flag = 1;
        yanzheng.setVisibility(View.GONE);
        pwdlayout.setVisibility(View.VISIBLE);
    }

    private void updateUI2() {
        time=60;
        send.setEnabled(false);
       send.postDelayed(new Runnable() {
           @Override
           public void run() {
              send.setText(time+"s");
               time--;
               if(time<0){
                   send.setEnabled(true);
                   send.setText("重发");
                   return;
               }else send.setEnabled(false);
               send.postDelayed(this,1000);
           }
       },1000);
    }

    private void sendcode() {
        number = account.getText().toString().trim();
        HttpManage.getInstance().forgetPasswd(number, accounttype, new HttpManage.ResultCallback<Map<String, Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                MyToast.show(FindpwdActivity.this,MyToast.TYPE_ERROR, "验证码发送失败", Toast.LENGTH_LONG);
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                MyToast.show(FindpwdActivity.this,MyToast.TYPE_OK, "已发送验证码", Toast.LENGTH_LONG);
                Log.e(TAG,code+"");
                updateUI2();
            }
        });
//        miCOUser.getVerifyCode(number, Comconst.APPID, new MiCOCallBack() {
//            @Override
//            public void onSuccess(String message) {
//                Toast.makeText(FindpwdActivity.this, message, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                Toast.makeText(FindpwdActivity.this, message, Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
