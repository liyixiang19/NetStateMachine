package com.yixiang.tsn.status.handler.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.yixiang.tsn.Device;
import com.yixiang.tsn.StatusHandlerRegistry;
import com.yixiang.tsn.status.handler.StatusHandler;

public class DeviceHandler {

    @Subscribe
    @AllowConcurrentEvents
    public void handle(Device device) {
        //获取到状态处理类，然后去处理StatusHandler的入口
        getStatusHandler(device).handle(device);
    }

    /**
     * 根据假单获取StatusHandler 状态处理对象
     * @param device
     * @return
     */
    public static StatusHandler getStatusHandler(Device device){
        return StatusHandlerRegistry.acquireStatusHandler(device.getDeviceType(),device.getStatus());
    }


}
