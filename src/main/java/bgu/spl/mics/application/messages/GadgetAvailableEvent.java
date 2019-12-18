package bgu.spl.mics.application.messages;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event<Boolean> {
    private String gadget;
    private String subscriberSerialNumber;

    public GadgetAvailableEvent(String gadget) {
        this.gadget = gadget;
    }

    public String getGadget() {
        return gadget;
    }

    public String getSubscriberSerialNumber() {
        return subscriberSerialNumber;
    }

    public void setSubscriberSerialNumber(String subscriberSerialNumber) {
        this.subscriberSerialNumber = subscriberSerialNumber;
    }
}
