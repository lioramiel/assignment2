package bgu.spl.mics.application.JsonObjects;

import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;

public class input {
    private List<String> inventory;
    private services services;
    private List<Agent> squad;

    @Override
    public String toString() {
        return "input{" +
                "inventory=" + inventory +
                ", services=" + services +
                ", squad=" + squad +
                '}';
    }
}
