package bgu.spl.mics.application;

import bgu.spl.mics.application.JsonObjects.input;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        Gson gson = new Gson();
        try (JsonReader reader = new JsonReader(new FileReader("input.json"))) {
            input data = gson.fromJson(reader, input.class);
            System.out.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
