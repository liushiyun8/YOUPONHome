package com.youpon.home1.ui.device;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LianDongDevActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.save)
    TextView save;
    private int device_id;
    List<SubDevice> myDvs=new ArrayList<>();
    private CommonAdapter<SubDevice> commonAdapter;
    private List<SubDevice> addLs=new ArrayList();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EventData eventData) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lian_dong_dev);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        device_id = getIntent().getIntExtra("device_id", 0);
        try {
            List<SubDevice> subs = App.db.selector(SubDevice.class).where("gateway_id", "=", device_id).findAll();
            if(subs!=null){
                myDvs.addAll(subs);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        commonAdapter = new CommonAdapter<SubDevice>(this,myDvs, R.layout.myroom_item) {
            @Override
            public void convert(ViewHolder helper, int position, final SubDevice item) {
                helper.getView(R.id.more).setVisibility(View.GONE);
                CheckBox box = helper.getView(R.id.check);
                box.setVisibility(View.VISIBLE);
                box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            if(!addLs.contains(item))
                                addLs.add(item);
                        }else {
                            if (addLs.contains(item))
                                addLs.remove(item);
                        }
                        notifyDataSetChanged();
                    }
                });
                if (addLs.size()==0){
                    box.setEnabled(true);
                }else box.setEnabled(false);
                if(addLs.contains(item)){
                    box.setChecked(true);
                    box.setEnabled(true);
                }else box.setChecked(false);
                ImageView icon = helper.getView(R.id.icon);
                final Panel panel = PanelManage.getInstance().getPanel(item.getMac());
                if(panel!=null){
                    helper.setText(R.id.panel_name,panel.getName());
                    helper.getView(R.id.panel_name).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final MyDialog myDialog = new MyDialog(LianDongDevActivity.this);
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
                icon.setVisibility(View.VISIBLE);
                TextView name = helper.getView(R.id.name);
                icon.setImageResource(Comconst.IMAGETYPE[item.getTp()]);
                name.setText(item.getName());
                helper.setText(R.id.count,item.getRoom());
            }
        };
        lv.setAdapter(commonAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:
                EventBus.getDefault().post(new EventData(EventData.TRANSDATA,addLs));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
