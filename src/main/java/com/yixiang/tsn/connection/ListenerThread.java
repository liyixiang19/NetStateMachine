package com.yixiang.tsn.connection;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.yixiang.tsn.common.NetworkInfo;
import com.yixiang.tsn.motion.Controller;
import com.yixiang.tsn.motion.SerialTool;
import com.yixiang.tsn.networking.Server;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author liyixiang
 */
public class ListenerThread implements Runnable{

    private String name;

    public ListenerThread(String name){
        this.name = name ;
    }

    // 覆写run()方法，作为线程 的操作主体
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
                System.out.println("收到的数据：" + msg);
                //初始化串口
                if (NetworkInfo.NET_MASTER_TO_SLAVE.equals(msg.substring(0, 4))) {
                    System.out.println("\n收到来自主站的组网应答...");
                    analysisNetDataGram(msg);
                } else if (NetworkInfo.CONTROL_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("\n收到来自主站的控制命令...");
                    //解析主站的控制命令，向串口发送控制命令
                    analysisControlDataGram(msg, controller);
                } else if (NetworkInfo.QUERY_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("\n收到来自主站的查询命令...");
                    analysisQueryDataGram(msg, controller);
                } else if (NetworkInfo.SET_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("\n收到来自主站的设置命令...");
                    analysisSetDataGram(msg, controller);
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
                    controller.runMode1();
                    break;
                case NetworkInfo.RUN_MODE_2:
                    System.out.println("运动控制 ====> 模式2");
                    controller.runMode2();
                    break;
                case NetworkInfo.RUN_MODE_3:
                    System.out.println("运动控制 ====> 模式3");
                    controller.runMode3();
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
                        result = controller.queryVelocity();
                        Thread.sleep(100);
                        result = controller.queryVelocity();
                        System.out.println("查询结果： ======== " + result);
                        dataDeal(result);
                        break;
                    case NetworkInfo.PLUS:
                        result = controller.queryPlus();
                        Thread.sleep(100);
                        result = controller.queryPlus();
                        System.out.println("查询结果： ======== " + result);
                        dataDeal(result);
                        break;
                    case NetworkInfo.POSITION:
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
            //发送给主站

        }
    }

    /**
     * Description: 设置报文分析
     * @param data
     */
    private static void analysisSetDataGram(String data, Controller controller) throws IOException {
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
                        controller.setVelocity(val);
                        break;
                    case NetworkInfo.PLUS:
                        System.out.println("指令： 设置脉冲,  数值： " + val);
                        Thread.sleep(500);
                        controller.setPlus(val);
                        break;
                    case NetworkInfo.POSITION:
                        System.out.println("指令： 设置位置,  数值： " + val);
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

