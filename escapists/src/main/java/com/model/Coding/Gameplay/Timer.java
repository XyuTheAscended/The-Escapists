package com.model.Coding.Gameplay;

public class Timer {
    private int startTime;
    private int remainingTime;
    private boolean isRunning;
    private static Timer timer;


private Timer(int startTime){
    this.startTime = startTime;
}

public static Timer getInstance(){
    return timer;

}

public void start(){

}

public void pause(){

}

public void reset(){

}

public int getRemainingTime(){
    return remainingTime;

}

}