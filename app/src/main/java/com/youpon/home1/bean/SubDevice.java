package com.youpon.home1.bean;

import android.util.Log;

import com.youpon.home1.comm.App;
import com.youpon.home1.manage.DeviceManage;
import com.youpon.home1.manage.PanelManage;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by liuyun on 2016/12/2.
 */
@Table(name = "subdevice")
public class SubDevice extends Devall implements Serializable {
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "mac")
    String mac;
    @Column(name = "unique",property = "Unique")
    String unique;
    @Column(name = "gateway_id")
    int gateway_id;
    @Column(name = "id")
    String id;
    @Column(name = "name")
    String name;
    @Column(name = "type")
    int type=-1;
    @Column(name = "room")
    String room="客厅";
    @Column(name = "isMain")
    boolean isMain;
    @Column(name = "dst")
    private int dst;
    @Column(name = "value1")
    int value1;
    @Column(name = "value2")
    int value2;
    @Column(name = "tp")
    int tp;
    @Column(name = "gateway_type")
    int gateway_type;
    @Column(name = "online")
    boolean online=true;
    @Column(name = "panel_mac")
    String panel_mac;
    @Column(name = "panel_id")
    int panel_id;
    @Column(name = "alloc")
    int alloc;
    @Column(name = "clas")
    int clas;

    public SubDevice() {
    }

    public SubDevice(String id, String mac, int type,int gateway_id) {
        this.id = id;
        this.mac = mac;
        this.type = type;
        this.unique=mac+dst;
        this.gateway_id=gateway_id;
    }

    public SubDevice(String id, int type,int gateway_id, int gateway_type) {
        this.id = id;
        this.mac =gateway_id+id;
        this.gateway_id=gateway_id;
        this.type = type;
        this.unique=mac+dst;
        this.gateway_type=gateway_type;
    }

    public SubDevice(String id,int gateway_id, int gateway_type) {
        this.id = id;
        this.gateway_id=gateway_id;
        this.gateway_type=gateway_type;
    }


    public String getMac() {
        return mac;
    }

    public int getDst() {
        return dst;
    }

    @Override
    public void setMain(boolean s) {
        isMain=s;
    }

    @Override
    public boolean isMain() {
        return isMain;
    }


    public String getName() {
        return name;
    }

    @Override
    public String getSID() {
        return unique;
    }

    @Override
    public int getSort() {
        return 3;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public  void setMyType(int type) {
        this.type =type;
        String index=dst+"";
        if("".equals(name)||name==null){
            String first="";
            if(clas==5){
               first="一键";
            }else if(clas==6){
                first="二键";
            }else if(clas==7){
                first="三键";
            }else if(clas==9){
                first="九位";
                index=dst-3+"";
            }else if(clas==299){
                index=dst-3+"";
            }
            if(type==0){
                Panel panel = PanelManage.getInstance().getPanel(mac);
                String my="";
                if(panel!=null)
                    my=panel.getMyName();
                this.name=my+dst;
            }else if(type==1){
                this.name=first+(clas==9?"风扇":"风暖");
            }else if(type==2){
                this.name=first+"光暖";
            }else if(type==3){
                this.name=first+"照明"+index;
            }else if(type==4){
                this.name=first+"换气";
            } else if(type==10){
                this.name=first+"备用";
            }else {
                this.name=first+"未知设备";
            }
        }
    }



    public void setMac(String mac) {
        if(dst!=0){
            this.unique=mac+dst;
        }else {
            this.dst=1;
            this.unique=mac+1;
        }
        this.mac = mac;
    }

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
    }


    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean getMain() {
        return isMain;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
        if(type==1&&value1==0){
            this.value2=1;
        }
        if(type==2){
            if(value1!=0){
                value2=1;
            }else value2=0;

        }
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getTp() {
        switch (type){
            case 1:
                tp=0;
                break;
            case 2:
                tp=1;
                break;
            case 3:
                tp=2;
                break;
            case 4:
                tp=3;
                break;
            case 10:
                tp=4;
                break;
            case 0:
                tp=5;
                break;
            default:
                tp=4;
                break;
        }
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
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

    public String getPanel_mac() {
        return panel_mac;
    }

    public void setPanel_mac(String panel_mac) {
        this.panel_mac = panel_mac;
    }

    public int getPanel_id() {
        return panel_id;
    }

    public void setPanel_id(int panel_id) {
        this.panel_id = panel_id;
    }

    public int getAlloc() {
        return alloc;
    }

    public void setAlloc(int alloc) {
        this.alloc = alloc;
    }

    public int getClas() {
        return clas;
    }

    public void setClas(int clas) {
        this.clas = clas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubDevice subDevice = (SubDevice) o;

        if (dst != subDevice.dst) return false;
        return mac.equals(subDevice.mac);

    }

    @Override
    public int hashCode() {
        int result = mac.hashCode();
        result = 31 * result + dst;
        return result;
    }

    @Override
    public String toString() {
        return "SubDevice{" +
                "mac='" + mac + '\'' +
                ", unique='" + unique + '\'' +
                ", gateway_id=" + gateway_id +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", room='" + room + '\'' +
                ", dst=" + dst +
                ", value1=" + value1 +
                ", value2=" + value2 +
                ", gateway_type=" + gateway_type +
                ", online=" + online +
                ", clas=" + clas +
                '}';
    }
}
