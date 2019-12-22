package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class AgentsAvailableEvent implements Event<Integer> {
    private List<String> serials;

    public AgentsAvailableEvent(List<String> serials) {
        this.serials = serials;
    }

    public List<String> getSerials() {
        return serials;
    }
}
