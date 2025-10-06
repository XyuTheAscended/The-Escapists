package com.model.Coding.Data;
import java.util.ArrayList;
import java.util.UUID;
import com.model.Coding.User.User;


import com.model.Coding.Progress.Progress;

public class DataLoader {

  private static DataLoader dataLoader;

  private DataLoader() {

  }

  public static DataLoader getInstance() {
    if (dataLoader == null) {
      dataLoader = new DataLoader();
    }
    return dataLoader;
  }

  public ArrayList<User> getUsers(){
    return new ArrayList<User>();
  }

  public Progress loadProgress(UUID progressId){
    return new Progress(); 
  }

}
