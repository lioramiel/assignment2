package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private Squad squad;
    private Integer serialNumber;

    public Moneypenny() {
        super("Moneypenny");
        this.squad = Squad.getInstance();
        this.serialNumber = 0;
    }

    public Moneypenny(String name, Integer serialNumber) {
        super(name);
        this.squad = Squad.getInstance();
        this.serialNumber = serialNumber;
    }

    @Override
    protected void initialize() {
        subscribeEvent(AgentsAvailableEvent.class, (c) -> {
            System.out.println("moneytpenny: " + serialNumber);
            Boolean agentsAvailable = squad.getAgents(c.getSerials());
            if(agentsAvailable) {
				c.setMoneypennySerialNumber(serialNumber);
				c.setAgentsName(squad.getAgentsNames(c.getSerials()));
				Future<Boolean> gadgetsAvailableFuture = new Future<>();
				complete(c, gadgetsAvailableFuture);
				Boolean gadgetAvailable = gadgetsAvailableFuture.get();
				if (gadgetAvailable)
					squad.releaseAgents(c.getSerials());
				else
					squad.sendAgents(c.getSerials(), c.getMissionDuration());
			} else {
				complete(c, null);
			}
        });
    }
}
