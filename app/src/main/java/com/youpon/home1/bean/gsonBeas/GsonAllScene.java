package com.youpon.home1.bean.gsonBeas;

import java.util.List;

/**
 * Created by liuyun on 2017/4/20.
 */
public class GsonAllScene {

    /**
     * chnl_id : 1
     * status : 0
     * actions : [{"mac":"B2CB3F0D006F0D00","nwkid":"1166","dstid":1,"nclu":"0008","cmd":1,"val":0},{"mac":"B2CB3F0D006F0D00","nwkid":"1166","dstid":2,"nclu":"0008","cmd":1,"val":1}]
     */

    private int chnl_id;
    private int status;
    /**
     * mac : B2CB3F0D006F0D00
     * nwkid : 1166
     * dstid : 1
     * nclu : 0008
     * cmd : 1
     * val : 0
     */

    private List<ActionsBean> actions;

    public int getChnl_id() {
        return chnl_id;
    }

    public void setChnl_id(int chnl_id) {
        this.chnl_id = chnl_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ActionsBean> getActions() {
        return actions;
    }

    public void setActions(List<ActionsBean> actions) {
        this.actions = actions;
    }

    public static class ActionsBean {
        private String mac;
        private String nwkid;
        private int dstid;
        private String nclu;
        private int cmd;
        private int val;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getNwkid() {
            return nwkid;
        }

        public void setNwkid(String nwkid) {
            this.nwkid = nwkid;
        }

        public int getDstid() {
            return dstid;
        }

        public void setDstid(int dstid) {
            this.dstid = dstid;
        }

        public String getNclu() {
            return nclu;
        }

        public void setNclu(String nclu) {
            this.nclu = nclu;
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
    }
}
