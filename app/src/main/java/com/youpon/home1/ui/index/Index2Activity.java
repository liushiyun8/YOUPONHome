package com.youpon.home1.ui.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.youpon.home1.R;
import com.youpon.home1.ui.user.MainActivity;
import com.youpon.home1.ui.user.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Index2Activity extends AppCompatActivity {

    @BindView(R.id.register)
    Button register;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.myLog)
    ImageView myLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index2);
        ButterKnife.bind(this);
//        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setRepeatCount(-1);
//        rotate.setDuration(5000);
//        LinearInterpolator linearInterpolator = new LinearInterpolator();
//        rotate.setInterpolator(linearInterpolator);
//        myLog.startAnimation(rotate);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Index2Activity.this, RegisterActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Index2Activity.this, MainActivity.class));
                finish();
            }
        });
    }
}
