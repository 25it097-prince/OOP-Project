package service;
import java.util.Comparator;
import model.Account;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountStore {

    // Store accounts using account number as key
    private final HashMap<String, Account> accounts = new HashMap<>();

    // Store transaction history
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    // Return accounts sorted by balance (ascending)
    public ArrayList<Account> sortedByBalance() {

        ArrayList<Account> sortedAccounts = getAll();

        sortedAccounts.sort(
                Comparator.comparingLong(Account::getBalance)
        );

        return sortedAccounts;
    }
    // Add an account
    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Account added successfully.");
    }

    // Get an account using account number
    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    // Get all accounts
    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }
    // Add a transaction to history
    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    // Get complete transaction history
    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
}
