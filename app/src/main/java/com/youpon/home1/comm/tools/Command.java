package com.youpon.home1.comm.tools;

import android.os.Handler;
import android.support.annotation.Nullable;
import io.xlink.wifi.sdk.util.MyLog;

import com.youpon.home1.bean.Scenebean;
import com.youpon.home1.bean.SubDevice;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.base.EventData;
import com.youpon.home1.manage.DeviceManage;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkCode;
import io.xlink.wifi.sdk.listener.SendPipeListener;
import io.xlink.wifi.sdk.util.MyLog;

/**
 *
 */
public class Command {
    static List<DataMessage> list= Collections.synchronizedList(new LinkedList<DataMessage>());
    public static String Star="{\"command_id\":";
    public static String middle=",\"CMD\":101,\"G\":[{\"PID\":\"2010\",\"T\":\"91\",\"RIU\":1,\"D\":\"";
    public static String middle1=",\"CMD\":101,\"G\":[{\"PID\":\"2010\",\"T\":\"91\",\"RIU\":0,\"D\":\"";
    public static String middle2=",\"CMD\":101,\"G\":[";
    public static String endStr="\\r\\n\"}]}";
    public static String endStr1="\"}]}";
    public static String shebei_con="zcl level-control o-mv-to-level ";
    public static String shebei_read="zcl global read ";
    public static final String NETID="network id";
    public static final String AUTONET="network unused";
    public static final String ALLOWNET="network pjoin 30";
    public static final String RESET="reset";
    public static final String CUSDEVICE="cus device discovery start 0 3 3\\r\\n send 0xFFFF 1 1";
    public static final String SCENE="cus scene ";
    public static final int ALLDEVICE=100;
    public static final int ALLSENSOR=111;
    public static final int ALLPANEL=110;
    public static final int ALLSCENE=112;
    public static final int ALLLIANDONG=151;
    public static final int ALLTIMER=139;
    private static boolean First=true;


    /**
     *
     * @param id 节点网络地址
     * @param remove 是否移除
     * @param rejoin  是否重新加入
     * @return
     */
    public static final String Exitnrt(int id,int remove,int rejoin){
        return "zdo leave "+id+" "+remove+" "+rejoin;
    }

    public static String getAll(int type){
        return "{\"command_id\":"+getRedam()+",\"CMD\":"+type+"}";
    }

    public static String dele(String mac,String id){
        return "{\"command_id\":"+getRedam()+",\"CMD\":113"+",\"devices\":[{\"mac\":\""+mac+"\", \"nwkid\":\""+id+"\"}]"+"}";
    }
    /**
     *
     * @param level 风暖调档：0-关闭，1-低档，2-高档，3-自动
     *                凉风调档：0-关闭，1-保留，3-高档
     *                风摆调档：0-保留，1-定向，2-自动
     *                照明、光暖调档：0-关闭，1-100：亮度调整
     *                换气调档：0-关闭，1-低档，2-高档，3-自动
     * @param devisort     设备实体
     * @param t    光暖调档为1，其余为0；
     * @return
     */
    public static String getDeviceStr(int level,SubDevice devisort,int t){
        String s=null;
        String id=devisort.getId();
        int dst=devisort.getDst();
        if(devisort.getType()==1&&t==1){
            dst=2;
        }
        if(t==1){
            devisort.setValue2(level);
        }else devisort.setValue1(level);
        try {
            App.db.update(devisort);
        } catch (DbException e) {
            e.printStackTrace();
        }
        MyLog.e("devisort","T:"+t+devisort.toString());
        if(devisort.getGateway_type()==0){
                String ls = Integer.toHexString(level);
                if(ls.length()==1){
                    ls="0"+ls;
                }
                if(devisort.getTp()==2||(t==1&&devisort.getTp()==1)||devisort.getTp()==4){
                    s=Star+getRedam()+middle1+"55AA5500113100"+id+"00000"+dst+"010006"+"02000010"+ls+"FF"+endStr1;
                }else {
                    s=Star+getRedam()+middle1+"55AA5500113100"+id+"00000"+dst+"010008"+"02000020"+ls+"FF"+endStr1;
                }
            }else {
                if((devisort.getTp()==2||(t==1&&devisort.getTp()==1)||devisort.getTp()==4)){
                    s=getGNComm(id,level,dst);
                }else {
                    s=Star+getRedam()+middle+shebei_con+level+" 1"+"\\r\\n send 0x"+id+" 1 "+dst+endStr;
                }
            }
        return s;
    }

