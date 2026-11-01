package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TransactionLog {

    public static void append(Path file, String line) {

        try {
            // Create parent folder if it does not exist
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }

            // Append the new transaction without deleting old records
            Files.write(
                    file,
                    (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            System.out.println("Transaction logged successfully.");

        } catch (IOException e) {
            System.out.println("Error writing transaction log: "
                    + e.getMessage());
        }
    }
}
