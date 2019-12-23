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

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory inventory;

    @BeforeEach
    public void setUp(){
        inventory = Inventory.getInstance();
    }

//    /**
//     * Test method for {@link bgu.spl.mics.application.passiveObjects.Inventory#getInstance}.
//     */
//    @Test public void testGetInstance() {
//        Inventory newInventory = Inventory.getInstance();
//        String item1 = "Sky Hook";
//        String[] inventoryArray = {item1};
//        inventory.load(inventoryArray);
//        assertEquals(inventory.getItem(item1), newInventory.getItem(item1));
//    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Inventory#load(String[] inventory)}.
     */
    @Test public void testLoad() {
        String item1 = "Sky Hook";
        String item2 = "Explosive Pen";
        String item3 = "Flying Broom";
        String[] inventoryArray = {item1, item2};
        inventory.load(inventoryArray);
        assertTrue(inventory.getItem(item1));
        assertTrue(inventory.getItem(item2));
        assertFalse(inventory.getItem(item3));
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Inventory#getItem(String)}.
     */
    @Test public void testGetItem() {
        String item1 = "Sky Hook";
        String item2 = "Explosive Pen";
        String item3 = "Flying Broom";
        String[] inventoryArray = {item1, item2};
        inventory.load(inventoryArray);
        assertTrue(inventory.getItem(item1));
        assertTrue(inventory.getItem(item2));
        assertFalse(inventory.getItem(item3));
    }

    /**
     * Test method for {@link bgu.spl.mics.application.passiveObjects.Inventory#printToFile(String)}.
     */
    @Test public void testPrintToFile() {
        String item1 = "Sky Hook";
        String item2 = "Explosive Pen";
        String item3 = "Flying Broom";
        String[] inventoryArray = {item1, item2, item3};
        inventory.load(inventoryArray);
        inventory.printToFile("inventoryOutputFile.json");
        Gson gson = new Gson();
        List<String> data;
        try (JsonReader reader = new JsonReader(new FileReader("inventoryOutputFile.json"))) {
            data = gson.fromJson(reader, ArrayList.class);
            assertEquals(item1, data.get(0));
            assertEquals(item2, data.get(1));
            assertEquals(item3, data.get(2));
        } catch (FileNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
