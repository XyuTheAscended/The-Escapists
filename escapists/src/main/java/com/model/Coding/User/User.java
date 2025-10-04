import java.util.ArrayList;
import java.util.List;

import com.model.Coding.Progress.Progress;


public class User {
    private String userName;
    private String password;
    private Progress currSave;
    private ArrayList<Progress> saves;

public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
}

public String getUserName() {
    return null; 
}

public void setUserName(String username) {
    
}

public String getPassword() {
    return null;
}

public void setPassword(String password) {

}

public void addSave(Progress save) {

}

public List<Progress> getSaves() {
    return new ArrayList<>();
}

public void pushSaves() {

}

public void changeCurrSave(int saveIndex) {

}

public boolean auth(String username, String password) {
    return false;
}

}