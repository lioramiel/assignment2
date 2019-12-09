package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    private Squad squad;
    @BeforeEach
    public void setUp(){
        squad = Squad.getInstance();
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#getInstance()}.
     */
    @Test public void testGetInstance() {
        Squad newSquad = Squad.getInstance();
        Agent agent001 = new Agent();
        agent001.setName("agent001");
        agent001.setSerialNumber("001");
        Agent[] agentsArray = {agent001};
        squad.load(agentsArray);
        List<String> serialsList= new ArrayList<String>();
        serialsList.add(agent001.getSerialNumber());
        assertTrue(newSquad.getAgents(serialsList));
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#load(Agent[] inventory)}.
     */
    @Test public void testLoad() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
        agent001.setName("agent001");
        agent001.setSerialNumber("001");
        agent002.setName("agent002");
        agent002.setSerialNumber("002");
        agent003.setName("agent003");
        agent003.setSerialNumber("003");
        Agent[] agentsArray = {agent001, agent002, agent003};
        squad.load(agentsArray);
        List<String> serialsList= new ArrayList<String>();
        serialsList.add(agent001.getSerialNumber());
        serialsList.add(agent002.getSerialNumber());
        serialsList.add(agent003.getSerialNumber());
        assertTrue(squad.getAgents(serialsList));
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#releaseAgents(List)}.
     */
    @Test public void testReleaseAgents() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
        agent001.setName("agent001");
        agent001.setSerialNumber("001");
        agent002.setName("agent002");
        agent002.setSerialNumber("002");
        agent003.setName("agent003");
        agent003.setSerialNumber("003");
        Agent[] agentsArray = {agent001, agent002, agent003};
        squad.load(agentsArray);
        List<String> serialsList= new ArrayList<String>();
        serialsList.add(agent001.getSerialNumber());
        serialsList.add(agent003.getSerialNumber());
        squad.releaseAgents(serialsList);
        assertTrue(agent001.isAvailable());
        assertFalse(agent002.isAvailable());
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#sendAgents(List, int)}.
     */
    @Test public void testSendAgents() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
        agent001.setName("agent001");
        agent001.setSerialNumber("001");
        agent002.setName("agent002");
        agent002.setSerialNumber("002");
        agent003.setName("agent003");
        agent003.setSerialNumber("003");
        Agent[] agentsArray = {agent001, agent002, agent003};
        squad.load(agentsArray);
        List<String> serialsList= new ArrayList<String>();
        serialsList.add(agent001.getSerialNumber());
        serialsList.add(agent003.getSerialNumber());
        squad.sendAgents(serialsList,2000);
        assertTrue(agent002.isAvailable());
        assertFalse(agent001.isAvailable());
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#getAgents(List)}.
     */
    @Test public void testGetAgents() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
        Agent agent004 = new Agent();
        agent001.setName("agent001");
        agent001.setSerialNumber("001");
        agent002.setName("agent002");
        agent002.setSerialNumber("002");
        agent003.setName("agent003");
        agent003.setSerialNumber("003");
        agent004.setName("agent004");
        agent004.setSerialNumber("004");
        Agent[] agentsArray = {agent001, agent002, agent003};
        squad.load(agentsArray);
        List<String> serialsList= new ArrayList<String>();
        serialsList.add(agent001.getSerialNumber());
        serialsList.add(agent002.getSerialNumber());
        serialsList.add(agent003.getSerialNumber());
        assertTrue(squad.getAgents(serialsList));
        serialsList.add(agent004.getSerialNumber());
        assertFalse(squad.getAgents(serialsList));
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#getAgentsNames(List)}.
     */
    @Test public void testGetAgentsNames() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
        agent001.setName("agent001");
        agent001.setSerialNumber("001");
        agent002.setName("agent002");
        agent002.setSerialNumber("002");
        agent003.setName("agent003");
        agent003.setSerialNumber("003");
//        Agent[] agentArray = {agent001, agent002, agent003};
//        squad.load(agentArray);
        List<String> serialsList= new ArrayList<>();
        serialsList.add(agent001.getSerialNumber());
        serialsList.add(agent002.getSerialNumber());
        serialsList.add(agent003.getSerialNumber());
        List<String> namesArray = new ArrayList<>();
        namesArray.add(agent001.getName());
        namesArray.add(agent002.getName());
        namesArray.add(agent003.getName());
        assertEquals(namesArray,squad.getAgentsNames(serialsList));
    }
}
