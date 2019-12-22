package bgu.spl.mics.application;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.JsonObjects.InputJson;
import bgu.spl.mics.application.JsonObjects.IntelligenceJson;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

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
    private static Future futureFinish;

    public static void main(String[] args) {
        loadDataFromJSON(args[0]);
        initializePassiveObjects();
        futureFinish = new Future();



        List<Thread> threads = new ArrayList<>(); //TODO: implements it better

        Q q = new Q();
        Thread t = new Thread(q);
        threads.add(t);

        for(int i = 0; i < input.getServices().getM(); i++) {
            M m = new M("M" + String.valueOf(i + 1), i + 1);
            Thread r = new Thread(m);
            threads.add(r);
        }

        for(int i = 0; i < input.getServices().getMoneypenny(); i++) {
            Moneypenny moneypenny = new Moneypenny("Moneypenny" + String.valueOf(i + 1), i + 1);
            Thread r = new Thread(moneypenny);
            threads.add(r);
        }

        int i = 0;
        for(IntelligenceJson intelligence : input.getServices().getIntelligence()) {
            List<MissionInfo> missions = new ArrayList<>();
            for(MissionInfo missionInfo : intelligence.getMissions())
                missions.add(missionInfo);
            Intelligence newIntelligence = new Intelligence("Intelligence" + String.valueOf(i+1), i+1, missions);
            Thread r = new Thread(newIntelligence);
            threads.add(r);
            i++;
        }

        TimeService timeService = new TimeService(input.getServices().getTime(), "TimeService" , futureFinish);
        Thread d = new Thread(timeService);
        threads.add(d);

        for(Thread l : threads)
            l.start();

//        while (timeService.getTime() < data.getServices().getTime()) {
//            System.out.println("lock 2");
//        }
        System.out.println("lock 2");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Thread l : threads)
            l.stop();
        inventory.printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);
        System.out.println("lock 3");
//        test t = new test();
//        try {
//            Writer writer = new FileWriter("test.json");
//            Gson gson2 = new GsonBuilder().create();
//            gson2.toJson(t, writer);
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        Inventory inventory = Inventory.getInstance();
        inventory.load(input.getInventory());
        Squad squad = Squad.getInstance();
        squad.load(input.getSquad());
    }
}
