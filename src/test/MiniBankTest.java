package test;
import exception.InsufficientFundsException;
import model.Account;
import model.SavingsAccount;
import service.AccountWorker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiniBankTest {

    // Test 1: Deposit increases balance
    @Test
    void depositIncreasesBalance() throws Exception {

        Account account = new SavingsAccount("Prince", 1000, 2000);

        account.deposit(500);

        assertEquals(1500, account.getBalance());
    }

    // Test 2: Withdrawal over limit throws exception
    @Test
    void withdrawalOverLimitThrowsException() {
        Account account = new SavingsAccount("Prince", 1000, 2000);

        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(3000));
    }

    // Test 3: Concurrent deposits keep balance correct
    @Test
    void concurrentDepositsKeepBalanceCorrect() throws InterruptedException {

        Account account = new SavingsAccount("Prince", 0, 2000);

        int numberOfThreads = 10;
        int times = 1000;
        long amount = 10;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            AccountWorker worker =
                    new AccountWorker(account, times, amount);

            threads[i] = new Thread(worker);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long expectedBalance = numberOfThreads * times * amount;

        assertEquals(expectedBalance, account.getBalance());
    }
}
