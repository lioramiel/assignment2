package bgu.spl.mics.application;

import bgu.spl.mics.application.JsonObjects.InputJson;
import bgu.spl.mics.application.JsonObjects.IntelligenceJson;
import bgu.spl.mics.application.JsonObjects.test;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        Object lock = new Object();
        Gson gson = new Gson();
        InputJson data = null;
        try (JsonReader reader = new JsonReader(new FileReader(args[0]))) {
            data = gson.fromJson(reader, InputJson.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Inventory inventory = Inventory.getInstance();
        inventory.load(data.getInventory());
        Squad squad = Squad.getInstance();
        squad.load(data.getSquad());

        new Q();

        for(int i = 0; i < data.getServices().getM(); i++)
            new M("M" + String.valueOf(i+1), i+1);

        for(int i = 0; i < data.getServices().getMoneypenny(); i++)
            new M("Moneypenny" + String.valueOf(i+1), i+1);

        int i = 0;
        for(IntelligenceJson intelligence : data.getServices().getIntelligence()) {
            new Intelligence("Moneypenny" + String.valueOf(i+1), i+1, intelligence.getMissions());
            i++;
        }

        TimeService timeService = new TimeService(data.getServices().getTime(), "TimeService");
        Thread t = new Thread(timeService);
        t.start();

//        while (timeService.getTime() < data.getServices().getTime()) {
//            System.out.println("lock 2");
//        }
//        System.out.println("lock 3");
//        inventory.printToFile(args[1]);
//        Diary.getInstance().printToFile(args[2]);
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
}
