package com.srthink.iotboxaar.models;

/**
 * @author liwanlian
 * @date 2021/5/26 15:21
 */
public class DeviceInfo {
    public String deviceCode;//设备名称
    public String IotProduction;//生产批号
    public String IotType;//设备类型
    public String IotSoftVersion;//软件版本
    public String IotFirmVersion;//固件版本
    public String IotCPUType;//cpu类型
    public String IotMemory;//内存容量
    public String IotRunning;//运行内存
    public String IotAddress;//设备当前的外网Ip地址
    public String IotDisplay;//屏幕信息

    public String SysVersion;
    public String SoftwareVersion;

    /**
     * 收银机需要上报的信息
     *
     * @param deviceCode
     * @param iotProduction
     * @param iotType
     * @param iotSoftVersion
     * @param iotFirmVersion
     * @param iotCPUType
     * @param iotMemory
     * @param iotRunning
     * @param iotAddress
     * @param iotDisplay
     */
    public DeviceInfo(String deviceCode, String iotProduction, String iotType, String iotSoftVersion, String iotFirmVersion, String iotCPUType, String iotMemory, String iotRunning, String iotAddress, String iotDisplay) {
        this.deviceCode = deviceCode;
        IotProduction = iotProduction;
        IotType = iotType;
        IotSoftVersion = iotSoftVersion;
        IotFirmVersion = iotFirmVersion;
        IotCPUType = iotCPUType;
        IotMemory = iotMemory;
        IotRunning = iotRunning;
        IotAddress = iotAddress;
        IotDisplay = iotDisplay;
    }

    /**
     * 雕刻机需要上报的信息
     *
     * @param iotSoftVersion
     * @param iotFirmVersion
     */
    public DeviceInfo(String iotSoftVersion, String iotFirmVersion) {
        IotSoftVersion = iotSoftVersion;
        IotFirmVersion = iotFirmVersion;
    }

    /**
     * 发卡机
     *
     * @param deviceCode
     * @param sysVersion
     * @param softwareVersion
     */
    public DeviceInfo(String deviceCode, String sysVersion, String softwareVersion) {
        this.deviceCode = deviceCode;
        SysVersion = sysVersion;
        SoftwareVersion = softwareVersion;
    }
}
