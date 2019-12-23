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
        private static MessageBrokerImpl instance = new MessageBrokerImpl();
    }

    private Map<Subscriber, Queue<Message>> subscribersQueuesMap;
    private Map<Event, Future> futuresMap;
    private Map<Class<? extends Event>, Queue<Subscriber>> eventsSubscribers;
    private Map<Class<? extends Broadcast>, List<Subscriber>> broadcastsSubscribers;

    private MessageBrokerImpl() {
//        System.out.println("MessageBrokerImpl");
        subscribersQueuesMap = new ConcurrentHashMap<>();
        eventsSubscribers = new ConcurrentHashMap<>();
        broadcastsSubscribers = new ConcurrentHashMap<>();
        futuresMap = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBrokerImpl getInstance() {
//        System.out.println("getInstance");
        return SingletonHolder.instance;
    }

    @Override
    public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
//        System.out.println("subscribeEvent");
        if (!eventsSubscribers.containsKey(type))
            eventsSubscribers.put(type, new ConcurrentLinkedQueue<>());
        eventsSubscribers.get(type).add(m);
    }

    @Override
    public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
//        System.out.println("subscribeBroadcast");
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
//        System.out.println("sendBroadcast");
        if (broadcastsSubscribers == null || broadcastsSubscribers.isEmpty() || broadcastsSubscribers.get(b.getClass()) == null || broadcastsSubscribers.get(b.getClass()).isEmpty())
            return;
        // TODO: check why it throws null pointer exception
//        synchronized (broadcastsSubscribers.get(b.getClass())) {
        for (Subscriber s : broadcastsSubscribers.get(b.getClass())) {
//            System.out.println("sendBroadcast2");
            if (subscribersQueuesMap.get(s) != null) {
                subscribersQueuesMap.get(s).add(b);
                synchronized(s) {
//                    System.out.println("sendBroadcast3");
                    s.notifyAll();
                }
            }
        }
//        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
//        System.out.println("messagebroker sendEvent");
        Queue<Subscriber> eTypeSubscribersQueue = eventsSubscribers.get(e.getClass());
        if (eTypeSubscribersQueue == null || eTypeSubscribersQueue.isEmpty())
            return null;
        Subscriber subscriber = eTypeSubscribersQueue.poll();
        Future<T> future = new Future<>();
        futuresMap.put(e, future);
        synchronized (subscriber) {
            subscribersQueuesMap.get(subscriber).add(e);
            subscriber.notifyAll();
        }
        eTypeSubscribersQueue.add(subscriber);
        return future;
    }

    @Override
    public void register(Subscriber m) {
//        System.out.println("register");
        subscribersQueuesMap.put(m, new ConcurrentLinkedQueue<>());
    }

    @Override
    public void unregister(Subscriber m) {
//        System.out.println("unregister");
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
        }
        return subscribersQueuesMap.get(m).poll();
    }


}
