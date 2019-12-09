package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<Integer> future;

    @BeforeEach
    public void setUp(){
        future = new Future<Integer>();
    }

    /**
     * Test method for {@link bgu.spl.mics.Future#get()}.
     */
    @Test public void testGet() {
        Integer i = future.get();
        assertEquals(null, i);
        future.resolve(25);
        assertEquals(25, future.get());
    }

    /**
     * Test method for {@link bgu.spl.mics.Future#resolve(Object)}.
     */
    @Test public void testResolve() {
        Integer i = future.get();
        assertEquals(null, i);
        future.resolve(25);
        assertEquals(25, future.get());
    }

    /**
     * Test method for {@link bgu.spl.mics.Future#isDone()}.
     */
    @Test public void testIsDone() {
        assertFalse(future.isDone());
        future.resolve(25);
        assertTrue(future.isDone());
    }

    /**
     * Test method for {@link bgu.spl.mics.Future#get(long, TimeUnit)} ()}.
     */
    @Test public void testGetWithTimeout() {
        // Test get when future resolve in timeout time.
        Integer i = future.get(1, TimeUnit.SECONDS);
        assertEquals(null, i);
        future.resolve(25);
        try {
            Thread.sleep(1001);
        } catch (InterruptedException e1) {}
        assertEquals(25, i);

        // Test get when future does not resolve in timeout time.
        Integer j = future.get(1, TimeUnit.SECONDS);
        try {
            Thread.sleep(1001);
        } catch (InterruptedException e1) {}
        assertEquals(null, j);
    }
}
