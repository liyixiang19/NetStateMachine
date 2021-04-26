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


    private static byte[] queryVelocity() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.QUERY_VELOCITY);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
        byte[] revData = SerialTool.readFromPort(serialPort);
        return revData;
    }

    private static void queryPlus() throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        byte[] bytes = hexStrToBinaryStr(NetworkInfo.QUERY_PLUS);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    private static void setVelocity(int velocity) throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        String hexPlus = String.format("%4s", Integer.toHexString(velocity)).replace(" ", "0");
        String str = NetworkInfo.SET_VELOCITY + hexPlus;
        //CRC checksum
        byte[] sbuf2 = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(sbuf2);

        byte[] bytes = hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    private static void setPlus(int plus) throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        String hexPlus = String.format("%4s", Integer.toHexString(plus)).replace(" ", "0");
        String str = NetworkInfo.SET_PLUS + hexPlus;
        //CRC checksum
        byte[] sbuf2 = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(sbuf2);

        byte[] bytes = hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    private static void setNumber(int number) throws Exception {
        ArrayList<String> list = SerialTool.findPort();
        final SerialPort serialPort = SerialTool.openPort(list.get(0), 9600);
        String hexPlus = String.format("%4s", Integer.toHexString(number)).replace(" ", "0");
        String str = NetworkInfo.SET_NUMBER + hexPlus;
        //CRC checksum
        byte[] sbuf2 = CRC_16.getSendBuf(str);
        String data = CRC16M.getBufHexStr(sbuf2);
        byte[] bytes = hexStrToBinaryStr(data);
        SerialTool.sendToPort(serialPort, bytes);
        SerialTool.closePort(serialPort);
    }

    //
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
     * 字符串转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
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

    }

    public static void runMode2() {

    }

    public static void runMode3() {

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
                    StringBuilder stringBuilder = new StringBuilder("");
                    for (int i = 0; i < bytes.length; i++) {
                        int v = bytes[i] & 0xFF;
                        String hv = Integer.toHexString(v);
                        if (hv.length() < 2) {
                            stringBuilder.append(0);
                        }
                        stringBuilder.append(hv);
                    }
                    String res = stringBuilder.toString().toUpperCase(Locale.ROOT).substring(6, 10);

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
        serialPort.notifyOnDataAvailable(true);//串口有数据监听
        serialPort.notifyOnBreakInterrupt(true);//中断事件监听
    }

}
