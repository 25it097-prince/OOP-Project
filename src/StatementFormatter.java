public class StatementFormatter {

    public static String buildStatement(Account account) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n========== MINI BANK STATEMENT ==========\n");

        sb.append("Account Number: ")
                .append(account.getAccountNumber())
                .append("\n");

        sb.append("Owner Name: ")
                .append(account.getOwnerName())
                .append("\n");

        sb.append("Balance: Rs. ")
                .append(account.getBalance())
                .append("\n");

        sb.append("Account Status: ")
                .append(account.isActive() ? "Active" : "Inactive")
                .append("\n");

        sb.append("=========================================");

        return sb.toString();
    }
}