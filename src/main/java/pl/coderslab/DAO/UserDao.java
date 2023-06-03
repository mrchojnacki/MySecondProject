package pl.coderslab.DAO;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;
import pl.coderslab.entity.User;

import java.sql.*;
import java.util.Scanner;

public class UserDao {
    public static void addUserToDatabase(User user) {
        try (Connection conn = DbUtil.connection()) {
            PreparedStatement preStmt = conn.prepareStatement(ADD_QUERY);
            preStmt.setString(1, user.getEmail());
            preStmt.setString(2, user.getUsername());
            preStmt.setString(3, hashPassword(user.getPassword()));
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User createUserFromDatabase(Scanner scanner) {
        System.out.println("You will now create a User object");
        String searchedColumn = DbUtil.chooseColumnToSearchUser(scanner);
        String searchedValueString = DbUtil.chooseValueUser(searchedColumn, scanner);
        int searchedValueInt;
        User user = new User();

        try (Connection conn = DbUtil.connection()) {
            PreparedStatement preStmt = conn.prepareStatement(READ_QUERY + searchedColumn + END_QUERY);
            if (searchedColumn.equals("id")) {
                searchedValueInt = Integer.parseInt(searchedValueString);
                preStmt.setInt(1, searchedValueInt);
            } else {
                preStmt.setString(1, searchedValueString);
            }
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateUserInDatabase(Scanner scanner) {
        System.out.println("You will now update a User in database.");

        String searchedColumn = DbUtil.chooseColumnToSearchUser(scanner);
        String searchedValue = DbUtil.chooseValueUser(searchedColumn, scanner);
        String columnOfValueToBeChanged = DbUtil.chooseColumnToUpdateUser(scanner);
        String valueToBeChanged = DbUtil.chooseValueUser(columnOfValueToBeChanged, scanner);
        int searchedValueInt = 0;
        int valueToBeChangedInt = 0;

        try (Connection conn = DbUtil.connection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_QUERY_SET +
                    searchedColumn + UPDATE_QUERY_WHERE + columnOfValueToBeChanged + END_QUERY);
            if (columnOfValueToBeChanged.equals("id")) {
                valueToBeChangedInt = Integer.parseInt(valueToBeChanged);
                preStmt.setInt(1, valueToBeChangedInt);
            } else {
                preStmt.setString(1, valueToBeChanged);
            }
            if (searchedColumn.equals("id")) {
                searchedValueInt = Integer.parseInt(searchedValue);
                preStmt.setInt(2, searchedValueInt);
            } else {
                preStmt.setString(2, searchedValue);
            }
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUserFromDatabase(Scanner scanner) {
        System.out.println("You will remove a User from database");
        String searchedColumn = DbUtil.chooseColumnToSearchUser(scanner);
        String searchedValueString = DbUtil.chooseValueUser(searchedColumn, scanner);
        int searchedValueInt = 0;

        try (Connection conn = DbUtil.connection()) {
            PreparedStatement preStmt = conn.prepareStatement(DELETE_QUERY + searchedColumn + END_QUERY);
            if (searchedColumn.equals("id")) {
                searchedValueInt = Integer.parseInt(searchedValueString);
                preStmt.setInt(1, searchedValueInt);
            } else {
                preStmt.setString(1, searchedValueString);
            }
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet resultFindAllUsers() {
        ResultSet rs = null;
        try (Connection conn = DbUtil.connection()) {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(FIND_ALL_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void readAllUsers() throws SQLException {
        ResultSet rs = resultFindAllUsers();
        while (rs.next()) {
            int id = rs.getInt("id");
            String email = rs.getString("email");
            String username = rs.getString("username");
            String password = rs.getString("password");
            System.out.println(id + " " + email + " " + username + " " + password);
        }
    }

    public static User[] createTableOfUsers() throws SQLException {
        ResultSet rs = resultFindAllUsers();
        User[] tableOfUsers = new User[0];
        User user = new User();
        while (rs.next()) {
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            tableOfUsers = DbUtil.dynamicTableIncrease(tableOfUsers, user);
        }
        return tableOfUsers;
    }

    public static void AllUsersDeleteFromDatabase(Scanner scanner) {
        System.out.println("You are going to purge your database, are you sure? Y/n");
        String confirmation = scanner.nextLine();
        if (confirmation.equals("Y")) {
            return;
        }
        try (Connection conn = DbUtil.connection()) {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(DELETE_ALL_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public static final String ADD_QUERY = "INSERT INTO users (email, username, password) " +
            "                               VALUES (?, ?, ?);";
    public static final String READ_QUERY_ID = "SELECT * FROM users WHERE id = ?;";
    public static final String READ_QUERY_EMAIL = "SELECT * FROM users WHERE email = ?;";
    public static final String READ_QUERY_USERNAME = "SELECT * FROM users WHERE username = ?;";
    public static final String READ_QUERY = "SELECT * FROM users WHERE ";
    public static final String UPDATE_QUERY_SET = "UPDATE users SET ";
    public static final String UPDATE_QUERY_WHERE = " = ? WHERE ";

    public static final String DELETE_QUERY = "DELETE FROM users WHERE ";
    public static final String END_QUERY = " = ?;";
    public static final String FIND_ALL_QUERY = "SELECT * FROM users;";
    public static final String DELETE_ALL_QUERY = "DELETE FROM users;";
}