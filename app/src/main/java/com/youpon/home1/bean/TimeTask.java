package com.youpon.home1.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2017/1/11.
 */
@Table(name = "timetask")
public class TimeTask {
    @Column(name = "index",isId = true)
    int index;
    @Column(name = "taskid",property = "Unique")
    String taskid;
    @Column(name = "devisort_id")
    String devisort_id;
    @Column(name = "device_id")
    int device_id;
    @Column(name = "week")
    String week;
    @Column(name = "hour")
    String hour;
    @Column(name = "minute")
    String minute;
    @Column(name = "src")
    int src;
    @Column(name = "tap")
    int tap;
    @Column(name = "tap2")
    int tap2;
    @Column(name = "on_off")
    boolean on_off=true;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Column(name = "command")
    String command;

    public TimeTask() {
    }

    public TimeTask(String taskid, int device_id, String week, String hour, String minute, boolean on_off, String command) {
        this.taskid = taskid;
        this.device_id = device_id;
        this.week = week;
        this.hour = hour;
        this.minute = minute;
        this.on_off = on_off;
        this.command = command;
        int weizi = command.lastIndexOf("0x");
        this.devisort_id=command.substring(weizi +2,weizi+6);
        this.src=Integer.parseInt(command.substring(weizi+9,weizi+10));
    }

    public TimeTask(String taskid, String devisort_id,int device_id, String week, String hour, String minute, int src, int tap, int tap2) {
        this.taskid = taskid;
        this.devisort_id = devisort_id;
        this.device_id = device_id;
        this.week = week;
        this.hour = hour;
        this.minute = minute;
        this.src = src;
        this.tap = tap;
        this.tap2 = tap2;
    }

    public boolean isOn_off() {
        return on_off;
    }

    public void setOn_off(boolean on_off) {
        this.on_off = on_off;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getDevisort_id() {
        return devisort_id;
    }

    public void setDevisort_id(String devisort_id) {
        this.devisort_id = devisort_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getTap() {
        return tap;
    }

    public void setTap(int tap) {
        this.tap = tap;
    }

    public int getTap2() {
        return tap2;
    }

    public void setTap2(int tap2) {
        this.tap2 = tap2;
    }

    @Override
    public String toString() {
        return "TimeTask{" +
                "index=" + index +
                ", taskid='" + taskid + '\'' +
                ", devisort_id='" + devisort_id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", week='" + week + '\'' +
                ", hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", src=" + src +
                ", tap=" + tap +
                ", tap2=" + tap2 +
                '}';
    }
}
