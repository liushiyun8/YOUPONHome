package com.youpon.home1.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuyun on 2017/6/22.
 */
@Table(name = "shareuser")
public class ShareUser implements Serializable {
    @Column(name = "id",isId = true)
    int id;
    @Column(name = "user_id",property = "Unique")
    private int user_id;
    @Column(name = "invite_code")
    private String invite_code;
    @Column(name = "user")
    private String user;
    @Column(name = "name")
    private String name;
    @Column(name = "beizu")
    private String beizu;
    @Column(name = "avater")
    String avater;

    public ShareUser() {
    }

    private Set<Integer> set=new HashSet<>();

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeizu() {
        return beizu;
    }

    public void setBeizu(String beizu) {
        this.beizu = beizu;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public Set<Integer> getSet() {
        return set;
    }

    public void setSet(Set<Integer> set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return "ShareUser{" +
                "user_id=" + user_id +
                ", invite_code='" + invite_code + '\'' +
                ", user='" + user + '\'' +
                ", name='" + name + '\'' +
                ", beizu='" + beizu + '\'' +
                ", avater='" + avater + '\'' +
                ", set=" + set +
                '}';
    }
}
