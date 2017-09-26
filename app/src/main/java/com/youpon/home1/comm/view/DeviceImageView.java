package com.youpon.home1.comm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youpon.home1.R;

/**
 * Created by liuyun on 2017/5/14.
 */
public class DeviceImageView extends ImageView {

    private Paint paint;
    int[] imagesOff={R.mipmap.equ_img_warmwind_off,R.mipmap.equ_img_warmlight_off,R.mipmap.equ_img_lighting_off,R.mipmap.equ_img_breath_off,R.mipmap.equ_img_backup_off};
    int[] imagesON1={R.mipmap.equ_img_warmwind_on1,R.mipmap.equ_img_warmlight_on,R.mipmap.equ_img_lighting_on,R.mipmap.equ_img_breath_on3,R.mipmap.equ_img_backup_on};
    int warmwind_on2=R.mipmap.equ_img_warmwind_on2;
    int warmwind_on3=R.mipmap.equ_img_warmwind_on3;

    int infrared[]={R.mipmap.equ_img_infrared_noone1,R.mipmap.equ_img_infrared_soone1};
    int sensorLight[]={R.mipmap.equ_img_sensorlight1,R.mipmap.equ_img_sensorlight2};
    int smoke[]={R.mipmap.equ_img_smoke1,R.mipmap.equ_img_smoke2};
    int air1[]={R.mipmap.equ_img_air_green1,R.mipmap.equ_img_air_blue1,R.mipmap.equ_img_air_yellow1,R.mipmap.equ_img_air_red1};
    int air2[]={R.mipmap.equ_img_air_green2,R.mipmap.equ_img_air_blue2,R.mipmap.equ_img_air_yellow2,R.mipmap.equ_img_air_red2};
    int temper[]={R.mipmap.equ_img_humiture,R.mipmap.equ_img_humiture2};
    int fontColor[]={Color.parseColor("#47ffa6"),Color.parseColor("#4791ff"),Color.parseColor("#f6ab00"),Color.parseColor("#ff4768")};

    int currentColor=fontColor[0];

    private Bitmap temp1;
    private Bitmap temp2;
    private Bitmap temp3;

    private int type=0;
    private int width;
    private int height;
    private int status;
    private long time=1000;
    private int level;
    private float scale=0.7f;  //光感的圆盘转率；其他的按照实际情况设置；
    private int maxValue=400;

    public void setSort(int sort) {
        this.sort = sort;
    }

    private int sort;

    private String s="";
    private String unit="";
    private int deep;

    public DeviceImageView(Context context) {
        this(context,null);
    }

