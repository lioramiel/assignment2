package bgu.spl.mics.application.messages;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;

public class AgentsAvailableEvent implements Event<Boolean> {
    private List<String> serials;
    private String subscriberSerialNumber;

    public AgentsAvailableEvent(List<String> serials) {
        this.serials = serials;
    }

    public List<String> getSerials() {
        return serials;
    }

    public String getSubscriberSerialNumber() {
        return subscriberSerialNumber;
    }

    public void setSubscriberSerialNumber(String subscriberSerialNumber) {
        this.subscriberSerialNumber = subscriberSerialNumber;
    }
}
