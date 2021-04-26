package com.yixiang.tsn.status.handler;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.device.DeviceType;
import com.yixiang.tsn.engine.StatusMachineEngine;
import com.yixiang.tsn.status.machine.factory.StatusMachineFactory;

public abstract class AbstractStatusHandler implements StatusHandler{

    protected void before(Device device) {

    }

    public void handle(Device device) {
        before(device);
        doHandler(device);
        after(device);
    }

    protected abstract void doHandler(Device device);

    protected void after(Device device) {
        goNextStatusHandler(device);
    }



    protected void goNextStatusHandler(Device device){
        //获取下一个状态
        device.setStatus(StatusMachineFactory.getStatusMachine(device.getDeviceType()).getNextStatus(device.getStatus(),device.getEvent()));
        //状态机引擎去处理该device
        StatusMachineEngine.post(device);
    }
}
