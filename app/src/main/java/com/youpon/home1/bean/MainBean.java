package com.youpon.home1.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2016/12/13.
 */

@Table(name = "mainbean")
public class MainBean {
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "sort")
    int sort;
    @Column(name = "sid",property = "Unique")
    String sid;
    @Column(name = "type")
    int type;

    @Column(name = "deviceId")
    int deviceId;
    @Column(name = "order")
    int order;
    @Column(name = "isDelete")
    boolean isDelete;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type=type;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public MainBean() {
    }



    public MainBean(int sort, String sid, int type,int deviceId) {
        this.sort = sort;
        this.sid = sid;
        this.type = type;
        this.deviceId=deviceId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        return "MainBean{" +
                "objectId='" + objectId + '\'' +
                ", sort=" + sort +
                ", sid='" + sid + '\'' +
                ", type=" + type +
                ", order=" + order +
                '}';
    }
}
