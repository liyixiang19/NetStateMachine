package com.yixiang.tsn;

import com.yixiang.tsn.device.DeviceType;
import com.yixiang.tsn.status.Status;
import com.yixiang.tsn.status.handler.StatusHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatusHandlerRegistry {

    private static Map<String, StatusHandler> statusHandlerMap;

    static {
        statusHandlerMap = new ConcurrentHashMap<String, StatusHandler>();
    }

    private StatusHandlerRegistry(){

    }

    private static String getKey(DeviceType deviceType, Status status) {
        return String.format("%s@-@%s", deviceType.getType(), status.name());
    }

    /**
     * 注册状态处理类
     * @param deviceType       设备类型
     * @param status           状态
     * @param statusHandler    状态处理对象
     */
    public static void registryStatusHandler(DeviceType deviceType, Status status, StatusHandler statusHandler){
        statusHandlerMap.put(getKey(deviceType,status),statusHandler);
    }

    /**
     * 获取状态处理类
     * @param deviceType  设备类型
     * @param status            状态
     * @return StatusHandler
     */
    public static StatusHandler acquireStatusHandler(DeviceType deviceType,Status status){
        return statusHandlerMap.get(getKey(deviceType,status));
    }
}
