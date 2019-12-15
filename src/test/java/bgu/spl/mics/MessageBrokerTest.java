package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    MessageBroker messageBroker;

    @BeforeEach
    public void setUp() {
        messageBroker = MessageBrokerImpl.getInstance();
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#subscribeEvent(Class<? extends Event<T>> type, Subscriber s)}.
     */
    @Test public void testSubscribeEvent() {
        Subscriber subscriber = new M();
        messageBroker.register(subscriber);
        Event<String> event = new ExampleEvent("agent001");
        assertTrue(messageBroker.sendEvent(event) == null);
        messageBroker.subscribeEvent(ExampleEvent.class, subscriber);
        try {
            messageBroker.awaitMessage(subscriber);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertFalse(messageBroker.sendEvent(event) == null);
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#subscribeBroadcast(Class<? extends Event<T>> type, Subscriber s)}.
     */
    @Test public void testSubscribeBroadcast() {
        Subscriber subscriber = new M();
        messageBroker.register(subscriber);
        Broadcast broadcast = new ExampleBroadcast("agent001");
        Broadcast broadcast2 = new ExampleBroadcast("agent002");
        messageBroker.sendBroadcast(broadcast);
        messageBroker.sendBroadcast(broadcast2);
        messageBroker.subscribeBroadcast(ExampleBroadcast.class, subscriber);
        try {
            Message nextMessage = messageBroker.awaitMessage(subscriber);
            assertSame(broadcast2, nextMessage);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#complete(Event<T> e, T result)}.
     */
    @Test public void testComplete() {
        Subscriber subscriber = new M();
        messageBroker.register(subscriber);
        Event<String> event = new ExampleEvent("agent001");
        messageBroker.subscribeEvent(ExampleEvent.class, subscriber);
        Future<String> future = messageBroker.sendEvent(event);
        try {
            messageBroker.awaitMessage(subscriber);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        messageBroker.complete(event, "result");
        assertEquals("result", future.get());
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#sendEvent(Broadcast b)}.
     */
    @Test public void testSendEvent() {
        Subscriber subscriber = new M();
        messageBroker.register(subscriber);
        Event<String> event = new ExampleEvent("agent001");
        assertTrue(messageBroker.sendEvent(event) == null);
        messageBroker.subscribeEvent(ExampleEvent.class, subscriber);
        try {
            messageBroker.awaitMessage(subscriber);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertFalse(messageBroker.sendEvent(event) == null);
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#sendBroadcast(Broadcast b)}.
     */
    @Test public void testSendBroadcast() {
        Subscriber subscriber = new M();
        messageBroker.register(subscriber);
        Broadcast broadcast = new ExampleBroadcast("agent001");
        Broadcast broadcast2 = new ExampleBroadcast("agent002");
        messageBroker.sendBroadcast(broadcast);
        messageBroker.sendBroadcast(broadcast2);
        messageBroker.subscribeBroadcast(ExampleBroadcast.class, subscriber);
        try {
            Message nextMessage = messageBroker.awaitMessage(subscriber);
            assertSame(broadcast2, nextMessage);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#register(Subscriber s)}.
     */
    @Test public void testRegister() {
        Subscriber subscriber = new M();
        Event<String> event = new ExampleEvent("agent001");
        messageBroker.subscribeEvent(ExampleEvent.class, subscriber);
        try {
            messageBroker.awaitMessage(subscriber);
            fail("Exception expected!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(messageBroker.sendEvent(event) == null);
        messageBroker.register(subscriber);
        try {
            messageBroker.awaitMessage(subscriber);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertFalse(messageBroker.sendEvent(event) == null);
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#unregister(Subscriber s)}.
     */
    @Test public void testUnregister() {
        Subscriber subscriber = new M();
        Event<String> event = new ExampleEvent("agent001");
        messageBroker.register(subscriber);
        messageBroker.subscribeEvent(ExampleEvent.class, subscriber);
        try {
            messageBroker.awaitMessage(subscriber);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertFalse(messageBroker.sendEvent(event) == null);
        messageBroker.unregister(subscriber);
        try {
            messageBroker.awaitMessage(subscriber);
            fail("Exception expected!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(messageBroker.sendEvent(event) == null);
    }

    /**
     * Test method for {@link bgu.spl.mics.MessageBroker#awaitMessage(Subscriber s)}.
     */
    @Test public void testAwaitMessage() {
        Subscriber subscriber = new M();
        Event<String> event = new ExampleEvent("agent001");
        Event<String> event2 = new ExampleEvent("agent002");
        messageBroker.register(subscriber);
        messageBroker.subscribeEvent(ExampleEvent.class, subscriber);
        messageBroker.sendEvent(event);
        messageBroker.sendEvent(event2);
        try {
            Message nextMessage = messageBroker.awaitMessage(subscriber);
            assertSame(event2, nextMessage);
        } catch (InterruptedException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertFalse(messageBroker.sendEvent(event) == null);
        messageBroker.unregister(subscriber);
        try {
            messageBroker.awaitMessage(subscriber);
            fail("Exception expected!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(messageBroker.sendEvent(event) == null);
    }
}
