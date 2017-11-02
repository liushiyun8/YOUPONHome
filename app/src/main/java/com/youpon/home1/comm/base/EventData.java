package com.youpon.home1.comm.base;

/**
 * Created by liuyun on 2016/12/6.
 */
public class EventData {
    public static final String REFRESHDB ="refresh_db" ;
    public static final int CODE_REFRESH_DEVICE=100;
    public static final int CODE_REFRESH_SENSOR=200;
    public static final int CODE_READ_STUTAS = 300;
    public static final int CODE_REFRESH_TASK = 400;
    public static final int CODE_GETDEVICE = 500;
    public static final int CODE_GETSCENE = 600;
    public static final int CODE_REFRESHLINK = 700;
    public static final int CODE_RECONNECT= 800;
    public static final int TRANSDATA = 50;
    public String Tag;
    public String massage;


    public Object data;
    public int code;
    public static final String TAG_REFRESH="refresh";



    public EventData(int code,String massage) {
        this.massage = massage;
        this.code = code;
    }

    public EventData(int code,Object data) {
        this.data = data;
        this.code = code;
    }

    public EventData(String tag, String massage) {
        Tag = tag;
        this.massage = massage;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
