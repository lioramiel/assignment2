package bgu.spl.mics.application.messages;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Agent;

public class AgentsAvailableEvent implements Event<Agent>, Callback<AgentsAvailableEvent> {
    public AgentsAvailableEvent() {}

    @Override
    public void call(AgentsAvailableEvent c) {

    }
}
