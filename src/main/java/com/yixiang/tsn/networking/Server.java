package com.yixiang.tsn.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import com.yixiang.tsn.common.*;

public class Server {
    private static final int MAX_NUM = 6000;
    private static boolean FLAG = true;


    public static int slaveOrganization() throws IOException, InterruptedException {
        //1、定义本地设备的基本信息，基本数据格式
        //todo：写成可配置文件进行读取，或者定义地址空间，根据地址直接进行读取。
        String deviceType = "0001";
        String deviceVId = "01X39FKS091LSO23";
        String status = "0001";
        //设备信息，等待
        String deviceInfo = "0002";


        //2、组装数据帧
        String datagram = assembleDatagram(deviceType, deviceVId, status, deviceInfo);

        //3、发送广播

        //4、广播时开始监听TCP消息，来自主站的应答, 根据主站的应答，判断组网状态，返回对应的数值。
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (200 == listenMaster()) {
                        FLAG = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

        DatagramSocket socket = new DatagramSocket();
        InetAddress addr = InetAddress.getByName(NetworkInfo.BROADCAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(datagram.getBytes(), datagram.length(), addr, NetworkInfo.NET_SLAVE_TO_MASTER_PORT);


        int tries = 0;
        while (FLAG && tries < MAX_NUM) {
            socket.send(packet);
            System.out.println("data send : " + datagram);
            //休眠 1s
            Thread.sleep(2000);
            tries++;
        }
        socket.close();

        if (!FLAG && tries < MAX_NUM) {
            return 200;
        } else {
            return 300;
        }

    }


    private static String assembleDatagram(String deviceType, String deviceVId, String status, String deviceInfo)  {
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
        String datagram = NetworkInfo.NET_SLAVE_TO_MASTER + deviceType + deviceInfo + status + deviceVId + "  time:" + now;

        return datagram;
    }


    private static int listenMaster() throws IOException {
        System.out.println("开始监听主站应答消息...");
        //监听主站的返回信息
        byte[] buff = new byte[1024];
        DatagramSocket receiveSocket = new DatagramSocket(NetworkInfo.NET_MASTER_TO_SLAVE_PORT);
        DatagramPacket revPacket = new DatagramPacket(buff, 1024);
        receiveSocket.receive(revPacket);
        //保存主站的ip地址
        NetworkInfo.MASTER_ADDR = revPacket.getAddress().getHostAddress();
        System.out.println(NetworkInfo.MASTER_ADDR);
        receiveSocket.close();

        //来自主站的应答
        String msg = new String(revPacket.getData(), StandardCharsets.UTF_8);
        System.out.println(msg);

        if (NetworkInfo.NET_MASTER_TO_SLAVE.equals(msg.substring(0, 4))) {
            System.out.println("收到来自主站的应答...");
            if ("0200".equals(msg.substring(4, 8))) {
                String deviceID = msg.substring(8, 12);
                //保存从站分配到的ID
                NetworkInfo.ID = deviceID;
                String status = "0001".equals(msg.substring(12, 16)) ? "normal" : "error";
                String deviceVId = msg.substring(16, 32);
                System.out.printf("组网成功, 从站ID为：%s, 设备ID为：%s, 设备状态: %s\n", deviceID, deviceVId, status);
                return 200;
            } else {
                System.out.println("设备类型错误...");
                return 301;
            }
        } else {
            System.out.println("主站返回消息，协议类型错误");
            return 300;
        }

    }

}