package com.model.Coding;

import java.util.Scanner;
import com.model.Coding.User.User;
import com.model.Coding.Data.DataWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("User Registration");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);

        DataWriter writer = DataWriter.getInstance();
        writer.addUser(newUser);

        System.out.println("User added successfully.");

        scanner.close();
    }
}


