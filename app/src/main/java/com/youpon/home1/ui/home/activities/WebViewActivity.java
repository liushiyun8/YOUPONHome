package com.youpon.home1.ui.home.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.youpon.home1.R;
import com.youpon.home1.comm.base.BaseActivity;

public class WebViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        final WebView webv = (WebView) findViewById(R.id.webv);
        webv.setWebViewClient(new WebViewClient());
        webv.getSettings().setJavaScriptEnabled(true);
        ImageView back= (ImageView) findViewById(R.id.back);
        webv.loadUrl("http://www.chinayoubang.com");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webv.canGoBack()){
                    webv.goBack();
                }else finish();
            }
        });
    }
}
