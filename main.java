import java.util.Scanner;
public class main {

    public static void main(String[] args) {

        Account[] ac = new Account[3];
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < ac.length; i++) {

            System.out.print("Enter name for Account " + (i + 1) + ": ");
            String name = sc.nextLine();

            System.out.print("Enter opening balance: ");
            long balance = sc.nextLong();
            sc.nextLine(); // consume newline

            ac[i] = new Account(name, balance);
        }
        for(int i = 0; i < ac.length; i++)
        {
            System.out.println("Enter Amount for Deposite (" + (i+1) + ") : ");
            long amount = sc.nextLong();
            ac[i].deposite(amount);
        }
        for(int i = 0; i < ac.length; i++)
        {
            System.out.println("Enter Amount for withdraw (i+1) : ");
            long amount = sc.nextLong();
            if(ac[i].withdraw(amount))
                System.out.println("Successfully  withdraw ");
            else
                System.out.println("Balance is not sufficient ");
        }
        for (Account account : ac) {

            System.out.println("Account Number: "
                    + account.getAccountNumber());

            System.out.println("Owner Name: "
                    + account.getOwnerName());

            System.out.println("Balance: ₹"
                    + account.getBalance());

            System.out.println("Active: "
                    + account.isActive());

        }

    }
}
