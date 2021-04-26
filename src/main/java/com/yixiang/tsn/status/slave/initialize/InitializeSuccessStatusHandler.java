package com.yixiang.tsn.status.slave.initialize;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class InitializeSuccessStatusHandler extends AbstractStatusHandler {

    protected void doHandler(Device device) {
        device.setEvent(null);
        System.out.println(String.format("--初始化成功-- device status: %s-%s", device.getStatus().getStatus(),
                device.getStatus().getMemo()));
    }
}
