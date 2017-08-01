package com.youpon.home1.bean;

import com.google.gson.Gson;
import com.youpon.home1.manage.DeviceManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuyun on 2017/4/20.
 */
@Table(name = "panel")
public class Panel extends Devall {
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "mac",property = "Unique")
    String mac;
    @Column(name = "gateway_id")
    int gateway_id;
    @Column(name = "id")
    String id;
    @Column(name = "name")
    String name;
    @Column(name = "room")
    String room="客厅";
    @Column(name = "isMain")
    Boolean isMain;
    @Column(name = "gateway_type")
    int gateway_type;
    @Column(name = "online")
    boolean online;
    @Column(name = "clas")
    int clas;
    @Column(name = "chnls")
    String chnls;

    Map<String,List<Scenebean.ActionsBean>> map=new ConcurrentHashMap<>();

    private List<ChnlBean> chnlBeanList;

    public Panel() {
    }

    public String getChnls() {
        return chnls;
    }

    public void setChnls(String chnls) {
        this.chnls = chnls;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setMain(boolean s) {
        isMain=s;
    }

    @Override
    public boolean isMain() {
        return isMain;
    }

    public String getMyName() {
        Device device = DeviceManage.getInstance().getDevice(gateway_id);
        if(device!=null){
            return device.getName()+name;
        }
        return name;
    }

    public String getName() {
        return name;
    }

    public Map<String, List<Scenebean.ActionsBean>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<Scenebean.ActionsBean>> map) {
        this.map = map;
    }

    @Override
    public String getSID() {
        return mac;
    }

    @Override
    public int getSort() {
        return 2;
    }

    @Override
    public int getType() {
        return 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }

    public int getGateway_type() {
        return gateway_type;
    }

    public void setGateway_type(int gateway_type) {
        this.gateway_type = gateway_type;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getClas() {
        return clas;
    }

    public void setClas(int clas) {
        this.clas = clas;
        if(name==null||"".equals(name)){
            String s="";
            switch (clas){
                case 299:
                    s="";
                    break;
                case 9:
                    s="-9位面板";
                    break;
                case 8:
                    s="-4位场景面板";
                    break;
                case 5:
                    s="-1位开关";
                    break;
                case 6:
                    s="-2位开关";
                    break;
                case 7:
                    s="-3位开关";
                    break;
            }
            this.name=s;
        }
    }
    public List<ChnlBean> getChnlBeanList() {
        if(chnlBeanList ==null){
            if(chnls!=null){
                chnlBeanList =new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(chnls);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        ChnlBean chnlBean = new Gson().fromJson(jsonObject.toString(), ChnlBean.class);
                        chnlBeanList.add(chnlBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return chnlBeanList;
    }

    public void setChnlBeanList(List<ChnlBean> chnlBeanList) {
        this.chnlBeanList = chnlBeanList;
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

    @Override
    public String toString() {
        return "Panel{" +
                "objectId='" + objectId + '\'' +
                ", mac='" + mac + '\'' +
                ", gateway_id=" + gateway_id +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", room='" + room + '\'' +
                ", isMain=" + isMain +
                ", gateway_type=" + gateway_type +
                ", online=" + online +
                ", clas=" + clas +
                ", chnls='" + chnls + '\'' +
                '}';
    }
}