    public  static String getGNComm(String id,int swi,int dst){
        String s=Star+getRedam()+middle+"zcl on-off "+(swi==0?"off":"on")+"\\r\\n send 0x"+id+" "+1+" "+dst+endStr;
        return s;
    }

    public  static String closeAll(){
        return Star+getRedam()+middle1+"55AA55000D1200FFFF00000101FC0003FF"+endStr1;
    }

    public static String getOtherStr(String command){
        return Star+getRedam()+middle+command+endStr;
    }

    public static String getOtherStr(String command,int type){
        if(type==1)
        return Star+getRedam()+middle+command+endStr;
        else return Star+getRedam()+middle1+command+endStr1;
    }

    public static String getOtherStr1(List<String> commands){
        String command="";
        for (int i = 0; i < commands.size(); i++) {
            command+="{\"D\":\""+commands.get(i)+"\"},";
        }
        return Star+getRedam()+middle2+command+"]}";
    }

    public static List<String> getCommands(List<Scenebean.ActionsBean> actionbeans){
        Map<String,StringBuffer> map=new HashMap<>();
        for (int i = 0; i <actionbeans.size(); i++) {
            StringBuffer command=new StringBuffer();
            Scenebean.ActionsBean actionsBean = actionbeans.get(i);
            int dstid = actionsBean.getDstid();
            String mac = actionsBean.getMac();
            String nclu = actionsBean.getNclu();
            SubDevice first = null;
            try {
                if("0008".equals(nclu)&&dstid==2){
                    first = App.db.selector(SubDevice.class).where("mac", "=", mac).and("dst", "=",1).findFirst();
                    if(first!=null)
                    first.setValue2(actionsBean.getVal());
                }else{
                    first = App.db.selector(SubDevice.class).where("mac", "=", mac).and("dst", "=", dstid).findFirst();
                    if(first!=null)
                    if(first.getTp()==1&&"0006".equals(actionsBean.getNclu())){
                        first.setValue2(actionsBean.getVal());
                    }else
                    first.setValue1(actionsBean.getVal());
                }

                if(first!=null){
                    if(first.getTp()==3&&first.getValue2()==1&&first.getValue1()>1){
                        first.setValue1(1);
                        actionsBean.setVal(1);
                    }
                    App.db.update(first);
                }

            } catch (DbException e) {
                e.printStackTrace();
            }
            if(first!=null)
            if (first.getGateway_type() == 1) {
                if ("0008".equals(actionsBean.getNclu())) {
                    command.append("{\"RIU\":1,\"D\":\"" + shebei_con + actionsBean.getVal() + " 1" + "\\r\\n send 0x" + first.getId() + " " + 1 + " " + dstid + "\\r\\n\"},");
                } else if ("0006".equals(actionsBean.getNclu())) {
                    command.append("{\"RIU\":1,\"D\":\"" + "zcl on-off " + (actionsBean.getVal() == 0 ? "off" : "on") + "\\r\\n send 0x" + first.getId() + " " + 1 + " " + dstid + "\\r\\n\"},");
                }
            } else {
                String ls = Integer.toHexString(actionsBean.getVal());
                if (ls.length() == 1) {
                    ls = "0" + ls;
                }
                if ("0008".equals(actionsBean.getNclu())) {
                    command.append("{\"RIU\":0,\"D\":\"" + "55AA5500113100" + first.getId() + "00000" +  dstid  + "010008"+"02000020" + ls + "FF" + "\"},");
                } else if ("0006".equals(actionsBean.getNclu())) {
                    command.append("{\"RIU\":0,\"D\":\"" + "55AA5500113100" + first.getId() + "00000" +  dstid + "010006" +"02000010" + ls + "FF" + "\"},");
                }
            }
            StringBuffer buffer = map.get(mac);
            if(buffer==null){
                map.put(mac,command);
            }else buffer.append(command);
        }
//            SceneDevice sceneDevice =sceneDevices.get(i);
//            if(sceneDevice.isControl()){
//                if(sceneDevice.getGateway_type()==0){
//                    String sid = sceneDevice.getSid();
//                    SubDevice sub=null;
//                    try {
//                       sub = App.db.findById(SubDevice.class, sid);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                    if(sub!=null){
//                        if(sub.getDst()==1){
//                            command.append("{\"D\":\""+shebei_con+sceneDevice.getValue1()+" 1"+"\\r\\n send 0x"+sub.getId()+" "+1+" "+1+"\\r\\n\"},");
//                            command.append("{\"D\":\""+"zcl on-off "+(sceneDevice.getValue2()==0?"off":"on")+"\\r\\n send 0x"+sub.getId()+" "+1+" "+2+"\\r\\n\"},");
//                        }else if(sub.getDst()==3){
//                            command.append("{\"D\":\""+shebei_con+sceneDevice.getValue1()+" 1"+"\\r\\n send 0x"+sub.getId()+" "+1+" "+3+"\\r\\n\"},");
//                            command.append("{\"D\":\""+shebei_con+sceneDevice.getValue1()+" 1"+"\\r\\n send 0x"+sub.getId()+" "+1+" "+3+"\\r\\n\"},");
//                        }else if(sub.getDst()==4||sub.getDst()==5){
//                            command.append("{\"D\":\""+"zcl on-off "+(sceneDevice.getValue2()==0?"off":"on")+"\\r\\n send 0x"+sub.getId()+" "+1+" "+sub.getDst()+"\\r\\n\"},");
//                        }else {
//                            command.append("{\"D\":\""+shebei_con+sceneDevice.getValue1()+" 1"+"\\r\\n send 0x"+sub.getId()+" "+1+" "+sub.getDst()+"\\r\\n\"},");
//                        }
//                    }
//                }else{
//                    String ls = Integer.toHexString(sceneDevice.getValue1());
//                    if(ls.length()==1){
//                        ls="0"+ls;
//                    }
//                    String ls2 = Integer.toHexString(sceneDevice.getValue2());
//                    if(ls.length()==1){
//                        ls2="0"+ls2;
//                    }
//                    String sid = sceneDevice.getSid();
//                    SubDevice sub=null;
//                    try {
//                        sub = App.db.findById(SubDevice.class, sid);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                    if(sub!=null) {
//                        if (sub.getDst()==1) {
//                            command.append("{\"D\":\"" + "55AA5500110100" + sub.getId() + "00000" + 1 + "0" + 1 + "0008" + "02000020" + ls + "FF" + "\"},");
//                            command.append("{\"D\":\"" + "55AA5500110100" + sub.getId() + "00000" + 1 + "0" + 2 + "0006" + "02000020" + ls2 + "FF" + "\"},");
//                        } else if (sub.getDst()==3) {
//                            command.append("{\"D\":\"" + "55AA5500110100" + sub.getId() + "00000" + 1 + "0" + 3 + "0006" + "02000020" + ls + "FF" + "\"},");
//                            command.append("{\"D\":\"" + "55AA5500110100" + sub.getId() + "00000" + 1 + "0" + 3 + "0006" + "02000020" + ls2 + "FF" + "\"},");
//                        } else if (sub.getDst()==4||sub.getDst()==5) {
//                            command.append("{\"D\":\"" + "55AA5500110100" + sub.getId() + "00000" + 1 + "0" + sub.getDst() + "0006" + "02000020" + ls + "FF" + "\"},");
//                        }else {
//                            command.append("{\"D\":\"" + "55AA5500110100" + sub.getId() + "00000" + 1 + "0" + sub.getDst() + "0008" + "02000020" + ls + "FF" + "\"},");
//                        }
//                    }
//                }
//            }
//        }
        Set<Map.Entry<String, StringBuffer>> entries = map.entrySet();
        List<String> list=new ArrayList<>();
        for (Map.Entry<String, StringBuffer> entry : entries) {
            StringBuffer value = entry.getValue();
            if(value.length()>0){
                list.add(Star+getRedam()+middle2+value.toString().substring(0,value.length()-1)+"]}");
            }
        }
        return list;
    }
    public static String getExitNet(String id){
        String s=Star+getRedam()+middle+"zdo leave 0x"+id+" 1 0"+endStr;
        return s;
    }
    public static String getReadString(int cluster,int attributeId,String id,int src,int dst){
        return Star+getRedam()+middle+shebei_read+cluster+" "+attributeId+"\\r\\n send 0x"+id+" "+src+" "+dst+endStr;
    }

