package service;

import model.Account;
import model.BankInfo;
import util.Repository;
import util.ReportGenerator;
import exception.BankException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class MiniBank {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("----------" + bank.name() + "----------");
        System.out.println("----------" + bank.branch() + "----------");

        // Initialize Repository and BankService
        Repository<Account> mainRepository = new Repository<>();
        BankService bankService = new BankService(mainRepository);

        // File path for saving accounts
        Path dataFile = Paths.get("data", "accounts.dat");

        // Load previously saved accounts
        if (Files.exists(dataFile)) {
            bankService.loadAccounts(dataFile);
        } else {
            System.out.println("No previous account data found. Starting fresh.");
        }

        int choice = 0;

        do {
            System.out.println("\n========== MINI BANK MENU ==========");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Account Statement");
            System.out.println("6. List All Accounts");
            System.out.println("7. Batch Deposit");
            System.out.println("8. Daily Report");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    // Open Account
                    case 1:
                        System.out.print("Enter customer name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter initial balance: ");
                        long balance = sc.nextLong();
                        sc.nextLine();

                        Account newAccount =
                                bankService.openAccount(name, balance);

                        System.out.println("Your Account Number: "
                                + newAccount.getAccountNumber());
                        break;

                    // Deposit
                    case 2:
                        System.out.print("Enter account number: ");
                        String depositId = sc.nextLine();

                        System.out.print("Enter deposit amount: ");
                        long depositAmount = sc.nextLong();
                        sc.nextLine();

                        bankService.deposit(depositId, depositAmount);
                        break;

                    // Withdraw
                    case 3:
                        System.out.print("Enter account number: ");
                        String withdrawId = sc.nextLine();

                        System.out.print("Enter withdrawal amount: ");
                        long withdrawAmount = sc.nextLong();
                        sc.nextLine();

                        bankService.withdraw(withdrawId, withdrawAmount);
                        break;

                    // Transfer
                    case 4:
                        System.out.print("Enter source account number: ");
                        String from = sc.nextLine();

                        System.out.print("Enter destination account number: ");
                        String to = sc.nextLine();

                        System.out.print("Enter transfer amount: ");
                        long transferAmount = sc.nextLong();
                        sc.nextLine();

                        bankService.transfer(from, to, transferAmount);
                        break;

                    // Account Statement
                    case 5:
                        System.out.print("Enter account number: ");
                        String statementId = sc.nextLine();

                        bankService.statement(statementId);
                        break;

                    // List All Accounts
                    case 6:
                        System.out.println("\n========== ALL ACCOUNTS ==========");

                        for (Account account : bankService.listAccounts()) {
                            System.out.println(account);
                        }
                        break;

                    // Batch Deposit
                    case 7:
                        System.out.print("Enter account number: ");
                        String batchId = sc.nextLine();

                        System.out.print("Enter number of transactions: ");
                        int count = sc.nextInt();

                        System.out.print("Enter amount per transaction: ");
                        long batchAmount = sc.nextLong();
                        sc.nextLine();

                        bankService.processBatch(
                                batchId, count, batchAmount);
                        break;

                    // Daily Report
                    case 8:
                        Path logsFolder = Paths.get("logs");
                        Path reportFile =
                                Paths.get("reports", "daily-report.txt");

                        ReportGenerator.generate(logsFolder, reportFile);
                        break;

                    // Exit
                    case 9:
                        bankService.saveAccounts(dataFile);
                        System.out.println("Exiting MiniBank...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (BankException e) {
                System.out.println("Bank Error: " + e.getMessage());

            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input! Please enter the correct data type.");
                sc.nextLine();
            }

        } while (choice != 9);

        System.out.println("\nThank you for using MiniBank. Goodbye!");

        sc.close();
    }
}
