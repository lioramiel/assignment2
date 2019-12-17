package bgu.spl.mics.application.JsonObjects;

import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class IntelligenceJson {
    List<MissionInfo> missions;

    public List<MissionInfo> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionInfo> missions) {
        this.missions = missions;
    }

    @Override
    public String toString() {
        return "intelligence{" +
                "missions=" + missions +
                '}';
    }
}
