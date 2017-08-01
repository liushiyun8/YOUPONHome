package com.youpon.home1.ui.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.comm.tools.StringUtils;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;

import org.apache.http.Header;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.send)
    Button getcode;
    @BindView(R.id.check)
    Button check;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.dele)
    ImageView dele;
    @BindView(R.id.dele1)
    ImageView dele1;
    @BindView(R.id.show)
    ImageView show;
    @BindView(R.id.agree)
    CheckBox agree;
    @BindView(R.id.xieyu)
    TextView xieyu;
    private SpUtils sp;
    private String number;
    private int time;
    private boolean Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        iniEvnet();
        sp = App.getSp();
    }

    private void iniEvnet() {
        back.setOnClickListener(this);
        getcode.setOnClickListener(this);
        check.setOnClickListener(this);
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
                        getcode.setEnabled(true);
                        account.clearFocus();
                        code.requestFocus();
                        code.performClick();
                    }else getcode.setEnabled(false);
                    if(s.length()>11){
                        s.delete(11,s.length());
                    }
                } else {
                    getcode.setEnabled(false);
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
                if(!"".equals(s.toString())&&s.length()>=6){
                    code.clearFocus();
                    pwd.requestFocus();
                    pwd.performClick();
                    if(s.length()>6){
                        s.delete(6,s.length());
                    }
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
                    if(s.length()>=6){
                        check.setEnabled(true);
                    }
                }else {
                    check.setEnabled(false);
                    dele1.setVisibility(View.GONE);
                }
            }
        });
        dele.setOnClickListener(this);
        dele1.setOnClickListener(this);
        show.setOnClickListener(this);
//        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                String s = account.getText().toString();
//                if (!hasFocus) {
//                    if (s != null && "".equals(s)) {
//                        account.setHint("请先输入账号");
//                        account.setHintTextColor(Color.RED);
//                        account.requestFocus();
//                    } else {
//                        if (!StringUtils.isPhoneNum(s)){
//                            new AlertDialog.Builder(RegisterActivity.this).setCancelable(false).setMessage("请输入正确的手机号").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    account.requestFocus();
//                                }
//                            }).show();
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.send:
                sendcode();
                break;
            case R.id.check:
                if(!agree.isChecked()){
                    XlinkUtils.shortTips("请先同意友邦智能协议");
                }else
                registByPhone();
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
        }
    }

    private void registByPhone() {
        HttpManage.getInstance().registerUserByPhone(account.getText().toString().trim(), "友邦小智", code.getText().toString().trim(), pwd.getText().toString().trim(), new HttpManage.ResultCallback<Map<String, Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                Log.e("register", error.getCode() + ";" + error.getMsg());
                Toast.makeText(RegisterActivity.this, error.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                sp.put(Comconst.NAME, account.getText().toString());
                sp.put(Comconst.PWD, pwd.getText().toString().trim());
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

        });
    }

    private void registByEmail() {
        HttpManage.getInstance().registerUserByMail(account.getText().toString().trim(), "友邦小智", pwd.getText().toString().trim(), new HttpManage.ResultCallback<Map<String, Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                Toast.makeText(RegisterActivity.this, error.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                Toast.makeText(RegisterActivity.this, "注册成功，请到邮箱激活账户！", Toast.LENGTH_SHORT).show();
                sp.put(Comconst.NAME, account.getText().toString());
                sp.put(Comconst.PWD, pwd.getText().toString().trim());
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

        });
    }

    private void sendcode() {
        time = 60;
        number = account.getText().toString().trim();
        HttpManage.getInstance().getCode(number, null, null, new HttpManage.ResultCallback<Map<String, Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                MyToast.show(RegisterActivity.this,MyToast.TYPE_ERROR, "验证码发送失败", Toast.LENGTH_LONG);
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                MyToast.show(RegisterActivity.this,MyToast.TYPE_OK, "已发送验证码", Toast.LENGTH_LONG);
                getcode.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getcode.setText(time + "s");
                        time--;
                        if (time < 0) {
                            getcode.setEnabled(true);
                            getcode.setText("重发");
                            return;
                        }
                        getcode.postDelayed(this, 1000);
                    }
                }, 1000);
            }
        });
    }
}
