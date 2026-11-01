public class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long balance) {
        this.ownerName = ownerName;
        this.balance = balance;
        this.accountNumber = generateAccountNumber();
        this.active=true;
    }

    public Account(String ownerName) {
        this(ownerName,0);
    }

    public void deposit(long amount)
    {
        if(amount>0)
            balance+=amount;
    }
    public boolean withdraw(long amount)
    {
        if(balance>=amount && amount>0)
        {
            balance-=amount;
            return true;
        }
        else
            return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
}
