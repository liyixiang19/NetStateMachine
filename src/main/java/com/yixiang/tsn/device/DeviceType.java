package com.yixiang.tsn.device;

public enum DeviceType {

    MASTER("master", "主站"),
    SLAVE("slave", "从站")
    ;

    private String type;
    private String memo;

    private DeviceType(String type, String memo){
        this.type=type;
        this.memo=memo;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
