package com.yixiang.tsn.status.machine;

import com.yixiang.tsn.status.Status;
import com.yixiang.tsn.status.event.Event;

public interface StatusMachine {

    public Status getNextStatus(Status status, Event event);

}

