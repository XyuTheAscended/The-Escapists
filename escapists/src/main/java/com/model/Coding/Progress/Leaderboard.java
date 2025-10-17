package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import com.model.Coding.User.User;

public class Leaderboard {
    private static Leaderboard leaderboard; 

    private Leaderboard() {
        leaderboard = this; 
    }

    public static Leaderboard getInstance() {
        return leaderboard != null ? leaderboard : new Leaderboard(); 
    }

    public ArrayList<String> getFormattedOrderedTimes(User user, int difficulty) {
        ArrayList<Integer> times = user.getCompletionTimes(difficulty);
        times.sort(null); // defaultly goes from smallest to greatest

        ArrayList<String> formattedTimes = new ArrayList<>();
        for (Integer secondsObj : times) {
            int totalSeconds = Integer.valueOf(secondsObj);
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            formattedTimes.add(formatted);
        }
        
        return formattedTimes;
    }

}
