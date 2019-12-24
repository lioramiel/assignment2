package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

    private static class SingletonHolder {
        private static MessageBrokerImpl instance = new MessageBrokerImpl();
    }

    private Map<Subscriber, Queue<Message>> subscribersQueuesMap;
    private Map<Event, Future> futuresMap;
    private Map<Class<? extends Event>, Queue<Subscriber>> eventsSubscribers;
    private Map<Class<? extends Broadcast>, List<Subscriber>> broadcastsSubscribers;
    private ReadWriteLock readWriteLock;

    private MessageBrokerImpl() {
        subscribersQueuesMap = new ConcurrentHashMap<>();
        eventsSubscribers = new ConcurrentHashMap<>();
        broadcastsSubscribers = new ConcurrentHashMap<>();
        futuresMap = new ConcurrentHashMap<>();
        readWriteLock = new ReentrantReadWriteLock();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBrokerImpl getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        if (!eventsSubscribers.containsKey(type))
            eventsSubscribers.put(type, new LinkedList<>());
        eventsSubscribers.get(type).add(m);
    }

    @Override
    public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        if (!broadcastsSubscribers.containsKey(type))
            broadcastsSubscribers.put(type, new LinkedList<>());
        synchronized (broadcastsSubscribers.get(type)) {
            broadcastsSubscribers.get(type).add(m);
        }
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        futuresMap.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        if (broadcastsSubscribers == null || broadcastsSubscribers.isEmpty() || broadcastsSubscribers.get(b.getClass()) == null || broadcastsSubscribers.get(b.getClass()).isEmpty())
            return;
        for (Subscriber s : broadcastsSubscribers.get(b.getClass())) {
            if (subscribersQueuesMap.get(s) != null) {
                subscribersQueuesMap.get(s).add(b);
                synchronized (this) {
                    this.notifyAll();
                }
            }
        }
    }

    @Override
    public synchronized <T> Future<T> sendEvent(Event<T> e) {
        if (eventsSubscribers.get(e.getClass()) == null)
            eventsSubscribers.put(e.getClass(), new ConcurrentLinkedQueue<>());
        Queue<Subscriber> eTypeSubscribersQueue = eventsSubscribers.get(e.getClass());
        if (eTypeSubscribersQueue.isEmpty())
            return null;
        Subscriber subscriber = eTypeSubscribersQueue.poll();
        Future<T> future = new Future<>();
        futuresMap.put(e, future);
        if (subscribersQueuesMap.get(subscriber) != null) {
            synchronized (this) {
                subscribersQueuesMap.get(subscriber).add(e);
                this.notifyAll();
            }
        }
        eTypeSubscribersQueue.add(subscriber);
        return future;
    }

    @Override
    public void register(Subscriber m) {
        subscribersQueuesMap.put(m, new ConcurrentLinkedQueue<>());
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
        synchronized (this) {
            while (subscribersQueuesMap.get(m).isEmpty())
                this.wait();
        }
        return subscribersQueuesMap.get(m).poll();
    }
}
