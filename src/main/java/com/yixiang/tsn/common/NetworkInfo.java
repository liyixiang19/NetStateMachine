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
    public final static int NET_MASTER_TO_SLAVE_PORT = 8000;
    public static String BROADCAST_ADDRESS = "255.255.255.255";

    //device info
    public static String DEVICE_ID = "";
    public static String VID = "01X39FKS091L111C";
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

    /**
     * set function
     * 需要填写数据，并追加CRC16/modbus
     * 校验SET_PLUS + DATA + CRC
     */
    public static String SET_VELOCITY = "01060005";
    public static String SET_NUMBER = "01060004";
    public static String SET_PLUS = "01060007";

    //返回值
    public static int NET_SUCCESS = 200;

    //查询值类型
    public final static String QUERY_TYPE = "0714";
    public final static String VELOCITY = "1011";
    public final static String PLUS = "1012";
    public final static String POSITION = "1013";
    //查询实时数据
    public final static String REAL_TIME_TYPE = "0716";
    public static boolean REAL_TIME_FLAG = true;
    //实时数据查询标志
    public final static String REALTIME_DATA_BACK_FLAG = "9988";
    //查询值返回类型
    public final static String QUERY_BACK_TYPE = "0715";
    //设置值类型
    public final static String SET_TYPE = "0861";

}
