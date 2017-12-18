package com.youpon.home1.comm.tools;

import io.xlink.wifi.sdk.XDevice;

/**
 * Created by liuyun on 2017/12/15.
 */

class DataMessage {
    XDevice xDevice;
    byte[] bs;
    String name;

    public DataMessage(XDevice xDevice, byte[] bs, String name) {
        this.xDevice = xDevice;
        this.bs = bs;
        this.name = name;
    }

    public XDevice getxDevice() {
        return xDevice;
    }

    public void setxDevice(XDevice xDevice) {
        this.xDevice = xDevice;
    }

    public byte[] getBs() {
        return bs;
    }

    public void setBs(byte[] bs) {
        this.bs = bs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
