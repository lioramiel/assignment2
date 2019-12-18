package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event<String> {
    private MissionInfo mission;
    private String subscriberSerialNumber;

    public MissionReceivedEvent(MissionInfo mission) {
        this.mission = mission;
    }

    public MissionInfo getMission() {
        return mission;
    }

    public String getSubscriberSerialNumber() {
        return subscriberSerialNumber;
    }

    public void setSubscriberSerialNumber(String subscriberSerialNumber) {
        this.subscriberSerialNumber = subscriberSerialNumber;
    }
}
