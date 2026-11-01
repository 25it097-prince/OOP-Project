public class Customer {
    private String name;
    private String email;
    private String mobile;
    private final String customerId;
    private static long customerCounter;

    private static String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }


    public Customer(String name, String email, String mobile, String customerId) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCustomerId() {
        return customerId;
    }
}
