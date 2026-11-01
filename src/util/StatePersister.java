package util;

import model.Account;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StatePersister {

    // Save accounts to a file
    public static void save(Account[] accounts, Path file) {

        try {
            // Create parent folder if it does not exist
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }

            try (ObjectOutputStream out =
                         new ObjectOutputStream(
                                 new FileOutputStream(file.toFile()))) {

                out.writeObject(accounts);

                System.out.println("Accounts saved successfully.");
            }

        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    // Load accounts from a file
    public static Account[] load(Path file) {

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(file.toFile()))) {

            return (Account[]) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
            return new Account[0];
        }
    }
}
