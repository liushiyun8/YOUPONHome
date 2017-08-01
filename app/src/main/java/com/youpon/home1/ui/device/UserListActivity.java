package com.youpon.home1.ui.device;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youpon.home1.R;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.UserList;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.JsonHelper;
import com.youpon.home1.ui.adpter.CommonAdapter;
import com.youpon.home1.ui.adpter.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fog.callbacks.MiCOCallBack;
import io.fog.fog2sdk.MiCODevice;

public class UserListActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.role)
    TextView roleTV;
    private MiCODevice miCODevice;
    List<UserList> lists = new ArrayList<>();
    private CommonAdapter<UserList> commonAdapter;
    private int role;
    private String TAG = getClass().getSimpleName();
    private String device_id;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        miCODevice = new MiCODevice(this);
        init();
    }

    private void init() {
        device_id = getIntent().getStringExtra("device_id");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        token = (String) App.getSp().get(Comconst.TOKEN, "");
        try {
            Gateway gateway = App.db.selector(Gateway.class).where("device_id", "=", device_id).findFirst();
            if (gateway != null) {
                role = gateway.getRole();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        switch (role){
            case 1:
                roleTV.setText("你是超级用户");
                break;
            case 2:
                roleTV.setText("你是管理员");
                break;
            default:
                roleTV.setText("你是普通用户,无权查询所有用户");
                break;
        }
        miCODevice.getMemberList(device_id, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.e("SSSUUCC",message);
                String fogData = JsonHelper.getFogData(message);
                try {
                    JSONArray jsonArray = new JSONArray(fogData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        Gson gson = new Gson();
                        UserList userList = gson.fromJson(jsonObject.toString(), UserList.class);
                        lists.add(userList);
                    }
                    commonAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e("fail",code+message);
            }
        }, token);
        commonAdapter = new CommonAdapter<UserList>(this, lists, R.layout.userlist_item) {
            @Override
            public void convert(ViewHolder helper, final int position, final UserList item) {
                helper.setText(R.id.phone, item.getPhone());
                helper.setText(R.id.mail, item.getEmail());
                helper.setText(R.id.nike, item.getNickname());
                helper.setText(R.id.real, item.getRealname());
                helper.setText(R.id.status, item.isIs_active() ? "在线" : "离线");
                View remove = helper.getView(R.id.remove);
                remove.setVisibility((role == 1 || role == 2) ? View.VISIBLE : View.GONE);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeRole(item.getEnduserid(), position);
                    }
                });
            }
        };
        lv.setAdapter(commonAdapter);
    }

    private void removeRole(String menduserid, final int position) {
        miCODevice.removeBindRole(device_id, menduserid, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(UserListActivity.this, message, Toast.LENGTH_LONG).show();
                Log.d(TAG, message);
                lists.remove(position);
                commonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(UserListActivity.this, message, Toast.LENGTH_LONG).show();
                Log.d(TAG, message);
            }
        }, token);

    }
}
