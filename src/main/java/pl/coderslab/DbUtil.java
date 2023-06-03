package pl.coderslab;

import pl.coderslab.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DbUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";

    public static Connection connection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
        return conn;
    }

    public static String chooseColumnToSearchUser(Scanner scanner) {
        Boolean isColumn = false;
        String searchedColumn = "";
        while (!isColumn) {
            System.out.println("Write down from which column would you like to " +
                    "search for the pl.coderslab.entity.User you are looking for (\"id\" or \"email\"): ");
            searchedColumn = scanner.nextLine();
            if (searchedColumn.equals("id") || searchedColumn.equals("email")) {
                isColumn = true;
            } else {
                System.out.println("You cannot search using column " + searchedColumn + " or there is no column of such name \n");
            }
        }
        return searchedColumn;
    }
    public static String chooseValueUser(String searchedColumn, Scanner scanner) { /* do optymalizacji*/
        Boolean isValue = false;
        int searchedValueInt = 0;
        String searchedValue = "";
        while (!isValue) {
            System.out.println("Write down value from chosen column (mind the type of value - id is an integer, email is a string:");
            if (searchedColumn.equals("id")) {
                try {
                    searchedValueInt = scanner.nextInt();
                    isValue = true;
                    searchedValue = Integer.toString(searchedValueInt);
                } catch (InputMismatchException e) {
                    e.printStackTrace();
                }
            } else {
                searchedValue = scanner.nextLine();
                isValue = true;
            }
        }
        return searchedValue;
    }
    public static String chooseColumnToUpdateUser(Scanner scanner) {
        Boolean isColumn = false;
        String searchedColumn = "";
        while (!isColumn) {
            System.out.println("Write down from which column would you like to " +
                    "change a value of the pl.coderslab.entity.User (\"id\", \"email\", \"username\" or \"password\"): ");
            searchedColumn = scanner.nextLine();
            if (searchedColumn.equals("id") || searchedColumn.equals("email") || searchedColumn.equals("username") || searchedColumn.equals("password")) {
                isColumn = true;
            } else {
                System.out.println("You cannot search using column " + searchedColumn + " or there is no column of such name \n");
            }
        }
        return searchedColumn;
    }
    public static User[] dynamicTableIncrease (User[] tableOfUsers, User user) {
        User[] tempTableOfUsers = Arrays.copyOf(tableOfUsers, tableOfUsers.length+1);
        tempTableOfUsers[tempTableOfUsers.length] = user;
        return tempTableOfUsers;
    }
}