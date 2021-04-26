package com.yixiang.tsn.status.machine.factory;

import com.yixiang.tsn.status.Status;
import com.yixiang.tsn.status.event.Event;
import com.yixiang.tsn.status.machine.StatusMachine;

public class SlaveStatusMachine implements StatusMachine {

    @Override
    public Status getNextStatus(Status status, Event event) {
        switch (status) {
            case POWER_ON:
                //上电后开始初始化
                return Status.INITIALIZE;

            case INITIALIZE:
                //根据初始化结果进入下一状态
                return getInitSuccessStatus(event);
            case INITIALIZE_SUCCESS:
                // 初始化成功，进入下一状态
                return Status.PRE_OPERATION;
            case INITIALIZE_FAILED:
                //初始化失败
                return Status.LAUNCH_FAILED;
            case INITIALIZE_RETRY:
                return getInitSuccessStatus(event);

            case PRE_OPERATION:
                //根据预操作结果进入下一状态
                return getResponseSucStatus(event);
            case PRE_OPERATION_SUCCESS:
                //预操作成功， 返回启动失败
                return Status.OPERATION;
            case PRE_OPERATION_FAILED:
                //预操作失败，返回启动失败
                return Status.LAUNCH_FAILED;
            case PRE_OPERATION_RETRY:
                return getResponseSucStatus(event);

            default:
                throw new RuntimeException("没有该流程");
        }
    }

    private Status getInitSuccessStatus(Event event){
        switch (event){
            case SUCCESS:
                return Status.INITIALIZE_SUCCESS;
            case FAILED:
                return Status.INITIALIZE_FAILED;
            case RETRY:
                return Status.INITIALIZE_RETRY;
            default:
                throw new RuntimeException("不支持该Event结果");
        }
    }

    private Status getResponseSucStatus(Event event){
        switch (event){
            case SUCCESS:
                return Status.PRE_OPERATION_SUCCESS;
            case FAILED:
                return Status.PRE_OPERATION_FAILED;
            case RETRY:
                return Status.PRE_OPERATION_RETRY;
            default:
                throw new RuntimeException("不支持该Event结果");
        }
    }
}
