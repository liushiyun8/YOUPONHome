package com.youpon.home1.ui.space.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortListView;
import com.youpon.home1.R;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.bean.SpaceBean;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.RoomlvAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomsetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.lv)
    ListView lv;
    private List<Roombean> all=new ArrayList<>();
    private CommonAdapter<Roombean> roomlvAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){
        if(eventData.getTag()==EventData.REFRESHDB){
            initData();
            roomlvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomset);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        initData();
        roomlvAdapter = new CommonAdapter<Roombean>(this,all,R.layout.myroom_item) {
                @Override
                public void convert(ViewHolder helper, int position, Roombean item) {
                    helper.getView(R.id.panel_name).setVisibility(View.GONE);
                    helper.getView(R.id.diver).setVisibility(View.GONE);
                    final String name = item.getName();
                    helper.setText(R.id.name, name);
                    try {
                        List<SpaceBean> all = App.db.selector(SpaceBean.class).where("room", "=", item.getName()).findAll();
                        helper.setText(R.id.count,(all==null?0:all.size())+"个");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            };
        lv.setAdapter(roomlvAdapter);
        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RoomsetActivity.this, SpaceDetailActivity.class);
                intent.putExtra("room",all.get(position).getName());
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    private void initData() {
        all.clear();
        try {
           List<Roombean> all1 = App.db.selector(Roombean.class).findAll();
            if(all1!=null){
                all.addAll(all1);
            }
        } catch (DbException e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                startActivity(new Intent(this,AddRoomActivity.class));
                break;
            case R.id.back:
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
