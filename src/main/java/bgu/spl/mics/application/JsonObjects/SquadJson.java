package bgu.spl.mics.application.JsonObjects;

import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;

public class SquadJson {
    private Agent[] squad;

    public Agent[] getSquad() {
        return squad;
    }

    public void setSquad(Agent[] squad) {
        this.squad = squad;
    }
}
