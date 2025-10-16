package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import com.model.Coding.User.User;

public class Leaderboard {
    private HashMap<User, String> times;

    public Leaderboard() {
        times = new HashMap<>();
    }

    public void addTime(String time, User user) {
        times.put(user, time);
    }

    public HashMap<User, String> getTimes() {
        return times;
    }

    public String getUserTime(User user) {
        return times.get(user);
    }
}
