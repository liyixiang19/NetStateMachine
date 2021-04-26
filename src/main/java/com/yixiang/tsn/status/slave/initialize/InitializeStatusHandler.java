package com.yixiang.tsn.status.slave.initialize;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.device.DeviceType;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class InitializeStatusHandler extends AbstractStatusHandler {

    protected void doHandler(Device device){
        System.out.println(String.format("--等待初始化结果--device status:%s-%s", device.getStatus().getStatus(),
                device.getStatus().getMemo()));
    }

    @Override
    protected void after(Device device) {
        if (device.getEvent() == null) {
            System.out.println(String.format("--等待初始化成功--, device status: %s-%s", device.getStatus().getStatus(),
                    device.getStatus().getMemo()));
            return;
        }
        goNextStatusHandler(device);
    }


}
