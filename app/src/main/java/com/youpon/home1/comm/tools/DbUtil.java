package com.youpon.home1.comm.tools;

import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Sensor;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.bean.Gateway;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.manage.DeviceManage;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyun on 2017/2/27.
 */
public class DbUtil {

    public static List<SubDevice> findMyDv(){
        List<SubDevice> subDevices =new ArrayList<>();
        try {
            List<Device> gates = DeviceManage.getInstance().getCurrentdev();
            if(gates!=null){
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<SubDevice> ds= App.db.selector(SubDevice.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).and("type","!=",0).findAll();
                    if(ds!=null){
                        subDevices.addAll(ds);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return subDevices;
    }

    public static List<Panel> findMyPanel(){
        List<Panel> list=new ArrayList<>();
        try {
            List<Device> gates = DeviceManage.getInstance().getCurrentdev();
            if(gates!=null){
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<Panel> ds= App.db.selector(Panel.class).where("gateway_id", "=",gateway.getXDevice().getDeviceId()).findAll();
                    if(ds!=null){
                        list.addAll(ds);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static long getGatewayCount(){
      return DeviceManage.getInstance().getCurrentdev().size();
  }

    public static List<SubDevice> findMyWind(){
        List<SubDevice> list=new ArrayList<>();
        List<SubDevice> myDv = findMyDv();
        for (int i = 0; i <myDv.size() ; i++) {
            SubDevice subDevice = myDv.get(i);
            if(subDevice.getType()==1)
                list.add(subDevice);
        }
        return list;
    }

    public static List<SubDevice> findMyLightWarm(){
        List<SubDevice> list=new ArrayList<>();
        List<SubDevice> myDv = findMyDv();
        for (int i = 0; i <myDv.size() ; i++) {
            SubDevice subDevice = myDv.get(i);
            if(subDevice.getType()==2)
                list.add(subDevice);
        }
        return list;
    }

    public static List<SubDevice> findMyLight(){
        List<SubDevice> list=new ArrayList<>();
        List<SubDevice> myDv = findMyDv();
        for (int i = 0; i <myDv.size() ; i++) {
            SubDevice subDevice = myDv.get(i);
            if(subDevice.getType()==3)
                list.add(subDevice);
        }
        return list;
    }

    public static List<SubDevice> findMyAir(){
        List<SubDevice> list=new ArrayList<>();
        List<SubDevice> myDv = findMyDv();
        for (int i = 0; i <myDv.size() ; i++) {
            SubDevice subDevice = myDv.get(i);
            if(subDevice.getType()==4)
                list.add(subDevice);
        }
        return list;
    }

    public static List<SubDevice> findMydevIndex(int index){
        switch (index){
            case 0:
                return findMyWind();
            case 1:
                return findMyLightWarm();
            case 2:
                return findMyLight();
            case 3:
                return findMyAir();
            default:
                return new ArrayList<SubDevice>();
        }
    }

    public static List<Scenebean> findMyScene(){
        List<Scenebean> scenebeens=new ArrayList<>();
        try {
            List<Device> gates = DeviceManage.getInstance().getCurrentdev();
            if(gates!=null){
                for (int i = 0; i < gates.size(); i++) {
                    List<Scenebean> sb = App.db.selector(Scenebean.class).where("gateway_id","=",gates.get(i).getXDevice().getDeviceId()).orderBy("groupId").orderBy("panel_mac").orderBy("sceneId").findAll();
                    if(sb!=null){
                        scenebeens.addAll(sb);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return scenebeens;
    }

    public static List<Sensor> findMySensor(){
        List<Sensor> list=new ArrayList<>();
        try {
            List<Device> gates = DeviceManage.getInstance().getCurrentdev();
            if(gates!=null){
                for (int i = 0; i < gates.size(); i++) {
                    Device gateway = gates.get(i);
                    List<Sensor> ds= App.db.selector(Sensor.class).where("device_id", "=",gateway.getXDevice().getDeviceId()).findAll();
                    if(ds!=null){
                        list.addAll(ds);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
      return list;
    }

    public static List<Sensor> findMySensorType(int type){
        List<Sensor> list=new ArrayList<>();
        List<Sensor> mySensor = findMySensor();
        for (int i = 0; i < mySensor.size(); i++) {
            if(mySensor.get(i).getType()==type+1)
                list.add(mySensor.get(i));
        }
        return list;
    }
}
