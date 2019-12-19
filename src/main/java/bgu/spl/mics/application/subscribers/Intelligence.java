package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private int time;
	private int serialNumber;
	private List<MissionInfo> missions;

	public Intelligence() {
		super("Intelligence");
		this.serialNumber = 0;
	}

	public Intelligence(String name, int serialNumber, List<MissionInfo> missions) {
		super(name);
		this.serialNumber = serialNumber;
		this.missions = missions;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
			for(MissionInfo mission : missions) {
				if (mission.getTimeIssued() == time) {
					getSimplePublisher().sendEvent(new MissionReceivedEvent(mission));
					System.out.println(mission.getMissionName());
				}
			}
		});
	}
}
