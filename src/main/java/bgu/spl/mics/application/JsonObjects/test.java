package bgu.spl.mics.application.JsonObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class test {
    //TODO: delete this class
    List<String> gadgets;
    private AtomicInteger total = new AtomicInteger(0);

    public test() {
        gadgets = new ArrayList<>();
        gadgets.add("123");
        gadgets.add("456");
        gadgets.add("789");
    }

    public List<String> getGadgets() {
        return gadgets;
    }

    public void setGadgets(List<String> gadgets) {
        this.gadgets = gadgets;
    }

    public AtomicInteger getTotal() {
        return total;
    }

    public void setTotal(AtomicInteger total) {
        this.total = total;
    }
}
