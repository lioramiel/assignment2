package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {

    private String missionName;
    private int m;
    private int moneypenny;
    private List<String> agentsSerialNumbers;
    private List<String> agentsNames;
    private String gadgetName;
    private int timeIssued;
    private int QTime;
    private int timeCreated;

//    public Report() {
//        agentsSerialNumbers = new ArrayList<>();
//        agentsNames = new ArrayList<>();
//    }


    public Report(String missionName, int m, int moneypenny, List<String> agentsSerialNumbers, List<String> agentsNames, String gadgetName, int timeIssued, int QTime, int timeCreated) {
        this.missionName = missionName;
        this.m = m;
        this.moneypenny = moneypenny;
        this.agentsSerialNumbers = agentsSerialNumbers;
        this.agentsNames = agentsNames;
        this.gadgetName = gadgetName;
        this.timeIssued = timeIssued;
        this.QTime = QTime;
        this.timeCreated = timeCreated;
    }

    /**
     * Retrieves the mission name.
     */
    public String getMissionName() {
        return missionName;
    }

    /**
     * Sets the mission name.
     */
    public void setMissionName(String missionName) {
        missionName = missionName;
    }

    /**
     * Retrieves the M's id.
     */
    public int getM() {
        return m;
    }

    /**
     * Sets the M's id.
     */
    public void setM(int m) {
        m = m;
    }

    /**
     * Retrieves the Moneypenny's id.
     */
    public int getMoneypenny() {
        return moneypenny;
    }

    /**
     * Sets the Moneypenny's id.
     */
    public void setMoneypenny(int moneypenny) {
        moneypenny = moneypenny;
    }

    /**
     * Retrieves the serial numbers of the agents.
     * <p>
     *
     * @return The serial numbers of the agents.
     */
    public List<String> getAgentsSerialNumbersNumber() {
        return agentsSerialNumbers;
    }

    /**
     * Sets the serial numbers of the agents.
     */
    public void setAgentsSerialNumbersNumber(List<String> agentsSerialNumbersNumber) {
        agentsSerialNumbers.addAll(agentsSerialNumbersNumber);
    }

    /**
     * Retrieves the agents names.
     * <p>
     *
     * @return The agents names.
     */
    public List<String> getAgentsNames() {
        return agentsNames;
    }

    /**
     * Sets the agents names.
     */
    public void setAgentsNames(List<String> agentsNames) {
        this.agentsNames.addAll(agentsNames);
    }

    /**
     * Retrieves the name of the gadget.
     * <p>
     *
     * @return the name of the gadget.
     */
    public String getGadgetName() {
        return gadgetName;
    }

    /**
     * Sets the name of the gadget.
     */
    public void setGadgetName(String gadgetName) {
        this.gadgetName = gadgetName;
    }

    /**
     * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public int getQTime() {
        return QTime;
    }

    /**
     * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public void setQTime(int qTime) {
        QTime = qTime;
    }

    /**
     * Retrieves the time when the mission was sent by an Intelligence Publisher.
     */
    public int getTimeIssued() {
        return timeIssued;
    }

    /**
     * Sets the time when the mission was sent by an Intelligence Publisher.
     */
    public void setTimeIssued(int timeIssued) {
        this.timeIssued = timeIssued;
    }

    /**
     * Retrieves the time-tick when the report has been created.
     */
    public int getTimeCreated() {
        return timeCreated;
    }

    /**
     * Sets the time-tick when the report has been created.
     */
    public void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }
}
