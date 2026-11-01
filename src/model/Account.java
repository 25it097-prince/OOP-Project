package model;
import java.io.Serializable;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import exception.BankException;
import exception.AccountNotFoundException;
import model.annotation.Id;
import model.annotation.Positive;
import model.annotation.MaxLength;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;

    @Id
    private final String accountNumber;

    @MaxLength(30)
    private String ownerName;

    @Positive
    private long balance;

    private boolean active;

    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long balance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }
    @Override
    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    @Override
    public synchronized void deposit(long amount)
            throws InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero."
            );
        }

        balance += amount;

        System.out.println("Deposit successful.");
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero."
            );
        }

        if (amount > balance) {
            long shortfall = amount - balance;

            throw new InsufficientFundsException(
                    "Insufficient balance. Required amount is not available.",
                    shortfall
            );
        }

        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(
                    "Withdrawal exceeds the allowed withdrawal limit.",
                    0
            );
        }

        balance -= amount;

        System.out.println("Withdrawal successful.");

        return true;
    }

    public void transfer(Account to, long amount) throws BankException {

        try {
            // Check whether destination account exists
            if (to == null) {
                throw new AccountNotFoundException(
                        "Destination account not found."
                );
            }

            // Withdraw from the current account
            this.withdraw(amount);

            // Deposit into destination account
            to.deposit(amount);

            System.out.println("Transfer successful.");

        } catch (BankException e) {

            System.out.println("Transfer failed: " + e.getMessage());

            // Rethrow the exception
            throw e;

        } finally {

            System.out.println("Transfer operation completed.");
        }
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Account account = (Account) obj;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}
