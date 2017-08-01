package com.youpon.home1.bean;

import com.youpon.home1.comm.Comconst;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2016/12/9.
 */
@Table(name = "scenedevice")
public class SceneDevice {
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "sid")
    String sid;
    @Column(name = "gatewang_id")
    int gatewang_id;
    @Column(name = "sceneid")
    int sceneid;
    @Column(name = "type")
    int type;
    @Column(name = "dst")
    int dst=1;
    @Column(name = "value1")
    int value1;
    @Column(name = "value2")
    int value2;
    @Column(name = "control")
    boolean control;
    @Column(name = "gateway_type")
    int gateway_type;

    public int getGateway_type() {
        return gateway_type;
    }

    public void setGateway_type(int gateway_type) {
        this.gateway_type = gateway_type;
    }

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public int getSceneid() {
        return sceneid;
    }

    public void setSceneid(int sceneid) {
        this.sceneid = sceneid;
    }

    public SceneDevice() {
    }

    public SceneDevice(int type, int tap2, int tap,int gatewang_id, int gateway_type, boolean control) {
        this.type = type;
        this.value2 = tap2;
        this.value1 = tap;
        this.gatewang_id=gatewang_id;
        this.gateway_type = gateway_type;
        this.control = control;
    }

    @Override
    public String toString() {
        return "SceneDevice{" +
                ", sceneid=" + sceneid +
                ", type=" + type +
                ", dst=" + dst +
                ", tap=" +value1 +
                ", tap2=" + value2 +
                ", control=" + control +
                '}';
    }

    public SceneDevice(String id, String name, String roomNum, int gatewang_id, int type, int tap2, int tap,int gateway_type) {
        this.gatewang_id = gatewang_id;
        this.type = type;
        this.value2 = tap2;
        this.value1 = tap;
        this.gateway_type=gateway_type;
    }

    public SceneDevice(String id, String name, String roomNum, int gatewang_id, int type, int tap2, int tap, int gateway_type, boolean control) {
        this.gatewang_id = gatewang_id;
        this.type = type;
        this.value2 = tap2;
        this.value1 = tap;
        this.gateway_type = gateway_type;
        this.control = control;
    }
    public int getGatewang_id() {
        return gatewang_id;
    }

    public void setGatewang_id(int gatewang_id) {
        this.gatewang_id = gatewang_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDst() {
        return dst;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int tap) {
        this.value1 = tap;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int tap2) {
        this.value2 = tap2;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