    public static String getRead485(String id){
        return Star+getRedam()+middle1+"55AA55000D1200"+id+"00000101FC0000FF"+endStr1;
    }

    public static String getReadSceneStr(String groupId,String sceneId,String id,int type){
        if(type==0){
            return Star+getRedam()+middle1+"55AA5500101200"+id+"00000101"+"000508"+groupId+sceneId+"FF"+endStr1;
        }
        return Star+getRedam()+middle+SCENE+"read 0x"+groupId+" "+sceneId+"\\r\\n send 0x"+id+" 1 1"+endStr;
    }

    public static String getWriteSceneStr(String dataLen, String groupId, String sceneId, String id, int type, String content){
        if(type==0){
            return Star+getRedam()+middle1+"55AA55"+dataLen+"1200"+id+"00000101"+"000509"+groupId+sceneId+content+"FF"+endStr1;
        }
        return Star+getRedam()+middle+SCENE+"write {"+("0001".equals(groupId)?"0100":groupId)+sceneId+content+"}\\r\\n send 0x"+id+" 1 1"+endStr;
    }

    public static String getCallSceneStr(String groupId,String sceneId,String id,int type){
        if(type==1){
            return Star+getRedam()+middle1+"55AA5500101200"+id+"00000101"+"000505"+groupId+sceneId+"FF"+endStr1;
        }
        return Star+getRedam()+middle+SCENE+"recall 0x"+groupId+" 0x"+sceneId+"\\r\\n send 0x"+id+" 1 1"+endStr;
    }

