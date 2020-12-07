package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Igor", "Ivanov", (byte) 20);
        userService.saveUser("Anton", "Pavlov", (byte) 21);
        userService.saveUser("Alex", "Ponin", (byte) 22);
        userService.saveUser("Sergei", "Birukov", (byte) 23);
        List<User> list = userService.getAllUsers();

        for(User user: list) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}
