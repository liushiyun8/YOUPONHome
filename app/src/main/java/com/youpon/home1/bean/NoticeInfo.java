package com.youpon.home1.bean;

/**
 * Created by liuyun on 2017/4/5.
 */
public class NoticeInfo {

    /**
     * type : 1
     * receive : true
     */
    private boolean receive;
    private int type;

    public NoticeInfo(int type, boolean receive) {
        this.receive = receive;
        this.type = type;
    }



    public boolean isReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
