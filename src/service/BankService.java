package service;
import util.AuditLogger;
import model.Account;
import model.SavingsAccount;
import model.annotation.Audit;
import util.Repository;
import util.Validator;
import util.StatePersister;
import exception.*;

import java.nio.file.Path;
import java.util.ArrayList;

public class BankService {

    private final Repository<Account> repository;

    public BankService(Repository<Account> repository) {
        this.repository = repository;
    }

    // Load saved accounts
    public void loadAccounts(Path file) {
        Account[] savedAccounts = StatePersister.load(file);
        repository.saveAll(savedAccounts);

        System.out.println("Saved accounts loaded successfully.");
    }

    // Save accounts
    public void saveAccounts(Path file) {
        Account[] accounts = repository.findAll()
                .toArray(new Account[0]);

        StatePersister.save(accounts, file);

        System.out.println("Accounts saved successfully.");
    }

    // Validate customer details
    public void validateCustomerDetails(
            String mobile, String email, String pan, String ifsc)
            throws BankException {

        if (!Validator.isValidMobile(mobile)) {
            throw new BankException("Invalid mobile number.");
        }

        if (!Validator.isValidEmail(email)) {
            throw new BankException("Invalid email address.");
        }

        if (!Validator.isValidPan(pan)) {
            throw new BankException("Invalid PAN number.");
        }

        if (!Validator.isValidIfsc(ifsc)) {
            throw new BankException("Invalid IFSC code.");
        }

        System.out.println("Customer details validated successfully.");
    }

    // Open a new account
    @Audit("Open Account")
    public Account openAccount(String name, long initialBalance)
            throws BankException {
        AuditLogger.log(BankService.class, "openAccount");

        if (name == null || name.trim().isEmpty()) {
            throw new BankException("Customer name cannot be empty.");
        }

        if (initialBalance < 0) {
            throw new InvalidAmountException(
                    "Initial balance cannot be negative.");
        }

        Account account = new SavingsAccount(name, initialBalance, 2000);

        repository.save(account);

        System.out.println("Account opened successfully.");
        System.out.println(account);

        return account;
    }

    // Deposit money
    @Audit("Deposit")
    public void deposit(String accountNumber, long amount)
            throws BankException {
        AuditLogger.log(BankService.class, "deposit");

        Account account = repository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Account not found.");
        }

        account.deposit(amount);
    }

    // Withdraw money
    @Audit("Withdraw")
    public void withdraw(String accountNumber, long amount)
            throws BankException {
        AuditLogger.log(BankService.class, "withdraw");

        Account account = repository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Account not found.");
        }

        account.withdraw(amount);
    }

    // Transfer money
    @Audit("Transfer")
    public void transfer(String fromAccount, String toAccount, long amount)
            throws BankException {

        AuditLogger.log(BankService.class, "transfer");
        Account source = repository.findById(fromAccount);
        Account destination = repository.findById(toAccount);

        if (source == null || destination == null) {
            throw new AccountNotFoundException("Account not found.");
        }

        source.transfer(destination, amount);
    }

    // Display account statement
    public void statement(String accountNumber)
            throws BankException {

        Account account = repository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Account not found.");
        }

        System.out.println("Account Statement");
        System.out.println("-------------------------");
        System.out.println(account);
    }

    // List all accounts
    public ArrayList<Account> listAccounts() {
        return repository.findAll();
    }

    // Process batch deposits safely
    @Audit("Batch Processing")
    public void processBatch(String accountNumber, int count, long amount)
            throws BankException {

        AuditLogger.log(BankService.class, "processBatch");
        Account account = repository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Account not found.");
        }

        if (count <= 0) {
            throw new BankException("Batch count must be positive.");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Transaction amount must be positive.");
        }

        TransactionProcessor processor = new TransactionProcessor();

        // Add account to thread-safe store
        processor.addAccount(account);

        // Submit batch deposit tasks
        for (int i = 1; i <= count; i++) {

            processor.submit(() -> {
                try {
                    Account sharedAccount =
                            processor.getAccount(accountNumber);

                    sharedAccount.deposit(amount);

                } catch (InvalidAmountException e) {
                    System.out.println(
                            "Batch transaction failed: " + e.getMessage());
                }
            });
        }

        // Wait for all tasks to finish
        processor.stop();

        System.out.println("Batch processing completed.");
    }
}
