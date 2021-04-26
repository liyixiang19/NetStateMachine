package com.yixiang.tsn.common;

public class NetworkInfo {
    //心跳协议相关
    public static String MASTER_ADDR;
    public static int HEARTBEAT_SLAVE_PORT = 9601;
    public static String SLAVE_ADDR;
    public static String HEARTBEAT_TYPE = "0312";
    //组网协议相关
    public static String NET_SLAVE_TO_MASTER = "0812";
    public static String NET_MASTER_TO_SLAVE = "0827";
    public static int NET_SLAVE_TO_MASTER_PORT = 9000;
    public static int NET_MASTER_TO_SLAVE_PORT = 8000;
    public static String BROADCAST_ADDRESS = "255.255.255.255";

    //device info
    public static String ID = "";
    public static String VID = "01X39FKS091LSO23";
    public static String DEVICE_TYPE = "0001";
    public static String DEVICE_INFO = "0000";

    //运动控制
    public static int CONTROL_BROADCAST_PORT = 9991;
    public final static String CONTROL_TYPE = "0625";
    public final static String RUN_MODE_1 = "2301";
    public final static String RUN_MODE_2 = "2302";
    public final static String RUN_MODE_3 = "2303";
    // query function
    public static String QUERY_NUM = "010300040001C5CB";
    public static String QUERY_PLUS = "01030007000135CB";
    public static String QUERY_VELOCITY = "010300050001940B";

    // control function
    public static String FORWARD = "010600000001480A";
    public static String BACKWARD = "01060001000119CA";
    public static String STOP = "010600020001E9CA";

    // set function
    // 需要填写数据，并追加CRC16/modbus校验
    // SET_PLUS + DATA + CRC
    public static String SET_VELOCITY = "01060005";
    public static String SET_NUMBER = "01060004";
    public static String SET_PLUS = "01060007";

}
