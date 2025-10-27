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
    private int cachedTimePassed;

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
        start(startTime, startTime);
    }

    public void start(int startTime, int remainingTime) {
        if (isRunning) return;
        isRunning = true;

        this.startTime = startTime;
        this.remainingTime = remainingTime;

        timerThread = new Thread(() -> {
            while (isRunning && this.remainingTime > 0) {
                int minutes = this.remainingTime / 60;
                int seconds = this.remainingTime % 60;
                // System.out.printf("Time remaining: %02d:%02d%n", minutes, seconds);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                this.remainingTime--;
            }

            if (this.remainingTime == 0) {
                System.out.println("Time's up!");
                isRunning = false;
            }
        });
        timerThread.start();
    }

    private void start() {
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
        cachedTimePassed = startTime - remainingTime;
        isRunning = false;
    }

    /**
     * Resets the timer
     */
    public void reset() {
        cachedTimePassed = startTime - remainingTime;
        isRunning = false;
        remainingTime = 0;
        startTime = 0;
    }

    /**
     * Resumes the timer
     */
    public void resume() {
        if (startTime == 0) {
            System.out.println("Cannot resume. Timer has not been started");
            return;
        } else if (remainingTime == 0) {
            System.out.println("Cannot resume. No time left.");
            return;
        }

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
        if (isRunning) {
            return startTime - remainingTime;
        } else {
            return cachedTimePassed;
        }
    }

    public String getTimePassedFormatted() {
        int totalSeconds = getTimePassed();
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
            
        String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return formatted;
    }
}
