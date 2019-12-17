package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

    private static class SingletonHolder {
        private static MessageBroker instance = new MessageBrokerImpl();
    }

    private Map<Subscriber, Queue<Message>> subscribersQueuesMap;
    private Map<Event, Future> futuresMap;
    private Map<Class<? extends Event>, Queue<Subscriber>> eventsSubscribers;
    private Map<Class<? extends Broadcast>, List<Subscriber>> broadcastsSubscribers;

    private MessageBrokerImpl() {
        subscribersQueuesMap = new ConcurrentHashMap<>();
        eventsSubscribers = new ConcurrentHashMap<>();
        broadcastsSubscribers = new ConcurrentHashMap<>();
        futuresMap = new ConcurrentHashMap<>();
        eventsSubscribers.put(AgentsAvailableEvent.class, new ConcurrentLinkedQueue<>());
        eventsSubscribers.put(GadgetAvailableEvent.class, new ConcurrentLinkedQueue<>());
        eventsSubscribers.put(MissionReceivedEvent.class, new ConcurrentLinkedQueue<>());
        eventsSubscribers.put(SendAgentsEvent.class, new ConcurrentLinkedQueue<>());
        broadcastsSubscribers.put(TickBroadcast.class, new LinkedList<>());
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        eventsSubscribers.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        broadcastsSubscribers.get(type).add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        futuresMap.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        for (Subscriber s : broadcastsSubscribers.get(b.getClass()))
            subscribersQueuesMap.get(s).add(b);
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Queue<Subscriber> eTypeSubscribersQueue = eventsSubscribers.get(e.getClass());
        if (eTypeSubscribersQueue.isEmpty())
            return null;
        Subscriber subscriber = eTypeSubscribersQueue.poll();
        synchronized (subscriber) {
            subscribersQueuesMap.get(subscriber).add(e);
            subscriber.notifyAll();
        }
        eTypeSubscribersQueue.add(subscriber);
        Future<T> future = new Future<>();
        futuresMap.put(e, future);
        return future;
    }

    @Override
    public void register(Subscriber m) {
        subscribersQueuesMap.put(m, new LinkedList<Message>());
        Thread t = new Thread(m);
        t.start();
    }

    @Override
    public void unregister(Subscriber m) {
        subscribersQueuesMap.remove(m);
        for (Class<? extends Event> type : eventsSubscribers.keySet())
            eventsSubscribers.get(type).remove(m);
        for (Class<? extends Broadcast> type : broadcastsSubscribers.keySet())
            broadcastsSubscribers.get(type).remove(m);
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        synchronized (m) {
            while (subscribersQueuesMap.get(m).isEmpty())
                m.wait();
            return subscribersQueuesMap.get(m).poll();
        }
    }


}
