package com.yixiang.tsn;


import com.yixiang.tsn.common.NetworkInfo;
import com.yixiang.tsn.device.DeviceType;
import com.yixiang.tsn.engine.StatusMachineEngine;
import com.yixiang.tsn.init.InitEvent;
import com.yixiang.tsn.connection.ListenerThread;
import com.yixiang.tsn.networking.Server;
import com.yixiang.tsn.status.Status;
import com.yixiang.tsn.status.event.Event;
import com.yixiang.tsn.status.handler.impl.DeviceHandler;
import com.yixiang.tsn.status.slave.OperationStatusHandler;
import com.yixiang.tsn.status.slave.SlavePowerOnStatusHandler;
import com.yixiang.tsn.status.slave.initialize.InitializeFailedStatusHandler;
import com.yixiang.tsn.status.slave.initialize.InitializeRetryStatusHandler;
import com.yixiang.tsn.status.slave.initialize.InitializeStatusHandler;
import com.yixiang.tsn.status.slave.initialize.InitializeSuccessStatusHandler;
import com.yixiang.tsn.status.slave.preOperation.PreOperatingStatusHandler;
import com.yixiang.tsn.status.slave.preOperation.PreOperationFailedStatusHandler;
import com.yixiang.tsn.status.slave.preOperation.PreOperationRetryStatusHandler;
import com.yixiang.tsn.status.slave.preOperation.PreOperationSuccessStatusHandler;
import com.yixiang.tsn.survive.HeartBeatThread;

/**
 *
 * @author liyixiang
 * @date 20/04/27
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("--->>>>>>>>>begin<<<<<<<<-------");

        //注册从站的状态和对应状态的处理类StatusHandler。
        registrySlaveStatusHandler();

        DeviceHandler deviceHandler = new DeviceHandler();
        //状态机引擎接受事件处理类
        StatusMachineEngine.addListener(deviceHandler);

        //设备出场自定义文件
        Device device = new Device();
        device.setDeviceType(DeviceType.SLAVE);

        //设备上电
        device.setStatus(Status.POWER_ON);
        System.out.println("----- 设备上电，开始进行初始化操作，等待init结果 -----");
        StatusMachineEngine.post(device);

        //初始化事件处理,如果返回值200则初始化成功，进入下一状态。
        if (200 == InitEvent.initBegin()) {
            System.out.println(">>>>>>>>>>>>>>>>>>设备初始化成功，等待设备进行预操作处理<<<<<<<<<<<<<<<<<<<<<");
            device.setEvent(Event.SUCCESS);
        } else {
            System.out.println("----- 设备初始化失败，等待下一状态 -----");
            device.setEvent(Event.FAILED);
        }
        StatusMachineEngine.post(device);

        //从站开始组网过程，等待主站回复，组网成功
        //开启监听线程，监听来自主站的消息，并对消息进行解析判断
        Runnable listenerThread = new ListenerThread("listener线程");
        Thread myThread = new Thread(listenerThread);
        myThread.start();

        if (NetworkInfo.NET_SUCCESS == Server.slaveOrganization()) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>设备组网成功，预操作完成<<<<<<<<<<<<<<<<<<<<");
            device.setEvent(Event.SUCCESS);
        } else {
            System.out.println("----- 设备组网失败，等待下一状态 -----");
            device.setEvent(Event.FAILED);
        }
        StatusMachineEngine.post(device);

        //启动心跳线程,每30s发送一次生存消息给主站
        System.out.println(">>>>>>>>>>>>>>>>>>>>>心跳机制启动<<<<<<<<<<<<<<<<<<<<<<<<<<<\n device status：alive");
        Runnable heartbeatThread = new HeartBeatThread("心跳线程");
        Thread myThread2 = new Thread(heartbeatThread);
        myThread2.start();

        //等待进行运动控制，同时阻塞主线程
        try {
            System.out.println("------------------等待主站控制命令----------------");
            myThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--->>>>>>>>>>>>end<<<<<<<<<<<-------");
    }

    public static void registrySlaveStatusHandler() {

        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.POWER_ON, new SlavePowerOnStatusHandler());

        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.INITIALIZE_SUCCESS, new InitializeSuccessStatusHandler());
        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.INITIALIZE_FAILED, new InitializeFailedStatusHandler());
        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.INITIALIZE_RETRY, new InitializeRetryStatusHandler());
        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.INITIALIZE, new InitializeStatusHandler());

        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.PRE_OPERATION_SUCCESS, new PreOperationSuccessStatusHandler());
        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.PRE_OPERATION_FAILED, new PreOperationFailedStatusHandler());
        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.PRE_OPERATION_RETRY, new PreOperationRetryStatusHandler());
        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.PRE_OPERATION, new PreOperatingStatusHandler());

        StatusHandlerRegistry.registryStatusHandler(DeviceType.SLAVE, Status.OPERATION, new OperationStatusHandler());
    }
}
