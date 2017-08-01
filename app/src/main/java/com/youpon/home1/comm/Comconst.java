package com.youpon.home1.comm;

import com.youpon.home1.R;

/**
 * Created by computer on 2016/11/24.
 */
public class Comconst {
    public static final String USERID="appId";
    public static final String HOST="https://v2.fogcloud.io";
//    public static final String APPID="192edb0e-b149-11e6-9baf-00163e120d98";
    public static final String APPID="2e0fa2b0b1227400";
    public static final String SP_FILE = "youpon_sp";
    public static final String TOKEN="token";
    public static final String CLIENTID="clientid";
    public static final String ISAUTO="isauto";

    public static final String PWD = "password";
    public static final String NAME = "name";

    public static final int[] IMAGETYPE={R.mipmap.equ_ic_warmwind,R.mipmap.equ_ic_warmlight,R.mipmap.equ_ic_lighting,R.mipmap.equ_ic_breath,R.mipmap.equ_ic_beiyong,R.mipmap.equ_ic_scene};
    public static final int[] SENSORTYPE={R.mipmap.equ_ic_infrared,R.mipmap.equ_ic_sensorlight,R.mipmap.equ_ic_humiture,R.mipmap.equ_ic_humiture,R.mipmap.equ_ic_co2,R.mipmap.equ_ic_air,R.mipmap.equ_ic_gas,R.mipmap.equ_ic_smoke};

    public static final int[] SENSORTYPEON={R.mipmap.equ_btn_infrared_on,R.mipmap.equ_btn_sensorlight_on,R.mipmap.equ_btn_humiture_on,R.mipmap.equ_btn_humiture_on,R.mipmap.equ_btn_co2_on,R.mipmap.equ_btn_air_on,R.mipmap.equ_btn_gas_on,R.mipmap.equ_btn_smoke_on};
    public static final int[] SENSORTYPEOFF={R.mipmap.equ_btn_infrared_drops,R.mipmap.equ_btn_sensorlight_drops,R.mipmap.equ_btn_humiture_drops,R.mipmap.equ_btn_humiture_drops,R.mipmap.equ_btn_co2_drops,R.mipmap.equ_btn_air_drops,R.mipmap.equ_btn_gas_drops,R.mipmap.equ_btn_smoke_drops};

    public static final int[] IMAGETYPEON={R.mipmap.equ_btn_warmwind_on,R.mipmap.equ_btn_warmlight_off,R.mipmap.equ_btn_lighting_on,R.mipmap.equ_btn_breath_on,R.mipmap.equ_btn_scene_drops_on};
    public static final int[] IMAGETYPEOFF={R.mipmap.equ_btn_warmwind_off,R.mipmap.equ_btn_warmlight_on,R.mipmap.equ_btn_lighting_off,R.mipmap.equ_btn_breath_off,R.mipmap.equ_btn_scene_drops_off};
    public static final int[] MAINTYPEON={R.mipmap.equ_btn_warmwind_on,R.mipmap.equ_btn_warmlight_on,R.mipmap.equ_btn_lighting_on,R.mipmap.equ_btn_breath_on,R.mipmap.equ_btn_beiyong_on,R.mipmap.equ_btn_scene_drops_on};
    public static final int[] MAINTYPEOFF={R.mipmap.equ_btn_warmwind_off,R.mipmap.equ_btn_warmlight_off,R.mipmap.equ_btn_lighting_off,R.mipmap.equ_btn_breath_off,R.mipmap.equ_btn_beiyong_off,R.mipmap.equ_btn_beiyong_off,R.mipmap.equ_btn_scene_drops_off};
    public static final int[] MAINTYPEOUT={R.mipmap.equ_btn_warmwind_drops,R.mipmap.equ_btn_warmlight_drops,R.mipmap.equ_btn_lighting_drops,R.mipmap.equ_btn_breath_drops,R.mipmap.equ_btn_beiyong_drops,R.mipmap.equ_btn_scene_drops};
    public static final String LASTTIME ="lasttime" ;
    public static final String[] SCENETEXT ={"开启照明，暖风全开工作12分钟后，蜂鸣器鸣叫20秒，关闭暖风后，转为光波全开","全天换气模式:选择此模式后，相应的背光灯点亮，负载工作逻辑如下:全天每四个小时打开换气（1档）10分钟","冷干燥模式:选择此模式后，相应的背光灯点亮，负载工作逻辑如下：打开换气1档和冷风5分钟，之后湿度如果大于50%，延时1小时后关闭，小于50%则立刻关闭"} ;
    public static int CURRENTUSER;
}
