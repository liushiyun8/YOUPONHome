package com.youpon.home1.bean;

import com.youpon.home1.comm.App;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyun on 2017/1/6.
 */
@Table(name="sensor")
public class Sensor extends Devall{
    @Column(name = "objectId",isId = true)
    String objectId;
    @Column(name = "id",property = "Unique")
    String id;
    @Column(name = "name")
    String name;
    @Column(name = "device_id")
    int device_id;
    @Column(name = "devisort_id")
    String devisort_id;
    @Column(name = "type")
    int type;
    @Column(name = "value1")
    int value1;
    @Column(name = "value2")
    int value2;
    @Column(name = "value3")
    int value3;
    @Column(name = "value4")
    int value4;
    @Column(name = "online")
    boolean online;
    @Column(name = "isMain")
    boolean isMain;
    @Column(name = "room")
    String room="客厅";
    @Column(name = "mac")
    String mac;

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public static int in= (int) App.getSp().get("sensorin",1);

    public Sensor() {
    }

    public Sensor(int device_id, String devisort_id, int type, int value1, int value2) {
        this.id =device_id+devisort_id+type;
        if(name==null||name.equals("")) {
            switch (type) {
                case 1:
                    this.name = "红外" + in;
                    break;
                case 2:
                    this.name = "光感" + in;
                    break;
                case 3:
                    this.name = "温度传感器" + in;
                    break;
                case 4:
                    this.name = "湿度传感器" + in;
                    break;
                case 5:
                    this.name = "二氧化碳传感器" + in;
                    break;
                case 6:
                    this.name = "TVOC传感器" + in;
                    break;
                case 7:
                    this.name = "可燃气" + in;
                    break;
                case 8:
                    this.name = "烟感" + in;
                    break;
            }
            in++;
            if (in >= 100) {
                in = 1;
            }
            App.getSp().put("sensorin", in);
        }
        this.device_id = device_id;
        this.devisort_id = devisort_id;
        this.type = type;
        this.value1=value1;
        this.value2=value2;
    }

    public String getStatus(){
        String s="";
        switch (type){
            case 1:
                s=value1==0?"无人":"有人";
                break;
            case 2:
                if(value1<=20){
                    s="昏暗";
                }else if(value1>20&&value1<=200){
                    s="柔弱";
                }else if(value1>200&&value1<=800){
                    s="明亮";
                }else if(value1>800){
                    s="强光";
                }
                break;
            case 3:
                if(value1<=0){
                    s="寒冷";
                }else if(value1>0&&value1<=14){
                    s="冰凉";
                }else if(value1>14&&value1<=29){
                    s="舒适";
                }else if(value1>29){
                    s="炎热";
                }
                if(value2<=39){
                s+=" 干燥";
                }else if(value2>39&&value1<=69){
                s+=" 适中";
                }else if(value2>69){
                s+=" 潮湿";
                 }
                break;
            case 4:
                if(value1<=39){
                    s="干燥";
                }else if(value1>39&&value1<=69){
                    s="适中";
                }else if(value1>69){
                    s="潮湿";
                }
                break;
            case 5:
                if(value1<=499){
                    s="清新";
                }else if(value1>499&&value1<=999){
                    s="良好";
                }else if(value1>999&&value1<=1999){
                    s="浑浊";
                }else if(value1>1999){
                    s="严重";
                }
                break;
            case 6:
                if(value1<=50){
                    s="优";
                }else if(value1>50&&value1<=100){
                    s="良";
                }else if(value1>100&&value1<=200){
                    s="中";
                }else if(value1>200){
                    s="差";
                }
                break;
            case 7:
                if(value1<=100){
                    s="正常";
                }else if(value1>100&&value1<=400){
                    s="轻度";
                }else if(value1>400&&value1<=800){
                    s="中度";
                }else if(value1>800){
                    s="重度";
                }
                break;
            case 8:
                if(value1<=100){
                    s="正常";
                }else if(value1>100&&value1<=200){
                    s="轻度";
                }else if(value1>200&&value1<=600){
                    s="中度";
                }else if(value1>600){
                    s="重度";
                }
                break;
        }
        return s;
    }

    public static List<String> getStatusList(int type){
        List<String> list=new ArrayList<>();
        switch (type){
            case 1:
                list.add("无人");
                list.add("有人");
                break;
            case 2:
                list.add("昏暗");
                list.add("柔弱");
                list.add("明亮");
                list.add("强光");
                break;
            case 3:
                list.add("寒冷");
                list.add("冰凉");
                list.add("舒适");
                list.add("炎热");
                break;
            case 4:
                list.add("干燥");
                list.add("适中");
                list.add("潮湿");
                break;
            case 5:
                list.add("清新");
                list.add("良好");
                list.add("浑浊");
                list.add("严重");
                break;
            case 6:
                list.add("优");
                list.add("良");
                list.add("中");
                list.add("差");
                break;
            case 7:
            case 8:
                list.add("正常");
                list.add("轻度");
                list.add("中度");
                list.add("重度");
                break;
        }
        return list;
    }

