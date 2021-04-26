package com.yixiang.tsn;

import com.yixiang.tsn.device.DeviceType;
import com.yixiang.tsn.status.Status;
import com.yixiang.tsn.status.event.Event;

public class Device {
    private Status status;
    private DeviceType deviceType;
    private Event event;
    private String user;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
