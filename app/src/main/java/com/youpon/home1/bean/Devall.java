package com.youpon.home1.bean;

import java.io.Serializable;

/**
 * Created by liuyun on 2016/12/14.
 */
public abstract class Devall implements Serializable{
    public abstract void setMain(boolean s);
    public abstract boolean isMain();
    public abstract String getName();
    public abstract String getSID();
    public abstract int getSort();
    public abstract int getType();
    public abstract void setRoom(String room);
    public abstract String getRoom();
    public abstract boolean isOnline();
}
