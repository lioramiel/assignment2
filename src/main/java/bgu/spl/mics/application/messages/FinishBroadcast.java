package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class FinishBroadcast implements Broadcast {
    int time;

    public FinishBroadcast(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
