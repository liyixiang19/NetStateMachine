package com.yixiang.tsn.status.slave;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class OperationStatusHandler extends AbstractStatusHandler {

    protected void doHandler(Device device){
        System.out.println(String.format("--进入OP状态--  device status:%s-%s",
                device.getStatus().getStatus(), device.getStatus().getMemo()));
    }

    @Override
    protected void after(Device device){
        System.out.println(String.format("--进入OP状态,等待运动控制--  device status:%s-%s",
                device.getStatus().getStatus(), device.getStatus().getMemo()));
    }
}
