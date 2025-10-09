package com.model.Coding;

import java.util.Scanner;

import com.model.Coding.Data.DataLoader;
import com.model.Coding.Data.DataWriter;
import com.model.Coding.User.User;

public class Main {
    public static void main(String[] args) throws Exception {
        for (User user : DataLoader.getInstance().getUsers()) {
            System.out.println(user + "\n-----------------------------------");
        }

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


