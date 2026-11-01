package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    public static void generate(Path logsFolder, Path reportFile) {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        StringBuilder report = new StringBuilder();

        report.append("===== END OF DAY REPORT =====\n");

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(logsFolder)) {

            for (Path file : stream) {

                if (!Files.isRegularFile(file)) {
                    continue;
                }

                report.append("\nFile: ")
                        .append(file.getFileName())
                        .append("\n");

                // Read file details
                BasicFileAttributes attributes =
                        Files.readAttributes(
                                file,
                                BasicFileAttributes.class
                        );

                report.append("File Size: ")
                        .append(attributes.size())
                        .append(" bytes\n");

                report.append("Last Modified: ")
                        .append(attributes.lastModifiedTime())
                        .append("\n");

                // Read transactions line by line
                try (BufferedReader reader =
                             Files.newBufferedReader(file)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        String[] parts = line.split(",");

                        if (parts.length < 3) {
                            continue;
                        }

                        String type = parts[0].trim();
                        long amount;

                        try {
                            amount = Long.parseLong(parts[2].trim());
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        if (type.equalsIgnoreCase("DEPOSIT")) {
                            totalDeposits += amount;
                        } else if (type.equalsIgnoreCase("WITHDRAWAL")) {
                            totalWithdrawals += amount;
                        }
                    }
                }
            }

            report.append("\n===== TRANSACTION SUMMARY =====\n");
            report.append("Total Deposits: Rs. ")
                    .append(totalDeposits).append("\n");

            report.append("Total Withdrawals: Rs. ")
                    .append(totalWithdrawals).append("\n");

            report.append("Net Transaction Amount: Rs. ")
                    .append(totalDeposits - totalWithdrawals).append("\n");

            // Create report folder if necessary
            if (reportFile.getParent() != null) {
                Files.createDirectories(reportFile.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    reportFile,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write(report.toString());
            }

            System.out.println(report);

            System.out.println("Report saved to: " + reportFile);

        } catch (IOException e) {
            System.out.println("Report generation failed: "
                    + e.getMessage());
        }
    }
}
