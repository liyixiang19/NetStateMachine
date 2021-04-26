package com.yixiang.tsn.motion;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.yixiang.tsn.common.NetworkInfo;
import com.yixiang.tsn.networking.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author liyixiang
 */
// 实现Runnable接口，作为线程的实现类
public class ListenerThread implements Runnable{
    // 表示线程的名称
    private String name ;

    // 通过构造方法配置name属性
    public ListenerThread(String name){
        this.name = name ;
    }

    // 覆写run()方法，作为线程 的操作主体
    @Override
    public void run(){
        System.out.println("开始监听主站应答消息...");
        DatagramSocket receiveSocket = null;
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
                if (NetworkInfo.NET_MASTER_TO_SLAVE.equals(msg.substring(0, 4))) {
                    System.out.println("收到来自主站的组网应答...");
                    analysisNetDataGram(msg);
                } else if (NetworkInfo.CONTROL_TYPE.equals(msg.substring(0, 4))) {
                    System.out.println("收到来自主站的控制命令...");
                    //解析主站的控制命令，向串口发送控制命令
                    analysisControlDataGram(msg);
                } else {
                    System.out.println("协议类型错误 ------ 非控制命令");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void analysisNetDataGram(String data) {
        if ("0200".equals(data.substring(4, 8))) {
            String deviceID = data.substring(8, 12);
            //保存从站分配到的ID
            NetworkInfo.ID = deviceID;
            String status = "0001".equals(data.substring(12, 16)) ? "normal" : "error";
            String deviceVId = data.substring(16, 32);
            System.out.printf("组网成功, 从站ID为：%s, 设备ID为：%s, 设备状态: %s\n", deviceID, deviceVId, status);
            Server.FLAG = false;
        } else {
            System.out.println("组网失败...");
        }
    }

    private static void analysisControlDataGram(String data) {
        String deviceId = data.substring(8, 12);
        String deviceType = data.substring(4, 8);
        String controlWord = data.substring(12, 16);
        switch (controlWord) {
            case NetworkInfo.RUN_MODE_1:
                System.out.println("运动控制 ====> 模式1");
                Controller.runMode1();
                break;
            case NetworkInfo.RUN_MODE_2:
                System.out.println("运动控制 ====> 模式2");
                Controller.runMode2();
                break;
            case NetworkInfo.RUN_MODE_3:
                System.out.println("运动控制 ====> 模式3");
                Controller.runMode3();
                break;
            default:
                System.out.println("未知的运动模式");
        }
    }
}