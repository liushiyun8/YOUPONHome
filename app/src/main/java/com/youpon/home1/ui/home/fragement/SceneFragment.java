package com.youpon.home1.ui.home.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.Command;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.tools.UpdateUI;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.MysceneListAdapter;
import com.youpon.home1.ui.scene.SceneAddActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.xlink.wifi.sdk.XDevice;

public class SceneFragment extends Fragment {
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.noscene)
    LinearLayout noscene;
    private List<Scenebean> lists = new ArrayList<>();
    private MysceneListAdapter mysceneListAdapter;
    private String TAG = getClass().getSimpleName();
    private List<Scenebean> fourthScene;
    private UpdateUI updateUI;
    private int i;
    private TimerTask task;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
        if (eventData.getTag() == EventData.REFRESHDB || eventData.getCode() == EventData.CODE_GETSCENE) {
            updateUI.updade();
        }
    }

    private void updateDate() {
        lists.clear();
            List<Panel> allScenepanel = PanelManage.getInstance().getAllScenepanel();
            for (int i = 0; i < fourthScene.size(); i++) {
                Scenebean scenebean = fourthScene.get(i);
                Scenebean first=null;
                try {
                    first = App.db.selector(Scenebean.class).where("panel_mac", "=", scenebean.getPanel_mac()).and("gateway_id","=",scenebean.getGateway_id()).and("groupId", "=", scenebean.getGroupId()).and("sceneId", "=", scenebean.getSceneId()).findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if(first!=null){
                    List<Scenebean.ActionsBean> list=new ArrayList<>();
                    for (int j = 0; j <allScenepanel.size() ; j++) {
                        Panel panel = allScenepanel.get(j);
                        if(panel.getGateway_id()!=first.getGateway_id())
                            continue;
                        List<Scenebean.ActionsBean> been = panel.getMap().get(first.getSceneId());
                        if (been!=null){
                            list.addAll(been);
                        }
                    }
                        first.setAction(list);
                        try {
                            App.db.update(first);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                }
        }
        lists.addAll(DbUtil.findMyScene());
        Log.e("lists111数量",lists.size()+"");
        for (int j = 0; j < lists.size(); j++) {
            Log.e("lists111内容",lists.get(j).toString());
        }

        if (lists.size() == 0) {
            noscene.setVisibility(View.VISIBLE);
        } else {
            noscene.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        ButterKnife.bind(this, view);
        updateUI = new UpdateUI() {
            @Override
            public void getData() {
                updateDate();
                mysceneListAdapter.notifyDataSetChanged();
            }
        };
        init();
        return view;
    }

    private void init() {
        fourthScene = PanelManage.getInstance().getFourthScene();
        for (int j = 0; j < fourthScene.size(); j++) {
            Scenebean scenebean = fourthScene.get(j);
            try {
                Scenebean first = App.db.selector(Scenebean.class).where("panel_mac", "=", scenebean.getPanel_mac()).and("gateway_id","=",scenebean.getGateway_id()).and("groupId", "=", scenebean.getGroupId()).and("sceneId", "=", scenebean.getSceneId()).findFirst();
                if(first==null){
                    HttpManage.getInstance().addSub(HttpManage.TYPE_SINGLE,HttpManage.SCENETABLE,new Gson().toJson(scenebean), new MyCallback() {
                        @Override
                        public void onSuc(String result) {
                           Scenebean sc = new Gson().fromJson(result, Scenebean.class);
                            try {
                                List<Scenebean> first = App.db.selector(Scenebean.class).where("panel_mac", "=", sc.getPanel_mac()).and("gateway_id","=",sc.getGateway_id()).and("groupId", "=", sc.getGroupId()).and("sceneId", "=", sc.getSceneId()).findAll();
                                if(first==null||first.size()==0){
                                    App.db.saveOrUpdate(sc);
                                }else
                                    HttpManage.getInstance().deleSub(sc.getObjectId(), HttpManage.SCENETABLE, new MyCallback() {
                                        @Override
                                        public void onSuc(String result) {

                                        }

                                        @Override
                                        public void onFail(int code, String msg) {

                                        }
                                    });

                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        updateDate();
        mysceneListAdapter = new MysceneListAdapter(getActivity(), lists);
        lv.setAdapter(mysceneListAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SceneAddActivity.class));
            }
        });
        //获取四位场景里面的设备状态
        byte[] bs2=Command.getAll(99).getBytes();
        List<Device> devices = DeviceManage.getInstance().getCurrentdev();
        for (Device de:devices) {
            Command.sendData(de.getXDevice(),bs2,TAG);
        }

        //获取网关上三个场景的内容
        List<Panel> panels = PanelManage.getInstance().get485Scenepanel();
        for (int i = 0; i < panels.size(); i++) {
            Panel panel = panels.get(i);
            Device device = DeviceManage.getInstance().getDevice(panel.getGateway_id());
            if(device!=null){
                if(device.isOnline()){
                    Command.sendData(device.getXDevice(), Command.getReadSceneStr("0000","00",panel.getId(),0).getBytes(), TAG);
                    Command.sendData(device.getXDevice(), Command.getReadSceneStr("0000","01",panel.getId(),0).getBytes(), TAG);
                    Command.sendData(device.getXDevice(), Command.getReadSceneStr("0000","02",panel.getId(),0).getBytes(), TAG);
                }

            }
        }

        //获取九位面板上两个场景的内容
        List<Panel> panels1 = PanelManage.getInstance().getNineScenepanel();
        for (int i = 0; i < panels1.size(); i++) {
            Panel panel = panels1.get(i);
            Device device = DeviceManage.getInstance().getDevice(panel.getGateway_id());
            if(device!=null){
                if(device.isOnline()){
                    Command.sendData(device.getXDevice(), Command.getReadSceneStr("0000","00",panel.getId(),1).getBytes(), TAG);
                    Command.sendData(device.getXDevice(), Command.getReadSceneStr("0000","01",panel.getId(),1).getBytes(), TAG);
                }

            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
