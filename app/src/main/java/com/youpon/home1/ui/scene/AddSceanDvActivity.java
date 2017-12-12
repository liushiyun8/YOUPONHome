package com.youpon.home1.ui.scene;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.SceneDevice;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddSceanDvActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.save)
    TextView save;
    private List<SubDevice> myDvs;
    private CommonAdapter<SubDevice> commonAdapter;
    private View view;
    private PopupWindow popupWindow;
    List<SubDevice> addLs=new ArrayList<>();
    List<SubDevice> dates;
    private Scenebean scenebean;
    private int gateway_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scean_dv);
        ButterKnife.bind(this);
        view = LayoutInflater.from(this).inflate(R.layout.popup_view, null);
        popupWindow = new PopupWindow(view, (int) (App.mWidth * 2 / 3), (int) (App.mHeight /3), true);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        dates = (List<SubDevice>) getIntent().getSerializableExtra("data");
        initData();
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
                            if(gateway_id==0){
                                gateway_id =item.getGateway_id();
                                notifyDataSetChanged();
                            }
                            if(!addLs.contains(item))
                                addLs.add(item);
                        }else {
                            if (addLs.contains(item))
                                addLs.remove(item);
                            if(addLs.size()==0){
                                gateway_id=0;
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
                if(gateway_id!=0&&item.getGateway_id()!=gateway_id){
                    box.setEnabled(false);
                }else box.setEnabled(true);
                if(addLs.contains(item)){
                    box.setChecked(true);
                }else box.setChecked(false);
                ImageView icon = helper.getView(R.id.icon);
                icon.setVisibility(View.VISIBLE);
                TextView name = helper.getView(R.id.name);
                icon.setImageResource(Comconst.IMAGETYPE[item.getTp()]);
                name.setText(item.getName());
                helper.setText(R.id.count,item.getRoom());
                Panel panel = PanelManage.getInstance().getPanel(item.getMac());
                helper.setText(R.id.panel_name,panel==null?"":panel.getMyName());
                }
        };
        lv.setAdapter(commonAdapter);
    }

    private void initData() {
            myDvs = DbUtil.findMyDv();
            if(dates!=null){
                for (int i = 0; i < dates.size(); i++) {
                    SubDevice subDevice = dates.get(i);
                    for (int j = 0; j < myDvs.size(); j++) {
                        SubDevice sub1 = myDvs.get(j);
                        if(sub1.getUnique().equals(subDevice.getUnique())){
                            addLs.add(subDevice);
                            break;
                        }
                    }
                }
            }
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
}
