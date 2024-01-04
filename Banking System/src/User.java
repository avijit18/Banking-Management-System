import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner sc;

    public User(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void Register() {
        System.out.print("Enter Full Name: ");
        String userName = sc.nextLine();
        System.out.print("Enter Email: ");
        String userEmail = sc.nextLine();
        System.out.print("Enter Country Name: ");
        String userCountry = sc.nextLine();
        System.out.print("Enter Password: ");
        String userPassword = sc.nextLine();

        if (userExists(userEmail)) {
            System.out.println("User Already Exists for this Email..!!");
            return;
        }
        String registerQuery = "insert into newuser(fullname, email, country, password) values (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userEmail);
            preparedStatement.setString(3, userCountry);
            preparedStatement.setString(4, userPassword);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Registration Successful....");
            } else {
                System.out.println("Registration Failed...!!!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String login() {
        System.out.print("Enter Email: ");
        String loginUserEmail = sc.nextLine();
        System.out.print("Enter Password: ");
        String loginUserPassword = sc.nextLine();

        String loginQuery = "select * from newuser where email = ? AND password = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1, loginUserEmail);
            preparedStatement.setString(2, loginUserPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return loginUserEmail;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean userExists(String email) {
        String existsQuery = "select * from newuser where email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(existsQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }
}
