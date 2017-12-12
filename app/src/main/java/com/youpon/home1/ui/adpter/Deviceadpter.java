package com.youpon.home1.ui.adpter;

import android.content.Context;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkCode;
import io.xlink.wifi.sdk.listener.GetSubscribeKeyListener;
import io.xlink.wifi.sdk.listener.SetDeviceAccessKeyListener;
import io.xlink.wifi.sdk.listener.SubscribeDeviceListener;

/**
 * Created by computer on 2016/11/28.
 */
public class Deviceadpter extends BaseAdapter {
    Context context;
    List<XDevice> list;
    private String TAG = "DeviceAdapter";

    public Deviceadpter(Context context, List<XDevice> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.device_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final XDevice device = list.get(position);
        if(device.getLocalAddress()==null){
            viewHolder.mdnsip.setVisibility(View.GONE);
        }else {
            String ip = device.getLocalAddress().toString();
            viewHolder.mdnsip.setText(ip);
        }
        viewHolder.mdnsname.setText(device.getDeviceName());
        final String mac = device.getMacAddress();
        StringBuffer sB= new StringBuffer();
        for (int i = 0; i < mac.length();i=i+2) {
            String sub = mac.substring(i, i + 2);
            sB.append(sub+"-");
        }
        viewHolder.mdnsmac.setText("MAC:"+sB.substring(0,sB.length()-1));
        final TextView bind = viewHolder.bind;
        bind.setEnabled(true);
        bind.setText("添加");
        if(DeviceManage.getInstance().getCurrentdev(device)!=null){
            bind.setEnabled(false);
            bind.setText("已绑定");
        }
         bind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpManage.getInstance().getAuthkey(new MyCallback() {
                        @Override
                        public void onSuc(String result) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(result);
                                String access_token = jsonObject.optString("access_token");
                                HttpManage.getInstance().addDevice(access_token, mac, "友邦小智", new MyCallback() {
                                    @Override
                                    public void onSuc(String result) {
                                        try {
                                            JSONObject jsonObject1 = new JSONObject(result);
                                            if (mac.equals(jsonObject1.optString("mac"))){
                                                bind.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        bindDevice(device, bind);
                                                    }
                                                },1000);
                                            }
                                        } catch (JSONException e) {

                                        }
                                    }

                                    @Override
                                    public void onFail(int code, String msg) {
                                        bindDevice(device, bind);
                                    }
                                });
                            } catch (JSONException e) {

                            }
                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
//                    bindDevice(device, bind);
                }
            });

        return convertView;
    }

    private void bindDevice(XDevice device, final TextView bind) {
        final Device dev = new Device(device);
        if (device.getAccessKey() < 0) {
            final int key =123456;
            XlinkAgent.getInstance().setDeviceAccessKey(device, key, new SetDeviceAccessKeyListener() {
                @Override
                public void onSetLocalDeviceAccessKey(XDevice device, int code, int messageId) {
                    MyLog.e("设置AccessKey:", "" + code);
                    switch (code) {
                        case XlinkCode.SUCCEED:
                            dev.setAccessKey(key);
                            break;
                    }
                }
            });
        } else {
            dev.setAccessKey(device.getAccessKey());
        }
        DeviceManage.getInstance().addDevice(dev);
//                    if (dev.getXDevice().getSubKey() <= 0) {
        XlinkAgent.getInstance().getInstance().getDeviceSubscribeKey(dev.getXDevice(), dev.getXDevice().getAccessKey(), new GetSubscribeKeyListener() {
            @Override
            public void onGetSubscribekey(XDevice xdevice, int code, int subKey) {
                MyLog.e(TAG,"getDeviceSubscribeKey"+ subKey);
                dev.getXDevice().setSubKey(subKey);
                DeviceManage.getInstance().updateDevice(dev);
            }
        });
//                    }
        XlinkAgent.getInstance().subscribeDevice(dev.getXDevice(), dev.getXDevice().getSubKey(), new SubscribeDeviceListener() {
            @Override
            public void onSubscribeDevice(XDevice xdevice, int code) {
                MyLog.e(TAG,"subscribeDevice:"+ code + " xdevice:" + xdevice);
                bind.setEnabled(false);
                if (code == XlinkCode.SUCCEED) {
                    dev.setSubscribe(true);
                    DeviceManage.getInstance().updateDevice(dev);
                    bind.setText("已绑定");
                    MyToast.show(context,MyToast.TYPE_OK,"添加成功",1);
                    EventBus.getDefault().post(new EventData(EventData.TAG_REFRESH,""));
                }else {
                    bind.setText("绑定失败");
                    MyToast.show(context,MyToast.TYPE_ERROR,"添加失败",1);
                }
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.mdnsname)
        TextView mdnsname;
        @BindView(R.id.mdnsmac)
        TextView mdnsmac;
        @BindView(R.id.mdnsip)
        TextView mdnsip;
        @BindView(R.id.bind)
        TextView bind;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
