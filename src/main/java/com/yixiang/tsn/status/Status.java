package com.yixiang.tsn.status;

public enum Status {
    //上电启动
    POWER_ON("powerOn", "上电启动"),

    //初始化
    INITIALIZE("initialize", "初始化中"),
    INITIALIZE_SUCCESS("initializeSuccess", "初始化成功"),
    INITIALIZE_FAILED("initializeFailed", "初始化失败"),
    INITIALIZE_RETRY("initializeRetry", "初始化重试"),

    //预操作
    PRE_OPERATION("preOperation", "预操作状态"),
    PRE_OPERATION_SUCCESS("preOperationSuccess", "预操作状态成功"),
    PRE_OPERATION_FAILED("preOperationFailed", "预操作状态失败"),
    PRE_OPERATION_RETRY("preOperationRetry", "预操作状态重试"),

    //运行态
    OPERATION("operation", "运行状态"),

    //组网完成
    ORGANIZATION_SUCCESS("organization_success", "组网成功"),
    ORGANIZATION_FAILED("organization_failed", "组网失败"),

    //启动完成
    LAUNCH_SUCCESS("launch_success", "启动成功"),
    LAUNCH_FAILED("launch_failed", "启动失败");

    private String status;
    private String memo;

    private Status(String status, String memo) {
        this.status = status;
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
