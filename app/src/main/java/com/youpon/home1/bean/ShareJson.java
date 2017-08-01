package com.youpon.home1.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by liuyun on 2017/6/5.
 */
@Table(name = "sharejson")
public class ShareJson implements Serializable {

    /**
     * expire_date : 1496744504621
     * visible : 0
     * state : accept
     * gen_date : 1496740904621
     * device_id : 1234694962
     * id : 2a0fa6b26ce85000
     * share_mode : qrcode
     * from_name : 流云
     * from_id : 845838381
     * from_user : 15278585197
     * invite_code : 120fa6b26ce85001
     * to_user : 13837121217
     * user_id : 845832204
     * to_name : 友邦小智
     */

    private long expire_date;
    private int visible;
    private String state;
    private long gen_date;
    @Column(name = "device_id")
    private int device_id;
    @Column(name = "id",isId =true)
    private String id;
    @Column(name = "share_mode")
    private String share_mode;
    @Column(name = "from_name")
    private String from_name;
    @Column(name = "from_id")
    private int from_id;
    @Column(name = "from_user")
    private String from_user;
    @Column(name = "invite_code")
    private String invite_code;
    @Column(name = "to_user")
    private String to_user;
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "to_name")
    private String to_name;
    @Column(name = "beizu")
    private String beizu;
    @Column(name = "avater")
    String avater;

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getBeizu() {
        return beizu;
    }

    public void setBeizu(String beizu) {
        this.beizu = beizu;
    }

    public long getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(long expire_date) {
        this.expire_date = expire_date;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getGen_date() {
        return gen_date;
    }

    public void setGen_date(long gen_date) {
        this.gen_date = gen_date;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShare_mode() {
        return share_mode;
    }

    public void setShare_mode(String share_mode) {
        this.share_mode = share_mode;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }
}
