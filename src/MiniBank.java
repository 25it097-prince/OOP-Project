import java.util.Scanner;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("----------" + bank.name() + "----------");
        System.out.println("----------" + bank.branch() + "----------");

        int choice;

        System.out.println("\n===== VALIDATOR TESTING =====");

        System.out.println("Valid Mobile: " +
                Validator.isValidMobile("9876543210"));

        System.out.println("Invalid Mobile: " +
                Validator.isValidMobile("12345"));

        System.out.println("Valid Email: " +
                Validator.isValidEmail("prince@gmail.com"));

        System.out.println("Invalid Email: " +
                Validator.isValidEmail("prince@"));

        System.out.println("Valid PAN: " +
                Validator.isValidPan("ABCDE1234F"));

        System.out.println("Invalid PAN: " +
                Validator.isValidPan("ABC123"));

        System.out.println("Valid IFSC: " +
                Validator.isValidIfsc("SBIN0001234"));

        System.out.println("Invalid IFSC: " +
                Validator.isValidIfsc("SBIN123"));

        System.out.println("\n===== COMMAND PARSER TESTING =====");

        Command command = CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("Transaction Type: " + command.type());

        System.out.println("Account Number: " +
                command.accountNumber());

        System.out.println("Amount: " + command.amount());

        System.out.println("\n===== ACCOUNT STATEMENT =====");

        Account account = new SavingsAccount("Prince" , 5000,1000);

        System.out.println(
                StatementFormatter.buildStatement(account)
        );

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

        System.out.println("\n===== PRACTICAL 5 =====");

        Account[] accounts = {
                new SavingsAccount("Prince", 10000, 2000),
                new CurrentAccount("Rahul", 5000, 3000),
                new FixedDepositAccount("Amit", 20000)
        };

        for (Account acc : accounts) {
            System.out.println("\nAccount Number: " + acc.getAccountNumber());
            System.out.println("Owner: " + acc.getOwnerName());
            System.out.println("Interest Rate: " + acc.interestRate() + "%");
        }

        System.out.println("\n===== INSTANCEOF TEST =====");

        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount savings) {
                System.out.println("Savings Account detected.");
                System.out.println("Balance: " + savings.getBalance());
            }
        }

        System.out.println("\n===== WITHDRAWAL TEST =====");

        accounts[0].withdraw(3000);
        accounts[1].withdraw(7000);
        accounts[2].withdraw(1000);

        System.out.println("\nThank you for using MiniBank. Goodbye!");

        sc.close();
    }
}