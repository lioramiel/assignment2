package bgu.spl.mics.application.JsonObjects;

import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;

public class InputJson {
    private String[] inventory;
    private ServicesJson services;
    private Agent[] squad;

    public String[] getInventory() {
        return inventory;
    }

    public void setInventory(String[] inventory) {
        this.inventory = inventory;
    }

    public ServicesJson getServices() {
        return services;
    }

    public void setServices(ServicesJson services) {
        this.services = services;
    }

    public Agent[] getSquad() {
        return squad;
    }

    public void setSquad(Agent[] squad) {
        this.squad = squad;
    }

    @Override
    public String toString() {
        return "input{" +
                "inventory=" + inventory +
                ", services=" + services +
                ", squad=" + squad +
                '}';
    }
}
