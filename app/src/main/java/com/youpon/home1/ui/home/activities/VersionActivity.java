package com.youpon.home1.ui.home.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.se7en.utils.SystemUtil;
import com.youpon.home1.R;
import com.youpon.home1.bean.APPInfo;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.tools.FileUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VersionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.currentV)
    TextView currentV;
    @BindView(R.id.describe)
    TextView describe;
    @BindView(R.id.update)
    TextView update;
    private APPInfo appinfo;
    private String TAG="VersionActivity";
    private String path;
    private File appFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        update.setOnClickListener(this);
        path= FileUtils.getInstance().getSDPath()+File.separator+"YouPonDowload";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            currentV.setText(SystemUtil.getSystemVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        appinfo = (APPInfo) getIntent().getSerializableExtra("appinfo");
        if(appinfo !=null){
            describe.setText(appinfo.getIllustration());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.update:
                if(appinfo!=null){
                    if(!appinfo.getVersion().equals(SystemUtil.getSystemVersion())){
                        showUpdateDialog();
                    }else {
                        showCancleDialog();
                    }
                }
                break;
        }

    }

    private void showCancleDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this) ;
        builer.setTitle("版本升级");
        builer.setMessage("已经是最新版本，无需更新！");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"已经是最新版本，无需更新！");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this) ;
        builer.setTitle("版本升级");
        builer.setMessage(appinfo.getIllustration());
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"下载apk,更新");
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        RequestParams entity = new RequestParams(appinfo.getUrl());
        appFile = new File(path + appinfo.getUrl());
        entity.setSaveFilePath(appFile.getAbsolutePath());
        x.http().get(entity, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                MyLog.e("HHHH","total:"+total+"curent:"+current);
                    pd.setMax((int) total);
                    pd.setProgress((int) current);
                    if(current>=total){
                    currentV.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            installApk(appFile);
                        }
                    },3000);
                }
            }

            @Override
            public void onSuccess(File file) {

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

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
