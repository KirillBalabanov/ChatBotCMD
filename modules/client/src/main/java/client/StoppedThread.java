package client;

public interface StoppedThread extends Runnable {
    void stopThread();
    boolean isAlive();
}
