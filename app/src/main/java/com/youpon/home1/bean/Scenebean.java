package com.youpon.home1.bean;

import android.util.Log;

import com.google.gson.Gson;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyun on 2016/12/9.
 */
@Table(name = "scene")
public class Scenebean extends Devall{
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "name")
    String name;
    @Column(name = "isMain")
    boolean isMain;
    @Column(name = "gateway_id")
    int gateway_id;
    @Column(name = "gateway_type")
    int gateway_type;
    @Column(name = "chnl_id")
    int chnl_id;
    @Column(name = "panel_mac")
    String panel_mac;
    @Column(name = "groupId")
    String groupId;
    @Column(name = "sceneId")
    String sceneId;
    @Column(name = "id")
    String id;
    @Column(name = "type")
    int type;
    @Column(name = "status")
    int status;
    @Column(name = "actions")
    String actions;
    @Column(name = "unique",property = "Unique")
    String unique;

    private List<ActionsBean> action;

    public Scenebean() {
    }

    public Scenebean(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSID() {
        return objectId;
    }

    @Override
    public int getSort() {
        return 0;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRoom() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isMain() {
        return isMain;
    }

    @Override
    public void setMain(boolean main) {
        isMain = main;
    }

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
    }

    public int getGateway_type() {
        return gateway_type;
    }

    public void setGateway_type(int gateway_type) {
        this.gateway_type = gateway_type;
    }

    public String getPanel_mac() {
        return panel_mac;
    }

    public void setPanel_mac(String panel_mac) {
        this.panel_mac = panel_mac;
    }

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

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public List<ActionsBean> getAction() {
            action=new ArrayList<>();
            if(actions!=null){
                try {
                    JSONArray jsonArray = new JSONArray(actions);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        ActionsBean actionsBean = new Gson().fromJson(jsonObject.toString(), ActionsBean.class);
                        action.add(actionsBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        return action;
    }

    public void synkData(){
        List<SubDevice> subdev = getSubdev();
        try {
            App.db.update(subdev);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void setAction(List<ActionsBean> action) {
        actions=new Gson().toJson(action);
        this.action = action;
    }

    public List<SubDevice> getSubdev(){
        List<SubDevice> deviList=new ArrayList<>();
        List<Scenebean.ActionsBean> action =getAction();
        if(action!=null&&action.size()>0){
            for (int i = 0; i < action.size(); i++) {
                Scenebean.ActionsBean actionsBean = action.get(i);
                String mac = actionsBean.getMac();
                int dstid = actionsBean.getDstid();
                try {
                    int id=dstid;
                    if("0008".equals(actionsBean.getNclu())&&dstid==2){
                        id=1;
                    }
                    SubDevice subDevice = App.db.selector(SubDevice.class).where("mac", "=", mac).and("dst", "=", id).findFirst();
                    if(subDevice!=null){
                        boolean tag=false;
                        if(subDevice.getType()==1){
                            for (int j = 0; j < deviList.size(); j++) {
                                if(deviList.get(j).getUnique().equals(subDevice.getUnique())){
                                    if(dstid==2){
                                        deviList.get(j).setValue2(actionsBean.getVal());
                                    }else deviList.get(j).setValue1(actionsBean.getVal());
                                    tag=true;
                                    break;
                                }
                            }
                            if(!tag){
                                if(dstid==2){
                                    subDevice.setValue2(actionsBean.getVal());
                                }else subDevice.setValue1(actionsBean.getVal());
                                deviList.add(subDevice);
                            }
                        }else if(subDevice.getType()==2){
                            for (int j = 0; j < deviList.size(); j++) {
                                if(deviList.get(j).getUnique().equals(subDevice.getUnique())){
                                    if("0006".equals(actionsBean.getNclu())){
                                        deviList.get(j).setValue2(actionsBean.getVal());
                                    }else deviList.get(j).setValue1(actionsBean.getVal());
                                    tag=true;
                                    break;
                                }
                            }
                            if(!tag){
                                if("0006".equals(actionsBean.getNclu())){
                                    subDevice.setValue2(actionsBean.getVal());
                                }else subDevice.setValue1(actionsBean.getVal());
                                deviList.add(subDevice);
                            }
                        }else {
                            subDevice.setValue1(actionsBean.getVal());
                            deviList.add(subDevice);
                        }

                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        return deviList;
    }

    public String sub2action(List<SubDevice> deviList){
        List<Scenebean.ActionsBean> list=new ArrayList<>();
        for (int i = 0; i < deviList.size(); i++) {
            SubDevice subDevice = deviList.get(i);
            Scenebean.ActionsBean actionsBean = new Scenebean.ActionsBean();
            actionsBean.setMac(subDevice.getMac());
            actionsBean.setDstid(subDevice.getDst());
            if(subDevice.getTp()==2||subDevice.getTp()==4){
                actionsBean.setNclu("0006");
            }else
            actionsBean.setNclu("0008");
            actionsBean.setNwkid(subDevice.getId());
            actionsBean.setVal(subDevice.getValue1());
            if(subDevice.getTp()==1&&subDevice.getValue2()==0){
                actionsBean.setNclu("0006");
                actionsBean.setVal(0);
            }
            list.add(actionsBean);
            if(subDevice.getTp()==0) {
                Scenebean.ActionsBean actionsBean1 = new Scenebean.ActionsBean();
                actionsBean1.setMac(subDevice.getMac());
                actionsBean1.setNwkid(subDevice.getId());
                actionsBean1.setVal(subDevice.getValue2());
                actionsBean1.setDstid(2);
                actionsBean1.setNclu("0008");
                list.add(actionsBean1);
            }
        }
       return new Gson().toJson(list);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
        Device device = DeviceManage.getInstance().getDevice(gateway_id);
        Panel panel= PanelManage.getInstance().getPanel(panel_mac);
        if(name==null||"".equals(name)&&device!=null){
            switch (sceneId){
                case "00":
                    name=device.getName()+panel.getName()+"场景1";
                    break;
                case "01":
                    name=device.getName()+panel.getName()+"场景2";
                    break;
                case "03":
                    name=device.getName()+panel.getName()+"场景3";
                    break;
                default:
                    name=device.getName()+panel.getName()+"场景3";
                    break;
            }
        }
        unique=panel_mac+groupId+sceneId;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Scenebean{" +
                "objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", isMain=" + isMain +
                ", gateway_id=" + gateway_id +
                ", gateway_type=" + gateway_type +
                ", chnl_id=" + chnl_id +
                ", panel_mac='" + panel_mac + '\'' +
                ", groupId='" + groupId + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", actions='" + actions + '\'' +
                ", action=" + action +
                '}';
    }

    public static class ActionsBean implements Serializable {
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

        @Override
        public String toString() {
            return "ActionsBean{" +
                    "mac='" + mac + '\'' +
                    ", nwkid='" + nwkid + '\'' +
                    ", dstid=" + dstid +
                    ", nclu='" + nclu + '\'' +
                    ", cmd=" + cmd +
                    ", val=" + val +
                    '}';
        }
    }
}
