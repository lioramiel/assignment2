package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private Diary diary;
	private int time;
	private String serialNumber;

	public M() {
		super("M");
		this.serialNumber = "021";
		this.diary = Diary.getInstance();
	}

	public M(String name, String serialNumber) {
		super(name);
		this.diary = Diary.getInstance();
		this.serialNumber = serialNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	@Override
	protected void initialize() {
		subscribeEvent(MissionReceivedEvent.class, (c) -> {
			c.setSubscriberSerialNumber(serialNumber);
			diary.incrementTotal();
			MissionInfo mission  = c.getMission();
			Future futureAgentAvailable = getSimplePublisher().sendEvent(new AgentsAvailableEvent(mission.getSerialAgentsNumbers()));
			futureAgentAvailable.get();
			Future futureGadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(mission.getGadget()));
			futureGadgetAvailable.get();
			if(time < mission.getTimeExpired()) {
				Future futureSendAgent = getSimplePublisher().sendEvent(new SendAgentsEvent(mission.getSerialAgentsNumbers(), mission.getDuration()));
				futureSendAgent.get();
//				diary.addReport(mission);
			}
		});

		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
		});
	}
}
