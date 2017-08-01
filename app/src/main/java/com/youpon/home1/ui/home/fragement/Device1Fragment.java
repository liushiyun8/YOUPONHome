package com.youpon.home1.ui.home.fragement;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.DbUtil;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;
import com.youpon.home1.ui.device.AddWGActivity;
import com.youpon.home1.ui.device.DeviceSortActivity;
import com.youpon.home1.ui.device.WangActivity;
import com.youpon.home1.ui.device.WifiSetActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Device1Fragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.gv)
    PullToRefreshGridView gv;
    private CommonAdapter<Integer> commonAdapter;
    private PopupWindow popupWindow;

    public Device1Fragment() {
        // Required empty public constructor
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventData(EventData eventData){
        if(eventData.getCode()==EventData.CODE_GETDEVICE){
            commonAdapter.notifyDataSetChanged();
            if(gv.isRefreshing()){
                gv.onRefreshComplete();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device1, container, false);
        DeviceUtils.setContext(getActivity());
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        init();
        return view;
    }

    private void init() {
        Integer[] ids={R.mipmap.equ_ic_gateway,R.mipmap.equ_ic_lighting,R.mipmap.equ_ic_warmlight,R.mipmap.equ_ic_warmwind,R.mipmap.equ_ic_breath,
                R.mipmap.equ_ic_infrared,R.mipmap.equ_ic_sensorlight,R.mipmap.equ_ic_air,R.mipmap.equ_ic_humiture,R.mipmap.equ_ic_smoke,R.mipmap.equ_ic_co2,R.mipmap.equ_ic_gas};
        final List<Integer> allsorts = Arrays.asList(ids);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.device_popup, null);
        View wifi = view.findViewById(R.id.wifiset);
        View gateway = view.findViewById(R.id.addwangguan);
        View shebei = view.findViewById(R.id.addshebei);
        wifi.setOnClickListener(this);
        gateway.setOnClickListener(this);
        shebei.setOnClickListener(this);
        popupWindow = new PopupWindow(view,DeviceUtils.dip2px(154),ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        commonAdapter = new CommonAdapter<Integer>(getActivity(), allsorts,R.layout.device_gridite1) {
            @Override
            public void convert(ViewHolder helper, int position,Integer item) {
                String name="";
                switch (position){
                    case 0:
                        name=DeviceManage.getInstance().getCurrentdev().size()+"个";
                        break;
                    case 1:
                        name= DbUtil.findMydevIndex(2).size()+"个";
                        break;
                    case 2:
                        name= DbUtil.findMydevIndex(1).size()+"个";
                        break;
                    case 3:
                        name= DbUtil.findMydevIndex(0).size()+"个";
                        break;
                    case 4:
                        name= DbUtil.findMydevIndex(3).size()+"个";
                        break;
                    case 5:
                    case 6:
                        name=DbUtil.findMySensorType(position-5).size()+"个";
                        break;
                    case 7:
                        name=DbUtil.findMySensorType(5).size()+"个";
                        break;
                    case 8:
                        name=DbUtil.findMySensorType(2).size()+"个";
                        break;
                    case 9:
                        name=DbUtil.findMySensorType(7).size()+"个";
                        break;
                    case 10:
                        name=DbUtil.findMySensorType(4).size()+"个";
                        break;
                    case 11:
                        name=DbUtil.findMySensorType(6).size()+"个";
                        break;
                }
//                if(position==0){
//                    name=DeviceManage.getInstance().getCurrentdev().size()+"个";
//                }else if (position>0&&position<=4){
//                    name= DbUtil.findMydevIndex(position-1).size()+"个";
//                }else if(position>4&&position<8){
//                    name=DbUtil.findMySensorType(position-5).size()+"个";
//                }else if(position>=8){
//                    name=DbUtil.findMySensorType(position-4).size()+"个";
//                }
                helper.setText(R.id.name,name);
                helper.setImageResource(R.id.icon,item);
            }
        };
        gv.setAdapter(commonAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DeviceSortActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        gv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                EventBus.getDefault().post(new EventData(EventData.TAG_REFRESH,""));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(v,-DeviceUtils.dip2px(120),0);
                backgroundAlpha(0.5f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);  getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onClick(View v) {
        if(popupWindow.isShowing())
            popupWindow.dismiss();
        switch (v.getId()){
            case R.id.wifiset:
                startActivity(new Intent(getActivity(),WifiSetActivity.class));
                break;
            case R.id.addwangguan:
                startActivity(new Intent(getActivity(),AddWGActivity.class));
                break;
            case R.id.addshebei:
                startActivity(new Intent(getActivity(),WangActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