    public static String getTimerOrLiandong(int cmd,String content){
        return "{\"cmd_id\":"+getRedam()+",\"CMD\":"+cmd+",\"list\":"+content+"}";
    }
    public static int getRedam() {
        return (int) (Math.random()*100000000);
    }

    public static boolean sendData1(int deviceid, final byte[] bs, String name){
        return sendData(DeviceManage.getInstance().getDevice(deviceid).getXDevice(),bs,name);
    }

    static Handler handler=new Handler();
    static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Iterator<DataMessage> iterator = list.iterator();
            if(iterator.hasNext()){
                DataMessage next = iterator.next();
                Boolean x = SendData(next.xDevice, next.bs,next.name);
                iterator.remove();
            }
            handler.postDelayed(this,60);
        }
    };

    public static boolean sendData(XDevice xDevice, final byte[] bs, String name) {
        list.add(new DataMessage(xDevice,bs,name));
        if(First){
            handler.post(runnable);
            First=false;
        }
//        Boolean x = SendData(xDevice, bs, name);
//        if (x != null) return x;
        return true;
    }

    @Nullable
    private static Boolean SendData(XDevice xDevice, byte[] bs, String name) {
        int  ret = XlinkAgent.getInstance().sendPipeData(xDevice, bs,10, pipeListener);
        if (ret < 0) {
            switch (ret) {
                case XlinkCode.NO_CONNECT_SERVER:
                    XlinkUtils.shortTips("发送数据失败，手机未连接服务器");
                    MyLog.e(name,"发送数据失败，手机未连接服务器");
                    EventBus.getDefault().post(new EventData(EventData.CODE_RECONNECT,xDevice));
                    break;
                case XlinkCode.NETWORD_UNAVAILABLE:
                    XlinkUtils.shortTips("当前网络不可用,发送数据失败");
                    MyLog.e(name,"当前网络不可用,发送数据失败");
                    break;
                case XlinkCode.NO_DEVICE:
                    XlinkUtils.shortTips("未找到设备");
                    MyLog.e(name,"未找到设备");
                    XlinkAgent.getInstance().initDevice(xDevice);
                    break;
                default:
                    XlinkUtils.shortTips("发送数据失败，错误码：" + ret);
                    MyLog.e(name,"发送数据失败，错误码：" + ret);
                    break;
            }

            return false;
        } else {
            if (name != null) {
                MyLog.e(name,"发送数据,msgId:" + ret + " data:(" + name + ")"
                        +new String(bs).trim());
            } else {
                MyLog.e("sendData:","发送数据,msgId:" + ret + " data:"
                        + new String(bs).trim());
            }
        }
        return null;
    }


    private static SendPipeListener pipeListener = new SendPipeListener() {

        @Override
        public void onSendLocalPipeData(XDevice device, int code, int messageId) {
            // setDeviceStatus(false);
            switch (code) {
                case XlinkCode.SUCCEED:
                    MyLog.e("pipe","发送数据,msgId:" + messageId + "成功");
                    break;
                case XlinkCode.TIMEOUT:
                    MyLog.e("pipe","发送数据,msgId:" + messageId + "超时"+",重连中...");
//                    EventBus.getDefault().post(new EventData(EventData.CODE_RECONNECT,device));
                    // XlinkUtils.shortTips("发送数据超时："
                    // + );
                    break;
                case XlinkCode.SERVER_CODE_UNAUTHORIZED:
                    XlinkUtils.shortTips("控制设备失败,当前帐号未订阅此设备，请重新订阅");
                    break;
                case XlinkCode.SERVER_DEVICE_OFFLIEN:
                    MyLog.e("pipe","设备不在线");
                    XlinkUtils.shortTips("设备不在线");
                    break;
                default:
                    XlinkUtils.shortTips("控制设备其他错误码:" + code);
                    MyLog.e("pipe","控制设备其他错误码:" + code);
                    break;
            }

        }
    };

}
