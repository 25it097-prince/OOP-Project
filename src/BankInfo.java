public class BankInfo {
    // Define the record
    record bankInfo(String name, String branch) {}

    public static void main(String[] args) {
        bankInfo bank = new bankInfo("State Bank of India", "Vadodara Branch");
        System.out.println("Bank Name: " + bank.name());
        System.out.println("Branch: " + bank.branch());
    }

}
