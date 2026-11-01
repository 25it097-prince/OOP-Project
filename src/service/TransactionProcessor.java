package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {

    private final ExecutorService executor;

    // Constructor
    public TransactionProcessor() {
        executor = Executors.newFixedThreadPool(4);
    }

    // Submit a task for execution
    public void submit(Runnable task) {
        executor.execute(task);
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
