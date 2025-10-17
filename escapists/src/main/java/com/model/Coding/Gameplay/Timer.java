package com.model.Coding.Gameplay;

public class Timer {
    private int startTime;
    private int remainingTime;
    private boolean isRunning = false;
    private static Timer timer;
    private Thread timerThread;

    private Timer(int startTime) {
        this.startTime = startTime;
        this.remainingTime = startTime;
    }

    // Parameter version for initialization
    public static Timer getInstance(int startTime) {
        if (timer == null) {
            timer = new Timer(startTime);
        }
        return timer;
    }

    public static Timer getInstance() {
        return timer;
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;

        timerThread = new Thread(() -> {
            while (isRunning && remainingTime > 0) {
                int minutes = remainingTime / 60;
                int seconds = remainingTime % 60;
                System.out.printf("Time remaining: %02d:%02d%n", minutes, seconds);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                remainingTime--;
            }

            if (remainingTime == 0) {
                System.out.println("Time's up!");
                isRunning = false;
            }
        });
        timerThread.start();
    }

    public void pause() {
        isRunning = false;
    }

    public void reset() {
        isRunning = false;
        remainingTime = startTime;
    }

    public void resume() {
        start();
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}
