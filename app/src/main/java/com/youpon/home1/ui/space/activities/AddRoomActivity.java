package com.youpon.home1.ui.space.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youpon.home1.R;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRoomActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.dele)
    ImageView dele;
    private String TAG = getClass().getSimpleName();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        dele.setOnClickListener(this);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    save.setEnabled(true);
                    dele.setVisibility(View.VISIBLE);
                }else {
                    save.setEnabled(false);
                    dele.setVisibility(View.GONE);
                }
                if(s.length()>10){
                    XlinkUtils.shortTips("最多只能输入10个字符");
                    s.delete(10,s.length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.dele:
                name.setText("");
                break;
            case R.id.save:
                try {
                    final Roombean entity = new Roombean(name.getText().toString().trim());
                    App.db.save(entity);
                    HttpManage.getInstance().addSub(HttpManage.TYPE_SINGLE, "Roombean", new Gson().toJson(entity), new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, "保存数据成功：" + entity.getName());
                            Roombean roombean = new Gson().fromJson(result, Roombean.class);
                            try {
                                App.db.replace(roombean);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
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
                    EventBus.getDefault().post(new EventData(EventData.REFRESHDB, "成功"));
                    MyToast.show(this,MyToast.TYPE_OK,"空间添加成功",1);
                    finish();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
