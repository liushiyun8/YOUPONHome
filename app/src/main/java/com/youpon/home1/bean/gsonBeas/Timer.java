package com.youpon.home1.bean.gsonBeas;

import com.youpon.home1.comm.tools.Command;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by liuyun on 2017/5/3.
 */
@Table(name = "timer")
public class Timer {

    /**
     * ctrl_id : 0
     * ctrl_n : timer1
     * status : 1
     * ctrl_type : 1
     * obj_id : 4
     * cmd : 1
     * val : 1
     * act_times : 1
     * timer_inter : 1
     * timer_week : 0
     * timer_time_exe : 56400
     * mac : D0BAE4500D130000
     */
    @Column(name = "id",isId = true)
    int id;
   @Column(name = "ctrl_id",property = "Unique")
    private int ctrl_id;
    @Column(name = "ctrl_n")
    private String ctrl_n="timer"+ Command.getRedam();
    @Column(name = "status")
    private int status=1;
    @Column(name = "ctrl_type")
    private int ctrl_type=1;
    @Column(name = "obj_id")
    private int obj_id;
    @Column(name = "cmd")
    private int cmd=1;
    @Column(name = "val")
    private int val;
    @Column(name = "act_times")
    private int act_times=1;
    @Column(name = "timer_inter")
    private int timer_inter=1;
    @Column(name = "timer_week")
    private int timer_week;
    @Column(name = "timer_time_exe")
    private int timer_time_exe;
    @Column(name = "mac")
    private String mac;

    public Timer() {
    }

    public int getCtrl_id() {
        return ctrl_id;
    }

    public void setCtrl_id(int ctrl_id) {
        this.ctrl_id = ctrl_id;
    }

    public String getCtrl_n() {
        return ctrl_n;
    }

    public void setCtrl_n(String ctrl_n) {
        this.ctrl_n = ctrl_n;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCtrl_type() {
        return ctrl_type;
    }

    public void setCtrl_type(int ctrl_type) {
        this.ctrl_type = ctrl_type;
    }

    public int getObj_id() {
        return obj_id;
    }

    public void setObj_id(int obj_id) {
        this.obj_id = obj_id;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getAct_times() {
        return act_times;
    }

    public void setAct_times(int act_times) {
        this.act_times = act_times;
    }

    public int getTimer_inter() {
        return timer_inter;
    }

    public void setTimer_inter(int timer_inter) {
        this.timer_inter = timer_inter;
    }

    public int getTimer_week() {
        return timer_week;
    }

    public void setTimer_week(int timer_week) {
        this.timer_week = timer_week;
    }

    public int getTimer_time_exe() {
        return timer_time_exe;
    }

    public void setTimer_time_exe(int timer_time_exe) {
        this.timer_time_exe = timer_time_exe;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "Timer{" +
                "ctrl_id=" + ctrl_id +
                ", ctrl_n='" + ctrl_n + '\'' +
                ", status=" + status +
                ", ctrl_type=" + ctrl_type +
                ", obj_id=" + obj_id +
                ", cmd=" + cmd +
                ", val=" + val +
                ", act_times=" + act_times +
                ", timer_inter=" + timer_inter +
                ", timer_week=" + timer_week +
                ", timer_time_exe=" + timer_time_exe +
                ", mac='" + mac + '\'' +
                '}';
    }
}
