package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;

public class TickBroadcast implements Broadcast {
    int time;

    public TickBroadcast(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
