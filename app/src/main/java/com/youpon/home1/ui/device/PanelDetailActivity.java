package com.youpon.home1.ui.device;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpon.home1.R;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.comm.base.BaseActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PanelDetailActivity extends BaseActivity implements View.OnClickListener {
    public String TAG=this.getClass().getSimpleName();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.set)
    ImageView set;
    @BindView(R.id.linear)
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panle_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        back.setOnClickListener(this);
        set.setOnClickListener(this);
        Panel panel = (Panel) getIntent().getSerializableExtra("panel");
        List<Panel.ChnlBean> chnlBeanList = panel.getChnlBeanList();
        Log.e(TAG,panel.getChnls()+"::::"+panel.getChnlBeanList().toString());
        if(chnlBeanList.size()<=4){
            for (int i = 0; i < chnlBeanList.size(); i++) {
                Panel.ChnlBean chnlBean = chnlBeanList.get(i);
                TextView textView = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                params.setMargins(30,30,30,30);
                textView.setLayoutParams(params);
                setName(chnlBean, textView);
                linear.addView(textView);
            }
        }else if(chnlBeanList.size()>4){
            for (int i = 0; i < chnlBeanList.size() / 4+1; i++) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j < 4; j++) {
                    if(i * 4 + j>=chnlBeanList.size())
                       break;
                    Panel.ChnlBean chnlBean = chnlBeanList.get(i * 4 + j);
                    Log.e(TAG,chnlBean.getChnl_id()+"");
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(30,30,30,30);
                    textView.setLayoutParams(params);
                    setName(chnlBean, textView);
                    linearLayout.addView(textView);
                }
                linear.addView(linearLayout);
            }
        }
    }

    private void setName(Panel.ChnlBean chnlBean, TextView textView) {
        String s=chnlBean.getConnected()==1?"已绑":"未绑";
        int chnl_id = chnlBean.getChnl_id();
        switch (chnlBean.getType()){
            case 0:
                textView.setText("情景"+chnl_id+"_"+s);
                break;
            case 1:
                textView.setText("风暖"+chnl_id+"_"+s);
                break;
            case 2:
                textView.setText("光暖"+chnl_id+"_"+s);
                break;
            case 4:
                textView.setText("照明"+chnl_id+"_"+s);
                break;
            case 5:
                textView.setText("照明"+chnl_id+"_"+s);
                break;
            case 8:
                textView.setText("换气"+chnl_id+"_"+s);
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
