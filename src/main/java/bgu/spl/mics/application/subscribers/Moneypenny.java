package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
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
    private int time;
    private int duration;

    public Moneypenny() {
        super("Moneypenny");
        this.squad = Squad.getInstance();
        this.serialNumber = 0;
    }

    public Moneypenny(String name, Integer serialNumber, int duration) {
        super(name);
        this.squad = Squad.getInstance();
        this.serialNumber = serialNumber;
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(AgentsAvailableEvent.class, (c) -> {
//            System.out.println("moneytpenny start AgentsAvailableEvent");
            Boolean agentsAvailable = squad.getAgents(c.getSerials());
            Future<Boolean> gadgetsAvailableFuture = new Future<>();
            if(agentsAvailable) {
				c.setMoneypennySerialNumber(serialNumber);
				c.setAgentsName(squad.getAgentsNames(c.getSerials()));
				complete(c, gadgetsAvailableFuture);
				Boolean gadgetAvailable = gadgetsAvailableFuture.get();
				if (gadgetAvailable)
                    squad.sendAgents(c.getSerials(), c.getMissionDuration());
                else
                    squad.releaseAgents(c.getSerials());
			} else {
                c.setMoneypennySerialNumber(-1);
                complete(c, gadgetsAvailableFuture);
			}
//            System.out.println("moneytpenny finished AgentsAvailableEvent");
        });

//        subscribeBroadcast(TickBroadcast.class, (c) -> {
//            this.time = c.getTime();
//            if(time == this.duration)
//                this.terminate();
//        });
    }
}
