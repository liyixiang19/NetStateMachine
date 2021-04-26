package com.yixiang.tsn.status.machine.factory;

import com.yixiang.tsn.device.DeviceType;
import com.yixiang.tsn.status.machine.StatusMachine;

public class StatusMachineFactory {
    
    private StatusMachineFactory(){

    }

    /**
     * 根据状态获取状态机
     * @param DeviceType
     * @return 对应请假类型的状态机
     */
    public static StatusMachine getStatusMachine(DeviceType DeviceType){
        switch (DeviceType){
            case MASTER:
                return new MasterStatusMachine();
            case SLAVE:
                return new SlaveStatusMachine();
            default:
                throw new RuntimeException("未知类型");
        }
    }
    
}
