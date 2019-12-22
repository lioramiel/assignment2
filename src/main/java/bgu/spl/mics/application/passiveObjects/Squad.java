package bgu.spl.mics.application.passiveObjects;

import java.util.*;

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
        for (Agent a : agents)
            this.agents.put(a.getSerialNumber(), a);
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
//        System.out.println("releaseAgents" + " - " + Thread.currentThread().getId());
        for (String sn : serials)
            agents.get(sn).release();
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {
        System.out.println("sendAgents" + " - " + Thread.currentThread().getId());
        try {
            Thread.currentThread().sleep(time * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendAgents" + " - " + Thread.currentThread().getId());
        releaseAgents(serials);
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public synchronized boolean getAgents(List<String> serials) {
        Agent temp;
        for (String sn : serials)
            if (!agents.containsKey(sn))
                return false;
        for (String sn : serials) {
            temp = agents.get(sn);
            while (!temp.isAvailable()) {
                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            temp.acquire();
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
            names.add(agents.get(sn).getName());
        return names;
    }

}
