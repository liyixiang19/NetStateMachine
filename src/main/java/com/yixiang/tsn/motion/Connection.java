package com.yixiang.tsn.motion;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.yixiang.tsn.common.NetworkInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * @author liyixiang
 */
public class Connection {
    public static void receive() throws IOException {
        System.out.println("----------------------等待控制命令--------------------");
        while (true) {
            //等待运动控制命令
            //监听主站的返回信息
            byte[] buff = new byte[1024];
            DatagramSocket receiveSocket = new DatagramSocket(NetworkInfo.CONTROL_BROADCAST_PORT);
            DatagramPacket revPacket = new DatagramPacket(buff, 1024);
            receiveSocket.receive(revPacket);
            System.out.println("收到来自主站的控制命令...");
            //来自主站的控制命令
            String msg = new String(revPacket.getData(), StandardCharsets.UTF_8);
            System.out.println(msg);

            if (NetworkInfo.CONTROL_TYPE.equals(msg.substring(0, 4))) {
                //解析主站的控制命令，向串口发送控制命令
                analysisDataGram(msg);
            } else {
                System.out.println("协议类型错误 ------ 非控制命令");
            }
        }
    }


    private static void analysisDataGram(String data) {
        String deviceId = data.substring(32, 36);
        String deviceType = data.substring(4, 8);
        String controlWord = data.substring(12, 16);
        switch (controlWord) {
            case NetworkInfo.RUN_MODE_1:
                Controller.runMode1();
                break;
            case NetworkInfo.RUN_MODE_2:
                Controller.runMode2();
                break;
            case NetworkInfo.RUN_MODE_3:
                Controller.runMode3();
                break;
            default:
                System.out.println("未知的运动模式");
        }
    }
}
