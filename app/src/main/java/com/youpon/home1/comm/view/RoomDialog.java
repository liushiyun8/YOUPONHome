package com.youpon.home1.comm.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.se7en.utils.DeviceUtils;
import com.youpon.home1.R;
import com.youpon.home1.bean.Roombean;
import com.youpon.home1.comm.App;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by liuyun on 2017/5/18.
 */
public class RoomDialog implements View.OnClickListener {

    private Dialog dialog;
    private String s="客厅";
    private Listener listener;

    public RoomDialog(Context context) {
        dialog = new Dialog(context,R.style.MyDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.folwdialog, null);
        final XCFlowLayout flow = (XCFlowLayout) view.findViewById(R.id.flowlay);
        flow.setMaxHeight(DeviceUtils.dip2px(180));
        try {
            List<Roombean> rooms = App.db.findAll(Roombean.class);
            if(rooms!=null){
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = DeviceUtils.dip2px(10);
                lp.rightMargin =DeviceUtils.dip2px(10);
                lp.topMargin =DeviceUtils.dip2px(10);
                lp.bottomMargin =DeviceUtils.dip2px(10);
                for (int i = 0; i <rooms.size() ; i++) {
                    TextView textView = new TextView(context);
                    textView.setText(rooms.get(i).getName());
                    textView.setTextColor(Color.parseColor("#6e7aa0"));
                    textView.setLayoutParams(lp);
                    textView.setBackgroundResource(R.drawable.text_bg);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView tv= (TextView) v;
                            for (int j = 0; j < flow.getChildCount(); j++) {
                                flow.getChildAt(j).setSelected(false);
                            }
                            v.setSelected(true);
                            s =tv.getText().toString();
                        }

                    });
                    flow.addView(textView);
                }
    }
} catch (DbException e) {
        e.printStackTrace();
        }
        view.findViewById(R.id.yes).setOnClickListener(this);
        view.findViewById(R.id.no).setOnClickListener(this);
        dialog.setContentView(view);
    }

    public void show(Listener listener){
        this.listener=listener;
        if(dialog!=null){
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes:
                if(listener!=null){
                    listener.yes(s);
                }
                dialog.dismiss();
                break;
            case R.id.no:
                dialog.dismiss();
                break;
        }
    }

   public interface Listener{
        void yes(String s);
    }
}
