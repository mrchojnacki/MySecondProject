package pl.coderslab;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // creating a new User
            pl.coderslab.entity.User testUser = new pl.coderslab.entity.User(1, "bob@gmail.com", "randomusername", "hagahaga");
            pl.coderslab.entity.User testUser2 = new pl.coderslab.entity.User(2, "miki@gmail.com", "randomusername2", "hagahaga2");
            // adding new user to database
            pl.coderslab.DAO.UserDao.addUserToDatabase(testUser);
            // creating User object by reading the database
            pl.coderslab.entity.User checkUser = pl.coderslab.DAO.UserDao.createUserFromDatabase(scanner);
            // sout the user
            System.out.println(checkUser.getId() + " " + checkUser.getEmail() + " " + checkUser.getUsername() + " " + checkUser.getPassword());
            // updating data on user
            pl.coderslab.DAO.UserDao.updateUserInDatabase(scanner);
            // removing user from database
            pl.coderslab.DAO.UserDao.deleteUserFromDatabase(scanner);
            // reading/viewing all users from database
            try {
                pl.coderslab.DAO.UserDao.readAllUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // creating a table of users from database
            try {
                pl.coderslab.DAO.UserDao.createTableOfUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}