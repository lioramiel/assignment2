package bgu.spl.mics.application.JsonObjects;


import java.util.List;

public class services {
    private int M;
    private int Moneypenny;
    private List<bgu.spl.mics.application.JsonObjects.intelligence> intelligence;
    private int time;

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public int getMoneypenny() {
        return Moneypenny;
    }

    public void setMoneypenny(int moneypenny) {
        Moneypenny = moneypenny;
    }

    public List<bgu.spl.mics.application.JsonObjects.intelligence> getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(List<bgu.spl.mics.application.JsonObjects.intelligence> intelligence) {
        this.intelligence = intelligence;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Services{" +
                "M=" + M +
                ", Moneypenny=" + Moneypenny +
                ", intelligence=" + intelligence +
                ", time=" + time +
                '}';
    }
}
