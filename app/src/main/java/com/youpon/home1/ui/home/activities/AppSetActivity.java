package com.youpon.home1.ui.home.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.youpon.home1.R;
import com.youpon.home1.bean.User;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.comm.tools.XlinkUtils;
import com.youpon.home1.comm.view.CustomShapeImageView;
import com.youpon.home1.comm.view.MyDialog;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.ui.index.Index2Activity;
import com.youpon.home1.ui.user.FindpwdActivity;
import com.youpon.home1.ui.user.MainActivity;
import com.youpon.home1.ui.user.ResetpwdActivity;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppSetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.touxiang)
    CustomShapeImageView touxiang;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.resetIv)
    LinearLayout resetIv;
    @BindView(R.id.nickTv)
    TextView nickTv;
    @BindView(R.id.nickIv)
    LinearLayout nickIv;
    @BindView(R.id.exit)
    TextView exit;
//    @BindView(R.id.save)
//    TextView save;
    private PopupWindow popupWindow;
    private static final int PHOTO_CAMARE = 1;
    private static final int PHOTO_GALLARY = 2;
    private static final int PHOTO_RESULT = 3;
    private File tempFile;
    private Bitmap bitmap;
    private User user;
    private MyDialog myDialog;
    private EditText editText;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventDatas(EventData eventData) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_set);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
        initData();
    }

    private void initData() {
        try {
            user = App.db.selector(User.class).where("id", "=", Comconst.CURRENTUSER).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(user !=null){
            name.setText(user.getName());
            nickTv.setText(user.getNickname());
            if(user.getAvatar()!=null&& user.getAvatar()!=""){
                MyLog.e("BBBBB",user.getAvatar());
                Picasso.with(this).load(user.getAvatar()).into(touxiang);
            }
        }

    }

    private void init() {
        back.setOnClickListener(this);
        touxiang.setOnClickListener(this);
        resetIv.setOnClickListener(this);
        nickIv.setOnClickListener(this);
        nickTv.setOnClickListener(this);
        exit.setOnClickListener(this);
//        save.setOnClickListener(this);
        View pupView = LayoutInflater.from(this).inflate(R.layout.photo_select, null);
        View album = pupView.findViewById(R.id.album);
        View photo = pupView.findViewById(R.id.photo);
        View cancel = pupView.findViewById(R.id.cancel);
        album.setOnClickListener(this);
        photo.setOnClickListener(this);
        cancel.setOnClickListener(this);
        popupWindow = new PopupWindow(pupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);  getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if(bitmap!=null){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    HttpManage.getInstance().upLoadImg("jpg",out.toByteArray(), new MyCallback() {
                        @Override
                        public void onSuc(String result) {
                            try {
                                User user = App.db.selector(User.class).where("id", "=", App.getApp().appid).findFirst();
                                JSONObject jsonObject = new JSONObject(result);
                                user.setAvatar(jsonObject.optString("url"));
                                App.db.replace(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            EventBus.getDefault().post(new EventData(EventData.TAG_REFRESH,""));
                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
                }
                HttpManage.getInstance().modifyUser(App.getApp().appid, nickTv.getText().toString(), new HttpManage.ResultCallback<Map<String, Object>>() {
                    @Override
                    public void onError(Header[] headers, HttpManage.Error error) {
                        XlinkUtils.longTips("修改失败");
                    }

                    @Override
                    public void onSuccess(int code, Map<String, Object> response) {
                        user.setNickname(nickTv.getText().toString());
                        try {
                            App.db.replace(user);
                            EventBus.getDefault().post(new EventData(EventData.TAG_REFRESH,""));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        XlinkUtils.longTips("保存成功");
                    }
                });
                finish();
                break;
            case R.id.touxiang:
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                backgroundAlpha(0.5f);
                break;
            case R.id.album:
                gallery();
                if(popupWindow.isShowing())
                    popupWindow.dismiss();
                break;
            case R.id.photo:
                camera();
                if(popupWindow.isShowing())
                    popupWindow.dismiss();
                break;
            case R.id.cancel:
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.resetIv:
                startActivity(new Intent(this,ResetpwdActivity.class));
                break;
            case R.id.nickIv:
            case R.id.nickTv:
//                View view = LayoutInflater.from(this).inflate(R.layout.name_edit, null);
//                final EditText nickname = (EditText) view.findViewById(R.id.edit);
//                new AlertDialog.Builder(this).setTitle("修改昵称").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                nickTv.setText(nickname.getText().toString());
//                                dialog.dismiss();
//                            }
//                        }
//                ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).show();
                editText = new EditText(this);
                editText.setHint("请输入昵称");
                editText.setBackgroundResource(R.drawable.edit_bg);
                myDialog = new MyDialog(this);
                myDialog.setType(MyDialog.EDITTYPE);
                myDialog.setTitle("更改昵称");
                myDialog.setYesOnclickListener("确认", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        String s = myDialog.getEditText();
                        if("".equals(s)){
                            Toast.makeText(AppSetActivity.this,"昵称不能为空！",Toast.LENGTH_SHORT).show();
                        }else {
                            nickTv.setText(s);
                            myDialog.dismiss();
                            backgroundAlpha(1f);
                        }
                    }
                });
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
            case R.id.exit:
                myDialog=new MyDialog(this);
                myDialog.setType(MyDialog.MESSAGETYPE);
                myDialog.setTitle("温馨提示");
                myDialog.setMessage("是否退出当前账号？");
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                        backgroundAlpha(1f);
                    }
                });
                myDialog.setYesOnclickListener("退出", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        myDialog.dismiss();
                        App.getSp().remove(Comconst.ISAUTO);
                        sendBroadcast(new Intent("exit"));
                        Intent startMain = new Intent(AppSetActivity.this,Index2Activity.class);
//                startMain.addCategory(Intent.CATEGORY_HOME);
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);//回到登录界面。
                        finish();
                    }
                });
                myDialog.show();
                backgroundAlpha(0.5f);
                break;
        }

    }

    private void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    "tem_photo.jpg");
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, PHOTO_CAMARE);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_GALLARY);
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_GALLARY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_CAMARE) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_RESULT) {
            // 从剪切图片返回的数据
            if (data != null) {
                bitmap = data.getParcelableExtra("data");
                File file = new File(Environment.getExternalStorageDirectory(),
                        "photo.jpg");
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG,90,new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(user!=null){
                    user.setAvatar(file.getPath());
                }
                touxiang.setImageBitmap(bitmap);
            }
            try {
                // 将临时文件删除
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
