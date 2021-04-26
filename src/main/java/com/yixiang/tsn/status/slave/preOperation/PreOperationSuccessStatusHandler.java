package com.yixiang.tsn.status.slave.preOperation;

import com.yixiang.tsn.Device;
import com.yixiang.tsn.status.handler.AbstractStatusHandler;

public class PreOperationSuccessStatusHandler extends AbstractStatusHandler {

    protected void doHandler(Device device) {
        System.out.println(String.format("--预操作成功--, device status: %s-%s", device.getStatus().getStatus(),
                device.getStatus().getMemo()));
    }
}
