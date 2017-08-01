package com.youpon.home1.bean.gsonBeas;

import com.youpon.home1.bean.Scenebean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuyun on 2017/5/3.
 */

public class Liandong {

    /**
     * ctrl_id : 0
     * ctrl_n : linkage1
     * status : 1
     * env_cond : linkage1
     * ctrl_type : 1
     * obj_id : 4
     * cmd : 1
     * value : 1
     * mac : D0BAE4500CBD0000
     * env_paras : [{"mac":"D0BAE4500CBD0000","sensor_type":1,"cond":">=","val":1}]
     */
    private int ctrl_id;
    private String ctrl_n;
    private int status;
    private String env_cond;
    private int ctrl_type;
    private int obj_id;
    private int cmd;
    private int value;
    private String mac;

    public static Map<Integer,Liandong> getMap() {
        return map;
    }

   public static Map<Integer,Liandong> map=new ConcurrentHashMap<>();
    /**
     * mac : D0BAE4500CBD0000
     * sensor_type : 1
     * cond : >=
     * val : 1
     */

    private List<EnvParasBean> env_paras;

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

    public String getEnv_cond() {
        return env_cond;
    }

    public void setEnv_cond(String env_cond) {
        this.env_cond = env_cond;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List<EnvParasBean> getEnv_paras() {
        return env_paras;
    }

    public void setEnv_paras(List<EnvParasBean> env_paras) {
        this.env_paras = env_paras;
    }

    public static class EnvParasBean {
        private String mac;
        private int sensor_type;
        private String cond;
        private long val;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getSensor_type() {
            return sensor_type;
        }

        public void setSensor_type(int sensor_type) {
            this.sensor_type = sensor_type;
        }

        public String getCond() {
            return cond;
        }

        public void setCond(String cond) {
            this.cond = cond;
        }

        public long getVal() {
            return val;
        }

        public void setVal(long val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "EnvParasBean{" +
                    "mac='" + mac + '\'' +
                    ", sensor_type=" + sensor_type +
                    ", cond='" + cond + '\'' +
                    ", val=" + val +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Liandong{" +
                "ctrl_id=" + ctrl_id +
                ", ctrl_n='" + ctrl_n + '\'' +
                ", status=" + status +
                ", env_cond='" + env_cond + '\'' +
                ", ctrl_type=" + ctrl_type +
                ", obj_id=" + obj_id +
                ", cmd=" + cmd +
                ", value=" + value +
                ", mac='" + mac + '\'' +
                ", env_paras=" + env_paras +
                '}';
    }
}
