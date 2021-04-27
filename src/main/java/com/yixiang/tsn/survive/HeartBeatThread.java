package com.yixiang.tsn.survive;

import com.yixiang.tsn.common.NetworkInfo;
import java.net.*;
import java.time.LocalTime;


/**
 * @author liyixiang
 */
public class HeartBeatThread implements Runnable {

    private String name;

    public HeartBeatThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("主站地址为： === " + NetworkInfo.MASTER_ADDR);
        try {
            while (true) {
                System.out.println("发送心跳信息");
                String aliveMsg = assembleData();
                DatagramSocket socket = new DatagramSocket();
                InetAddress addr = InetAddress.getByName(NetworkInfo.MASTER_ADDR);
                DatagramPacket packet = new DatagramPacket(aliveMsg.getBytes(), aliveMsg.length(), addr, NetworkInfo.HEARTBEAT_SLAVE_PORT);
                socket.send(packet);
                Thread.sleep(20000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * protocol type : heartbeat == 0312
     * deviceType : 01--motor 02--I/O
     * vid : (the only different id for every device, 16bytes == eg.01X39FKS091LSO23
     * status : 01--normal 02--malfunction
     */
    private static String assembleData() {
        //定义传输的数据格式
        String dataGram = "";
        String controlWord = "0000";
        LocalTime now = LocalTime.now();
        dataGram = NetworkInfo.HEARTBEAT_TYPE + NetworkInfo.DEVICE_TYPE + NetworkInfo.DEVICE_INFO + controlWord +
                NetworkInfo.VID + NetworkInfo.ID + "  time:" + now;

        return dataGram;
    }
}