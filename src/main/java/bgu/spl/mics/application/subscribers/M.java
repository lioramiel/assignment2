package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private Diary diary;
	private int time;
	private int serialNumber;

	public M() {
		super("M");
		this.serialNumber = 0;
		this.diary = Diary.getInstance();
	}

	public M(String name, int serialNumber) {
		super(name);
		this.diary = Diary.getInstance();
		this.serialNumber = serialNumber;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	@Override
	protected void initialize() {
		subscribeEvent(MissionReceivedEvent.class, (c) -> {
			System.out.println("1.M - " + serialNumber +" : " + System.currentTimeMillis());
			diary.incrementTotal();
			System.out.println("2.M - " + serialNumber +" : " + System.currentTimeMillis());
			MissionInfo mission  = c.getMission();
			System.out.println("3.M - " + serialNumber +" : " + System.currentTimeMillis());
			Future futureAgentAvailable = getSimplePublisher().sendEvent(new AgentsAvailableEvent(mission.getSerialAgentsNumbers()));
			System.out.println("4.M - " + serialNumber +" : " + System.currentTimeMillis());
			Integer moneypennySerialNum = (Integer) futureAgentAvailable.get();
			System.out.println("5.M - " + serialNumber +" : " + System.currentTimeMillis());
			Future futureGadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(mission.getGadget()));
			System.out.println("6.M - " + serialNumber +" : " + System.currentTimeMillis());
			Integer QTime = (Integer) futureGadgetAvailable.get();
			System.out.println("7.M - " + serialNumber +" : " + System.currentTimeMillis());
			if(time < mission.getTimeExpired() && moneypennySerialNum != -1 && QTime != -1) {
				System.out.println("8.M - " + serialNumber +" : " + System.currentTimeMillis());
				Future futureSendAgent = getSimplePublisher().sendEvent(new SendAgentsEvent(mission.getSerialAgentsNumbers(), mission.getDuration()));
				System.out.println("9.M - " + serialNumber +" : " + System.currentTimeMillis());
				List<String> agentsName = (List<String>) futureSendAgent.get();
				System.out.println("10.M - " + serialNumber +" : " + System.currentTimeMillis());
				Report report = new Report(mission.getMissionName(), getSerialNumber(), moneypennySerialNum.intValue(), mission.getSerialAgentsNumbers(), agentsName, mission.getGadget(), mission.getTimeIssued(), QTime, time);
				System.out.println("11.M - " + serialNumber +" : " + System.currentTimeMillis());
				diary.addReport(report);
			} else {
				System.out.println("12.M - " + serialNumber +" : " + System.currentTimeMillis());
				getSimplePublisher().sendEvent(new ReleaseAgentsEvent(mission.getSerialAgentsNumbers()));
				System.out.println("13.M - " + serialNumber +" : " + System.currentTimeMillis());
			}
		});

		subscribeBroadcast(TickBroadcast.class, (c) -> {
			this.time = c.getTime();
		});
	}
}
