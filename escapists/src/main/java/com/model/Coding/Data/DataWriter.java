package com.model.Coding.Data;

public class DataWriter {
  private static DataWriter dataWriter;

  private DataWriter() {

  }

  public static DataWriter getInstance() {
    if (dataWriter == null) {
      dataWriter = new DataWriter();
    }
    return dataWriter;
  }


  public void addUser(User user){
    
  }

  public void updateUser(User user){

  }

  public void saveProgress(Progress progress){
      
  }

}