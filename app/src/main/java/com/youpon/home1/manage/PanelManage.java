package com.youpon.home1.manage;


import android.util.Log;

import com.youpon.home1.bean.Device;
import com.youpon.home1.bean.Panel;
import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by liuyun on 2017/5/24.
 */
public class PanelManage {

    public static ConcurrentHashMap<String,Panel> panelMap = new ConcurrentHashMap<String,Panel>();
    private static PanelManage instance;


    public static PanelManage getInstance() {
        if (instance == null) {
            instance = new PanelManage();
        }
        return instance;
    }

    private PanelManage() {
    }

    // 通过静态语句快，优先初始化，避免因为线程安全，重复调用.
    static {
        try {
            List<Panel> panels= App.db.findAll(Panel.class);
            if(panels!=null){
                for (int i = 0; i < panels.size(); i++) {
                    Panel panel = panels.get(i);
                    panelMap.put(panel.getMac(),panel);
                }

            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        }

    public synchronized List<Panel> getAllpanel(){
        List<Panel> list=new ArrayList<>();
        list.addAll(panelMap.values());
        return list;
    }

    public synchronized List<Panel> getAllScenepanel(){
        List<Panel> list=new ArrayList<>();
        for (Panel panel : panelMap.values()) {
            if(panel.getClas()==5||panel.getClas()==6||panel.getClas()==7||panel.getClas()==9||panel.getClas()==299){
                list.add(panel);
            }
        }
        return list;
    }

    public synchronized List<Scenebean> getFourthScene(){
        List<Scenebean> list=new ArrayList<>();
        List<Device> currentdev = DeviceManage.getInstance().getCurrentdev();
        try {
            for (int j = 0; j <currentdev.size(); j++) {
                Device device = currentdev.get(j);
                List<SubDevice> subs = App.db.selector(SubDevice.class).where("clas", "=", 8).and("gateway_id","=",device.getXDevice().getDeviceId()).findAll();
                if(subs!=null){
                    for (int i = 0; i < subs.size(); i++) {
                        SubDevice subDevice = subs.get(i);
                        Scenebean scenebean = new Scenebean();
                        scenebean.setId(subDevice.getId());
                        scenebean.setGateway_id(subDevice.getGateway_id());
                        scenebean.setGateway_type(0);
                        scenebean.setName(subDevice.getName());
                        scenebean.setType(1);
                        scenebean.setPanel_mac(subDevice.getMac());
                        scenebean.setGroupId("0001");
                        scenebean.setSceneId("0"+(subDevice.getDst()-1));
                        list.add(scenebean);
                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Panel getPanel(String mac){
        if(panelMap!=null&&mac!=null)
        return panelMap.get(mac);
        else return null;
    }

    public Panel getPanelById(String id,int deviceId){
        Collection<Panel> values = panelMap.values();
        for (Panel panel : values) {
            if (id.equals(panel.getId())&&panel.getGateway_id()==deviceId) {
                return panel;
            }
            }
        return null;
    }

    /**
     * 保存面板到数据库
     */
    public void savePanel(Panel panel) {
        try {
            App.db.replace(panel);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public synchronized void clearAllPanel(){
        panelMap.clear();
        try {
            App.db.delete(Panel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void addPanel(Panel panel) {
        panelMap.put(panel.getMac(),panel);
        savePanel(panel);
    }

    public void updatePanel(Panel panel) {
        panelMap.remove(panel.getMac());
        panelMap.put(panel.getMac(),panel);
        savePanel(panel);
    }

    public void updateNoSavePanel(Panel panel) {
        panelMap.remove(panel.getMac());
        panelMap.put(panel.getMac(),panel);
    }


    public void removePanel(String mac) {
        try {
            App.db.delete(panelMap.get(mac));
        } catch (DbException e) {
            e.printStackTrace();
        }
        panelMap.remove(mac);
    }

    public List<Panel> get485Scenepanel() {
        List<Panel> list=new ArrayList<>();
        for (Panel panel : panelMap.values()) {
            if(panel.getClas()==299){
                list.add(panel);
            }
        }
        return list;
    }
}
