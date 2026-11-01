package service;

import model.Account;
import model.SavingsAccount;

import java.util.concurrent.CountDownLatch;

public class DeadlockDemo {

    public static void main(String[] args) throws InterruptedException {

        Account accountA =
                new SavingsAccount("Account A", 10000, 2000);

        Account accountB =
                new SavingsAccount("Account B", 10000, 2000);

        System.out.println("===== Deadlock Demonstration =====");

        CountDownLatch latch = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            synchronized (accountA) {
                System.out.println("Thread 1 locked Account A");

                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (accountB) {
                    System.out.println("Thread 1 locked Account B");
                }
            }
        }, "Transfer-A-to-B");

        Thread t2 = new Thread(() -> {
            synchronized (accountB) {
                System.out.println("Thread 2 locked Account B");

                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (accountA) {
                    System.out.println("Thread 2 locked Account A");
                }
            }
        }, "Transfer-B-to-A");

        // Daemon threads prevent the demonstration from keeping the JVM alive.
        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        t1.join(1000);
        t2.join(1000);

        if (t1.isAlive() && t2.isAlive()) {
            System.out.println("Deadlock detected! Both threads are waiting.");
        }

        System.out.println("\n===== Deadlock Prevention =====");

        Account accountC =
                new SavingsAccount("Account C", 10000, 2000);

        Account accountD =
                new SavingsAccount("Account D", 10000, 2000);

        Thread t3 = new Thread(
                () -> safeTransfer(accountC, accountD),
                "Safe-Transfer-C-to-D"
        );

        Thread t4 = new Thread(
                () -> safeTransfer(accountD, accountC),
                "Safe-Transfer-D-to-C"
        );

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        System.out.println("Both safe transfers completed.");
    }

    // Always lock the account with the smaller account number first.
    public static void safeTransfer(Account from, Account to) {

        Account first;
        Account second;

        if (from.getAccountNumber().compareTo(to.getAccountNumber()) < 0) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        synchronized (first) {
            synchronized (second) {

                try {
                    from.transfer(to, 500);
                    System.out.println(
                            Thread.currentThread().getName()
                                    + " completed safely."
                    );
                } catch (Exception e) {
                    System.out.println("Transfer failed: " + e.getMessage());
                }
            }
        }
    }
}
