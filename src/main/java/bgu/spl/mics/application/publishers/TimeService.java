package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
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
    private MessageBroker messageBroker;


    public TimeService(int duration, String name) {
        super(name);
        this.duration = duration;
        this.time = 0;
        this.messageBroker = MessageBrokerImpl.getInstance();
    }

    @Override
    protected void initialize() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Broadcast tickBroadcast = new TickBroadcast(time);
                messageBroker.sendBroadcast(tickBroadcast);
                time = time + 1;
                if (duration < time * 100)
                    cancel();
            }
        }, 0, 100);
    }

    protected void stopTimer() {
        timer.cancel();
    }

}
