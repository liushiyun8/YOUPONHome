package com.youpon.home1.bean.gsonBeas;

/**
 * Created by computer on 2016/11/28.
 */
public class Devicebean {
    public Devicebean() {
    }

    public Devicebean(String name, String IP, int port, String MAC, String firmwareRev, String fogProductId, String isEasylinkOK, String isHaveSuperUser, String remainingUserNumber, String hardwareRev, String MICOOSRev, String model, String protocol, String manufacturer, String seed) {
        Name = name;
        this.IP = IP;
        Port = port;
        this.MAC = MAC;
        FirmwareRev = firmwareRev;
        FogProductId = fogProductId;
        IsEasylinkOK = isEasylinkOK;
        IsHaveSuperUser = isHaveSuperUser;
        RemainingUserNumber = remainingUserNumber;
        HardwareRev = hardwareRev;
        this.MICOOSRev = MICOOSRev;
        Model = model;
        Protocol = protocol;
        Manufacturer = manufacturer;
        Seed = seed;
    }

    /**
     * Name : EMW3081 Module#DA3F8E
     * IP : 192.168.31.114
     * Port : 8002
     * MAC : C8:93:46:DA:3F:8E
     * FirmwareRev : fog_3081_probe_device@002
     * FogProductId : 3311097c-17f5-11e6-a739-00163e0204c0
     * IsEasylinkOK : false
     * IsHaveSuperUser : false
     * RemainingUserNumber : 3
     * HardwareRev : 3081
     * MICOOSRev : 30810002.049
     * Model : EMW3081
     * Protocol : com.mxchip.fog
     * Manufacturer : MXCHIP Inc.
     * Seed : 135
     */

    private String Name;
    private String IP;
    private int Port;
    private String MAC;
    private String FirmwareRev;
    private String FogProductId;
    private String IsEasylinkOK;
    private String IsHaveSuperUser;
    private String RemainingUserNumber;
    private String HardwareRev;
    private String MICOOSRev;
    private String Model;
    private String Protocol;
    private String Manufacturer;
    private String Seed;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int Port) {
        this.Port = Port;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getFirmwareRev() {
        return FirmwareRev;
    }

    public void setFirmwareRev(String FirmwareRev) {
        this.FirmwareRev = FirmwareRev;
    }

    public String getFogProductId() {
        return FogProductId;
    }

    public void setFogProductId(String FogProductId) {
        this.FogProductId = FogProductId;
    }

    public String getIsEasylinkOK() {
        return IsEasylinkOK;
    }

    public void setIsEasylinkOK(String IsEasylinkOK) {
        this.IsEasylinkOK = IsEasylinkOK;
    }

    public String getIsHaveSuperUser() {
        return IsHaveSuperUser;
    }

    public void setIsHaveSuperUser(String IsHaveSuperUser) {
        this.IsHaveSuperUser = IsHaveSuperUser;
    }

    public String getRemainingUserNumber() {
        return RemainingUserNumber;
    }

    public void setRemainingUserNumber(String RemainingUserNumber) {
        this.RemainingUserNumber = RemainingUserNumber;
    }

    public String getHardwareRev() {
        return HardwareRev;
    }

    public void setHardwareRev(String HardwareRev) {
        this.HardwareRev = HardwareRev;
    }

    public String getMICOOSRev() {
        return MICOOSRev;
    }

    public void setMICOOSRev(String MICOOSRev) {
        this.MICOOSRev = MICOOSRev;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String Protocol) {
        this.Protocol = Protocol;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String Manufacturer) {
        this.Manufacturer = Manufacturer;
    }

    public String getSeed() {
        return Seed;
    }

    public void setSeed(String Seed) {
        this.Seed = Seed;
    }

    @Override
    public String toString() {
        return "Devicebean{" +
                "Name='" + Name + '\'' +
                ", IP='" + IP + '\'' +
                ", Port=" + Port +
                ", MAC='" + MAC + '\'' +
                ", FirmwareRev='" + FirmwareRev + '\'' +
                ", FogProductId='" + FogProductId + '\'' +
                ", IsEasylinkOK='" + IsEasylinkOK + '\'' +
                ", IsHaveSuperUser='" + IsHaveSuperUser + '\'' +
                ", RemainingUserNumber='" + RemainingUserNumber + '\'' +
                ", HardwareRev='" + HardwareRev + '\'' +
                ", MICOOSRev='" + MICOOSRev + '\'' +
                ", Model='" + Model + '\'' +
                ", Protocol='" + Protocol + '\'' +
                ", Manufacturer='" + Manufacturer + '\'' +
                ", Seed='" + Seed + '\'' +
                '}';
    }
}
