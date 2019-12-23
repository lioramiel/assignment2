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
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
//        System.out.println("releaseAgents" + " - " + Thread.currentThread().getId());
        for (String sn : serials) {
            if(agents.containsKey(sn)) {
                agents.get(sn).release();
                synchronized (agents.get(sn)) {
                    agents.get(sn).notifyAll();
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
//        System.out.println("sendAgents" + "  - " + Thread.currentThread().getId());
//        System.out.println("sendAgents" + "  - " + System.currentTimeMillis());
        try {
            Thread.currentThread().sleep(time * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        for (String sn : serials) {
//            if(agents.containsKey(sn)) {
////                agents.get(sn).release();
//                synchronized (agents.get(sn)) {
//                    try {
//                        agents.get(sn).wait(time * 100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        System.out.println("sendAgents2" + " - " + System.currentTimeMillis());
//        System.out.println("sendAgents2" + " - " + Thread.currentThread().getId());
        releaseAgents(serials);
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public synchronized boolean getAgents(List<String> serials) {
//        System.out.println("getAgents" + " - " + Thread.currentThread().getId());
        for (String sn : serials) {
            while (!agents.get(sn).isAvailable()) {
                try {
                    synchronized (agents.get(sn)) {
                        agents.get(sn).wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(agents.containsKey(sn))
                agents.get(sn).acquire();
            else {
                releaseAgents(serials);
                return false;
            }
        }
        return true;

//        Agent agent;
//        for (String sn : serials)
//            if (!agents.containsKey(sn))
//                return false;
//        for (String sn : serials) {
//            agent = agents.get(sn);
//            while (!agent.isAvailable()) {
//                try {
//                    synchronized (agent) {
//                        agent.wait();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            agent.acquire();
//        }
//        return true;
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
