package com.youpon.home1.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2017/2/17.
 */
@Table(name = "spacebean")
public class SpaceBean {
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "sort")
    int sort;
    @Column(name = "sid",property = "Unique")
    String sid;
    @Column(name = "type")
    int type;
    @Column(name = "room")
    String room;
    @Column(name = "isdele")
    boolean isdele;
    @Column(name = "order")
    int order;


    public SpaceBean() {
    }

    public SpaceBean(int sort, String sid, int type, String room) {
        this.sort = sort;
        this.sid = sid;
        this.type = type;
        this.room = room;
    }

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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean isdele() {
        return isdele;
    }

    public void setIsdele(boolean isdele) {
        this.isdele = isdele;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "SpaceBean{" +
                ", sort=" + sort +
                ", sid='" + sid + '\'' +
                ", type=" + type +
                ", room='" + room + '\'' +
                '}';
    }
}
