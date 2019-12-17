package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.JsonObjects.inventory;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	Diary diary;
	private int time;

	public M(String name) {
		super(name);
		diary = Diary.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(MissionReceivedEvent.class, (c) -> {
			// TODO: implement it
		});

		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
		});
	}
}
