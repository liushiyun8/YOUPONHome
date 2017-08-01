package com.youpon.home1.ui.device;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.WangAdapter;

import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WangActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.lv)
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wang);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        List<Device> all = DeviceManage.getInstance().getCurrentdev();
        WangAdapter wangAdapter = new WangAdapter(all, this);
        lv.setAdapter(wangAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
