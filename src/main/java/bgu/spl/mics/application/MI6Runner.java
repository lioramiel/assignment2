package bgu.spl.mics.application;

import bgu.spl.mics.application.JsonObjects.InputJson;
import bgu.spl.mics.application.JsonObjects.test;
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
        Gson gson = new Gson();
        InputJson data = null;
        try (JsonReader reader = new JsonReader(new FileReader("input.json"))) {
            data = gson.fromJson(reader, InputJson.class);
            System.out.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: read the JSON and create all relevant objects
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
