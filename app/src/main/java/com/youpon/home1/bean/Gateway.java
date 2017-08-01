package com.youpon.home1.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2016/12/6.
 */
@Table(name = "gateway")
public class Gateway extends Devall {
    /**
     * device_pw : 8330
     * product_icon :
     * gatewaytype : 0
     * parentid : null
     * device_name : 爱焙客
     * is_sub : false
     * mac : D0BAE4500D11
     * role : 1
     * on_off : true
     * product_name : YA01
     * device_id : 42c93ede-94dc-11e6-9d95-00163e103941
     */
    @Column(name = "id",isId = true)
    int id;
    @Column(name = "room")
    private String room;
    @Column(name = "device_pw")
    private String device_pw;
    @Column(name = "product_ico")
    private String product_icon;
    @Column(name = "gatewaytype")
    private int gatewaytype;
    @Column(name = "parrentid")
    private String parentid;
    @Column(name = "device_name")
    private String device_name;
    @Column(name = "is_sub")
    private boolean is_sub;
    @Column(name = "mac")
    private String mac;
    @Column(name = "role")
    private int role;
    @Column(name = "on_off")
    private boolean online;
    @Column(name = "product_name")
    private String product_name;
    @Column(name = "device_id",property = "Unique")
    private String device_id;
    @Column(name = "isMain")
    private boolean isMain;
    @Column(name = "user")
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isMain() {
        return isMain;
    }

    @Override
    public String getName() {
        return device_name;
    }

    @Override
    public String getSID() {
        return device_id;
    }

    @Override
    public int getSort() {
        return 1;
    }

    @Override
    public int getType() {
        return -1;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getDevice_pw() {
        return device_pw;
    }

    public void setDevice_pw(String device_pw) {
        this.device_pw = device_pw;
    }
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
    public String getProduct_icon() {
        return product_icon;
    }

    public void setProduct_icon(String product_icon) {
        this.product_icon = product_icon;
    }

    public int getGatewaytype() {
        return gatewaytype;
    }

    public void setGatewaytype(int gatewaytype) {
        this.gatewaytype = gatewaytype;
    }

    public Object getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public boolean isIs_sub() {
        return is_sub;
    }

    public void setIs_sub(boolean is_sub) {
        this.is_sub = is_sub;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

}
