package com.yixiang.tsn.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.Date;

public class Client {
    private static final int TIMEOUT = 5000;
    private static final int MAXNUM = 3;

    public static void main(String[] args) throws IOException {
//        LocalTime now = LocalTime.now();

        while (true) {
            System.out.println("开始接收从站组网信息...");
            //创建一个服务器socket接收实例，并绑定监听端口
            DatagramSocket serverSocket = new DatagramSocket(9000);
            //创建接收缓冲区实例
            byte[] buff = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            //将数据缓存到packet中
            serverSocket.receive(packet);

            String msg = new String(packet.getData());
            InetAddress addr = packet.getAddress();
            int port = packet.getPort();
            System.out.printf("收到来自 %s：%d 的数据：======%s\n", addr.toString(), port, msg);

            if (200 == judgeMsg(msg, addr)) {
                System.out.println("收到从站信息，发送响应...");
                break;
            }

            serverSocket.close();
        }


    }

    public static int judgeMsg(String msg, InetAddress addr) throws IOException {

        if ("0812".equals(msg.substring(0, 4))) {
            System.out.println("protocol type correct!");
            String organizationStatus = "0200";
            String deviceId = "0001";
            String status = "0001";
            LocalTime now = LocalTime.now();
            String response_msg = "0827" + organizationStatus + deviceId + status + msg.substring(16,32) + "  time:" + now;
            DatagramSocket responseSocket = new DatagramSocket();
            DatagramPacket responsePacket = new DatagramPacket(response_msg.getBytes(), response_msg.length(), addr, 8000);

            responseSocket.send(responsePacket);
            System.out.println("data response send : " + response_msg);
            responseSocket.close();
            return 200;
        }else {
            return 300;
        }

    }
}

