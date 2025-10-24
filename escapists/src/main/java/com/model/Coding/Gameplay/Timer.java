package com.model.Coding.Gameplay;

/**
 * A countdown timer
 * @author Mason Adams
 */
public class Timer {
    private int startTime;
    private int remainingTime;
    private boolean isRunning = false;
    private static Timer timer;
    private Thread timerThread;

    /**
     * Initializes startTime and remainingTime
     * @param startTime Start time (in seconds)
     */
    private Timer() {
        
    }

    /**
     * If timer doesn't exist, it created one. If it does, it returns the timer
     * @return Timer object
     */
    public static Timer getInstance() {
        if (timer == null) {
            timer = new Timer();
        }
        return timer;
    }

    /**
     * Starts the timer
     * @param startTime time in seconds timer starts at
     */
    public void start(int startTime) {
        if (isRunning) return;
        isRunning = true;

        this.startTime = startTime;
        this.remainingTime = startTime;

        timerThread = new Thread(() -> {
            while (isRunning && remainingTime > 0) {
                int minutes = remainingTime / 60;
                int seconds = remainingTime % 60;
                // System.out.printf("Time remaining: %02d:%02d%n", minutes, seconds);
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

    public void start() {
        if (isRunning) return;
        isRunning = true;

        if (this.remainingTime < 0)
            throw new RuntimeException("This remaining time is not possible. what?");

        timerThread = new Thread(() -> {
            while (isRunning && remainingTime > 0) {
                int minutes = remainingTime / 60;
                int seconds = remainingTime % 60;
                // System.out.printf("Time remaining: %02d:%02d%n", minutes, seconds);
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

    /**
     * Pauses the timer
     */
    public void pause() {
        isRunning = false;
    }

    /**
     * Resets the timer
     */
    public void reset() {
        isRunning = false;
        remainingTime = startTime;
    }

    /**
     * Resumes the timer
     */
    public void resume() {
        start();
    }

    /**
     * Gets the remaining amount of time
     * @return Remaining time as an integer
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    public int getTimePassed() {
        return startTime - remainingTime;
    }
}
