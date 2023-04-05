package Bomber.GameFunction;

public class TimeCounter {
    //Time when system started
    private long timeStarted = System.currentTimeMillis();

    public void resetCounter() {
        timeStarted = System.currentTimeMillis();
    }

    public double getTime() {
        return (System.currentTimeMillis() - timeStarted) / 1000F;
    }
}
