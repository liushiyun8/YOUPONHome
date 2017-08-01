package com.youpon.home1.ui.home.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.se7en.utils.DeviceUtils;
import com.squareup.picasso.Picasso;
import com.youpon.home1.R;
import com.youpon.home1.bean.ShareJson;
import com.youpon.home1.bean.ShareUser;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.edit)
    ImageView edit;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.noshare)
    LinearLayout noshare;
    private static final int REQUEST_CODE = 200;
    private Map<Integer,ShareUser> map1=new HashMap<>();
    private Map<Integer,ShareUser> map2=new HashMap<>();
    private List<ShareJson> list=new ArrayList<>();
    private List<ShareUser> list1 = new ArrayList<>();
    private List<ShareUser> list2 = new ArrayList<>();
    private CommonAdapter<ShareUser> commonAdapter;
    public  Fragment[] fragments=new Fragment[2];
    private PopupWindow popupWindow;
    private CommonAdapter<ShareUser> commonAdapter1;
    private int pos;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventDatas(EventData eventData) {
        if(EventData.REFRESHDB.equals(eventData.getTag())){
            getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initEvent();
        EventBus.getDefault().register(this);
        init();
    }



    private void init() {
        View view = LayoutInflater.from(this).inflate(R.layout.share_popup, null);
        View sd = view.findViewById(R.id.sharedevice);
        View code = view.findViewById(R.id.sharecode);
        sd.setOnClickListener(this);
        code.setOnClickListener(this);
        popupWindow = new PopupWindow(view, DeviceUtils.dip2px(154), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        commonAdapter = new CommonAdapter<ShareUser>(this, list1, R.layout.myshare_item) {
            @Override
            public void convert(ViewHolder helper, int position, ShareUser item) {
                helper.setText(R.id.name,item.getName());
                ImageView iv = helper.getView(R.id.touxiang);
                iv.setTag(item.getUser_id());
                updateShareJson(item.getUser_id(),iv);
            }
        };
        commonAdapter1 = new CommonAdapter<ShareUser>(this, list2, R.layout.myshare_item) {
            @Override
            public void convert(ViewHolder helper, int position, ShareUser item) {
                        helper.setText(R.id.name,item.getName());
                ImageView iv = helper.getView(R.id.touxiang);
                iv.setTag(item.getUser_id());
                Log.e("TTTAG",item.getUser_id()+"");
                updateShareJson(item.getUser_id(),iv);
            }
        };
        getData();
        ListFragment listFragment = new MyFragement();
        ListFragment listFragment1 = new MyFragement1();
        listFragment.setListAdapter(commonAdapter);
        listFragment1.setListAdapter(commonAdapter1);
        fragments[0]=listFragment;
        fragments[1]=listFragment1;
        MyvpAdapter adapter = new MyvpAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos=position;
                showorhide(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.setupWithViewPager(vp);
        for (int i = 0; i < tab.getTabCount(); i++) {
            TabLayout.Tab tabL = this.tab.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tabL.setCustomView(tabView);
            if(i==0){
                tabView.findViewById(R.id.textView).setSelected(true);
                tabView.findViewById(R.id.indicator).setBackgroundColor(Color.parseColor("#f6ab00"));
            }
        }
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                customView.findViewById(R.id.textView).setSelected(true);
                customView.findViewById(R.id.indicator).setBackgroundColor(Color.parseColor("#f6ab00"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                customView.findViewById(R.id.textView).setSelected(false);
                customView.findViewById(R.id.indicator).setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showorhide(int position) {
        if(position==0){
            if(list1.size()==0){
                noshare.setVisibility(View.VISIBLE);
            }else noshare.setVisibility(View.GONE);
        }else {
            if(list2.size()==0){
                noshare.setVisibility(View.VISIBLE);
            }else noshare.setVisibility(View.GONE);
        }
    }

    private void updateShareJson(final int user_id, final ImageView iv) {
        HttpManage.getInstance().getPubUserInfo(user_id, new MyCallback() {
            @Override
            public void onSuc(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String avatar = jsonObject.optString("avatar");
                    if(Integer.parseInt(iv.getTag().toString())==user_id&&avatar!=null&&!"".equals(avatar)){
                        Picasso.with(ShareActivity.this).load(avatar).into(iv);
                    }else {
                        iv.setImageResource(R.mipmap.user_img_def);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    private void getData() {
        HttpManage.getInstance().getShareList(new HttpManage.ResultCallback<String>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {

            }

            @Override
            public void onSuccess(int code, String response) {
                Log.e("TAG", response);
                list.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        ShareJson shareJson = new Gson().fromJson(jsonObject.toString(), ShareJson.class);
                        if(shareJson.getUser_id()==0||!"accept".equals(shareJson.getState())){
                            HttpManage.getInstance().deleteShare(shareJson.getInvite_code(), new HttpManage.ResultCallback<String>() {
                                @Override
                                public void onError(Header[] headers, HttpManage.Error error) {

                                }

                                @Override
                                public void onSuccess(int code, String response) {

                                }
                            });
                        }else {
                            list.add(shareJson);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateView();
            }

        });
    }
    private void updateView() {
        list1.clear();
        list2.clear();
        map1.clear();
        map2.clear();
        for (int i = 0; i < list.size(); i++) {
            ShareJson shareJson = list.get(i);
            if(shareJson.getFrom_id()==App.getApp().appid){
                ShareUser shareUser= map1.get(shareJson.getUser_id());
                if(shareUser==null){
                    shareUser=new ShareUser();
                }
                try {
                    ShareUser byId = App.db.selector(ShareUser.class).where("user_id","=",shareJson.getUser_id()).findFirst();
                    if(byId!=null){
                        shareUser.setBeizu(byId.getBeizu());
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                shareUser.setInvite_code(shareJson.getInvite_code());
                shareUser.setName(shareJson.getTo_name());
                shareUser.setUser(shareJson.getTo_user());
                shareUser.setUser_id(shareJson.getUser_id());
                shareUser.getSet().add(shareJson.getDevice_id());
                map1.put(shareJson.getUser_id(),shareUser);
            }else {
                ShareUser shareUser= map2.get(shareJson.getFrom_id());
                if(shareUser==null){
                    shareUser=new ShareUser();
                }
                try {
                    ShareUser byId =App.db.selector(ShareUser.class).where("user_id","=",shareJson.getFrom_id()).findFirst();
                    if(byId!=null){
                        shareUser.setBeizu(byId.getBeizu());
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                shareUser.setInvite_code(shareJson.getInvite_code());
                shareUser.setName(shareJson.getFrom_name());
                shareUser.setUser(shareJson.getFrom_user());
                shareUser.setUser_id(shareJson.getFrom_id());
                shareUser.getSet().add(shareJson.getDevice_id());
                map2.put(shareJson.getUser_id(),shareUser);
            }
        }
        list1.addAll(map1.values());
        list2.addAll(map2.values());
        showorhide(pos);
        commonAdapter.notifyDataSetChanged();
        commonAdapter1.notifyDataSetChanged();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void initEvent() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                popupWindow.showAsDropDown(v, -DeviceUtils.dip2px(123), 2);
                backgroundAlpha(0.5f);
                break;
            case R.id.sharedevice:
                startActivity(new Intent(this, ShareDeviceActivity.class));
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                break;
            case R.id.sharecode:
                callCapture("UTF-8");
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (null != data && requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    String json = data.getStringExtra(Intents.Scan.RESULT);
                    bindDevice(json);
//                    bindGateway(json);
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressLint("validFragment")
    public class MyFragement extends ListFragment{
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("YYY",position+"");
                    ShareUser shareUser = list1.get(position);
                    Intent intent = new Intent(ShareActivity.this, ShareUserInfoActivity.class);
                    intent.putExtra("user",shareUser);
                    startActivity(intent);
                }
            });
        }
    }

    @SuppressLint("validFragment")
    public class MyFragement1 extends ListFragment{
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("YYY",position+"");
                    ShareUser shareJson = list2.get(position);
                    Intent intent = new Intent(ShareActivity.this, ShareUserInfoActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra("user",shareJson);
                    startActivity(intent);
                }
            });
        }
    }

    private void bindDevice(String json) {
        String s = new String(Base64.decode(json, Base64.DEFAULT)).trim();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonArray!=null)
        for (int i = 0; i < jsonArray.length(); i++) {
            String js= jsonArray.optString(i);
            JSONObject jo = null;
            try {
                jo = new JSONObject(js);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(jo!=null){
                String invite_code = jo.optString("invite_code");
                HttpManage.getInstance().acceptShare(invite_code, new HttpManage.ResultCallback<String>() {
                    @Override
                    public void onError(Header[] headers, HttpManage.Error error) {
                        MyToast.show(ShareActivity.this,MyToast.TYPE_ERROR,error.getMsg(),1);
                    }

                    @Override
                    public void onSuccess(int code, String response) {
                        MyToast.show(ShareActivity.this,MyToast.TYPE_OK,"分享成功",1);
                        EventBus.getDefault().post(new EventData(EventData.TAG_REFRESH,""));
                        getData();
                    }
                });
            }
        }

    }

    private void callCapture(String characterSet) {

        Intent intent = new Intent();
        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.CHARACTER_SET, characterSet);
        intent.putExtra(Intents.Scan.WIDTH,DeviceUtils.dip2px(260));
        intent.putExtra(Intents.Scan.HEIGHT,DeviceUtils.dip2px(260));
        intent.putExtra(Intents.Scan.PROMPT_MESSAGE,"将分享二维码放入框内，即可自动扫描");
        intent.setClass(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private class MyvpAdapter extends FragmentPagerAdapter {
        String[] titles = {"我的分享", "他人的分享"};

        public MyvpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        public View getTabView(int position){
            View view = LayoutInflater.from(ShareActivity.this).inflate(R.layout.tab_item, null);
            TextView tv= (TextView) view.findViewById(R.id.textView);
            tv.setText(titles[position]);
            return view;
        }
    }
}
