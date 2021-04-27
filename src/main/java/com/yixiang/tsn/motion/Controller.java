package com.yixiang.tsn.motion;


import com.yixiang.tsn.common.NetworkInfo;
import com.yixiang.tsn.util.CRC16M;
import com.yixiang.tsn.util.CRC_16;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Controller {
    /**
     * Description: 前进功能
     * @throws Exception
     */
    private static void forward() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.FORWARD);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    /**
     * Description: 后退功能
     * @throws Exception
     */
    private static void backward() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.BACKWARD);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    /**
     * Description: 停止功能
     * @throws Exception
     */
    private static void stop() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.STOP);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    /**
     * Description: 查询电机速度
     * @return 16进制数据的字节流
     * @throws Exception
     */
    private static byte[] queryVelocity() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.QUERY_VELOCITY);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
        return SerialTool.readFromPort(serialPort);
    }

    /**
     * Description: 查询电机脉冲数
     * @throws Exception
     */
    private static void queryPlus() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.QUERY_PLUS);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }
    /**
     * Description: 查询电机计数
     * @throws Exception
     */
    private static void queryNum() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.QUERY_NUM);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    /**
     * Description: 设置电机速度
     * @throws Exception
     */
    private static void setVelocity(int velocity) throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        String hexData = String.format("%4s", Integer.toHexString(velocity)).replace(" ", "0");
        String str = NetworkInfo.SET_VELOCITY + hexData;
        //CRC checksum
        byte[] crcBuf = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(crcBuf);
        byte[] bytes = hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    /**
     * Description: 设置电机脉冲数
     * @throws Exception
     */
    private static void setPlus(int plus) throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        String hexPlus = String.format("%4s", Integer.toHexString(plus)).replace(" ", "0");
        String str = NetworkInfo.SET_PLUS + hexPlus;
        //CRC checksum
        byte[] crcBuf = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(crcBuf);

        byte[] bytes = hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    /**
     * Description: 设置电机计数
     * @throws Exception
     */
    private static void setNumber(int number) throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        String hexPlus = String.format("%4s", Integer.toHexString(number)).replace(" ", "0");
        String str = NetworkInfo.SET_NUMBER + hexPlus;
        //CRC checksum
        byte[] crcBuf = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(crcBuf);
        byte[] bytes = hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    /**
     * Description: 十六进制字符串转二进制字符串
     * @return
     */
    public static byte[] hexStrToBinaryStr(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }


    public static void runMode1() {
        System.out.println("run mode 1");
        try {
            forward();
            Thread.sleep(5000);
            stop();
            backward();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void runMode2() {
        System.out.println("run mode 2");
        try {
            backward();
            Thread.sleep(5000);
            stop();
            Thread.sleep(2000);
            forward();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runMode3() {
        System.out.println("run mode 3");
        int counter = 0;
        try {
            while (true) {
                forward();
                Thread.sleep(5000);
                stop();
                Thread.sleep(2000);
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


    /**
     * Description: 将串口接收到的二进制数据转化成16进制的字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexStr(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase(Locale.ROOT).substring(6, 10);
    }


    public static void main(String[] args) throws Exception {

        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.QUERY_PLUS);
        SerialTool.sendToPort(serialPort, bytes);

        //设置串口的listener
        Controller.setListenerToSerialPort(serialPort, new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) { //数据通知
                    byte[] bytes = new byte[0];
                    try {
                        bytes = SerialTool.readFromPort(serialPort);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(bytesToHexStr(bytes));
                }
            }
        });

        //closeSerialPort(serialPort);


    }

    public static void setListenerToSerialPort(SerialPort serialPort, SerialPortEventListener listener) {
        try {
            //给串口添加事件监听
            serialPort.addEventListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //串口有数据监听
        serialPort.notifyOnDataAvailable(true);
        //中断事件监听
        serialPort.notifyOnBreakInterrupt(true);
    }

}
