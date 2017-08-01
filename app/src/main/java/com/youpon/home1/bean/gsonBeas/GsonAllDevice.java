package com.youpon.home1.bean.gsonBeas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyun on 2017/4/17.
 */
public class GsonAllDevice {

    /**
     * mac : 6EC93F0D006F0D0000000000
     * class : 5
     * online : 1
     * riu : 1
     * nwkid : 283B
     * endp : [{"dstid":1,"t":3,"val":1}]
     */

    private String mac;
    @SerializedName("class")
    private int classX;
    private int online;
    private int riu;
    private String nwkid;
    /**
     * dstid : 1
     * t : 3
     * val : 1
     */

    private List<EndpBean> endp;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getClassX() {
        return classX;
    }

    public void setClassX(int classX) {
        this.classX = classX;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getRiu() {
        return riu;
    }

    public void setRiu(int riu) {
        this.riu = riu;
    }

    public String getNwkid() {
        return nwkid;
    }

    public void setNwkid(String nwkid) {
        this.nwkid = nwkid;
    }

    public List<EndpBean> getEndp() {
        return endp;
    }

    public void setEndp(List<EndpBean> endp) {
        this.endp = endp;
    }

    public static class EndpBean {
        private int dstid;
        private int t;
        private int val;

        public int getDstid() {
            return dstid;
        }

        public void setDstid(int dstid) {
            this.dstid = dstid;
        }

        public int getT() {
            return t;
        }

        public void setT(int t) {
            this.t = t;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }
    }
}
