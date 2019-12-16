package bgu.spl.mics.application.passiveObjects;

import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private static Squad squad;
    private Map<String, Agent> agents;

    private Squad() {
        agents = new HashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        if (squad == null)
            squad = new Squad();
        return squad;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        for (Agent a : agents) {
            this.agents.put(a.getSerialNumber(), a);
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
        for (String sn : serials) {
            agents.remove(agents.get(sn));
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {
        try {
            Thread.sleep(time);
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
    public boolean getAgents(List<String> serials) {
        Agent temp;
        for (String sn : serials) {
            if (!agents.containsKey(sn))
                return false;
            temp = agents.get(sn);
            if (temp.isAvailable())
                temp.acquire();
//            else
//                wait();
            //TODO: write waiting
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
