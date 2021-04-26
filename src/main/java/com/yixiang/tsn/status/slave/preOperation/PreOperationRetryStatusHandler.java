package com.yixiang.tsn.status.slave.preOperation;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class PreOperationRetryStatusHandler extends AbstractStatusHandler {

    @Override
    protected void after(Device device) {
        if (device.getEvent() == null) {
            //没有事件改变节点状态，等待中
            System.out.println(String.format("--等待下一状态--device status: %s", device.getStatus().getStatus()));
            return;
        }
        goNextStatusHandler(device);
    }

    protected void doHandler(Device device){
        System.out.println(String.format("user:%s--等待初始化中--device status:%s-%s",device.getUser(),
                device.getStatus().getStatus(), device.getStatus().getMemo()));
    }
}
