package com.yixiang.tsn.status.event;

public enum Event {

    SUCCESS("success","成功"),
    FAILED("failed","失败"),
    RETRY("retry","重试");

    private String type;
    private String memo;

    private Event(String type, String memo){
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
