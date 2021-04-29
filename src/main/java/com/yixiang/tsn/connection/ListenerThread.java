package com.yixiang.tsn.connection;

import com.yixiang.tsn.common.NetworkInfo;
import com.yixiang.tsn.motion.Controller;
import com.yixiang.tsn.networking.Server;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * @author liyixiang
 */
public class ListenerThread implements Runnable{

    private String name;

    public ListenerThread(String name){
        this.name = name ;
    }

    @Override
    public void run(){
        System.out.println("开始监听主站应答消息...");
        DatagramSocket receiveSocket = null;
        Controller controller = new Controller();
        try {
            receiveSocket = new DatagramSocket(NetworkInfo.NET_MASTER_TO_SLAVE_PORT);
            while (true) {
                //监听主站的返回信息
                byte[] buff = new byte[1024];
                DatagramPacket revPacket = new DatagramPacket(buff, 1024);
                receiveSocket.receive(revPacket);
                //保存主站的ip地址
                NetworkInfo.MASTER_ADDR = revPacket.getAddress().getHostAddress();
                //来自主站的消息
                String msg = new String(revPacket.getData(), StandardCharsets.UTF_8);
                System.out.println("\n收到的数据：" + msg);
                //初始化串口
                if (NetworkInfo.NET_MASTER_TO_SLAVE.equals(msg.substring(0, 4))) {
                    System.out.println("收到来自主站的组网应答...");
                    analysisNetDataGram(msg);
                } else if (NetworkInfo.CONTROL_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("收到来自主站的控制命令...");
                    //解析主站的控制命令，向串口发送控制命令
                    analysisControlDataGram(msg, controller);
                } else if (NetworkInfo.QUERY_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("收到来自主站的查询命令...");
                    analysisQueryDataGram(msg, controller);
                } else if (NetworkInfo.SET_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("收到来自主站的设置命令...");
                    analysisSetDataGram(msg, controller);
                } else if (NetworkInfo.REAL_TIME_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("收到来自主站的实时数据监测命令...");
                    analysisRealtimeDataGram(msg, controller);
                } else {
                    System.out.println("协议类型错误 ------ 非控制命令");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 组网报文分析
     * @param data
     */
    private static void analysisNetDataGram(String data) {
        if ("0200".equals(data.substring(4, 8))) {
            String deviceID = data.substring(8, 12);
            //保存从站分配到的ID
            NetworkInfo.DEVICE_ID = deviceID;
            String status = "0001".equals(data.substring(12, 16)) ? "normal" : "error";
            String deviceVId = data.substring(16, 32);
            System.out.printf("组网成功, 从站ID为：%s, 设备ID为：%s, 设备状态: %s\n", deviceID, deviceVId, status);
            Server.FLAG = false;
        } else {
            System.out.println("组网失败...");
        }
    }

    /**
     * Description: 控制报文分析
     * @param data
     */
    private static void analysisControlDataGram(String data, Controller controller) {
        String deviceId = data.substring(8, 12);
        String deviceType = data.substring(4, 8);
        String controlWord = data.substring(12, 16);
        if (deviceId.equals(NetworkInfo.DEVICE_ID)) {
            System.out.println("-----------收到本机的控制指令---------");
            switch (controlWord) {
                case NetworkInfo.RUN_MODE_1:
                    System.out.println("运动控制 ====> 模式1");
                    Thread t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            controller.runMode1();
                        }
                    });
                    t1.start();
                    break;
                case NetworkInfo.RUN_MODE_2:
                    System.out.println("运动控制 ====> 模式2");
                    Thread t2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            controller.runMode2();
                        }
                    });
                    t2.start();
                    break;
                case NetworkInfo.RUN_MODE_3:
                    System.out.println("运动控制 ====> 模式3");
                    Thread t3 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            controller.runMode3();
                        }
                    });
                    t3.start();
                    break;
                default:
                    System.out.println("未知的运动模式");
            }
        }
    }

    /**
     * Description: 查询报文分析
     * @param data
     */
    private static void analysisQueryDataGram(String data, Controller controller) {
        String queryDataType = data.substring(12, 16);
        String deviceId = data.substring(8, 12);
        String result = "";
        if (NetworkInfo.DEVICE_ID.equals(deviceId)) {
            System.out.println("-----------收到本机的查询指令---------");
            try {
                switch (queryDataType) {
                    case NetworkInfo.VELOCITY:
                        Thread.sleep(100);
                        result = controller.queryVelocity();
                        Thread.sleep(100);
                        result = controller.queryVelocity();
                        System.out.println("查询结果： ======== " + result);
                        dataDeal(result);
                        break;
                    case NetworkInfo.PLUS:
                        Thread.sleep(100);
                        result = controller.queryPlus();
                        Thread.sleep(100);
                        result = controller.queryPlus();
                        System.out.println("查询结果： ======== " + result);
                        dataDeal(result);
                        break;
                    case NetworkInfo.POSITION:
                        Thread.sleep(100);
                        result = controller.queryNum();
                        Thread.sleep(100);
                        result = controller.queryNum();
                        System.out.println("查询结果： ======== " + result);
                        dataDeal(result);
                        break;
                    default:
                        System.out.println("未知的查询信息");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description: 设置报文分析
     * @param data
     */
    private static void analysisSetDataGram(String data, Controller controller) {
        String setDataType = data.substring(12, 16);
        String deviceId = data.substring(8, 12);
        String value = data.substring(32, 36);
        int val = Integer.parseInt(value);
        if (NetworkInfo.DEVICE_ID.equals(deviceId)) {
            System.out.println("-----------收到本机的设置指令---------");
            System.out.println("指令： ");
            try {
                switch (setDataType) {
                    case NetworkInfo.VELOCITY:
                        System.out.println("指令： 设置速度,  数值： " + val);
                        Thread.sleep(100);
                        controller.setVelocity(val);
                        break;
                    case NetworkInfo.PLUS:
                        System.out.println("指令： 设置脉冲,  数值： " + val);
                        Thread.sleep(100);
                        controller.setPlus(val);
                        break;
                    case NetworkInfo.POSITION:
                        System.out.println("指令： 设置位置,  数值： " + val);
                        Thread.sleep(100);
                        controller.setNumber(val);
                        break;
                    default:
                        System.out.println("未知的设置信息");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description: 实时数据查询报文分析
     * @param data
     * @param controller
     */
    private void analysisRealtimeDataGram(String data, Controller controller) {
        Thread realtimeSendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String queryDataType = data.substring(12, 16);
                String deviceId = data.substring(8, 12);
                try {
                    String result = "";
                    int counter = 0;
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress addr = InetAddress.getByName(NetworkInfo.BROADCAST_ADDRESS);
                    if (NetworkInfo.DEVICE_ID.equals(deviceId)) {
                        if (NetworkInfo.POSITION.equals(queryDataType)) {
                            System.out.println("-----------收到本机的实时监测指令---------");
                            while (NetworkInfo.REAL_TIME_FLAG) {
                                try {
                                    Thread.sleep(100);
                                    result = controller.queryNum();
                                    Thread.sleep(100);
                                    result = controller.queryNum();
                                    System.out.println("查询结果： ======== " + result);
                                    //发送实时结果
                                    String data_4 = String.format("%4s", result).replace(" ", "0");
                                    String datagram = NetworkInfo.QUERY_BACK_TYPE + NetworkInfo.DEVICE_TYPE + NetworkInfo.DEVICE_ID +
                                            NetworkInfo.REALTIME_DATA_BACK_FLAG + NetworkInfo.VID + data_4;
                                    DatagramPacket packet = new DatagramPacket(datagram.getBytes(), datagram.length(),
                                            addr, NetworkInfo.NET_SLAVE_TO_MASTER_PORT);
                                    socket.send(packet);
                                    Thread.sleep(1000);
                                    if (counter > 120) {
                                        NetworkInfo.REAL_TIME_FLAG = false;
                                    }
                                    counter++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        realtimeSendThread.start();
    }

    /**
     * Description: 返回查询结果
     * @param data
     * @throws Exception
     */
    private static void dataDeal(String data) throws Exception {
        String data_4 = String.format("%4s", data).replace(" ", "0");
        String datagram = NetworkInfo.QUERY_BACK_TYPE + NetworkInfo.DEVICE_TYPE + NetworkInfo.DEVICE_ID + "0000" + NetworkInfo.VID + data_4;
        DatagramSocket socket = new DatagramSocket();
        InetAddress addr = InetAddress.getByName(NetworkInfo.BROADCAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(datagram.getBytes(), datagram.length(), addr, NetworkInfo.NET_SLAVE_TO_MASTER_PORT);
        socket.send(packet);
        socket.close();
    }

}

