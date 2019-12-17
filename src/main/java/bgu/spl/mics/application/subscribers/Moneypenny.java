package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
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

	public Moneypenny(String name) {
		super(name);
		squad = Squad.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(AgentsAvailableEvent.class, (c) -> {
			Boolean result = squad.getAgents(c.getSerials());
			complete(c, result);
		});

		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
		});
	}
}
