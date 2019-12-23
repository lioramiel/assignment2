package bgu.spl.mics.application;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.JsonObjects.InputJson;
import bgu.spl.mics.application.JsonObjects.IntelligenceJson;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.management.monitor.MonitorNotification;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    private static InputJson input;
    private static Inventory inventory;
    private static Squad squad;
    private static Future<Boolean> futureFinish;
    private static List<Thread> threads;
    private static Q q;
    private static M[] mArray;
    private static Moneypenny[] moneypenniesArray;
    private static Intelligence[] intelligencesArray;

    public static void main(String[] args) {
        threads = new ArrayList<>();
        futureFinish = new Future();
        loadDataFromJSON(args[0]);
        initializePassiveObjects();
        createActiveObjects();
        runThreads();
        Boolean finished = futureFinish.get();
        if(finished)
            finishApp(args[1], args[2]);
    }

    private static void loadDataFromJSON(String fileName) {
        Gson gson = new Gson();
        try (JsonReader reader = new JsonReader(new FileReader(fileName))) {
            input =  gson.fromJson(reader, InputJson.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializePassiveObjects() {
        inventory = Inventory.getInstance();
        inventory.load(input.getInventory());
        squad = Squad.getInstance();
        squad.load(input.getSquad());
    }

    private static void createActiveObjects() {
        createQ();
        createMs();
        createMoneypennys();
        createIntelligences();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createTimeService();
    }

    private static void createQ() {
        q = new Q("Q", 1, input.getServices().getTime());
        Thread t = new Thread(q);
        threads.add(t);
    }

    private static void createMs() {
        mArray = new M[input.getServices().getM()];
        for(int i = 0; i < mArray.length; i++) {
            mArray[i] = new M("M" + String.valueOf(i + 1), i + 1, input.getServices().getTime());
            Thread r = new Thread(mArray[i]);
            threads.add(r);
        }
    }

    private static void createMoneypennys() {
        moneypenniesArray = new Moneypenny[input.getServices().getMoneypenny()];
        for(int i = 0; i < moneypenniesArray.length; i++) {
            moneypenniesArray[i] = new Moneypenny("Moneypenny" + String.valueOf(i + 1), i + 1, input.getServices().getTime());
            Thread r = new Thread(moneypenniesArray[i]);
            threads.add(r);
        }
    }

    private static void createIntelligences() {
        intelligencesArray = new Intelligence[input.getServices().getIntelligence().length];
        for(int i = 0; i < input.getServices().getIntelligence().length; i++) {
            intelligencesArray[i] = new Intelligence("Intelligence" + String.valueOf(i+1), i+1, input.getServices().getIntelligence()[i].getMissions(), input.getServices().getTime());
            Thread r = new Thread(intelligencesArray[i]);
            threads.add(r);
        }
    }

    private static void createTimeService() {
        TimeService timeService = new TimeService(input.getServices().getTime(), "TimeService" , futureFinish);
        Thread d = new Thread(timeService);
        threads.add(d);
    }

    private static void runThreads() {
        for(Thread l : threads)
            l.start();
    }

    private static void finishApp(String inventoryFileName, String diaryFileName) {
        inventory.printToFile(inventoryFileName);
        Diary.getInstance().printToFile(diaryFileName);
        inventory.load(new String [0]);
        squad.load(new Agent[0]);
    }
}
