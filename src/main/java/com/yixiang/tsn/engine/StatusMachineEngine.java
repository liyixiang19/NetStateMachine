package com.yixiang.tsn.engine;

import com.google.common.eventbus.EventBus;
import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.impl.DeviceHandler;

public class StatusMachineEngine {

    private static EventBus eventBus;
    static {
        eventBus = new EventBus();
    }

    /**
     * 更新设备状态
     * @param device
     */
    public static void post(Device device) {
        eventBus.post(device);
    }

    public static void addListener(DeviceHandler statusDeviceHandler) {
        eventBus.register(statusDeviceHandler);
    }
}
