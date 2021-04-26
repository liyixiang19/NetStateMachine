package com.yixiang.tsn.status.machine.factory;

import com.yixiang.tsn.status.Status;
import com.yixiang.tsn.status.event.Event;
import com.yixiang.tsn.status.machine.StatusMachine;

public class MasterStatusMachine implements StatusMachine {

    @Override
    public Status getNextStatus(Status status, Event event) {
        return null;
    }
}
