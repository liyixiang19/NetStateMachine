package com.yixiang.tsn.status.slave.initialize;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class InitializeRetryStatusHandler extends AbstractStatusHandler {
    protected void doHandler(Device device){
        System.out.println(String.format("--设备初始化重试-- device status: %s-%s",
                device.getStatus().getStatus(), device.getStatus().getMemo()));
    }
}
