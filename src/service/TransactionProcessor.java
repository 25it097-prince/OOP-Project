package service;
import model.Account;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {

    private final ExecutorService executor;

    // Thread-safe shared account storage
    private final ConcurrentHashMap<String, Account> sharedAccounts =
            new ConcurrentHashMap<>();
    // Constructor
    public TransactionProcessor() {
        executor = Executors.newFixedThreadPool(4);
    }

    // Submit a task for execution
    public void submit(Runnable task) {
        executor.execute(task);
    }

    // Add an account to shared storage
    public void addAccount(Account account) {
        sharedAccounts.put(account.getAccountNumber(), account);
    }

    // Get an account by account number
    public Account getAccount(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }

    // Get all shared accounts
    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return sharedAccounts;
    }
    // Stop the processor and wait for submitted tasks
    public void stop() {

        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("Tasks taking too long. Forcing shutdown...");
                executor.shutdownNow();
            }

        } catch (InterruptedException e) {

            executor.shutdownNow();

            Thread.currentThread().interrupt();

            System.out.println("Transaction processor interrupted.");
        }
    }
}
