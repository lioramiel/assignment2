package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event<List<String>> {
    private List<String> serials;
    private int duration;

    public SendAgentsEvent(List<String> serials, int time) {
        this.serials = serials;
        this.duration = time;
    }

    public List<String> getSerials() {
        return serials;
    }

    public int getDuration() {
        return duration;
    }
}
