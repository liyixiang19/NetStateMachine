package com.yixiang.tsn.motion;


import com.yixiang.tsn.common.NetworkInfo;
import com.yixiang.tsn.util.CRC16M;
import com.yixiang.tsn.util.CRC_16;
import com.yixiang.tsn.util.Util;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Controller {

    SerialPort serialPort = null;

    public Controller() {
        try {
            ArrayList<String> list = SerialTool.findPort();
            serialPort = SerialTool.openPort(list.get(0), 9600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Description: 前进功能
     * @throws Exception
     */
    private void forward() throws Exception {
        byte[] bytes = Util.hexStrToBinaryStr(NetworkInfo.FORWARD);
        SerialTool.sendToPort(serialPort, bytes);
    }

    /**
     * Description: 后退功能
     * @throws Exception
     */
    private void backward() throws Exception {
        byte[] bytes = Util.hexStrToBinaryStr(NetworkInfo.BACKWARD);
        SerialTool.sendToPort(serialPort, bytes);
    }

    /**
     * Description: 停止功能
     * @throws Exception
     */
    private void stop() throws Exception {
        byte[] bytes = Util.hexStrToBinaryStr(NetworkInfo.STOP);
        SerialTool.sendToPort(serialPort, bytes);
    }

    /**
     * Description: 查询电机速度
     * @return 16进制数据的字节流
     * @throws Exception
     */
    public String queryVelocity() throws Exception {
        return query(NetworkInfo.QUERY_VELOCITY);
    }

    /**
     * Description: 查询电机脉冲数
     * @throws Exception
     */
    public String queryPlus() throws Exception {
        return query(NetworkInfo.QUERY_PLUS);
    }

    /**
     * Description: 查询电机计数
     * @throws Exception
     */
    public String queryNum() throws Exception {
        return query(NetworkInfo.QUERY_NUM);
    }

    /**
     * Description: 查询功能
     * @param content
     * @return
     * @throws Exception
     */
    public String query(String content) throws Exception {
        byte[] bytes = Util.hexStrToBinaryStr(content);
        SerialTool.sendToPort(serialPort, bytes);
        Thread.sleep(50);
        byte[] receiveBytes = SerialTool.readFromPort(serialPort);
        if (null != receiveBytes) {
            long dec_num = Long.parseLong(Util.bytesToHexStr(receiveBytes), 16);
            return String.valueOf(dec_num);
        }
        return "";
    }

    /**
     * Description: 设置电机速度
     * @throws Exception
     */
    public void setVelocity(int velocity) throws Exception {
        String hexData = String.format("%4s", Integer.toHexString(velocity)).replace(" ", "0");
        String str = NetworkInfo.SET_VELOCITY + hexData;
        //CRC checksum
        byte[] crcBuf = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(crcBuf);
        byte[] bytes = Util.hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
    }

    /**
     * Description: 设置电机脉冲数
     * @throws Exception
     */
    public void setPlus(int plus) throws Exception {
        String hexPlus = String.format("%4s", Integer.toHexString(plus)).replace(" ", "0");
        String str = NetworkInfo.SET_PLUS + hexPlus;
        //CRC checksum
        byte[] crcBuf = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(crcBuf);
        byte[] bytes = Util.hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
    }

    /**
     * Description: 设置电机计数
     * @throws Exception
     */
    public void setNumber(int number) throws Exception {
        String hexPlus = String.format("%4s", Integer.toHexString(number)).replace(" ", "0");
        String str = NetworkInfo.SET_NUMBER + hexPlus;
        //CRC checksum
        byte[] crcBuf = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(crcBuf);
        byte[] bytes = Util.hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
    }

    public void runMode1() {
        System.out.println("run mode 1");
        int count = 0;
        try {
            while (true) {
                System.out.println(">>>>>>>前进模式<<<<<<<");
                Thread.sleep(100);
                forward();
                Thread.sleep(1000);
                System.out.println(">>>>>>>停止<<<<<<<");
                stop();
                Thread.sleep(500);
                System.out.println(">>>>>>>后退模式<<<<<<<");
                backward();
                Thread.sleep(1000);
                stop();
                if (count > 5) {
                    break;
                }
                count++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void runMode2() {
        System.out.println("run mode 2");
        try {
            Thread.sleep(100);
            System.out.println(">>>>>>>前进模式<<<<<<<");
            backward();
            Thread.sleep(5000);
            System.out.println(">>>>>>>停止<<<<<<<");
            stop();
            Thread.sleep(2000);
            System.out.println(">>>>>>>前进模式<<<<<<<");
            forward();
            Thread.sleep(10000);
            stop();
            Thread.sleep(500);
            System.out.println(">>>>>>>后退模式<<<<<<<");
            backward();
            Thread.sleep(6000);
            System.out.println(">>>>>>>停止<<<<<<<");
            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runMode3() {
        System.out.println("run mode 3");
        int counter = 0;
        try {
            while (true) {
                Thread.sleep(100);
                System.out.println(">>>>>>>前进模式<<<<<<<");
                forward();
                Thread.sleep(5000);
                System.out.println(">>>>>>>停止<<<<<<<");
                stop();
                Thread.sleep(2000);
                System.out.println(">>>>>>>后退模式<<<<<<<");
                backward();
                Thread.sleep(5000);
                if (counter > 3) {
                    break;
                }
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        Controller controller = new Controller();
        controller.forward();
    }
}