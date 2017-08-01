package com.youpon.home1.bean;

import java.io.Serializable;

/**
 * Created by liuyun on 2017/3/16.
 */
public class APPInfo implements Serializable{
    String illustration;
    String md5;
    String url;
    String version;

    public APPInfo() {
    }

    public APPInfo(String illustration, String md5, String url, String version) {
        this.illustration = illustration;
        this.md5 = md5;
        this.url = url;
        this.version = version;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
