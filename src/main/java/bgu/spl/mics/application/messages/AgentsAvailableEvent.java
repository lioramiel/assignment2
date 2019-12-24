package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

import java.util.List;

public class AgentsAvailableEvent implements Event<Future> {
    private List<String> serials;
    private int moneypennySerialNumber;
    private int missionDuration;
    private List<String> agentsName;

    public AgentsAvailableEvent(List<String> serials, int missionDuration) {
        this.serials = serials;
        this.missionDuration = missionDuration;
    }

    public List<String> getSerials() {
        return serials;
    }

    public int getMoneypennySerialNumber() {
        return moneypennySerialNumber;
    }

    public void setMoneypennySerialNumber(int moneypennySerialNumber) {
        this.moneypennySerialNumber = moneypennySerialNumber;
    }

    public int getMissionDuration() {
        return missionDuration;
    }

    public List<String> getAgentsName() {
        return agentsName;
    }

    public void setAgentsName(List<String> agentsName) {
        this.agentsName = agentsName;
    }
}
