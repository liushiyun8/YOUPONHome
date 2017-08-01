package com.youpon.home1.ui.home.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.comm.tools.SpUtils;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.MyExlvAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.draw_menu)
    ImageView drawMenu;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.relayout)
    RelativeLayout relayout;
    @BindView(R.id.exlv)
    PullToRefreshExpandableListView exlv;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.drawlay)
    DrawerLayout drawlay;
    @BindView(R.id.title)
    TextView title;
    private String TAG = "---DevFrag---";

    Animation anim;
    Handler handler;
    private static final int REFRESH_LIST = 0x10001;
    private SpUtils sp;
    private List<Object> myList=Collections.synchronizedList(new ArrayList<Object>());
    private List<List<SubDevice>> lists= Collections.synchronizedList(new ArrayList<List<SubDevice>>());
    private List<List<Sensor>> lists1= Collections.synchronizedList(new ArrayList<List<Sensor>>());
    private List<Panel> lists2= Collections.synchronizedList(new ArrayList<Panel>());
    private MyExlvAdapter myExlvAdapter;
    private List<Device> all1=new ArrayList<>();
    List<String> titles=new ArrayList<>();

    private ExpandableListView refreshableView;
    private String[] sensors;
    private String[] devices;
    private CommonAdapter<String> commonAdapter;
    private int selectPositon;
    private String[] allsort;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){
        if(eventData.getCode()==EventData.CODE_REFRESH_DEVICE){
            updatatDblist();
            myExlvAdapter.notifyDataSetChanged();
            commonAdapter.notifyDataSetChanged();
        }else if(eventData.getCode()==EventData.CODE_REFRESH_SENSOR){
            getSensorList();
            myExlvAdapter.notifyDataSetChanged();
            commonAdapter.notifyDataSetChanged();
        }
    }

    private void getPanelList(){
        lists2.clear();
        lists2.addAll(DbUtil.findMyPanel());
    }

    private void updatatDblist() {

        for (int i = 0; i< devices.length; i++) {
            lists.get(i).clear();
            lists.get(i).addAll(DbUtil.findMydevIndex(i));
        }

    }

    private void getSensorList(){
        for (int i = 0; i< sensors.length; i++){
            lists1.get(i).clear();
            lists1.get(i).addAll(DbUtil.findMySensorType(i));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void even(EventData eventdatas){
//        if(eventdatas.getTag()==EventData.TAG_GETDIVICE){
//                all1.clear();
//                all1.addAll(DeviceManage.getInstance().getCurrentdev());
//                getPanelList();
//                updatatDblist();
//                getSensorList();
//                myExlvAdapter.notifyDataSetChanged();
//                commonAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensors = getResources().getStringArray(R.array.sensor_sort);
        devices = getResources().getStringArray(R.array.device_sort);
        all1.clear();
        all1.addAll(DeviceManage.getInstance().getCurrentdev());
        getPanelList();
        myList.add(all1);
        myList.add(lists2);
        for (int i=0;i<devices.length;i++){
            List<SubDevice> list=Collections.synchronizedList(new ArrayList<SubDevice>());
            lists.add(list);
        }
        for(int i=0;i<sensors.length;i++){
            List<Sensor> list=Collections.synchronizedList(new ArrayList<Sensor>());
            lists1.add(list);
        }
        for (int i = 0; i < lists.size(); i++) {
            myList.add(lists.get(i));
        }
        for (int i = 0; i < lists1.size(); i++) {
            myList.add(lists1.get(i));
        }
        updatatDblist();
        getSensorList();
        titles.add("网关");
        titles.add("控制面板");
        for (int i = 0; i < devices.length; i++) {
            titles.add(devices[i]);
        }
        for (int i = 0; i < sensors.length; i++) {
            titles.add(sensors[i]);
        }
        myExlvAdapter = new MyExlvAdapter(getContext(),myList,titles);
        allsort = getResources().getStringArray(R.array.all_sort);
        final List<String> allsorts = Arrays.asList(allsort);
        commonAdapter = new CommonAdapter<String>(getActivity(), allsorts, R.layout.drawlist_item) {
            @Override
            public void convert(ViewHolder helper, int position, String item) {
                String name = allsorts.get(position);
                if(selectPositon==position){
                    helper.getView(R.id.ok).setVisibility(View.VISIBLE);
                }else {
                    helper.getView(R.id.ok).setVisibility(View.GONE);
                }
                int count = 0;
                int gate_size = all1.size();
                int devisor_size = 0;
                for (int i = 0; i < lists.size(); i++) {
                    List<SubDevice> list = lists.get(i);
                    devisor_size += list.size();
                }
                int sensors_size = 0;
                for (int i = 0; i < lists1.size(); i++) {
                    sensors_size += lists1.get(i).size();
                }
                if (position == 0) {
                    count = gate_size + devisor_size + sensors_size;
                }else if(position==1){
                    count=gate_size;
                }else if (position > 1 && position < 2 + lists.size()) {
                    count = lists.get(position - 2).size();
                } else if (position > lists.size()) {
                    count = lists1.get(position - lists.size() - 2).size();
                }
                helper.setText(R.id.name, name + "(" + count + ")");
            }
        };
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.devicelist_home, container, false);
        ButterKnife.bind(this,view);
        handler = new MyHandler();
        sp = App.getSp();
        initView(view);
        initClick();
        return view;
    }

    private void initView(View view) {
        all1.clear();
        all1.addAll(DeviceManage.getInstance().getCurrentdev());
        lv.setAdapter(commonAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPositon =position;
                title.setText(allsort[position]+"设备");
                String[] array = {allsort[position]};
                if(position==0){
                    refreshableView.setAdapter(myExlvAdapter);
                }else if (position==1){
                    MyExlvAdapter adapter = new MyExlvAdapter(getActivity(), null,Arrays.asList(array));
                    refreshableView.setAdapter(adapter);
                }else if(position>1&&position<lists.size()+2){
                    ArrayList<Object> lits = new ArrayList<>();
                    lits.add(lists.get(position-2));
                    MyExlvAdapter adapter = new MyExlvAdapter(getActivity(),lits,Arrays.asList(array));
                    refreshableView.setAdapter(adapter);
                }else if(position>lists.size()+1){
                    ArrayList<List<Sensor>> lits = new ArrayList<>();
                    lits.add(lists1.get(position-2-lists.size()));
                    MyExlvAdapter adapter = new MyExlvAdapter(getActivity(),null,Arrays.asList(array));
                    refreshableView.setAdapter(adapter);
                }
                commonAdapter.notifyDataSetChanged();
                refreshableView.expandGroup(0);
                drawlay.closeDrawers();
            }
        });
        anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.my_rotate);
        refreshableView = exlv.getRefreshableView();
        exlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                reload();
            }
        });
        refreshableView.setAdapter(myExlvAdapter);
        for (int i=0;i<1+lists.size()+lists1.size();i++){
            refreshableView.expandGroup(i);
        }
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
    }

    private void initClick() {
        drawMenu.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    private void reload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new EventData(EventData.TAG_REFRESH,""));
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
//                startActivity(new Intent(getActivity(),AddActivity.class));
                break;
            case R.id.draw_menu:
                drawlay.openDrawer(GravityCompat.START);
                break;

        }
    }


    //右下角的悬浮刷新按钮
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                exlv.onRefreshComplete();
            } else if (msg.what == 2) {
                reload();
            }
        }
    }

}
