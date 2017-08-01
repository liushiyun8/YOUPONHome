package com.youpon.home1.ui.home.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.comm.base.BaseActivity;
import com.youpon.home1.comm.base.EventData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareQCodeActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.end)
    TextView end;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventDatas(EventData eventData) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_qcode);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventData(EventData.REFRESHDB, ""));
                finish();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareQCodeActivity.this,ShareActivity.class));
                finish();
            }
        });
        String code = getIntent().getStringExtra("code");
        createQRimg(new String(Base64.encode(code.getBytes(), Base64.DEFAULT)));
    }

    private void createQRimg(String text) {
        int QR_WIDTH = DeviceUtils.dip2px(160); // 图像宽度
        int QR_HEIGHT = DeviceUtils.dip2px(160); // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = null;// 生成矩阵
        try {
            bitMatrix = new MultiFormatWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
        for (int y = 0; y < QR_HEIGHT; y++) {
            for (int x = 0; x < QR_WIDTH; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * QR_WIDTH + x] = 0xff000000;
                } else {
                    pixels[y * QR_WIDTH + x] = 0xffffffff;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        icon.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
