package service;

import model.*;
import util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import exception.BankException;
import model.Account;
import model.SavingsAccount;
import util.Repository;
import service.AccountWorker;
import service.AccountStore;
import model.SavingsAccount;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import util.BankSession;
import util.AnnotationValidator;
import util.StatePersister;
import util.TransactionLog;
import util.ReportGenerator;
import static util.StatementFormatter.buildStatement;
import java.util.Scanner;

public class MiniBank {


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("----------" + bank.name() + "----------");
        System.out.println("----------" + bank.branch() + "----------");

        int choice;

        // Practical 6: Interface Testing

        System.out.println("\n===== INTERFACE TESTING =====");

        Account savings = new SavingsAccount("Prince", 10000, 2000);

        System.out.println("Interest Rate: " + savings.interestRate() + "%");
        System.out.println("Yearly Interest: Rs. " + savings.yearlyInterest());

// Anonymous class implementation
        WithdrawRule rule1 = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.canWithdraw(amount);
            }
        };

        System.out.println("\nAnonymous Class Result: "
                + rule1.allow(savings, 3000));

// Lambda expression implementation
        WithdrawRule rule2 = (account, amount) ->
                account.canWithdraw(amount);

        System.out.println("Lambda Result: "
                + rule2.allow(savings, 3000));

// Premium marker interface testing
        if (savings instanceof Premium) {
            System.out.println("This is a Premium Account.");
        }

// Static import testing
        System.out.println("\n===== ACCOUNT STATEMENT =====");
        System.out.println(buildStatement(savings));

        // Practical 7: Annotation Validation

        System.out.println("\n===== Annotation Validation =====");

        Account testAccount = new SavingsAccount("Prince", -1000, 2000);

        String[] errors = AnnotationValidator.validate(testAccount);

        if (errors.length == 0) {
            System.out.println("No validation errors found.");
        } else {
            System.out.println("Validation Errors:");

            for (String error : errors) {
                System.out.println(error);
            }
        }


        System.out.println("\n===== Practical 8: Exception Handling =====");

        Account saving = new SavingsAccount("Prince", 10000, 2000);
        Account current = new SavingsAccount("Parth", 5000, 2000);

// 1. Successful deposit
        try {
            savings.deposit(2000);
        } catch (InvalidAmountException e) {
            System.out.println("Deposit Error: " + e.getMessage());
        } finally {
            System.out.println("Deposit operation finished.");
        }

// 2. Invalid withdrawal
        try {
            savings.withdraw(-500);
        } catch (InvalidAmountException e) {
            System.out.println("Invalid Amount: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Insufficient Funds: " + e.getMessage());
        }

// 3. Insufficient balance
        try {
            savings.withdraw(50000);
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal Error: " + e.getMessage());
            System.out.println("Shortfall: Rs. " + e.getShortfall());
        } catch (InvalidAmountException e) {
            System.out.println("Invalid Amount: " + e.getMessage());
        }

// 4. Account transfer
        try {
            savings.transfer(current, 2000);
        } catch (BankException e) {
            System.out.println("Bank Error: " + e.getMessage());
        }

// 5. Try-with-resources
        try (BankSession session = new BankSession()) {
            System.out.println("Performing banking operations...");
        } catch (Exception e) {
            System.out.println("Session Error: " + e.getMessage());
        }

        // Practical 9: Multithreading

        System.out.println("\n===== Practical 9: Multithreading =====");

// Create one shared account
        Account sharedAccount = new SavingsAccount("Prince", 0, 2000);

// Create 10 threads
        int numberOfThreads = 10;
        int times = 1000;
        long amount = 10;

        Thread[] threads = new Thread[numberOfThreads];

// Start threads
        for (int i = 0; i < numberOfThreads; i++) {

            AccountWorker worker =
                    new AccountWorker(sharedAccount, times, amount);

            threads[i] = new Thread(worker, "BankThread-" + (i + 1));

            threads[i].start();
        }

// Wait for all threads to finish
        for (int i = 0; i < numberOfThreads; i++) {

            try {
                threads[i].join();

            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }

// Print final balance
        System.out.println("Expected Balance: Rs. " +
                (numberOfThreads * times * amount));

        System.out.println("Actual Balance: Rs. " +
                sharedAccount.getBalance());


// Practical 10: Transaction Processing

        System.out.println("\n===== Practical 10: Transaction Processing =====");

// Create a shared account
        Account transactionAccount =
                new SavingsAccount("Prince", 100000, 2000);

// Create transaction processor
        TransactionProcessor processor = new TransactionProcessor();

// Submit deposit tasks
        for (int i = 1; i <= 5; i++) {

            final int taskNumber = i;

            processor.submit(() -> {

                try {
                    System.out.println(
                            Thread.currentThread().getName()
                                    + " performing deposit task " + taskNumber
                    );

                    transactionAccount.deposit(1000);

                } catch (Exception e) {
                    System.out.println("Deposit failed: " + e.getMessage());
                }
            });
        }

// Submit withdrawal tasks
        for (int i = 1; i <= 5; i++) {

            final int taskNumber = i;

            processor.submit(() -> {

                try {
                    System.out.println(
                            Thread.currentThread().getName()
                                    + " performing withdrawal task " + taskNumber
                    );

                    transactionAccount.withdraw(500);

                } catch (BankException e) {
                    System.out.println("Withdrawal failed: " + e.getMessage());
                }
            });
        }

// Wait for all transactions to finish
        processor.stop();

// Print final balance
        System.out.println("Initial Balance: Rs. 100000");
        System.out.println("Final Balance: Rs. "
                + transactionAccount.getBalance());


// Practical 10: Producer-Consumer

        System.out.println("\n===== Producer Consumer =====");

        TransactionBuffer transactionBuffer = new TransactionBuffer(3);

        Account bufferAccount =
                new SavingsAccount("Buffer Test", 10000, 2000);

// Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {

                    final int taskNumber = i;

                    transactionBuffer.put(() -> {
                        try {
                            bufferAccount.deposit(100);
                            System.out.println(
                                    "Processed deposit transaction " + taskNumber
                            );
                        } catch (Exception e) {
                            System.out.println("Transaction failed: " + e.getMessage());
                        }
                    });
                }

                // Tell consumer that no more transactions will arrive
                transactionBuffer.put(TransactionBuffer.STOP);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

// Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                while (true) {

                    Runnable task = transactionBuffer.take();

                    if (task == TransactionBuffer.STOP) {
                        System.out.println("Consumer finished processing.");
                        break;
                    }

                    task.run();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        do {
            System.out.println("\n===== MiniBank Menu =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            String message = switch (choice) {
                case 1 -> "Open Account — to be implemented in a later lab";
                case 2 -> "Deposit — to be implemented in a later lab";
                case 3 -> "Withdraw — to be implemented in a later lab";
                case 4 -> "Transfer — to be implemented in a later lab";
                case 5 -> "Exiting MiniBank...";
                default -> "Invalid choice! Please enter a number from 1 to 5.";
            };

            System.out.println(message);

        } while (choice != 5);

        System.out.println("\nThank you for using MiniBank. Goodbye!");

// Practical 11: Persistence and Reporting

        System.out.println("\n===== PRACTICAL 11 =====");

// Create accounts
        Account[] accounts = {
                new SavingsAccount("Prince", 10000, 2000),
                new SavingsAccount("Parth", 15000, 2000)
        };

// Define file locations
        Path dataFile = Paths.get("data", "accounts.dat");
        Path logsFolder = Paths.get("logs");
        Path logFile = logsFolder.resolve("transactions.log");
        Path reportFile = Paths.get("reports", "daily-report.txt");

// 1. Perform transactions and write transaction logs
        try {
            accounts[0].deposit(1000);

            TransactionLog.append(
                    logFile,
                    "DEPOSIT," + accounts[0].getAccountNumber() + ",1000"
            );

            accounts[1].withdraw(500);

            TransactionLog.append(
                    logFile,
                    "WITHDRAWAL," + accounts[1].getAccountNumber() + ",500"
            );

        } catch (Exception e) {
            System.out.println("Transaction error: " + e.getMessage());
        }

// 2. Save all accounts
        StatePersister.save(accounts, dataFile);

// 3. Load accounts into a new array
        Account[] loadedAccounts = StatePersister.load(dataFile);

// 4. Print loaded accounts
        System.out.println("\n===== LOADED ACCOUNTS =====");

        for (Account account : loadedAccounts) {
            System.out.println(account);
        }

// 5. Generate end-of-day report
        System.out.println("\n===== DAILY REPORT =====");

        ReportGenerator.generate(logsFolder, reportFile);


// Practical 12: Collections Framework

        System.out.println("\n===== PRACTICAL 12 =====");

// Create AccountStore
        AccountStore store = new AccountStore();

// Create accounts
        Account account1 =
                new SavingsAccount("Prince", 15000, 2000);

        Account account2 =
                new SavingsAccount("Parth", 5000, 2000);

        Account account3 =
                new SavingsAccount("Yug", 10000, 2000);

// Add accounts to HashMap
        store.add(account1);
        store.add(account2);
        store.add(account3);

// Add transaction history
        store.addTransaction("DEPOSIT,ACC001,1000");
        store.addTransaction("WITHDRAWAL,ACC002,500");

// Display all accounts
        System.out.println("\n===== ALL ACCOUNTS =====");

        for (Account account : store.getAll()) {
            System.out.println(account);
        }

// Search account
        System.out.println("\n===== SEARCH ACCOUNT =====");

        Account found = store.get(account1.getAccountNumber());

        if (found != null) {
            System.out.println("Account found: " + found);
        }

// Display transaction history
        System.out.println("\n===== TRANSACTION HISTORY =====");

        for (String transaction : store.getTransactionHistory()) {
            System.out.println(transaction);
        }

// Display accounts sorted by balance
        System.out.println("\n===== ACCOUNTS SORTED BY BALANCE =====");

        for (Account account : store.sortedByBalance()) {
            System.out.println(
                    account.getOwnerName() + " - Rs. " + account.getBalance()
            );
        }

        Repository<Account> accountRepository = new Repository<>();

        account1 = new SavingsAccount("Prince", 15000, 2000);
        account2 = new SavingsAccount("Parth", 5000, 2000);
        account3 = new SavingsAccount("Yug", 10000, 2000);

// Save multiple accounts
        accountRepository.saveAll(account1, account2, account3);

// Find account using ID
        Account fetched = accountRepository.findById(
                account1.getAccountNumber()
        );

        System.out.println("\nFetched Account:");
        System.out.println(fetched);

// Display all accounts
        System.out.println("\nAll Repository Accounts:");

        for (Account account : accountRepository.findAll()) {
            System.out.println(account);
        }

// Test bounded generic method
        Integer[] numbers = {10, 20, 30, 40, 50};

        System.out.println("\nAverage: " + Repository.average(numbers));


        sc.close();

    }
}