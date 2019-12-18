package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event<Integer> {
    private List<String> serials;

    public ReleaseAgentsEvent(List<String> serials) {
        this.serials = serials;
    }

    public List<String> getSerials() {
        return serials;
    }
}
