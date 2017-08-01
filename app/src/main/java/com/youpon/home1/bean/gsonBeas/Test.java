package com.youpon.home1.bean.gsonBeas;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2017/4/7.
 */
@Table(name = "Test")
public class Test {
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "name")
    String name;
    @Column(name = "user")
    int user;
    @Column(name = "updateAt")
    String updateAt;
    String createAt;

    public Test() {
    }

    public Test(String name) {
        this.name = name;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
