package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    private static MessageBroker messageBroker;
    private Map<Subscriber, Queue<Message>> subscribersQueuesMap;
    private Map<Class<? extends Event>, List<Subscriber>> eventsSubscribers;
    private Map<Class<? extends Event>, Integer> lastSubscriber;
    private Map<Class<? extends Broadcast>, List<Subscriber>> broadcastsSubscribers;

    private MessageBrokerImpl() {
        subscribersQueuesMap = new HashMap<>();
        eventsSubscribers = new HashMap<>();
        lastSubscriber = new HashMap<>();
        broadcastsSubscribers = new HashMap<>();
        eventsSubscribers.put(AgentsAvailableEvent.class, new LinkedList<>());
        lastSubscriber.put(AgentsAvailableEvent.class, 0);
        eventsSubscribers.put(GadgetAvailableEvent.class, new LinkedList<>());
        lastSubscriber.put(GadgetAvailableEvent.class, 0);
        eventsSubscribers.put(MissionReceivedEvent.class, new LinkedList<>());
        lastSubscriber.put(MissionReceivedEvent.class, 0);
        broadcastsSubscribers.put(TickBroadcast.class, new LinkedList<>());

    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        if (messageBroker == null)
            messageBroker = new MessageBrokerImpl();
        return messageBroker;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        eventsSubscribers.get(type).add(m);
        m.subscribeEvent(type, c -> {
            try {
                awaitMessage(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        broadcastsSubscribers.get(type).add(m);
        m.subscribeBroadcast(type, c -> {
            try {
                awaitMessage(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        // TODO Auto-generated method stub
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        for (Subscriber s : broadcastsSubscribers.get(b.getClass()))
            subscribersQueuesMap.get(s).add(b);
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        if(lastSubscriber.get(e.getClass()) == eventsSubscribers.get(e.getClass()).size())
            lastSubscriber.put(e.getClass(), 0);
        if(!subscribersQueuesMap.get(eventsSubscribers.get(e.getClass())).isEmpty()) {
            subscribersQueuesMap.get(eventsSubscribers.get(e.getClass()).get(lastSubscriber.get(e.getClass()))).add(e);
            for(Subscriber s : eventsSubscribers.get(e.getClass())) {
                synchronized (s) {
                    s.notifyAll();
                }
            }
            lastSubscriber.put(e.getClass(), lastSubscriber.get(e.getClass()) + 1);
            Future<T> future = new Future<>();
            return future;
        }
        return null;
    }

    @Override
    public void register(Subscriber m) {
        subscribersQueuesMap.put(m, new LinkedList<Message>());
        m.initialize();
    }

    @Override
    public void unregister(Subscriber m) {
        m.terminate();
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        synchronized(m) {
            while (subscribersQueuesMap.get(m).isEmpty())
                m.wait();
            return subscribersQueuesMap.get(m).poll();
        }
    }


}
