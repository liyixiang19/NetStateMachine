package com.yixiang.tsn.status.slave.preOperation;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class PreOperatingStatusHandler extends AbstractStatusHandler {

    protected void doHandler(Device device) {
        System.out.println(String.format("--等待系统组网中--, device status: %s-%s", device.getStatus().getStatus(),
                device.getStatus().getMemo()));
    }

    @Override
    protected void after(Device device) {
        if (device.getEvent() == null) {
            System.out.println(String.format("--等待组网完成--， device status: %s-%s", device.getStatus().getStatus(),
                    device.getStatus().getMemo()));
            return;
        }
        goNextStatusHandler(device);
    }
}
