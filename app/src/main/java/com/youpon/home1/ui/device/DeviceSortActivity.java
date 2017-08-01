package com.youpon.home1.ui.device;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Devall;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.ShebeiAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceSortActivity extends BaseActivity {

    ImageView back;
    TextView title;
    ListView lv;
    private List<Devall> list=new ArrayList<>();
    private ShebeiAdapter shebeiAdapter;
    private int position;
    private String[] allsort;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){

        if(eventData.getCode()==EventData.CODE_REFRESH_DEVICE){
            if(position<=4){
                initData();
                shebeiAdapter.notifyDataSetChanged();
            }
        }else if(eventData.getCode()==EventData.CODE_REFRESH_SENSOR){
            if(position>4){
                initData();
                shebeiAdapter.notifyDataSetChanged();
            }
        }else if(EventData.CODE_GETDEVICE==eventData.getCode()){
            initData();
            shebeiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_sort);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back= (ImageView) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title);
        lv= (ListView) findViewById(R.id.lv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        position = getIntent().getIntExtra("position", 0);
        allsort = getResources().getStringArray(R.array.all_sort);
        title.setText(allsort[position]);
        initData();
        shebeiAdapter = new ShebeiAdapter(this, list);
        lv.setAdapter(shebeiAdapter);
    }

    private void initData() {
        list.clear();
        switch (position){
            case 0:
                list.addAll(DeviceManage.getInstance().getCurrentdev());
                break;
            case 1:
                list.addAll(DbUtil.findMydevIndex(2));
                break;
            case 2:
                list.addAll(DbUtil.findMydevIndex(1));
                break;
            case 3:
                list.addAll(DbUtil.findMydevIndex(0));
                break;
            case 4:
                list.addAll(DbUtil.findMydevIndex(3));
                break;
            case 5:
            case 6:
                list.addAll(DbUtil.findMySensorType(position-5));
                break;
            case 7:
                list.addAll(DbUtil.findMySensorType(5));
                break;
            case 8:
                list.addAll(DbUtil.findMySensorType(2));
                break;
            case 9:
                list.addAll(DbUtil.findMySensorType(7));
                break;
            case 10:
                list.addAll(DbUtil.findMySensorType(4));
                break;
            case 11:
                list.addAll(DbUtil.findMySensorType(6));
                break;
        }
//        if(position==0){
//            list.addAll(DeviceManage.getInstance().getCurrentdev());
//        }else if (position>0&&position<=4){
//            list.addAll(DbUtil.findMydevIndex(position-1));
//        }else if(position>4&&position<8){
//            list.addAll(DbUtil.findMySensorType(position-5));
//        }else if(position>=8){
//            list.addAll(DbUtil.findMySensorType(position-4));
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
