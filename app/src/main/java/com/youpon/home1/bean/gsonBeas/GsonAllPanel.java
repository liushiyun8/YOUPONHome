package com.youpon.home1.bean.gsonBeas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyun on 2017/4/20.
 */
public class GsonAllPanel {

    /**
     * mac : D0BAE4500D320000
     * class : 0
     * online : 1
     * nwkid : 1111
     * chnl : [{"chnl_id":1,"type":0,"endp_id":0,"connected":0},{"chnl_id":2,"type":0,"endp_id":0,"connected":0},{"chnl_id":3,"type":0,"endp_id":0,"connected":0},{"chnl_id":4,"type":0,"endp_id":0,"connected":0},{"chnl_id":5,"type":1,"endp_id":1,"connected":1},{"chnl_id":6,"type":1,"endp_id":2,"connected":1},{"chnl_id":7,"type":2,"endp_id":3,"connected":1},{"chnl_id":8,"type":4,"endp_id":4,"connected":1},{"chnl_id":9,"type":4,"endp_id":5,"connected":1},{"chnl_id":10,"type":8,"endp_id":6,"connected":1}]
     */

    private String mac;
    @SerializedName("class")
    private int classX;
    private int online;
    private String nwkid;
    /**
     * chnl_id : 1
     * type : 0
     * endp_id : 0
     * connected : 0
     */
    private List<ChnlBean> chnl;
    /**
     * riu : 1
     */

    private int riu;

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

    public int getRiu() {
        return riu;
    }

    public void setRiu(int riu) {
        this.riu = riu;
    }

    public static class ChnlBean {
        private int chnl_id;
        private int type;
        private int endp_id;
        private int connected;

        public int getChnl_id() {
            return chnl_id;
        }

        public void setChnl_id(int chnl_id) {
            this.chnl_id = chnl_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getEndp_id() {
            return endp_id;
        }

        public void setEndp_id(int endp_id) {
            this.endp_id = endp_id;
        }

        public int getConnected() {
            return connected;
        }

        public void setConnected(int connected) {
            this.connected = connected;
        }
    }
}
