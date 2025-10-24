package com.model.Coding.Progress;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.Coding.User.User;
import com.model.Coding.User.UserList;

/**
 * Leader that displays the top times from each user
 * @author
 */
public class Leaderboard {
    private static Leaderboard leaderboard;

    /**
     * Initializes leaderboard
     */
    private Leaderboard() {
        leaderboard = this; 
    }

    /**
     * Gets instance of leaderboard
     * @return Leaderboard
    */
    public static Leaderboard getInstance() {
        return leaderboard != null ? leaderboard : new Leaderboard(); 
    }
    
    public Integer getUserBestTime(User user, int difficulty) {
        ArrayList<Integer> times = user.getCompletionTimes(difficulty);
        Integer best = null; 
        if (times != null) {
            for (Integer timeInt : times) {
                if (best == null || timeInt < best) // smaller time = better
                    best = timeInt; 
            }
        }

        return best;
    } 

    /**
     * Gets the users completion time and difficulty.
     * @param user Respective user
     * @param difficulty Game difficulty
     * @return ArrayList of the users top times
    */
    public ArrayList<String> getFormattedOrderedTimes(int difficulty) {
        ArrayList<Map.Entry<String, Integer>> times = new ArrayList<>();
        for (User user : UserList.getInstance().getUsers()) {
            Integer bestTime = getUserBestTime(user, difficulty);
            if (bestTime != null) times.add(new AbstractMap.SimpleEntry<String,Integer>(user.getUserName(), bestTime));
        }
        times.sort(Map.Entry.comparingByValue()); // defaultly goes from smallest to greatest

        ArrayList<String> formattedTimes = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Integer> time : times) {
            int totalSeconds = Integer.valueOf(time.getValue());
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;
            
            String userName = time.getKey();
            String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            formattedTimes.add(++index + ". " + userName + ": " + formatted);
        }
        return formattedTimes;
    }
}
