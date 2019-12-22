package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.ReleaseAgentsEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private Squad squad;
	private int time;
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

	public Integer getSerialNumber() {
		return serialNumber;
	}

	@Override
	protected void initialize() {
		subscribeEvent(AgentsAvailableEvent.class, (c) -> {
			System.out.println("moneytpenny: " + serialNumber);
			Boolean agentsAvailable = squad.getAgents(c.getSerials());
			if(agentsAvailable)
				complete(c, getSerialNumber());
			else
				complete(c, -1);
		});

		subscribeEvent(SendAgentsEvent.class, (c) -> {
			squad.sendAgents(c.getSerials(), c.getDuration());
			complete(c, squad.getAgentsNames(c.getSerials()));
		});

		subscribeEvent(ReleaseAgentsEvent.class, (c) -> {
			squad.releaseAgents(c.getSerials());
			complete(c, 0);
		});
//
//		subscribeBroadcast(TickBroadcast.class, (c) -> {
//			this.time = c.getTime();
//		});
	}
}
