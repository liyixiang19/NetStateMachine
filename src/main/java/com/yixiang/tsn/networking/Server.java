package com.yixiang.tsn.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalTime;
import com.yixiang.tsn.common.*;

public class Server {
    public static final int MAX_NUM = 6000;
    public static boolean FLAG = true;


    public static int slaveOrganization() throws IOException, InterruptedException {
        //1、定义本地设备的基本信息，基本数据格式
        //todo：写成可配置文件进行读取，或者定义地址空间，根据地址直接进行读取。
        String status = "0001";

        //2、组装数据帧
        String datagram = assembleDatagram(NetworkInfo.DEVICE_TYPE, NetworkInfo.VID, status, NetworkInfo.DEVICE_INFO);

        //3、发送广播
        DatagramSocket socket = new DatagramSocket();
        InetAddress addr = InetAddress.getByName(NetworkInfo.BROADCAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(datagram.getBytes(), datagram.length(), addr, NetworkInfo.NET_SLAVE_TO_MASTER_PORT);

        int tries = 0;
        while (FLAG && tries < MAX_NUM) {
            socket.send(packet);
            System.out.println("data send : " + datagram);
            //休眠 1s
            Thread.sleep(5000);
            tries++;
        }
        socket.close();

        if (!FLAG && tries < MAX_NUM) {
            return 200;
        } else {
            return 300;
        }

    }


    private static String assembleDatagram(String deviceType, String deviceVid, String status, String deviceInfo)  {
        //定义传输的数据格式
        /**
         * deviceType :
         * 01--motor 02--I/O
         * deviceId : (the only different id for every device, 16)
         * 01X39FKS091LSO23
         * status ：
         * 01--normal 02--malfunction
         */

        LocalTime now = LocalTime.now();
        return NetworkInfo.NET_SLAVE_TO_MASTER + deviceType + deviceInfo + status + deviceVid + "  time:" + now;
    }




}