    public DeviceImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DeviceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        temp3=BitmapFactory.decodeResource(getResources(),R.mipmap.equ_img_gas);
    }

    public void setType(int type){
        this.type=type;
    }

    public void setDeviceLevel(int progress){
        level =progress;
        status=0;
        if(sort==0){
            if(progress==0){
                setImageResource(imagesOff[type]);
            }else
                switch (type){
                    case 0:
                        if(progress==1){
                            setImageResource(imagesON1[0]);
                        }else if(progress==2)
                            setImageResource(warmwind_on2);
                        else if(progress==3){
                            setImageResource(warmwind_on3);
                        }
                        break;
                    case 1:
                        status=1;
                        temp1=BitmapFactory.decodeResource(getResources(),imagesON1[1]);
                        invalidate();
                        break;
                    case 2:
                        setImageResource(imagesON1[2]);
                        break;
                    case 4:
                        setImageResource(imagesON1[4]);
                        break;
                    case 3:
                        if(progress==1){
                            time=1000;
                            setImageResource(imagesON1[type]);
                        }else if(progress==2){
                            time=500;
                            setImageResource(imagesON1[type]);
                        }
                        break;
                }
        }else if(sort==1){
            switch (type){
                case 1:
                    setImageResource(infrared[progress]);
                    break;
                case 2:
                    status=1;
                    temp1=BitmapFactory.decodeResource(getResources(),sensorLight[0]);
                    temp2=BitmapFactory.decodeResource(getResources(),sensorLight[1]);
                    getStatus();
                    invalidate();
                    break;
                case 3:
                case 4:
                    status=1;
                    getStatus();
                    temp1=BitmapFactory.decodeResource(getResources(),temper[0]);
                    temp2=BitmapFactory.decodeResource(getResources(),temper[1]);
                    invalidate();
                    break;
                case 5:
                case 7:
                case 8:
                    status=1;
                    getStatus();
                    temp1=BitmapFactory.decodeResource(getResources(),smoke[0]);
                    temp2=BitmapFactory.decodeResource(getResources(),smoke[1]);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp2px(252)+temp3.getHeight(), dp2px(252)+temp3.getHeight());
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    setLayoutParams(params);
                    invalidate();
                    break;
                case 6:
                    status=1;
                    getStatus();
                    temp1=BitmapFactory.decodeResource(getResources(),air1[deep]);
                    temp2=BitmapFactory.decodeResource(getResources(),air2[deep]);
                    currentColor=fontColor[deep];
                    invalidate();
                    break;

            }
        }
    }

    public void setDeviceStatus(int status){
        if(type==0||type==3){
            if(status==1)
            rotate(time);
            else clearAnimation();
        }
    }

    private void rotate(long time) {
        RotateAnimation animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        animation.setDuration(time);
        startAnimation(animation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(status==0){
            super.onDraw(canvas);
        }else {
            width = getWidth();
            height = getHeight();
            float centerX=width/2.0f;
            float centerY=height/2.0f;
            int size;
            if(width>height){
                size=height;
            }else size=width;
            float radios=size/2.0f;
            scale=270.0f/maxValue;
            if(sort==0){
                paint.setStrokeWidth(dp2px(1));
                paint.setColor(Color.WHITE);
                canvas.save();
                int count= (int) (1.2f*level);
                for (int i = 0; i <count ; i++) {
                    canvas.drawLine(centerX,0,centerX,dp2px(5),paint);
                    canvas.rotate(3,centerX,centerY);
                }
                canvas.restore();
                paint.setColor(Color.parseColor("#f6ab00"));
                paint.setStrokeWidth(dp2px(7));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(centerX,centerY,radios-dp2px(13),paint);
                canvas.drawBitmap(temp1,(width-temp1.getWidth())/2,(height-temp1.getHeight())/2,paint);
            }else if(sort==1){
                float startAngle=135;
                if(type==6||type==4||type==3)
                    startAngle=90;
                if(type==4){
                    scale=360.f/maxValue;
                }else if(type==3){
                    scale=360.f/160;
                }
                paint.setStrokeWidth(1);
                paint.setColor(Color.GREEN);
//                Log.e("TAGPaint","width:"+width+" height:"+height+" radio:"+radios);
//                canvas.drawCircle(centerX,centerY,radios,paint);
                canvas.drawBitmap(temp1,centerX-temp1.getWidth()/2,centerY-temp1.getHeight()/2,paint);
                int flag = canvas.saveLayer(0, 0, width, height, paint);
                if(type==3){
                    canvas.drawArc(centerX-temp2.getWidth()/2.0f,centerY-temp2.getHeight()/2.0f,centerX+temp2.getWidth()/2.0f,centerY+temp2.getHeight()/2.0f,startAngle,((level+40)<maxValue?(level+40):maxValue)*scale,true,paint);
                }else
                canvas.drawArc(centerX-temp2.getWidth()/2.0f,centerY-temp2.getHeight()/2.0f,centerX+temp2.getWidth()/2.0f,centerY+temp2.getHeight()/2.0f,startAngle,(level<maxValue?level:maxValue)*scale,true,paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(temp2,null,new RectF(centerX-temp2.getWidth()/2,centerY-temp2.getHeight()/2,centerX+temp2.getWidth()/2,centerY+temp2.getHeight()/2),paint);
                canvas.restoreToCount(flag);
                paint.setXfermode(null);
                if(type==5||type==7||type==8){
                    canvas.save();
                    canvas.rotate((level<maxValue?level:maxValue)*scale-startAngle,centerX,centerY);
                    canvas.drawBitmap(temp3,centerX-temp3.getWidth(),2,paint);
                    canvas.restore();
                }
                int unitColor=Color.parseColor("#47506c");
                int levelColor=Color.parseColor("#bad3f8");
                int sColor=Color.parseColor("#bad3f9");
                int levelSize=60;
                paint.setTextAlign(Paint.Align.CENTER);
                if(type==6){
                    levelSize=40;
                    unitColor=levelColor=sColor=currentColor;
                    paint.setTextSize(dp2px(14));
                    paint.setColor(sColor);
                    canvas.drawText(s,centerX,centerY+dp2px(38),paint);
                    paint.setColor(unitColor);
                    paint.setTextSize(dp2px(10));
                    canvas.drawText(unit,centerX,centerY-dp2px(27),paint);
                }else if(type==3||type==4){
                    levelSize=40;
                    paint.setTextSize(dp2px(14));
                    Paint.FontMetricsInt f= paint.getFontMetricsInt();
                    int baseY=height-dp2px(47) -(f.bottom-f.top)/2- f.top;
                    paint.setColor(sColor);
                    canvas.drawText(s,centerX,baseY,paint);
                    paint.setColor(unitColor);
                    paint.setTextSize(dp2px(12));
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(unit,centerX+dp2px(30),centerY-dp2px(20),paint);
                }else {
                    paint.setColor(Color.argb(23,176,205,251));
                    paint.setStrokeWidth(dp2px(2));
                    canvas.drawRoundRect(centerX-dp2px(25),height-dp2px(48),centerX+dp2px(25),height-dp2px(20),dp2px(6),dp2px(6),paint);
                    paint.setTextSize(dp2px(16));
                    Paint.FontMetricsInt f= paint.getFontMetricsInt();
                    int baseY=height-dp2px(34) -(f.bottom-f.top)/2- f.top;
                    paint.setColor(sColor);
                    canvas.drawText(s,centerX,baseY,paint);
                    paint.setColor(unitColor);
                    paint.setTextSize(dp2px(18));
                    canvas.drawText(unit,centerX,centerY-dp2px(40),paint);
                }
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(dp2px(levelSize));
                paint.setColor(levelColor);
                Paint.FontMetricsInt fm= paint.getFontMetricsInt();
                float baseLineY = centerY -(fm.bottom-fm.top)/2- fm.top;
                if(type==3||type==4){
                    paint.setTextSize(dp2px(levelSize));
                    baseLineY = centerY-dp2px(10) -(fm.bottom-fm.top)/2- fm.top;
                }
                canvas.drawText(level+"",centerX,baseLineY,paint);
            }

//            canvas.drawBitmap();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imagesOff[type]);
//                paint.setColor(Color.RED);
//                canvas.drawCircle(width/2,height/2,size/2,paint);
//                paint.setColor(Color.GREEN);
//                canvas.drawCircle(width/2,height/2,size/2-dp2px(20),paint);
//                canvas.drawBitmap(bitmap,(width-bitmap.getWidth())/2,(height-bitmap.getHeight())/2,paint);
        };
    }

    public int dp2px(int dp) {
        return (int) ((getResources().getDisplayMetrics().density * dp) + 0.5);
    }

    public void getStatus(){
        switch (type){
            case 1:
                s=level==0?"无人":"有人";
                break;
            case 2:
                unit="Lux";
                maxValue=1200;
                if(level<=20){
                    s="昏暗";
                }else if(level>20&&level<=200){
                    s="柔弱";
                }else if(level>200&&level<=800){
                    s="明亮";
                }else if(level>800){
                    s="强光";
                }
                break;
            case 3:
                unit="°";
                maxValue=120;
                if(level<=0){
                    s="寒冷";
                }else if(level>0&&level<=14){
                    s="冰凉";
                }else if(level>14&&level<=30){
                    s="舒适";
                }else if(level>30){
                    s="炎热";
                }
                break;
            case 4:
                unit="%";
                maxValue=100;
                if(level<=39){
                    s="干燥";
                }else if(level>39&&level<=69){
                    s="适中";
                }else if(level>69){
                    s="潮湿";
                }
                break;
            case 5:
                maxValue=3000;
                unit="ppm";
                if(level<=499){
                    s="清新";
                }else if(level>499&&level<=999){
                    s="良好";
                }else if(level>1000&&level<=1999){
                    s="浑浊";
                }else if(level>1999){
                    s="严重";
                }
                break;
            case 6:
                unit="ppb";
                maxValue=250;
                if(level<=50){
                    s="优";
                    deep=0;
                }else if(level>50&&level<=100){
                    s="良";
                    deep=1;
                }else if(level>100&&level<=200){
                    s="中";
                    deep=2;
                }else if(level>200){
                    s="差";
                    deep=3;
                }
                break;
            case 7:
                unit="ppm";
                maxValue=1200;
                if(level<=100){
                    s="正常";
                }else if(level>100&&level<=400){
                    s="轻度";
                }else if(level>400&&level<=800){
                    s="中度";
                }else if(level>800){
                    s="重度";
                }
                break;
            case 8:
                maxValue=1200;
                unit="ppm";
                if(level<=100){
                    s="正常";
                }else if(level>100&&level<=200){
                    s="轻度";
                }else if(level>200&&level<=600){
                    s="中度";
                }else if(level>600){
                    s="重度";
                }
                break;
        }
    }
}
