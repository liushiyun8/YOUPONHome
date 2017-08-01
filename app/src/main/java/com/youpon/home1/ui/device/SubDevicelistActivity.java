package com.youpon.home1.ui.device;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class SubDevicelistActivity extends BaseActivity {

    private ImageView back;
    private ListView lv;
    private List<SubDevice> list=new ArrayList<>();
    private CommonAdapter<SubDevice> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_devicelist);
        init();
    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        int device_id=getIntent().getIntExtra("device_id",0);
        try {
            List<SubDevice> subs = App.db.selector(SubDevice.class).where("gateway_id", "=", device_id).and("type","!=",0).findAll();
            if(subs!=null){
                list.addAll(subs);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        lv = (ListView) findViewById(R.id.lv);
        adapter = new CommonAdapter<SubDevice>(this, list, R.layout.myroom_item) {
            @Override
            public void convert(ViewHolder helper, int position, final SubDevice item) {
                ImageView icon = helper.getView(R.id.icon);
                icon.setVisibility(View.VISIBLE);
                icon.setImageResource(Comconst.IMAGETYPE[item.getTp()]);
                helper.setText(R.id.name,item.getName());
                helper.setText(R.id.count,item.getRoom());
                final Panel panel = PanelManage.getInstance().getPanel(item.getMac());
                if(panel!=null){
                    helper.setText(R.id.panel_name,panel.getName());
                    helper.getView(R.id.panel_name).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final MyDialog myDialog = new MyDialog(SubDevicelistActivity.this);
                            myDialog.setTitle("更改面板名称");
                            myDialog.setType(MyDialog.EDITTYPE);
                            myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                                @Override
                                public void onYesClick() {
                                    panel.setName(myDialog.getEditText());
                                    PanelManage.getInstance().updatePanel(panel);
                                    myDialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            });
                            myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                                @Override
                                public void onNoClick() {
                                    myDialog.dismiss();
                                }
                            });
                            myDialog.show();
                        }
                    });
                }
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubDevicelistActivity.this, DeviceDetailActivity.class);
                        intent.putExtra("device",item);
                        startActivity(intent);
                    }
                });
            }
        };
        lv.setAdapter(adapter);
    }
}
