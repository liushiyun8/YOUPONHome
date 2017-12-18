package com.youpon.home1.comm.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.ui.index.Index2Activity;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.no)
    Button no;
    @BindView(R.id.yes)
    Button yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        String data = getIntent().getStringExtra("data");
        title.setText("下线通知");
        yes.setText("重新登陆");
        no.setText("退出");
        message.setTextSize(15);
        message.setText(data);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes:
                App.getSp().remove(Comconst.ISAUTO);
                sendBroadcast(new Intent("exit"));
                Intent startMain = new Intent(this,Index2Activity.class);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);//回到登录界面。
                finish();
                break;
            case R.id.no:
                App.getSp().remove(Comconst.ISAUTO);
                sendBroadcast(new Intent("exit"));
                finish();
                break;
        }
    }
}
