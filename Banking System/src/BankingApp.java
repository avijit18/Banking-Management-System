import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class BankingApp {
    // JDBC Connection
    private static final String url = "jdbc:mysql://localhost/Your_dataBase_name";
    private static final String userName = "Your User Name";
    private static final String password = "Your SQL Password";

    public static void main(String[] args) {
        try {
            // establish connection
            Connection connection = DriverManager.getConnection(url, userName, password);
            Scanner sc = new Scanner(System.in);

            // Creating objects for other classes
            User user = new User(connection, sc);
            AccountManager accountManager = new AccountManager(connection, sc);
            Accounts accounts = new Accounts(connection, sc);

            String email;
            String loginUserEmail;
            long account_number;

            while (true) {
                System.out.println("*** Welcome to Bank of Java ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your Choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        user.Register();
                        break;
                    case 2:
                        loginUserEmail = user.login();
                        if (loginUserEmail != null) {
                            System.out.println();
                            System.out.println("User Logged in...");
                            if (!accounts.AccountExists(loginUserEmail)) {
                                System.out.println();
                                System.out.println("1. Open a Bank Account");
                                System.out.println("2. Exit");
                                if (sc.nextInt() == 1) {
                                    account_number = accounts.openAccount(loginUserEmail);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }
                            account_number = accounts.getAccountNumber(loginUserEmail);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debitMoney(account_number);
                                        break;
                                    case 2:
                                        accountManager.creditMoney(account_number);
                                        break;
                                    case 3:
                                        accountManager.transferMoney(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password..!!");
                        }
                    case 3:
                        System.out.println("Thank You for using Basona Bank");
                        System.out.println("Existing System...");
                        return;
                    default:
                        System.out.println("Please enter valid choice...");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
