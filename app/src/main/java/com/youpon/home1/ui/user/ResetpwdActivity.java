package com.youpon.home1.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;

import org.apache.http.Header;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetpwdActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.oldpwd)
    EditText oldpwd;
    @BindView(R.id.newpwd)
    EditText newpwd;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.dele)
    ImageView dele;
    @BindView(R.id.dele1)
    ImageView dele1;
    @BindView(R.id.show)
    ImageView show;
    private boolean Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        oldpwd.addTextChangedListener(new TextWatcher() {
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
                }else {
                    dele.setVisibility(View.GONE);
                }
            }
        });
        newpwd.addTextChangedListener(new TextWatcher() {
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
                        ok.setEnabled(true);
                }else {
                    ok.setEnabled(false);
                    dele1.setVisibility(View.GONE);
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpManage.getInstance().resetPassword(newpwd.getText().toString(), oldpwd.getText().toString(), new HttpManage.ResultCallback<Map<String, Object>>() {
                    @Override
                    public void onError(Header[] headers, HttpManage.Error error) {
                        MyLog.e("OOOO", error.getCode() + error.getMsg());
                        MyToast.show(ResetpwdActivity.this, 2, error.getMsg(), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onSuccess(int code, Map<String, Object> response) {
                        MyLog.e("GGGG", "OK");
                        MyToast.show(ResetpwdActivity.this, 1, "密码重置成功！", Toast.LENGTH_LONG);
                        finish();
                    }
                });
            }
        });
        dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpwd.setText("");
                v.setVisibility(View.GONE);
            }
        });
        dele1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newpwd.setText("");
                v.setVisibility(View.GONE);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Flag){
                    newpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_invis_nor);
                }else {
                    newpwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    show.setImageResource(R.mipmap.login_btn_visible_nor);
                }
                Flag=!Flag;
            }
        });
    }
}
