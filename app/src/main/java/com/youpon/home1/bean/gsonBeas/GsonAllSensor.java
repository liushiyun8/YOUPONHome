package com.youpon.home1.bean.gsonBeas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyun on 2017/5/3.
 */
public class GsonAllSensor {

    /**
     * mac : 9D48A60B006F0D00
     * class : 4294967295
     * online : 1
     * nwkid : CD98
     * chnl : [{"type":2,"val":151,"alloc":1},{"type":2,"val":55,"alloc":1},{"type":2,"val":55,"alloc":1},{"type":2,"val":55,"alloc":1},{"type":2,"val":55,"alloc":1},{"type":2,"val":55,"alloc":1},{"type":2,"val":55,"alloc":1}]
     */

    private String mac;
    @SerializedName("class")
    private long classX;
    private int online;
    private String nwkid;
    /**
     * type : 2
     * val : 151
     * alloc : 1
     */

    private List<ChnlBean> chnl;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public long getClassX() {
        return classX;
    }

    public void setClassX(long classX) {
        this.classX = classX;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getNwkid() {
        return nwkid;
    }

    public void setNwkid(String nwkid) {
        this.nwkid = nwkid;
    }

    public List<ChnlBean> getChnl() {
        return chnl;
    }

    public void setChnl(List<ChnlBean> chnl) {
        this.chnl = chnl;
    }

    public static class ChnlBean {
        private int type;
        private int val;
        private int alloc;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public int getAlloc() {
            return alloc;
        }

        public void setAlloc(int alloc) {
            this.alloc = alloc;
        }
    }
}
