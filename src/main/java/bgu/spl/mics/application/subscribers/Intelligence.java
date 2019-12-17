package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private int time;

	public Intelligence(String name) {
		super(name);
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
		});
	}
}
