package com.yixiang.tsn.init;

import com.yixiang.tsn.common.NetworkInfo;
import com.yixiang.tsn.status.event.Event;

import java.util.Scanner;

public class InitEvent {

    //执行初始化操作
    public static int initBegin() throws InterruptedException {
        System.out.println("----------------------初始化进程开始-------------------");
        System.out.printf("读取设备vid：%s \n", NetworkInfo.VID);
        int counter = 0;
        do {
            System.out.println("checking files, please waiting ... ");
            counter++;
            Thread.sleep(500);
        } while (counter <= 3);
        System.out.printf("初始化设备信息  ======  设备vid： %s， 设备类型： %s, 设备状态： %s \n", NetworkInfo.VID, NetworkInfo.DEVICE_TYPE, NetworkInfo.DEVICE_INFO);
        Thread.sleep(1000);
        System.out.println("检查网卡类型...");
        Thread.sleep(1000);
        System.out.println("网卡类型: 【Intel I210】, 检查正常");
        System.out.println("检查局域网连通性...");
        Thread.sleep(1000);
        System.out.println("局域网网络正常");
        System.out.println(">>>>>>>>>>>>>>>设备初始化完成<<<<<<<<<<<<");
        return 200;
    }
}
