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
        squad = new Squad();
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#load(Agent[] inventory)}.
     */
    @Test public void testLoad() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
        Agent[] agentArray = {agent001, agent002, agent003};
        squad.load(agentArray);
        List<String> serialList= new ArrayList<String>();
        serialList.add(agent001.getSerialNumber());
        serialList.add(agent002.getSerialNumber());
        serialList.add(agent003.getSerialNumber());
        assertTrue(squad.getAgents(serialList));
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#releaseAgents(List)}.
     */
    @Test public void testReleaseAgents() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
        Agent[] agentArray = {agent001, agent002, agent003};
        squad.load(agentArray);
        List<String> serialList= new ArrayList<String>();
        serialList.add(agent001.getSerialNumber());
        serialList.add(agent003.getSerialNumber());
        squad.releaseAgents(serialList);
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
        Agent[] agentArray = {agent001, agent002, agent003};
        squad.load(agentArray);
        List<String> serialList= new ArrayList<String>();
        serialList.add(agent001.getSerialNumber());
        serialList.add(agent003.getSerialNumber());
        squad.sendAgents(serialList,2000);
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
        Agent[] agentArray = {agent001, agent002, agent003};
        squad.load(agentArray);
        List<String> serialList= new ArrayList<String>();
        serialList.add(agent001.getSerialNumber());
        serialList.add(agent002.getSerialNumber());
        serialList.add(agent003.getSerialNumber());
        assertTrue(squad.getAgents(serialList));
        serialList.add(agent004.getSerialNumber());
        assertFalse(squad.getAgents(serialList));
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Squad#getAgentsNames(List)}.
     */
    @Test public void testGetAgentsNames() {
        Agent agent001 = new Agent();
        Agent agent002 = new Agent();
        Agent agent003 = new Agent();
//        Agent[] agentArray = {agent001, agent002, agent003};
//        squad.load(agentArray);
        List<String> serialList= new ArrayList<>();
        serialList.add(agent001.getSerialNumber());
        serialList.add(agent002.getSerialNumber());
        serialList.add(agent003.getSerialNumber());
        List<String> names=new ArrayList<>();
        names.add(agent001.getName());
        names.add(agent002.getName());
        names.add(agent003.getName());
        assertEquals(names,squad.getAgentsNames(serialList));
    }
}
