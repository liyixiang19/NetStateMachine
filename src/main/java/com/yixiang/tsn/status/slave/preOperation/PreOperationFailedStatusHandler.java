package com.yixiang.tsn.status.slave.preOperation;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class PreOperationFailedStatusHandler extends AbstractStatusHandler {

    protected void doHandler(Device device){
        System.out.println(String.format("user:%s--等待初始化中--device status:%s-%s",device.getUser(),
                device.getStatus().getStatus(), device.getStatus().getMemo()));
    }
}
