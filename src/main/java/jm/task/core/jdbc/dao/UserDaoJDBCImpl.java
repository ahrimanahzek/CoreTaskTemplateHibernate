package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    String sqlQueryCreateTable = "CREATE TABLE `testdb`.`users` (\n" +
            "  `id` BIGINT(1) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(45) NULL,\n" +
            "  `lastName` VARCHAR(45) NULL,\n" +
            "  `age` TINYINT(1) NULL,\n" +
            "  PRIMARY KEY (`id`))\n" +
            "ENGINE = InnoDB\n" +
            "DEFAULT CHARACTER SET = utf8;\n";

    String sqlQueryDeleteTable = "DROP TABLE `users`";

    public UserDaoJDBCImpl() {



    }

    public void createUsersTable() {
        Util util = new Util();

        Connection connection = util.getConnection();

        try {

            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE 'users'");

            boolean tableExist = false;

            while (resultSet.next()) {
                tableExist = true;
            }

            if (!tableExist) {
                statement.execute(sqlQueryCreateTable);
                System.out.println("В базу данных добавлена таблица users");
                connection.commit();
            } else {
                System.out.println("Таблица users уже есть в базе данных!");
            }

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        Util util = new Util();

        Connection connection = util.getConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE 'users'");

            boolean tableExist = false;

            while (resultSet.next()) {
                tableExist = true;
            }

            if (tableExist) {
                statement.execute(sqlQueryDeleteTable);
                System.out.println("Из базы данных удалена таблица users");
                connection.commit();
            } else {
                System.out.println("Невозможно удалить таблицу users! Ее нет в базе данных");
            }

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Util util = new Util();

        Connection connection = util.getConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            String query = " insert into users (name, lastName, age)" + " values (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.execute();

            connection.commit();

            System.out.println("User с именем – " + name + " добавлен в базу данных");

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        Util util = new Util();

        Connection connection = util.getConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            String query = "DELETE FROM users\n" +
                    "WHERE id=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            preparedStatement.execute();

            connection.commit();

            System.out.println("Пользователь удален из базы данных");

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {

        List<User> list = new ArrayList<>();

        Util util = new Util();

        Connection connection = util.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name, lastName, age FROM users");

            while (resultSet.next()) {

                String name = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                byte age = resultSet.getByte(3);

                User user = new User(name, lastName, age);
                list.add(user);
            }

            System.out.println("Получен список пользователей!");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return list;
    }

    public void cleanUsersTable() {
        Util util = new Util();

        Connection connection = util.getConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.execute("TRUNCATE TABLE users");

            System.out.println("Таблица users была очищена!");

            connection.commit();

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
