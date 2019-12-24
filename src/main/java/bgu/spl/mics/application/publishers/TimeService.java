package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private int duration;
    private int time;
    private Timer timer;
    private Future<Boolean> futureFinish;
    private boolean terminated = false;

    public TimeService(int duration, String name, Future futureFinish) {
        super(name);
        this.duration = duration;
        this.time = 0;
        this.futureFinish = futureFinish;
        timer = new Timer();
    }

    @Override
    protected void initialize() {

    }

    @Override
    public void run() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Broadcast tickBroadcast = new TickBroadcast(time);
                getSimplePublisher().sendBroadcast(tickBroadcast);
                time = time + 1;
                if (duration < time) {
                    timer.cancel();
                    futureFinish.resolve(true);
                }
            }
        }, 0, 100);
    }

    public int getTime() {
        return time;
    }
}
