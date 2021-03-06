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
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private Diary diary;
    private int time;
    private int serialNumber;
    private int duration;

    public M() {
        super("M");
        this.serialNumber = 0;
        this.diary = Diary.getInstance();
    }

    public M(String name, int serialNumber, int duration) {
        super(name);
        this.diary = Diary.getInstance();
        this.serialNumber = serialNumber;
        this.duration = duration;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    protected void initialize() {
        subscribeEvent(MissionReceivedEvent.class, (c) -> {
            diary.incrementTotal();
            MissionInfo mission = c.getMission();
            AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(mission.getSerialAgentsNumbers(), mission.getDuration());
            Future futureAgentsAvailable = getSimplePublisher().sendEvent(agentsAvailableEvent);
            Future futureMoneypennyResponse = (Future) futureAgentsAvailable.get();
            if (agentsAvailableEvent.getMoneypennySerialNumber() == -1)
                futureMoneypennyResponse.resolve(false);
            else {
                if (time <= mission.getTimeExpired()) {
                    Future futureGadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(mission.getGadget()));
                    Integer QTime = -1;
                    if (futureGadgetAvailable != null)
                        QTime = (Integer) futureGadgetAvailable.get();
                    if (time <= mission.getTimeExpired() && QTime != -1) {
                        futureMoneypennyResponse.resolve(true);
                        Report report = new Report(mission.getMissionName(), getSerialNumber(), agentsAvailableEvent.getMoneypennySerialNumber(), mission.getSerialAgentsNumbers(), agentsAvailableEvent.getAgentsName(), mission.getGadget(), mission.getTimeIssued(), QTime, time);
                        diary.addReport(report);
                    } else {
                        futureMoneypennyResponse.resolve(false);
                    }
                }
            }
        });

        subscribeBroadcast(TickBroadcast.class, (c) -> {
            this.time = c.getTime();
            if (time == this.duration)
                this.terminate();
        });
    }
}
