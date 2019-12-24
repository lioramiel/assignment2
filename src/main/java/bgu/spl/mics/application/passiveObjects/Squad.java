package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private static class SingletonHolder {
        private static Squad instance = new Squad();
    }

    private Map<String, Agent> agents;
    private Object lock = new Object();

    private Squad() {
        agents = new HashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        this.agents.clear();
        for (Agent a : agents)
            this.agents.put(a.getSerialNumber(), a);
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
//        System.out.println("releaseAgents" + " - " + Thread.currentThread().getId());
        for (String sn : serials) {
            if (agents.containsKey(sn)) {
                agents.get(sn).release();
                synchronized (this) {
                    this.notifyAll();
                }
            }
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {
        try {
            Thread.currentThread().sleep(time * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        releaseAgents(serials);
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public synchronized boolean getAgents(List<String> serials) {
        for (String sn : serials) {
            if (agents.keySet().contains(sn)) {
                while (agents.keySet().contains(sn) && !agents.get(sn).isAvailable()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (agents.keySet().contains(sn))
                    agents.get(sn).acquire();
            } else {
                releaseAgents(serials);
                return false;
            }
        }
        return true;
    }

    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        List<String> names = new ArrayList<>();
        for (String sn : serials)
            if(agents.keySet().contains(sn))
                names.add(agents.get(sn).getName());
        return names;
    }

}
