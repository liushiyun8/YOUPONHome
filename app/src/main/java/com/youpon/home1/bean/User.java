package com.youpon.home1.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by computer on 2016/11/29.
 */
@Table(name = "user")
public class User {
    public User() {
    }
    public User(int id,String name, String pwd) {
        this.id=id;
        this.name = name;
        this.pwd = pwd;
    }

    public User(int id, String name, String avatar, String nickname, int gender, int age) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }

    public User(int id, String name, String nickname, int gender, int age) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }



    @Column(isId = true,name = "ip")
    int ip;
    @Column(name = "id",property = "Unique")
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "phone")
    String phone;
    @Column(name = "email")
    String email;
    @Column(name = "password")
    String pwd;
    @Column(name = "avatar")
    String avatar;
    @Column(name = "nickname")
    String nickname;
    @Column(name = "gender")
    int gender;
    @Column(name = "age")
    int age;
    @Column(name = "setting")
    String setting;
    @Column(name = "account")
    String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        this.name=account;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", setting='" + setting + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
