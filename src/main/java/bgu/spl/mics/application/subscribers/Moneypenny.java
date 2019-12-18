package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
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
	private String serialNumber;

	public Moneypenny() {
		super("Moneypenny");
		this.squad = Squad.getInstance();
		this.serialNumber = "024";
	}

	public Moneypenny(String name, String serialNumber) {
		super(name);
		this.squad = Squad.getInstance();
		this.serialNumber = serialNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	@Override
	protected void initialize() {
		subscribeEvent(AgentsAvailableEvent.class, (c) -> {
			c.setSubscriberSerialNumber(serialNumber);
			Boolean result = squad.getAgents(c.getSerials());
			complete(c, result);
		});

		subscribeEvent(SendAgentsEvent.class, (c) -> {
			c.setSubscriberSerialNumber(serialNumber);
			squad.sendAgents(c.getSerials(), c.getDuration());
			complete(c, squad.getAgentsNames(c.getSerials()));
		});

		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
		});
	}
}
