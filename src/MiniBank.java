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
    
Account a1 = new Account("Prince", 5000);
Account a2 = new Account("Parth", 3000);

System.out.println("\nAccount Details:");
System.out.println(a1);
System.out.println(a2);

System.out.println("\nAccount Comparison:");
System.out.println(a1.equals(a2));

System.out.println("\nType Checking:");
System.out.println(a1 instanceof Account);

Customer.Address address =
    new Customer.Address("Street 1", "Vadodara", "390001");

Customer c1 = new Customer(
    "Prince", "prince@gmail.com", "9876543210", address
);

Customer c2 = c1.clone();

System.out.println("\nCustomer Details:");
System.out.println("Customer ID: " + c1.getCustomerId());
System.out.println("Name: " + c1.getName());
System.out.println("City: " + c1.getAddress().getCity());

System.out.println("\nCloned Customer:");
System.out.println("Customer ID: " + c2.getCustomerId());
System.out.println("Name: " + c2.getName());

System.out.println("Same Object: " + (c1 == c2));

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