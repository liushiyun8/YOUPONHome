package com.youpon.home1.bean;

import com.youpon.home1.comm.Comconst;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2016/12/12.
 */
@Table(name = "room")
public class Roombean {
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "name",property = "Unique")
    String name;

    public Roombean() {
    }

    public Roombean(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
