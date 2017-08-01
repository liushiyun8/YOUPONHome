package com.youpon.home1.ui.home.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.se7en.utils.DeviceUtils;
import com.squareup.picasso.Picasso;
import com.youpon.home1.R;
import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.ShareJson;
import com.youpon.home1.bean.ShareUser;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.view.CustomShapeImageView;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.comm.view.MyToast;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;
import com.youpon.home1.ui.device.GatewaySetActivity;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareUserInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit)
    ImageView edit;
    @BindView(R.id.touxiang)
    CustomShapeImageView touxiang;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.beizu)
    TextView beizu;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.edit_img)
    ImageView editImg;
    @BindView(R.id.info)
    TextView info;
    private ShareUser user;
    private int type;
    private PopupWindow popupWindow;
    private boolean editTag;
    private CommonAdapter<Integer> adapter;
    private List<Integer> list = new ArrayList<>();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventDatas(EventData eventData) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_user_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        editImg.setOnClickListener(this);
        View view = LayoutInflater.from(this).inflate(R.layout.shareuser_popup, null);
        View sd = view.findViewById(R.id.sharedevice);
        final View dele = view.findViewById(R.id.sharedele);
        View devider = view.findViewById(R.id.divider);
        sd.setOnClickListener(this);
        dele.setOnClickListener(this);
        popupWindow = new PopupWindow(view, DeviceUtils.dip2px(154), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        user = (ShareUser) getIntent().getSerializableExtra("user");
        type = getIntent().getIntExtra("type", 0);
        if (user != null) {
            if (type == 0) {
                name.setText(user.getName());
                username.setText(user.getUser());
            } else {
                name.setText(user.getName());
                username.setText(user.getUser());
                info.setText("他分享给我的设备");
                sd.setVisibility(View.GONE);
                devider.setVisibility(View.GONE);
                popupWindow.setHeight(DeviceUtils.dip2px(60));
            }
            list.addAll(user.getSet());
            beizu.setText("".equals(user.getBeizu()) ? "无" : user.getBeizu());
            HttpManage.getInstance().getPubUserInfo(user.getUser_id(), new MyCallback() {
                @Override
                public void onSuc(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String avatar = jsonObject.optString("avatar");
                        if(avatar!=null&&!"".equals(avatar)){
                            Picasso.with(ShareUserInfoActivity.this).load(avatar).into(touxiang);
                        }else {
                            touxiang.setImageResource(R.mipmap.user_img_def);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(int code, String msg) {

                }
            });
            adapter = new CommonAdapter<Integer>(this, list, R.layout.myroom_item) {
                @Override
                public void convert(ViewHolder helper, int position, Integer item) {
                    final Device device = DeviceManage.getInstance().getDevice(item);
                    if (device != null) {
                        ImageView iv = helper.getView(R.id.icon);
                        iv.setVisibility(View.VISIBLE);
                        iv.setImageResource(R.mipmap.equ_ic_gateway);
                        helper.setText(R.id.name, device.getName());
                        helper.setText(R.id.count, device.getRoom());
                        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShareUserInfoActivity.this, GatewaySetActivity.class);
                                intent.putExtra("device_id", device.getXDevice().getDeviceId());
                                startActivity(intent);
                            }
                        });
                    }
                }
            };
            lv.setAdapter(adapter);
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                popupWindow.showAsDropDown(edit, -DeviceUtils.dip2px(120), 0);
                backgroundAlpha(0.5f);
                break;
            case R.id.edit_img:
                final MyDialog dialog1 = new MyDialog(this);
                dialog1.setType(MyDialog.EDITTYPE);
                dialog1.setTitle("修改备注");
                dialog1.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog1.dismiss();
                    }
                });
                dialog1.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        dialog1.dismiss();
                        beizu.setText(dialog1.getEditText());
                        user.setBeizu(beizu.getText().toString());
                        try {
                            App.db.replace(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                    }
                });
                dialog1.show();
                break;
            case R.id.sharedevice:
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                startActivity(new Intent(this, ShareDeviceActivity.class));
                finish();
                break;
            case R.id.sharedele:
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                final MyDialog dialog = new MyDialog(this);
                dialog.setType(MyDialog.MESSAGETYPE);
                if(type==0){
                    dialog.setMessage("删除该成员将取消对他的设备分享");
                }else {
                    dialog.setMessage("删除该成员将会删除他对您分享的设备");
                }

                dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener("删除", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                                if(type==1){
                                    for (int i = 0; i < list.size(); i++) {
                                        unSubscribe(list.get(i));
                                    }
//                                    HttpManage.getInstance().deleteUser(user.getInvite_code(), new MyCallback() {
//                                        @Override
//                                        public void onSuc(String result) {
//                                        }
//
//                                        @Override
//                                        public void onFail(int code, String msg) {
//                                            MyToast.show(ShareUserInfoActivity.this, MyToast.TYPE_ERROR, msg, 1);
//                                            dialog.dismiss();
//                                            finish();
//                                        }
//                                    });
                                } else {
                                    Log.e("tag",user.getInvite_code()+"");
                                    HttpManage.getInstance().cancelShare(user.getInvite_code(), new MyCallback() {
                                        @Override
                                        public void onSuc(String result) {
                                            Log.e("TAG,success:",result);
                                            MyToast.show(ShareUserInfoActivity.this, MyToast.TYPE_OK,"删除分享成功", 1);
                                            EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                                            dialog.dismiss();
                                            finish();
                                        }

                                        @Override
                                        public void onFail(int code, String msg) {
                                            Log.e("TAG,fail:",msg);
                                            MyToast.show(ShareUserInfoActivity.this, MyToast.TYPE_ERROR,msg, 1);
                                            EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                                }
                            }
                });
                dialog.show();
                break;
        }
    }

    private void unSubscribe(final Integer deviceId) {
        HttpManage.getInstance().unsubscribe(deviceId, new HttpManage.ResultCallback<Map<String,Object>>() {
            @Override
            public void onError(Header[] headers, HttpManage.Error error) {
                Log.e("fail",error.getCode()+"  info:"+error.getMsg());
                MyToast.show(ShareUserInfoActivity.this,MyToast.TYPE_ERROR,error.getMsg(),1);
            }

            @Override
            public void onSuccess(int code, Map<String, Object> response) {
                deleData(DeviceManage.getInstance().getDevice(deviceId));
                MyToast.show(ShareUserInfoActivity.this, MyToast.TYPE_OK, "删除成功", 1);
                EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                EventBus.getDefault().post(new EventData(EventData.CODE_GETDEVICE,""));
                EventBus.getDefault().post(new EventData(EventData.CODE_REFRESH_DEVICE,""));
                finish();
            }
        });
    }

    private void deleData(Device gateway) {
        DeviceManage.getInstance().removeDevice(gateway.getMac());
        DeviceManage.getInstance().removeCurrentdev(gateway);
        int device_id=gateway.getXDevice().getDeviceId();
        try {
            List<SubDevice> subs = App.db.selector(SubDevice.class).where("gateway_id","=",device_id).findAll();
            List<Sensor> sensors=App.db.selector(Sensor.class).where("device_id","=",device_id).findAll();
            List<Panel> panels=App.db.selector(Panel.class).where("gateway_id","=",device_id).findAll();
            List<Scenebean> scenebeen=App.db.selector(Scenebean.class).where("gateway_id","=",device_id).findAll();
            if(subs!=null){
                App.db.delete(subs);
                for (int i = 0; i < subs.size(); i++) {
                    String objectId = subs.get(i).getObjectId();
                    HttpManage.getInstance().deleSub(objectId,HttpManage.SUBTABLE, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {

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
                }

            }
            if(sensors!=null){
                App.db.delete(sensors);
                for (int i = 0; i < sensors.size(); i++) {
                    String objectId =sensors.get(i).getObjectId();
                    HttpManage.getInstance().deleSub(objectId, HttpManage.SENSORTABLE, new MyCallback() {
                        @Override
                        public void onSuc(String result) {

                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
                }
            }
            if(panels!=null){
                for (int i = 0; i < panels.size(); i++) {
                    PanelManage.getInstance().removePanel(panels.get(i).getMac());
                    String objectId =panels.get(i).getObjectId();
                    HttpManage.getInstance().deleSub(objectId, "panel", new MyCallback() {
                        @Override
                        public void onSuc(String result) {

                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
                }
            }
            if(scenebeen!=null){
                App.db.delete(scenebeen);
                for (int i = 0; i < scenebeen.size(); i++) {
                    Scenebean entity = scenebeen.get(i);
                    HttpManage.getInstance().deleSub(entity.getObjectId(), HttpManage.SCENETABLE, new MyCallback() {
                        @Override
                        public void onSuc(String result) {

                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
