package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event<List<String>> {
    List<String> serials;
    int time;

    public SendAgentsEvent(List<String> serials, int time) {
        this.serials = serials;
        this.time = time;
    }

    public List<String> getSerials() {
        return serials;
    }

    public int getTime() {
        return time;
    }
}
