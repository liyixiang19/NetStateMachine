package com.yixiang.tsn.status.slave;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;
import com.yixiang.tsn.status.handler.StatusHandler;

public class SlavePowerOnStatusHandler extends AbstractStatusHandler {

    protected void doHandler(Device device){
        System.out.println(String.format("--设备上电-- device status: %s-%s", device.getStatus().getStatus(),
                device.getStatus().getMemo()));
    }

}
