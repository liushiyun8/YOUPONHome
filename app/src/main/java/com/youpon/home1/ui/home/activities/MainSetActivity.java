package com.youpon.home1.ui.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortListView;
import com.youpon.home1.R;
import com.youpon.home1.bean.MainBean;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.ui.adpter.MainsetAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainSetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.lv)
    DragSortListView lv;
    @BindView(R.id.movebar)
    TextView movebar;
    private String TAG = getClass().getSimpleName();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData) {
        if ("maindata".equals(eventData.getTag())) {
            initDatas();
            adapter.notifyDataSetChanged();
        }
    }

    List<MainBean> list = new ArrayList<>();
    private MainsetAdapter adapter;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            MainBean mainBean = list.get(from);
            list.remove(from);
            list.add(to, mainBean);
            adapter.notifyDataSetChanged();
        }
    };
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {

        private MyDialog myDialog;

        @Override
        public void remove(final int which) {
            myDialog = new MyDialog(MainSetActivity.this);
            myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    myDialog.dismiss();
//                    adapter.notifyDataSetChanged();
                }
            });
            myDialog.setYesOnclickListener("移出", new MyDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    final MainBean mainBean = list.get(which);
                    HttpManage.getInstance().deleSub(mainBean.getObjectId(), "MainBean", new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, "删除成功" + result);
                            try {
                                App.db.delete(mainBean);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
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
                    myDialog.dismiss();
                    list.remove(which);
                    if(list.size()>0){
                        movebar.setVisibility(View.VISIBLE);
                    }else movebar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            });
            myDialog.setType(MyDialog.MESSAGETYPE);
            myDialog.setMessage("确定要将该设备移出首页吗？");
            myDialog.setTitle("温馨提示");
            adapter.notifyDataSetChanged();
            myDialog.show();
        }
    };
    private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
        @Override
        public float getSpeed(float w, long t) {
            if (w > 0.8f) {
                return ((float) adapter.getCount()) / 0.001f;
            } else {
                return 10.0f * w;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_set);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void initDatas() {
        list.clear();
        try {
            List<MainBean> all = App.db.findAll(MainBean.class);
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
                            App.db.delete(mainBean);
                        }
                        break;
                    case 1:
//                    Gateway ga = App.db.selector(Gateway.class).where("device_id", "=", mainBean.getSid()).findFirst();
//                    if(ga==null){
//                        App.db.delete(mainBean);
//                    }
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
            List<MainBean> all = App.db.selector(MainBean.class).orderBy("order", false).findAll();
            if (all != null) {
                list.addAll(all);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(list.size()>0){
            movebar.setVisibility(View.VISIBLE);
        }else movebar.setVisibility(View.GONE);
    }

    private void init() {
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        initDatas();
        adapter = new MainsetAdapter(list, this);
        lv.setAdapter(adapter);
        lv.setRemoveListener(onRemove);
        lv.setDropListener(onDrop);
        lv.setDragScrollProfile(ssProfile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                try {
                    App.db.delete(MainBean.class);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setOrder(i);
                    }
                    App.db.save(list);
                } catch (DbException e) {
                    e.printStackTrace();
                }
//                if(list.size()>0)
//                HttpManage.getInstance().addSub(HttpManage.TYPE_MORE, "MainBean", new Gson().toJson(list), new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.e(TAG,"上传首页数据成功"+result);
//                        try {
//                            JSONArray jsonArray = new JSONArray(result);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
//                                MainBean mainBean= new Gson().fromJson(jsonObject1.toString(), MainBean.class);
//                                App.db.replace(mainBean);
//                            }
//                            EventBus.getDefault().post(new EventData("mainsave",""));
//                            finish();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        ex.printStackTrace();
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//                        Log.e("TAG","finish");
//                    }
//                });
//else {
                EventBus.getDefault().post(new EventData("mainsave", ""));
                finish();
                break;
            case R.id.add:
                startActivity(new Intent(this, MainAddActivity.class));
                break;
            case R.id.save:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
