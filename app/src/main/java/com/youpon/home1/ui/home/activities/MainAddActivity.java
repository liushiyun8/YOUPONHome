package com.youpon.home1.ui.home.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.youpon.home1.R;
import com.youpon.home1.bean.Devall;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.MainBean;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.MyMainExAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAddActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.save)
    TextView save;
    List<List<Object>> lists=new ArrayList<>();
    private List<Devall> devallList =new ArrayList<>();
    private List<Devall> devallChecked =new ArrayList<>();
    private CommonAdapter<Devall> commonAdapter;
    private String TAG=getClass().getSimpleName();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void init() {
                devallList.addAll(DbUtil.findMyScene());
                devallList.addAll(DbUtil.findMyDv());
                devallList.addAll(DbUtil.findMySensor());
        commonAdapter = new CommonAdapter<Devall>(this, devallList, R.layout.main_addex_item) {
            @Override
            public void convert(ViewHolder helper, int position, final Devall item) {
                helper.setText(R.id.name,item.getName());
                helper.setText(R.id.room,item.getRoom());
                CheckBox check = helper.getView(R.id.check);
                check.setOnCheckedChangeListener(null);
                MainBean mainBean=null;
                try {
                   mainBean = App.db.selector(MainBean.class).where("sid", "=", item.getSID()).findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if(mainBean!=null){
                    check.setEnabled(false);
                    check.setSelected(true);
                }else {
                    check.setEnabled(true);
                    check.setSelected(false);
                }
                if(devallChecked.contains(item)){
                    check.setChecked(true);
                }else check.setChecked(false);
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            if(!devallChecked.contains(item)){
                                devallChecked.add(item);
                            }
                        }else {
                            if(devallChecked.contains(item))
                                devallChecked.remove(item);
                        }
                    }
                });
                if(item instanceof Scenebean){
                      helper.setImageResource(R.id.icon,R.mipmap.equ_ic_scene);
                      Scenebean sc= (Scenebean) item;
                    Device device = DeviceManage.getInstance().getDevice(sc.getGateway_id());
                    helper.setText(R.id.panel_name,device==null?"":device.getName());
                }else if(item instanceof SubDevice){
                    SubDevice subDevice= (SubDevice) item;
                    Panel panel = PanelManage.getInstance().getPanel(subDevice.getMac());
                    helper.setText(R.id.panel_name,panel==null?"":panel.getName());
                    helper.setImageResource(R.id.icon,Comconst.IMAGETYPE[subDevice.getTp()]);
                }else if(item instanceof Sensor){
                    Sensor sensor= (Sensor) item;
                    Device device = DeviceManage.getInstance().getDevice(sensor.getDevice_id());
                    helper.setText(R.id.panel_name,device==null?"":device.getName());
                    helper.setImageResource(R.id.icon,Comconst.SENSORTYPE[sensor.getType()-1]);
                }
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
                List<MainBean> list=new ArrayList<>();
                try {
                    for (int i = 0; i < devallChecked.size(); i++) {
                        Devall devall = devallChecked.get(i);
                        devall.setMain(true);
                        App.db.replace(devall);
                        list.add(new MainBean(devall.getSort(), devall.getSID(), devall.getType()));
                    }
                    if(list.size()>0)
                    HttpManage.getInstance().addSub(HttpManage.TYPE_MORE, "MainBean", new Gson().toJson(list), new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG,"上传首页数据成功"+result);
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                    MainBean mainBean= new Gson().fromJson(jsonObject1.toString(), MainBean.class);
                                    App.db.replace(mainBean);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            EventBus.getDefault().post(new EventData("maindata",""));
                            finish();
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            ex.printStackTrace();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                    else {
                        EventBus.getDefault().post(new EventData("maindata",""));
                        finish();
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
//                EventBus.getDefault().post(new EventData("maindata",""));
//                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
