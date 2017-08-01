package com.youpon.home1.ui.home.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareDeviceActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.check_all)
    CheckBox checkAll;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.checkLayout)
    LinearLayout checkLayout;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.sure)
    TextView sure;
    private CommonAdapter<Device> commonAdapter;
    private boolean moveMode = true;
    private List<Device> selectList = new ArrayList();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_device);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        final List<Device> list = DeviceManage.getInstance().getCurrentdev();
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("正在生成二维码");
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectList.clear();
                    selectList.addAll(list);
                } else {
                    selectList.clear();
                }
                commonAdapter.notifyDataSetChanged();
            }
        });
        commonAdapter = new CommonAdapter<Device>(this, list, R.layout.myroom_item) {
            @Override
            public void convert(ViewHolder helper, int position, final Device item) {
                helper.getView(R.id.more).setVisibility(View.GONE);
                helper.getView(R.id.panel_name).setVisibility(View.GONE);
                CheckBox box = helper.getView(R.id.check);
                if (moveMode) {
                    box.setVisibility(View.VISIBLE);
                } else box.setVisibility(View.GONE);
                box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (!selectList.contains(item))
                                selectList.add(item);
                        } else {
                            if (selectList.contains(item))
                                selectList.remove(item);
                        }
                        if (selectList.size() > 0) {
                            sure.setEnabled(true);
                        }else sure.setEnabled(false);
                        count.setText(selectList.size() + "");
                    }
                });
                if (selectList.contains(item)) {
                    box.setChecked(true);
                } else box.setChecked(false);
                ImageView icon = helper.getView(R.id.icon);
                icon.setVisibility(View.VISIBLE);
                icon.setImageResource(R.mipmap.equ_ic_gateway);
                helper.setText(R.id.count,item.getRoom());
                TextView name = helper.getView(R.id.name);
                name.setText(item.getName());
            }
        };
        lv.setAdapter(commonAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                dialog.show();
                for (int i = 0; i < selectList.size(); i++) {
                    Device device = selectList.get(i);
                    final List<String> list=new ArrayList<>();
                    HttpManage.getInstance().shareDevice(device.getXDevice().getDeviceId(), new MyCallback() {
                        @Override
                        public void onSuc(String result) {
                            Log.e("TAG",result);
                            list.add(result);
                            if(list.size()>=selectList.size()){
                                dialog.dismiss();
                                Intent intent = new Intent(ShareDeviceActivity.this, ShareQCodeActivity.class);
                                intent.putExtra("code",new Gson().toJson(list));
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            dialog.dismiss();
                            Toast.makeText(ShareDeviceActivity.this,"生成失败..."+msg,Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
        }
    }
}
