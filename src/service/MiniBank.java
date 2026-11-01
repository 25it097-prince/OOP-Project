package service;

import model.*;
import util.*;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import util.BankSession;
import model.Account;
import model.SavingsAccount;
import util.AnnotationValidator;
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

        Account savings = new SavingsAccount("Prince", 10000, 2000);
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

        sc.close();
    }
}