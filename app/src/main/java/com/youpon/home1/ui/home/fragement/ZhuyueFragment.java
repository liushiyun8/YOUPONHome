package com.youpon.home1.ui.home.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.se7en.utils.SystemUtil;
import com.squareup.picasso.Picasso;
import com.youpon.home1.R;
import com.youpon.home1.bean.APPInfo;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.MainBean;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.User;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.MainGridAdapter;
import com.youpon.home1.ui.device.DeviceSortActivity;
import com.youpon.home1.ui.home.activities.AboutActivity;
import com.youpon.home1.ui.home.activities.AppSetActivity;
import com.youpon.home1.ui.home.activities.DeviceMainActivity;
import com.youpon.home1.ui.home.activities.MainSetActivity;
import com.youpon.home1.ui.home.activities.PushSetActivity;
import com.youpon.home1.ui.home.activities.ShareActivity;
import com.youpon.home1.ui.home.activities.VersionActivity;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhuyueFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.draw_menu)
    ImageView drawMenu;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.shezi)
    ImageView shezi;
    @BindView(R.id.drawlayout)
    DrawerLayout drawlayout;
    @BindView(R.id.draw_image)
    ImageView drawImage;

    List<MainBean> list = new ArrayList<>();
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.gateway_count)
    TextView gatewayCount;
    @BindView(R.id.device_count)
    TextView deviceCount;
    @BindView(R.id.scene_count)
    TextView sceneCount;
    @BindView(R.id.updateicon)
    ImageView updateicon;
    @BindView(R.id.no_tianjia)
    RelativeLayout noTianjia;
    @BindView(R.id.no_device)
    RelativeLayout noDevice;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.pointc)
    LinearLayout pointc;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.right)
    LinearLayout right;
    @BindView(R.id.mygateway)
    LinearLayout mygateway;
    @BindView(R.id.mydevice)
    LinearLayout mydevice;
    @BindView(R.id.myscene)
    LinearLayout myscene;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.push)
    LinearLayout push;
    @BindView(R.id.about)
    LinearLayout about;
    @BindView(R.id.version)
    LinearLayout version;

    private APPInfo appInfo;
    private MainGridAdapter mainGridAdapter;
    private PagerAdapter vpAdapter;
    private GridView gvView;
    private int count;
    private List<MainBean> list1 = new ArrayList<>();
    private String TAG = getClass().getSimpleName();
    public int pos;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventDatas(EventData eventData) {
        if (eventData.getCode() == EventData.CODE_REFRESH_DEVICE ||
                eventData.getCode() == EventData.CODE_REFRESH_SENSOR) {
            mainGridAdapter.notifyDataSetChanged();
        }
        if (eventData.getTag() == "mainsave"||EventData.TAG_REFRESH.equals(eventData.getTag()) || EventData.CODE_GETDEVICE == eventData.getCode()) {
            initData();
            vpAdapter.notifyDataSetChanged();
            mainGridAdapter.notifyDataSetChanged();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (appInfo != null) {
                    if (!SystemUtil.getSystemVersion().equals(appInfo.getVersion())) {
                        updateicon.setVisibility(View.VISIBLE);
                    } else updateicon.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhuyue, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        init();
        return view;
    }

    private void init() {
        drawMenu.setOnClickListener(this);
        shezi.setOnClickListener(this);
        drawImage.setOnClickListener(this);
        share.setOnClickListener(this);
        push.setOnClickListener(this);
        about.setOnClickListener(this);
        version.setOnClickListener(this);
        mygateway.setOnClickListener(this);
        mydevice.setOnClickListener(this);
        myscene.setOnClickListener(this);
        drawlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
//                Display display = manager.getDefaultDisplay();
//                right.layout((int) slideOffset, 0,
//                        (int) slideOffset + display.getWidth(), display.getHeight());
                right.scrollTo((int) slideOffset,0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                ((DeviceMainActivity) getActivity()).hideBar();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                ((DeviceMainActivity) getActivity()).showBar();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    ((DeviceMainActivity) getActivity()).hideBar();
                } else if (newState == DrawerLayout.STATE_IDLE && !drawlayout.isDrawerOpen(GravityCompat.START)) {
                    ((DeviceMainActivity) getActivity()).showBar();
                }
            }
        });
        HttpManage.getInstance().getAppVersion(new HttpManage.ResultCallback<Map<String, Object>>() {

            @Override
            public void onError(Header[] headers, HttpManage.Error error) {

            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                String illustration = (String) response.get("illustration");
                String md5 = (String) response.get("md5");
                String url = (String) response.get("url");
                String version = (String) response.get("version");
                appInfo = new APPInfo(illustration, md5, url, version);
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            ;
        });
        HttpManage.getInstance().getUserInfo(Comconst.CURRENTUSER, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                User user = new Gson().fromJson(result, User.class);
                try {
                    User dbuser = App.db.selector(User.class).where("id", "=", user.getId()).findFirst();
                    if (dbuser != null) {
                        user.setName(dbuser.getName());
                        user.setPwd(dbuser.getPwd());
                    }
                    App.db.replace(user);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initDrawData();
                        }
                    });
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
        initData();
        mainGridAdapter = new MainGridAdapter(list1, getActivity());
