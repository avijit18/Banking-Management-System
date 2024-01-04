import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner sc;
    public Accounts(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public long openAccount(String email){
        if(!AccountExists(email)){
            String openAccountQuery = "INSERT INTO accounts(account_number, fullname, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Enter Full Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security Pin: ");
            String securityPin = sc.nextLine();
            try{
                long account_number = GenerateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(openAccountQuery);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, securityPin);
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows > 0){
                    //System.out.println("Account Created Successfully....");
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation Failed..!!!");
                }
            } catch(Exception e){
                System.out.println(e);
            }
        }
        throw new RuntimeException("Account Already Exist...");
    }

    public long getAccountNumber(String email){
        String accNumberQuery = "select account_number from accounts where email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(accNumberQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (Exception e){
            System.out.println(e);
        }
        try {
            throw new Exception("Account Number Does not Exists");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long GenerateAccountNumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()) {
                long lastAccountNumber = resultSet.getLong("account_number");
                return lastAccountNumber + 1;
            } else {
                return 10001100;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 10001100;
    }

    public boolean AccountExists(String email){
        String AccountExistsQuery = "select account_number from accounts where email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(AccountExistsQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
