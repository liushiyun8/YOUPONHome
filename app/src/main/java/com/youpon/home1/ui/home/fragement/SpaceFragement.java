package com.youpon.home1.ui.home.fragement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.bean.Devall;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.tools.UpdateUI;
import com.youpon.home1.comm.view.MyListView;
import com.youpon.home1.ui.adpter.ShebeiAdapter;
import com.youpon.home1.ui.adpter.SpaceAdapter;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;
import com.youpon.home1.ui.home.activities.DeviceMainActivity;
import com.youpon.home1.ui.space.activities.RoomsetActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by computer on 2016/11/26.
 */
public class SpaceFragement extends Fragment implements View.OnClickListener {

    //    @BindView(R.id.drawset)
//    ImageView drawset;
//    @BindView(R.id.drawlv)
//    ListView drawlv;
//    @BindView(R.id.draw_menu)
//    ImageView drawMenu;
    @BindView(R.id.shezi)
    ImageView shezi;
    @BindView(R.id.lv)
    ListView lv;
//    @BindView(R.id.drawlayout)
//    DrawerLayout drawerLayout;

    List<Devall> lists = new ArrayList<>();
    List<Devall> sbList = new ArrayList<>();
    List<String> roombeanList = new ArrayList<>();
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_more)
    ImageView titleMore;
    @BindView(R.id.no_device)
    RelativeLayout noDevice;
    @BindView(R.id.titleBar)
    RelativeLayout titleBar;
    private DeviceMainActivity activity;
    private ShebeiAdapter spaceAdapter;
    private String s = "所有空间";
    private PopupWindow popupWindow;
    private CommonAdapter<String> roomAdapter;
    private int weizi;
    private UpdateUI updateUI;
    private boolean FRESH=true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
        if (eventData.getTag() == EventData.REFRESHDB||eventData.getCode()==EventData.CODE_GETDEVICE) {
            updateData(s);
            updateRoomData();
        }
        if (eventData.getTag() == "spacedata" || eventData.getCode() == EventData.CODE_REFRESH_DEVICE || eventData.getCode() == EventData.CODE_REFRESH_SENSOR) {
           updateUI.updade();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_space, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        updateUI = new UpdateUI() {
            @Override
            public void getData() {
                updateData(s);
            }
        };
        Roombean room = new Roombean("客厅");
        try {
            App.db.replace(room);
        } catch (DbException e) {
            e.printStackTrace();
        }
        initEvent();
        return view;
    }


    private void initEvent() {
        updateData(s);
        spaceAdapter = new ShebeiAdapter(getActivity(), sbList);
        lv.setAdapter(spaceAdapter);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                   if(scrollState==SCROLL_STATE_IDLE){
                       Log.e("TAG",scrollState+"");
                       FRESH=true;
                   }else FRESH=false;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        shezi.setOnClickListener(this);
        title.setOnClickListener(this);
        titleMore.setOnClickListener(this);
        updateRoomData();
        title.setText(roombeanList.get(0));
        roomAdapter =new CommonAdapter<String>(getActivity(), roombeanList, R.layout.simple_list_item) {
            @Override
            public void convert(ViewHolder helper, int position, String item) {
                TextView tv = helper.getView(R.id.textV);
                tv.setText(item);
                if(position==weizi){
                    tv.setTextColor(Color.parseColor("#f6ab00"));
                }else tv.setTextColor(Color.parseColor("#666666"));
            }
        };
        MyListView listView = new MyListView(getActivity());
        listView.setBackgroundColor(Color.WHITE);
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setMaxheight(DeviceUtils.dip2px(360));
        listView.setAdapter(roomAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weizi =position;
                String na=roombeanList.get(position);
                s=na.substring(0,na.indexOf("("));
                updateData(s);
                title.setText(na);
                roomAdapter.notifyDataSetChanged();
                spaceAdapter.notifyDataSetChanged();
                if(popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(listView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

//        drawlv.setAdapter(roomAdapter);
//        drawlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                s = roombeanList.get(position);
//                title.setText(s);
//                updateData(s);
//                spaceAdapter.notifyDataSetChanged();
//                drawerLayout.closeDrawers();
//            }
//        });
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);  getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void updateRoomData() {
        roombeanList.clear();
        try {
            roombeanList.add("所有空间"+"("+getMyData("所有空间").size()+")");
            List<Roombean> all = App.db.selector(Roombean.class).findAll();
            if (all != null) {
                for (int i = 0; i < all.size(); i++) {
                    String name = all.get(i).getName();
                    roombeanList.add(name+"("+getMyData(name).size()+")");
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        title.setText(roombeanList.get(weizi));
        if(roomAdapter!=null&&FRESH)
        roomAdapter.notifyDataSetChanged();
    }

    private void updateData(final String s) {
                sbList.clear();
                lists.clear();
           try {
            List<Device> gates = DeviceManage.getInstance().getCurrentdev();
            if(gates!=null){
                if("所有空间".equals(s)){
                    sbList.addAll(gates);
                }else
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    if(gateway.getRoom().equals(s)){
                        sbList.add(gateway);
                    }
                }
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<SubDevice> ds=null;
                    if("所有空间".equals(s)){
                        ds= App.db.selector(SubDevice.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).and("type","!=",0).findAll();
                    }else
                        ds= App.db.selector(SubDevice.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).and("type","!=",0).and("room","=",s).findAll();
                    if(ds!=null){
                        sbList.addAll(ds);
                    }
                }
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<Sensor> ds=null;
                    if("所有空间".equals(s)) {
                         ds= App.db.selector(Sensor.class).where("device_id", "=", gateway.getXDevice().getDeviceId()).findAll();
                    }else
                        ds=App.db.selector(Sensor.class).where("device_id", "=", gateway.getXDevice().getDeviceId()).and("room", "=", s).findAll();
                    if(ds!=null){
                        sbList.addAll(ds);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
//                try {
//                    lists.addAll(DeviceManage.getInstance().getCurrentdev());
//                    lists.addAll(DbUtil.findMyDv());
//                    lists.addAll(DbUtil.findMySensor());
//                    App.db.update(SpaceBean.class,null, new KeyValue("isdele", true));
//                    for (int i = 0; i < lists.size(); i++) {
//                        Devall devall = lists.get(i);
//                        SpaceBean spaceBean = App.db.selector(SpaceBean.class).where("sid", "=", devall.getSID()).findFirst();
//                        if (spaceBean == null) {
//                            App.db.replace(new SpaceBean(devall.getSort(), devall.getSID(), devall.getType(),devall.getRoom()));
//                        } else {
//                            spaceBean.setIsdele(false);
//                            spaceBean.setRoom(devall.getRoom());
//                            App.db.replace(spaceBean);
//                        }
//                    }
//                    App.db.delete(SpaceBean.class, WhereBuilder.b("isdele", "=", true));
////            }else {
////                List<Device> all = DeviceManage.getInstance().getCurrentdev();
////                if (all != null) {
////                    for (int i = 0; i < all.size(); i++) {
////                        if (all.get(i).getRoom().equals(s)) {
////                            lists.add(all.get(i));
////                        }
////                    }
////                    for (int i = 0; i < all.size(); i++) {
////                        List<SubDevice> room1 = App.db.selector(SubDevice.class).where("room", "=", s).and("gateway_id", "=", all.get(i).getXDevice().getDeviceId()).orderBy("type").findAll();
////                        if (room1 != null) {
////                            lists.addAll(room1);
////                        }
////                    }
////                    for (int i = 0; i < all.size(); i++) {
////                        List<Sensor> all1 = App.db.selector(Sensor.class).where("room", "=", s).and("device_id", "=", all.get(i).getXDevice().getDeviceId()).orderBy("type").findAll();
////                        if (all1 != null)
////                            lists.addAll(all1);
////                    }
////                }
////                App.db.update(SpaceBean.class, WhereBuilder.b("room", "=", s), new KeyValue("isdele", true));
////                for (int i = 0; i < lists.size(); i++) {
////                    Devall devall = lists.get(i);
////                    SpaceBean spaceBean = App.db.selector(SpaceBean.class).where("sid", "=", devall.getSID()).findFirst();
////                    if (spaceBean == null) {
////                        App.db.save(new SpaceBean(devall.getSort(), devall.getSID(), devall.getType(), s));
////                    } else {
////                        spaceBean.setIsdele(false);
////                        App.db.replace(spaceBean);
////                    }
////                }
////                App.db.delete(SpaceBean.class, WhereBuilder.b("isdele", "=", true));
////            }
//                    List<SpaceBean> ls=null;
//                    if("所有空间".equals(s)){
//                        ls = App.db.selector(SpaceBean.class).orderBy("sort").orderBy("order", false).findAll();
//                    }else
//                        ls= App.db.selector(SpaceBean.class).where("room", "=", s).orderBy("sort").orderBy("order", false).findAll();
//                    if (ls != null) {
//                        for (int i = 0; i < ls.size(); i++) {
//                            ls.get(i).setOrder(i);
//                        }
//                        App.db.replace(ls);
//                        sbList.addAll(ls);
//                    }
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
                        if (spaceAdapter != null&&FRESH)
                            spaceAdapter.notifyDataSetChanged();
                        if(sbList.size()==0){
                            noDevice.setVisibility(View.VISIBLE);
                        }else {
                            noDevice.setVisibility(View.GONE);
                        }
    }

    public List<Devall> getMyData(String s){
        List<Devall> list=new ArrayList<>();
        try {
            List<Device> gates = DeviceManage.getInstance().getCurrentdev();
            if(gates!=null){
                if("所有空间".equals(s)){
                    list.addAll(gates);
                }else
                    for (int i = 0; i < gates.size(); i++) {
                        Device gateway = gates.get(i);
                        if(s.equals(gateway.getRoom())){
                            list.add(gateway);
                        }
                    }
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<SubDevice> ds=null;
                    if("所有空间".equals(s)){
                        ds= App.db.selector(SubDevice.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).and("type","!=",0).findAll();
                    }else
                        ds= App.db.selector(SubDevice.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).and("type","!=",0).and("room","=",s).findAll();
                    if(ds!=null){
                        list.addAll(ds);
                    }
                }
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<Sensor> ds=null;
                    if("所有空间".equals(s)) {
                        ds= App.db.selector(Sensor.class).where("device_id", "=", gateway.getXDevice().getDeviceId()).findAll();
                    }else
                        ds=App.db.selector(Sensor.class).where("device_id", "=", gateway.getXDevice().getDeviceId()).and("room", "=", s).findAll();
                    if(ds!=null){
                        list.addAll(ds);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.draw_menu:
////                drawerLayout.openDrawer(GravityCompat.START);
//                break;
//            case R.id.drawset:
//                startActivity(new Intent(getContext(),RoomsetActivity.class));
//                break;
            case R.id.shezi:
                Intent intent = new Intent(getContext(), RoomsetActivity.class);
                intent.putExtra("name", title.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.title:
            case R.id.title_more:
                popupWindow.showAsDropDown(titleBar);
                backgroundAlpha(0.5f);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