    public static long getStatusValue(int type,String level){
        long value=0;
        switch (type){
            case 1:
                value="无人".equals(level)?0:1;
                break;
            case 2:
                if("昏暗".equals(level)){
                    value=20;
                }else if("柔弱".equals(level)){
                    value=200+(20<<16);
                }else if("明亮".equals(level)){
                    value=800+(200<<16);
                }else if("强光".equals(level)){
                    value=1200+(800<<16);
                }
                break;
            case 3:
                if("寒冷".equals(level)){
                    value=-20<<16;
                }else if("冰凉".equals(level)){
                    value=14;
                }else if("舒适".equals(level)){
                    value=29+(14<<16);
                }else if("炎热".equals(level)){
                    value=50+(29<<16);
                }
                break;
            case 4:
                if("干燥".equals(level)){
                    value=39;
                }else if("适中".equals(level)){
                    value=69+(39<<16);
                }else if("潮湿".equals(level)){
                    value=80+(69<<16);
                }
                break;
            case 5:
                if("清新".equals(level)){
                    value=499;
                }else if("良好".equals(level)){
                    value=999+(499<<16);
                }else if("浑浊".equals(level)){
                    value=1999+(999<<16);
                }else if("严重".equals(level)){
                    value=2500+(1999<<16);
                }
                break;
            case 6:
                if("优".equals(level)){
                    value=50;
                }else if("良".equals(level)){
                    value=100+(50<<16);
                }else if("中".equals(level)){
                    value=200+(100<<16);
                }else if("差".equals(level)){
                    value=500+(200<<16);
                }
                break;
            case 7:
                if("正常".equals(level)){
                    value=100;
                }else if("轻度".equals(level)){
                    value=400+(100<<16);
                }else if("中度".equals(level)){
                    value=800+(400<<16);
                }else if("重度".equals(level)){
                    value=1200+(800<<16);
                }
                break;
            case 8:
                if("正常".equals(level)){
                    value=100;
                }else if("轻度".equals(level)){
                    value=200+(100<<16);
                }else if("中度".equals(level)){
                    value=600+(200<<16);
                }else if("重度".equals(level)){
                    value=1200+(600<<16);
                }
                break;
        }
        return value;
    }

    public static int getStatusLevel(int type,long value){
        int value1= (int) (value&0xFFFF);
        int level=-1;
        switch (type){
            case 1:
                level=value1==0?0:1;
                break;
            case 2:
                if(value1<=20){
                    level=0;
                }else if(value1>20&&value1<=200){
                    level=1;
                }else if(value1>200&&value1<=800){
                    level=2;
                }else if(value1>800){
                    level=3;
                }
                break;
            case 3:
                if(value1<=0){
                    level=0;
                }else if(value1>0&&value1<=14){
                    level=1;
                }else if(value1>14&&value1<=29){
                    level=2;
                }else if(value1>29){
                    level=3;
                }
                break;
            case 4:
                if(value1<=39){
                    level=0;
                }else if(value1>39&&value1<=69){
                    level=1;
                }else if(value1>69){
                    level=2;
                }
                break;
            case 5:
                if(value1<=499){
                    level=0;
                }else if(value1>499&&value1<=999){
                    level=1;
                }else if(value1>999&&value1<=1999){
                    level=2;
                }else if(value1>1999){
                    level=3;
                }
                break;
            case 6:
                if(value1<=50){
                    level=0;
                }else if(value1>50&&value1<=100){
                    level=1;
                }else if(value1>100&&value1<=200){
                    level=2;
                }else if(value1>200){
                    level=3;
                }
                break;
            case 7:
                if(value1<=100){
                    level=0;
                }else if(value1>100&&value1<=400){
                    level=1;
                }else if(value1>400&&value1<=800){
                    level=2;
                }else if(value1>800){
                    level=3;
                }
                break;
            case 8:
                if(value1<=100){
                    level=0;
                }else if(value1>100&&value1<=200){
                    level=1;
                }else if(value1>200&&value1<=600){
                    level=2;
                }else if(value1>600){
                    level=3;
                }
                break;
        }
        return level;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getSID() {
        return id;
    }

    @Override
    public int getSort() {
        return 4;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevisort_id() {
        return devisort_id;
    }

    public void setDevisort_id(String devisort_id) {
        this.devisort_id = devisort_id;
    }

    public int getType() {
        return type;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String getRoom() {
        return room;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMytype(int type){
        id=mac+type;
        this.type = type;
        if(name==null||"".equals(name)) {
            switch (type) {
                case 1:
                    this.name = "红外" + in;
                    break;
                case 2:
                    this.name = "光感" + in;
                    break;
                case 3:
                case 4:
                    this.name = "温湿度传感器" + in;
                    break;
                case 5:
                    this.name = "二氧化碳传感器" + in;
                    break;
                case 6:
                    this.name = "TVOC传感器" + in;
                    break;
                case 7:
                    this.name = "可燃气" + in;
                    break;
                case 8:
                    this.name = "烟感" + in;
                    break;
            }
            in++;
            if (in >= 100) {
                in = 1;
            }
            App.getSp().put("sensorin", in);
        }
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
    }

    public void setValue(int index,int value) {
        switch (index){
            case 0:
                setValue1(value);
                break;
            case 1:
                setValue2(value);
                break;
            case 2:
                setValue3(value);
                break;
            case 3:
                setValue4(value);
                break;
        }
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", device_id=" + device_id +
                ", devisort_id='" + devisort_id + '\'' +
                ", type=" + type +
                ", value1=" + value1 +
                ", online=" + online +
                ", isMain=" + isMain +
                ", room='" + room + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
