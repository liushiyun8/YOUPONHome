package com.youpon.home1.bean;

/**
 * Created by liuyun on 2017/1/18.
 */
public class UserList {

    /**
     * enduserid : e32bd592-1bf8-11e6-a739-00163e0204c0
     * phone :
     * email : wzbdroid@126.com
     * nickname :
     * realname :
     * is_active : true
     * app : db456b4a-17fc-11e6-a739-00163e0204c0
     */

    private String enduserid;
    private String phone;
    private String email;
    private String nickname;
    private String realname;
    private boolean is_active;
    private String app;

    public String getEnduserid() {
        return enduserid;
    }

    public void setEnduserid(String enduserid) {
        this.enduserid = enduserid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
