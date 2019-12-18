package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory inventory;
	private int time;
	private int serialNumber;

	public Q() {
		super("Q");
		this.inventory = Inventory.getInstance();
		this.serialNumber = 0;
	}

	public Q(String name, int serialNumber) {
		super(name);
		this.inventory = Inventory.getInstance();
		this.serialNumber = serialNumber;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	@Override
	protected void initialize() {
		subscribeEvent(GadgetAvailableEvent.class, (c) -> {
			Boolean result = inventory.getItem(c.getGadget());
			if(result)
				complete(c, Integer.valueOf(time));
			else
				complete(c, -1);
		});

		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
		});
	}
}