//        gv.setAdapter(mainGridAdapter);
        vpAdapter = new MyVpAdapter();
        vp.setAdapter(vpAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                list1.clear();
                pos=position;
                for (int i = 9 * position; i < (list.size() >= 9 * (position + 1) ? 9 * (position + 1) : (9 * position + list.size() % 9)); i++) {
                    list1.add(list.get(i));
                }
                mainGridAdapter.notifyDataSetChanged();
                pointc.removeAllViews();
                for (int i = 0; i < count; i++) {
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                    params.setMargins(10, 0, 10, 0);
                    imageView.setLayoutParams(params);
                    if (i == position) {
                        imageView.setImageResource(R.drawable.point_yellow);
                    } else imageView.setImageResource(R.drawable.point_gray);
                    pointc.addView(imageView);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDrawData() {
        User user = null;
        try {
            user = App.db.selector(User.class).where("id", "=", Comconst.CURRENTUSER).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (user != null) {
            if (user.getAvatar() != null && user.getAvatar() != "") {
                Picasso.with(getActivity()).load(user.getAvatar()).into(drawImage);
            }
            username.setText(user.getName().replace((user.getName().substring(3,9)),"******"));
            nickname.setText(user.getNickname());
        }
        gatewayCount.setText(DbUtil.getGatewayCount() + "个");
        deviceCount.setText(DbUtil.findMyDv().size() + "个");
        sceneCount.setText(DbUtil.findMyScene().size() + "个");
    }

    private void initData() {
        initDrawData();
        list.clear();
        try {
            List<MainBean> all = App.db.selector(MainBean.class).orderBy("order").findAll();
            MyLog.e("tag",all+"");
            if (all != null) {
                list.addAll(all);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            MainBean mainBean = list.get(i);
            try {
                switch (mainBean.getSort()) {
                    case 0:
                        Scenebean scenebean = App.db.selector(Scenebean.class).where("objectId", "=", mainBean.getSid()).findFirst();
                        if (scenebean == null) {
                            App.db.delete(MainBean.class, WhereBuilder.b("sid", "=", mainBean.getSid()));
                        }
                        break;
                    case 1:
                        List<Device> currentdev = DeviceManage.getInstance().getCurrentdev();
                        boolean dele = true;
                        for (int j = 0; j < currentdev.size(); j++) {
                            Device device = currentdev.get(j);
                            if (device.getSID().equals(mainBean.getSid())) {
                                dele = false;
                                break;
                            }
                        }
                        if (dele) {
                            App.db.delete(mainBean);
                        }
                        break;
                    case 3:
                        SubDevice de = App.db.selector(SubDevice.class).where("unique", "=", mainBean.getSid()).findFirst();
                        if (de == null) {
                            App.db.delete(mainBean);
                        }
                        break;
                    case 4:
                        Sensor sensor = App.db.selector(Sensor.class).where("id", "=", mainBean.getSid()).findFirst();
                        if (sensor == null) {
                            App.db.delete(mainBean);
                        }
                        break;
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        list.clear();
        try {
            List<MainBean> all = App.db.selector(MainBean.class).orderBy("order").findAll();
            if (all != null) {
                list.addAll(all);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        MyLog.e(TAG, list.size() + "");
        count = (list.size() - 1) / 9 + 1;
        list1.clear();
        if (count == 1) {
            list1.addAll(list);
        } else {
            if(pos>count-1){
                pos=count-1;
            }
            for (int i = 9 * pos; i < (list.size() >= 9 * (pos + 1) ? 9 * (pos + 1) : (9 * pos + list.size() % 9)); i++) {
                list1.add(list.get(i));
            }
        }
        pointc.removeAllViews();
        if (count > 1)
            for (int j = 0; j < count; j++) {
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                params.setMargins(10, 0, 10, 0);
                imageView.setLayoutParams(params);
                if (j ==pos) {
                    imageView.setImageResource(R.drawable.point_yellow);
                } else imageView.setImageResource(R.drawable.point_gray);
                pointc.addView(imageView);
            }
        if (list.size() == 0) {
            if (DbUtil.findMyDv().size() != 0) {
                noTianjia.setVisibility(View.VISIBLE);
                noDevice.setVisibility(View.GONE);
            } else {
                noTianjia.setVisibility(View.GONE);
                noDevice.setVisibility(View.VISIBLE);
            }
        } else {
            noTianjia.setVisibility(View.GONE);
            noDevice.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shezi:
                startActivity(new Intent(getContext(), MainSetActivity.class));
                break;
            case R.id.draw_menu:
                ((DeviceMainActivity) getActivity()).hideBar();
                drawlayout.openDrawer(GravityCompat.START);
                break;
            case R.id.draw_image:
                startActivity(new Intent(getContext(), AppSetActivity.class));
                drawlayout.closeDrawers();
                break;
            case R.id.share:
                startActivity(new Intent(getActivity(), ShareActivity.class));
                drawlayout.closeDrawers();
                break;
            case R.id.push:
                startActivity(new Intent(getActivity(), PushSetActivity.class));
                drawlayout.closeDrawers();
                break;
            case R.id.about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                drawlayout.closeDrawers();
                break;
            case R.id.version:
                Intent intent = new Intent(getContext(), VersionActivity.class);
                intent.putExtra("appinfo", appInfo);
                startActivity(intent);
                drawlayout.closeDrawers();
                break;
            case R.id.mygateway:
                Intent intent1 = new Intent(getActivity(), DeviceSortActivity.class);
                intent1.putExtra("position",0);
                startActivity(intent1);
                break;
            case R.id.mydevice:
                drawlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        ((DeviceMainActivity)getActivity()).showBar();
                        ((DeviceMainActivity)getActivity()).rg.check(R.id.shebei);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                });
                drawlayout.closeDrawers();
                break;
            case R.id.myscene:
                drawlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        ((DeviceMainActivity)getActivity()).showBar();
                        ((DeviceMainActivity)getActivity()).rg.check(R.id.changjing);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                });
                drawlayout.closeDrawers();
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class MyVpAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.vp_item, null);
            gvView = (GridView) view.findViewById(R.id.gv);
            gvView.setAdapter(mainGridAdapter);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
//            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
